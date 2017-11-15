const express = require('express')
const app = express()
const url = require('url')
const body_parser = require('body-parser')
const session = require('express-session')
const mongoose = require('mongoose')
const mongodb = require('mongodb')
const ObjectID = mongodb.ObjectID
const Schema = mongoose.Schema
const pug = require('pug')
const bcrypt = require('bcrypt')
const salt_rounds = 10
const fs = require('fs-extra')
const merge = require('deepmerge')
const lean_id = require('mongoose-lean-id')

mongoose_connect_options = {
	user: 'mongoadmin',
	pass: 'm0ngodb',
	useMongoClient: true,
	bufferMaxEntries: 0
}

mongoose.Promise = global.Promise
mongoose.plugin(lean_id)

const Business = mongoose.model('Business', {
	eir_id: { type: String, required: true, unique: true },
	name: { type: String, required: true },
	phone_number: { type: String },
	type: { type: String },
	loc: { type: [ Number ], index: '2dsphere' },
	__v: { type: Number, select: false }
})

const PasswordResetSchema = new Schema({
	value: String,
	creation_date: { type: Date, required: true, default: new Date(0) }
})

const CareCompany = mongoose.model('CareCompany', {
	name: { type: String, required: true },
	email: { type: String, required: true, unique: true },
	password_hash: { type: String, required: true, select: false },
	password_reset_token: { type: PasswordResetSchema, required: true, select: false, default: {} },
	carers: [{ type: Schema.Types.ObjectId, ref: 'Carer' }],
	avatar: { type: String, required: true, default: '/uploads/images/default-avatar.png'},
	__v: { type: Number, select: false }
})

const Carer = mongoose.model('Carer', {
	name: { type: String, required: true },
	email: { type: String, required: true, unique: true },
	password_hash: { type: String, required: true, select: false },
	password_reset_token: { type: PasswordResetSchema, required: true, select: false, default: {} },
	patients: [{ type: Schema.Types.ObjectId, ref: 'Patient' }],
	companies: [{ type: Schema.Types.ObjectId, ref: 'CareCompany' }],
	avatar: { type: String, required: true, default: '/uploads/images/default-avatar.png'},
	__v: { type: Number, select: false }
})

const RemoteCameraSchema = new Schema({
	enabled: { type: Boolean, default: true, required: true },
	last_picture: { type: String, required: true, default: '/uploads/images/no-camera.jpg' }
},{ _id : false })

const Patient = mongoose.model('Patient', {
	name: { type: String, required: true },
	email: { type: String, required: true, unique: true },
	password_hash: { type: String, required: true, select: false },
	password_reset_token: { type: PasswordResetSchema, required: true, select: false, default: {} },
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
	avatar: { type: String, required: true, default: '/uploads/images/default-avatar.png'},
	__v: { type: Number, select: false }
})

app.use(body_parser.urlencoded({extended: true}))
app.use(body_parser.json())
app.use(session({
	secret: 'Z"\'l!|FiIL<7ty(^',
	resave: false,
	saveUninitialized: true,
	cookie: {
		maxAge: 1000 * 60 * 60 * 24 * 7 // 7 days (in milliseconds)
	}
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
		res.status(403).send({errors: errors})
		return
	}
	if (req.url === '/api/places' || req.url === '/api/login' || req.url === '/api/register') {
		return next()
	}
	if (!req.session.logged_in) {
		res.status(401).send({errors: [{ type: "flow", key: "login", message: "You need to be logged in before you can use this." }]})
		return
	}
	next()
})

app.post('/api/places', async function (req, res) {
	let errors = []
	req.body.longitude = parseFloat(req.body.longitude)
	req.body.latitude = parseFloat(req.body.latitude)
	if (isNaN(req.body.longitude)) {
		errors.push({ type: "required", key: "longitude", message: "Path `longitude` is required." })
	}
	if (isNaN(req.body.latitude)) {
		errors.push({ type: "required", key: "latitude", message: "Path `latitude` is required." })
	}
	if (typeof req.body.type !== 'string') {
		errors.push({ type: "required", key: "type", message: "Path `type` is required." })
	}
	if (errors.length > 0) {
		res.status(400).send({ errors: errors })
		return
	}
	try {
		const businesses = await Business.find().where('type', req.body.type).where('loc').near({
			center: {
				type: 'Point',
				coordinates: [req.body.longitude, req.body.latitude]
			}
		}).limit(20).lean().exec()
		res.send({data: businesses})
	} catch (db_error) {
		handle_api_db_error(db_error, res)
	}
})

app.post('/api/register', async function (req, res) {
	delete req.body._id // Prevents users from modifying this.
	delete req.body.__v
	let errors = []
	const user_model = get_model_from_name(req.body.account_model_name)
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
	} catch (db_error) {
		handle_api_db_error(db_error, res)
	}
})

