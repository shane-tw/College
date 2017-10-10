const express = require('express')
const app = express()
const url = require('url')
const body_parser = require('body-parser')
const session = require('express-session')
const mongoose = require('mongoose')
const Schema = mongoose.Schema
const pug = require('pug')
const bcrypt = require('bcrypt')
const salt_rounds = 10
const fs = require('fs-extra')

mongoose_connect_options = {
	user: 'mongoadmin',
	pass: 'm0ngodb',
	useMongoClient: true,
	bufferMaxEntries: 0
}

mongoose.Promise = global.Promise

const CareCompany = mongoose.model('CareCompany', {
	name: { type: String, required: true },
	email: { type: String, required: true, unique: true },
	password_hash: { type: String, required: true, select: false },
	carers: [{ type: Schema.Types.ObjectId, ref: 'Carer' }],
	__v: { type: Number, select: false },
	avatar: { type: String, required: true, default: 'uploads/images/default-avatar.png'}
})

const Carer = mongoose.model('Carer', {
	name: { type: String, required: true },
	email: { type: String, required: true, unique: true },
	password_hash: { type: String, required: true, select: false },
	patients: [{ type: Schema.Types.ObjectId, ref: 'Patient' }],
	companies: [{ type: Schema.Types.ObjectId, ref: 'CareCompany' }],
	__v: { type: Number, select: false },
	avatar: { type: String, required: true, default: 'uploads/images/default-avatar.png'}
})

const RemoteCameraSchema = new Schema({
	enabled: { type: Boolean, default: true, required: true },
	last_picture: { type: String, required: true, default: 'uploads/images/no-camera.jpg' }
},{ _id : false })

const Patient = mongoose.model('Patient', {
	name: { type: String, required: true },
	email: { type: String, required: true, unique: true },
	password_hash: { type: String, required: true, select: false },
	allow_location_tracking: { type: Boolean, default: true, required: true },
	facebook_token: String,
	twitter_token: String,
	last_location: { latitude: Number, longitude: Number },
	last_location_time: Date,
	remote_camera: { type: RemoteCameraSchema, required: true, default: {} },
	calendar_events: [{ name: { type: String, required: true }, date: { type: Date, required: true }}],
	enable_geofence: { type: Boolean, default: false, required: true },
	geofence_points: [{ latitude: { type: Number, required: true }, longitude: { type: Number, required: true }}],
	carers: [{ type: Schema.Types.ObjectId, ref: 'Carer' }],
	__v: { type: Number, select: false },
	avatar: { type: String, required: true, default: 'uploads/images/default-avatar.png'}
})

app.use(body_parser.urlencoded({extended: true}))
app.use(body_parser.json())
app.use(session({
	secret: 'Z"\'l!|FiIL<7ty(^',
	resave: false,
	saveUninitialized: true,
	cookie: { maxAge: 60 * 60 * 24 * 7 }
}))
app.use(express.static('public'))

app.all('/api/*', (req, res, next) => { // This route mitigates CSRF attacks, and also blocks non-logged in users.
	let errors = []
	if (req.header('X-Requested-With') !== 'XMLHttpRequest') {
		errors.push({ type: "csrf", key: "x-requested-with", message: "Missing 'X-Requested-With: XMLHttpRequest' header from request." })
	}
	let source_domain = req.headers.origin
	if (source_domain == null) {
		source_domain = req.headers.referer
	}
	if (source_domain == null) {
		errors.push({ type: "csrf", key: "origin", message: "Request must contain 'Origin' or 'Referer' header; contains neither." })
	} else {
		source_domain = url.parse(source_domain).hostname
		const target_domain = req.headers.host
		if (source_domain != target_domain) {
			errors.push({ type: "csrf", key: "origin", message: "Origin domain does not match target."})
		}
	}
	if (errors.length > 0) {
		res.status(403).send(errors)
		return
	}
	if (req.url === '/api/login' || req.url === '/api/register') {
		return next()
	}
	if (!req.session.logged_in) {
		res.status(401).send({errors: [{ type: "flow", key: "login", message: "You need to be logged in before you can use this." }]})
		return
	}
	next()
})

app.post('/api/register', async function (req, res) {
	delete req.body._id // Prevents users from modifying this.
	delete req.body.__v
	let errors = []
	const user_model = get_model_from_account_type(req.body.account_type)
	if (!check_auth_params(req, res, errors, user_model)) {
		return
	}
	try {
		req.body.password_hash = await bcrypt.hash(req.body.password, salt_rounds)
	} catch (hash_error) {
		handle_hash_error(hash_error, res, errors)
		return
	}
	const user = new user_model(req.body)
	try {
		const new_user = await user.save()
		login_user(new_user, req, res)
	}
	catch (db_error) {
		handle_db_error(db_error, res)
	}
})