app.post('/api/login', async function (req, res) { // This allows a user to log in.
	let errors = []
	const user_model = get_model_from_name(req.body.account_model_name)
	if (!check_auth_params(req, res, errors, user_model)) {
		return
	}
	let user
	try {
		user = await user_model.findOne({ email: req.body.email }, '+password_hash').lean().exec()
	} catch (db_error) {
		handle_api_db_error(db_error, res)
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
	get_user(req.session.account_model_name, req, res)
})

app.post('/api/patients/:user_id', (req, res) => update_user('Patient', req, res))
app.post('/api/carers/:user_id', (req, res) => update_user('Carer', req, res))
app.post('/api/companies/:user_id', (req, res) => update_user('CareCompany', req, res))
app.post('/api/me', (req, res) => {
	req.params.user_id = req.session.user_id
	update_user(req.session.account_model_name, req, res)
})

app.post('/api/patients/:user_id/invite', (req, res) => invite_user('Patient', req, res))
app.post('/api/carers/:user_id/invite', (req, res) => invite_user('Carer', req, res))
app.post('/api/companies/:user_id/invite', (req, res) => invite_user('CareCompany', req, res))

app.all('/api/*', (req, res) => { // In the event that a route is not handled, 404.
	res.status(404).send({errors: [{ type: "not-found", key: "endpoint", message: "Endpoint does not exist." }]})
})

app.get('/', async (req, res) => {
	show_pug(200, 'views/index.pug', req, res)
})

app.get('*', (req, res, next) => {
	if (!req.session.logged_in) {
		res.redirect('/?next_url=' + encodeURIComponent(req.url))
		return
	}
	next()
})

app.get('/settings', async (req, res) => {
	show_pug(200, 'views/settings.pug', req, res)
})

app.get('/:them_account_path', async (req, res) => {
	const them_model = get_model_from_path(req.params.them_account_path)
	if (them_model == null) {
		show_pug(404, 'views/not_found.pug', req, res)
		return
	}
	show_pug(200, 'views/' + req.params.them_account_path + '.pug', req, res)
})

app.get('/:them_account_path/:them_id', async (req, res) => {
	const me_model = get_model_from_name(req.session.account_model_name)
	const me_account_path = get_path_from_model_name(req.session.account_model_name)
	const them_account_path = req.params.them_account_path
	const them_model = get_model_from_path(them_account_path)
	let them = null
	let status_code = 404
	if (them_model == null || !ObjectID.isValid(req.params.them_id)) {
		show_pug(404, 'views/not_found.pug', req, res)
		return
	}
	them = await them_model.findOne({ _id: req.params.them_id }).lean().exec()
	const me = await me_model.findOne({ _id: req.session.user_id }).lean().exec()
	if (them == null) {
		show_pug(404, 'views/not_found.pug', req, res)
		return
	}
	const can_view_user = ((me._id.equals(them._id) && me_account_path == them_account_path) || (them_account_path in me && me[them_account_path].some(function (me_user_objectid) { // Check user[them_account_path] and see if they're already in it.
		return me_user_objectid.equals(them._id)
	})))
	if (!can_view_user) {
		them = null
	} else {
		status_code = 200
	}
	show_pug(status_code, 'views/' + them_account_path + '_details.pug', req, res, { them: them })
})

app.get('/:them_account_path/:them_id/invite', async (req, res) => {
	const me_model = get_model_from_name(req.session.account_model_name)
	const them_model = get_model_from_path(req.params.them_account_path)
	let them = null
	if (them_model == null || !ObjectID.isValid(req.params.them_id)) {
		show_pug(404, 'views/not_found.pug', req, res)
		return
	}
	let status_code = 404
	them = await them_model.findOne({ _id: req.params.them_id }).lean().exec()
	if (them == null) {
		show_pug(404, 'views/not_found.pug', req, res)
		return
	}
	show_pug(status_code, 'views/invite.pug', req, res, { them: them })
})

app.get('*', async (req, res) => {
	show_pug(404, 'views/not_found.pug', req, res)
})

async function show_pug(status_code, pug_name, req, res, extra_vars = {}) {
	let vars = { current_url: req.path, next_url: req.query.next_url }
	vars = merge(vars, extra_vars)
	let user = { logged_in: false }
	if (!req.session.logged_in) {
		vars['me'] = user
		res.status(status_code).send(pug.renderFile(pug_name, vars))
		return
	}
	const user_model = mongoose.model(req.session.account_model_name)
	try {
		let temp_user = await user_model.findOne({ _id: req.session.user_id }).populate(['carers', 'patients', 'companies']).lean().exec()
		user = merge(user, temp_user)
		user.account_path = get_path_from_model_name(req.session.account_model_name)
		user.logged_in = true
		vars['me'] = user
		res.send(pug.renderFile(pug_name, vars))
	} catch (db_error) {
		if (db_error.code === 'ENOENT') {
			show_pug(404, 'views/not_found.pug', req, res)
			return
		}
		console.log(db_error)
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
  console.log('Unhandled Rejection at: Promise', p, 'reason:', reason)
})

// TODO: Check if logged-in user has permission to view/edit this user's details.
async function get_user(model_name, req, res) {
	let errors = []
	const user_model = mongoose.model(model_name)
	try {
		let user = null
		if (ObjectID.isValid(req.params.user_id)) {
			user = await user_model.findOne({ _id: req.params.user_id }).lean().exec()
		}
		respond_user(user, req, res)
	} catch (db_error) {
		handle_api_db_error(db_error, res)
	}
}

async function update_user(model_name, req, res) {
	delete req.body.password_hash // We don't want someone trying to modify this.
	delete req.body.__v
	req.body.account_model_name = model_name
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
		respond_user(new_user, req, res)
	} catch (db_error) {
		handle_api_db_error(db_error, res)
	}
}