app.post('/api/login', async function (req, res) { // This allows a user to log in.
	let errors = []
	const user_model = get_model_from_account_type(req.body.account_type)
	if (!check_auth_params(req, res, errors, user_model)) {
		return
	}
	let user
	try {
		user = await user_model.findOne({ email: req.body.email }, '+password_hash').lean().exec()
	} catch (db_error) {
		handle_db_error(db_error, res)
		return
	}
	if (user == null) {
		errors.push({ type: "invalid", key: "login-details", message: "Invalid email or password." })
		res.status(401).send({ errors: errors })
		return
	}
	let password_matches
	try {
		password_matches = await bcrypt.compare(req.body.password, user.password_hash)
	} catch (hash_error) {
		handle_hash_error(hash_error, res, errors)
		return
	}
	if (!password_matches) {
		errors.push({ type: "invalid", key: "login-details", message: "Invalid email or password." })
		res.status(401).send({ errors: errors })
		return
	}
	login_user(user, req, res)
})

app.get('/api/logout', (req, res) => {
	req.session.destroy()
	res.send({})
})

app.get('/api/patients/:user_id', (req, res) => get_user('Patient', req, res))
app.get('/api/carers/:user_id', (req, res) => get_user('Carer', req, res))
app.get('/api/companies/:user_id', (req, res) => get_user('CareCompany', req, res))
app.get('/api/me', (req, res) => {
	req.params.user_id = req.session.user_id
	get_user(req.session.account_type, req, res)
})

app.post('/api/patients/:user_id', (req, res) => update_user('Patient', req, res))
app.post('/api/carers/:user_id', (req, res) => update_user('Carer', req, res))
app.post('/api/companies/:user_id', (req, res) => update_user('CareCompany', req, res))
app.post('/api/me', (req, res) => {
	req.params.user_id = req.session.user_id
	update_user(req.session.account_type, req, res)
})

app.all('/api/*', (req, res) => { // In the event that a route is not handled, 404.
	res.status(404).send({errors: [{ type: "not-found", key: "endpoint", message: "Endpoint does not exist." }]})
})

app.get('/', async (req, res) => {
	show_pug('views/index.pug', req, res)
})

app.get('*', (req, res, next) => {
	if (!req.session.logged_in) {
		res.redirect('/?next_url=' + encodeURIComponent(req.url));
		return
	}
	next()
})

app.get('/settings', async (req, res) => {
	show_pug('views/settings.pug', req, res)
})

async function show_pug(pug_name, req, res) {
	if (!req.session.logged_in) {
		res.send(pug.renderFile(pug_name, { current_url: req.path, user: null, logged_in: false }))
		return
	}
	const user_model = mongoose.model(req.session.account_type)
	try {
		const user = await user_model.findOne({ _id: req.session.user_id }).lean().exec()
		res.send(pug.renderFile(pug_name, { current_url: req.path, user: user, logged_in: true }))
	} catch (db_error) {
		res.status(503).send("Failed to communicate with the database.")
	}
}

const server = app.listen(80, async () => {
	const port = server.address().port
	console.log('Listening on port %d.', port)
	try {
		await mongoose.connect('mongodb://localhost/care_assistant?authSource=admin', mongoose_connect_options)
		console.log('Connected to database successfully.')
	} catch (db_error) {
		console.log('Failed to connect to database.')
		console.log(db_error.stack)
		process.exit(1)
	}
})

process.on('unhandledRejection', (reason, p) => { // Without this, unhandled exceptions in promises would give unhelpful messages.
  console.log('Unhandled Rejection at: Promise', p, 'reason:', reason);
});

// TODO: Check if logged-in user has permission to view/edit this user's details.
async function get_user(model_name, req, res) {
	let errors = []
	const user_model = mongoose.model(model_name)
	try {
		const user = await user_model.findOne({ _id: req.params.user_id }).lean().exec()
		respond_user(user, res)
	} catch (db_error) {
		handle_db_error(db_error, res)
	}
}

async function update_user(model_name, req, res) {
	delete req.body.password_hash // We don't want someone trying to modify this.
	delete req.body.__v
	req.body.account_type = model_name
	const user_model = mongoose.model(model_name)
	if (!await run_password_checks(req, res)) {
		return
	}
	if (!await run_image_checks({ name: 'avatar', content: req.body.avatar }, req, res)) {
		return
	}
	if (!await run_image_checks({ name: 'remote-camera', content: req.body['remote_camera[last_picture]'] }, req, res)) {
		return
	}
	try {
		const new_user = await user_model.findByIdAndUpdate(req.params.user_id, req.body, {new: true, runValidators: true}).exec()
		respond_user(new_user, res)
	} catch (db_error) {
		handle_db_error(db_error, res)
	}
}

const account_models = ['Patient', 'Carer', 'CareCompany']
const account_paths = ['patients', 'carers', 'companies']

function get_path_from_type(account_type) {
	const models_index = account_models.indexOf(account_type)
	if (models_index === -1) {
		return null
	}
	return account_paths[models_index]
}

async function run_image_checks(image, req, res) {
	if (image.content == null) {
		return true
	}
	const image_matches = image.content.match(/^data:(image\/[a-z.-]+);base64,(.+)$/)
	if (image_matches == null || image_matches.length !== 3) {
		res.status(400).send({ errors: [{ type: "failure", key: "base64", message: "Invalid image format. Should be in base64 with data-type."}]})
		return false
	}
	const image_directory_web = 'uploads/images/' + get_path_from_type(req.body.account_type) + '/' + req.params.user_id
	const image_path_web = image_directory_web + '/' + image.name
	const image_directory = 'public/' + image_directory_web
	const image_path = image_directory + '/' + image.name
	try {
		await fs.mkdirs(image_directory)
		await fs.writeFile(image_path, image_matches[2], { encoding: 'base64' })
		switch (image.name) {
			case 'avatar':
				req.body.avatar = image_path_web
				break
			case 'remote-camera':
				req.body['remote_camera[last_picture]'] = image_path_web
		}
	} catch (err) {
		res.status(500).send({ errors: [{ type: "failure", key: "io-write", message: "Failed to write image to file."}]})
		return false
	}
	return true
}

async function run_password_checks(req, res) {
	let errors = []
	if (req.body.password_new == null) {
		return true
	}
	if (req.body.password_new != req.body.password_confirm) {
		res.status(400).send({ errors: [{ type: "invalid", key: "password_confirm", message: "New passwords must match."}] })
		return false
	}
	let user
	try {
		user = await user_model.findOne({ _id: req.session.user_id }, '+password_hash').lean().exec()
	} catch (db_error) {
		handle_db_error(db_error, res)
		return false
	}
	let old_password_matches
	try {
		old_password_matches = await bcrypt.compare(req.body.password_old, user.password_hash)
	} catch (hash_error) {
		handle_hash_error(hash_error, res, errors)
		return false
	}
	if (!old_password_matches) {
		errors.push({ type: "invalid", key: "login-details", message: "Old password does not match the one on record." })
		res.status(401).send({ errors: errors })
		return false
	}
	try {
		req.body.password_hash = await bcrypt.hash(req.body.password_new, salt_rounds)
	} catch (hash_error) {
		handle_hash_error(hash_error, res, errors)
		return false
	}
	return true
}

function get_model_from_account_type(account_type) {
	if (account_models.indexOf(account_type) === -1) {
		return null
	}
	return mongoose.model(account_type)
}

function check_auth_params(req, res, errors, user_model) {
	if (typeof req.body.email !== 'string') {
		errors.push({ type: "required", key: "email", message: "Path `email` is required." })
	}
	if (typeof req.body.password !== 'string') {
		errors.push({ type: "required", key: "password", message: "Path `password` is required." })
	}
	if (user_model == null) {
		errors.push({ type: "required", key: "account_type", message: "Path `account_type` is required." })
	}
	if (errors.length > 0) {
		res.status(400).send({ errors: errors })
		return false
	}
	return true
}

function login_user(user, req, res) {
	if (typeof user.toObject === 'function') { // Convert mongoose document to plain object
		user = user.toObject()
	}
	delete user.password_hash
	delete user.__v
	req.session.logged_in = true
	req.session.user_id = user._id
	req.session.account_type = req.body.account_type
	res.send(user)
}

function handle_hash_error(hash_error, res, errors) {
	errors.push({ type: "failure", key: "hash", message: hash_error.message })
	res.status(500).send({ errors: errors })
}

function respond_user(user, res) {
	if (user == null) {
		res.status(404).send({errors: [{ type: "not-found", key: "user", message: "User does not exist." }]})
		return
	}
	res.send(user)
}

function handle_db_error(db_error, res) {
	if (!('errors' in db_error)) { // Mongoose has errors, MongoDB has single error
		db_error = { errors: [ db_error ] }
	}
	let errors = []
	for (const key in db_error.errors) {
		const error = db_error.errors[key]
		if (error.kind === 'ObjectId') {
			res.status(404).send({ errors: [{ type: "not-found", key: "user", message: "User does not exist." }]})
			return
		} else if (error.kind === 'required') {
			errors.push({ type: error.kind, key: error.path, message: error.message })
		} else if (error.code === 11000) {
			res.status(409).send({ errors: [{ type: "conflict", key: "database", message: "User already exists." }]})
			return
		}
	}
	if (errors.length > 0) {
		res.status(400).send({ errors: errors })
		return
	}
	res.status(503).send({ errors: [{ type: "communication", key: "database", message: "Failed to communicate with database." }]})
}