async function invite_user(model_name, req, res) {
	const me_model = mongoose.model(req.session.account_model_name)
	const me_account_path = get_path_from_model_name(req.session.account_model_name)
	const them_account_path = get_path_from_model_name(model_name)
	const them_model = mongoose.model(model_name)
	if (them_model == null) {
		res.status(400).send({ errors: [{ type: "invalid", key: "user-type", message: "This user type is invalid; It doesn't exist."}]})
	}
	try {
		const me = await me_model.findOne({ _id: req.session.user_id })
		let them = null
		if (ObjectID.isValid(req.params.user_id)) {
			them = await them_model.findOne({ _id: req.params.user_id })
		}
		if (them == null) {
			res.status(404).send({ errors: [{ type: "not-found", key: "target-user", message: "No user exists with this account type and ID."}]})
		}
		if (!(them_account_path in me) || !(me_account_path in them)) {
			res.status(400).send({ errors: [{ type: "invalid", key: "user-type", message: "You can't add the other user. Your account types are incompatible."}]})
			return
		}
		const already_invited = me[them_account_path].some(function (me_user_objectid) { // Check user[them_account_path] and see if they're already in it.
			return me_user_objectid.equals(them._id)
		})
		if (already_invited) {
			res.status(409).send({ errors: [{ type: "conflict", key: "target-user", message: "You've already invited this user."}]})
			return
		}
		me[them_account_path].push(them)
		them[me_account_path].push(me)
		me.save()
		them.save()
		res.status(200).send({})
	} catch (db_error) {
		handle_api_db_error(db_error, res)
		return
	}
}

const account_models = ['Patient', 'Carer', 'CareCompany']
const account_paths = ['patients', 'carers', 'companies']

function get_model_from_name(model_name) {
	if (account_models.indexOf(model_name) === -1) { // Ensure only account models can be retrieved with this method
		return null
	}
	return mongoose.model(model_name)
}

function get_model_from_path(account_path) {
	const path_index = account_paths.indexOf(account_path)
	if (path_index === -1) {
		return null
	}
	return get_model_from_name(account_models[path_index])
}

function get_path_from_model_name(account_model_name) {
	const models_index = account_models.indexOf(account_model_name)
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
	const image_directory_web = '/uploads/images/' + get_path_from_model_name(req.body.account_model_name) + '/' + req.params.user_id
	const image_path_web = image_directory_web + '/' + image.name
	const image_directory = 'public' + image_directory_web
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
	const user_model = mongoose.model(req.body.account_model_name)
	let user
	try {
		user = await user_model.findOne({ _id: req.session.user_id }, '+password_hash').lean().exec()
	} catch (db_error) {
		handle_api_db_error(db_error, res)
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

function check_auth_params(req, res, errors, user_model) {
	if (typeof req.body.email !== 'string') {
		errors.push({ type: "required", key: "email", message: "Path `email` is required." })
	}
	if (typeof req.body.password !== 'string') {
		errors.push({ type: "required", key: "password", message: "Path `password` is required." })
	}
	if (user_model == null) {
		errors.push({ type: "required", key: "account_model_name", message: "Path `account_model_name` is required." })
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
	req.session.account_model_name = req.body.account_model_name
	user.account_model_name = req.session.account_model_name
	res.send({data: user})
}

function handle_hash_error(hash_error, res, errors) {
	errors.push({ type: "failure", key: "hash", message: hash_error.message })
	res.status(500).send({ errors: errors })
}

function respond_user(user, req, res) {
	if (user == null) {
		res.status(404).send({errors: [{ type: "not-found", key: "user", message: "User does not exist." }]})
		return
	}
	user.account_model_name = req.session.account_model_name
	res.send({data: user})
}

function handle_api_db_error(db_error, res) {
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
	console.log(db_error)
	res.status(503).send({ errors: [{ type: "communication", key: "database", message: "Failed to communicate with database." }]})
}