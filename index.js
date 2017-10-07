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

mongoose.Promise = global.Promise
mongoose.connect('mongodb://localhost/care_assistant?authSource=admin', {
	user: 'shane',
	pass: 'mongodb',
	useMongoClient: true,
	bufferMaxEntries: 0
})

const CareCompany = mongoose.model('CareCompany', {
	email: { type: String, required: true, select: false },
	password_hash: { type: String, required: true, select: false },
	name: { type: String, required: true },
	country: { type: String, required: true },
	carers: [{ type: Schema.Types.ObjectId, ref: 'Carer' }],
	__v: { type: Number, select: false }
})

const Carer = mongoose.model('Carer', {
	email: { type: String, required: true, select: false },
	password_hash: { type: String, required: true, select: false },
	patients: [{ type: Schema.Types.ObjectId, ref: 'Patient' }],
	companies: [{ type: Schema.Types.ObjectId, ref: 'CareCompany' }],
	__v: { type: Number, select: false }
})

const Patient = mongoose.model('Patient', {
	email: { type: String, required: true, unique: true, select: false },
	password_hash: { type: String, required: true, select: false },
	allow_location_tracking: { type: Boolean, default: true, required: true },
	facebook_token: String,
	twitter_token: String,
	last_location: { latitude: Number, longitude: Number },
	last_location_time: Date,
	allow_remote_camera: { type: Boolean, default: true, required: true },
	calendar_events: [{ name: { type: String, required: true }, date: { type: Date, required: true }}],
	enable_geofence: { type: Boolean, default: false, required: true },
	geofence_points: [{ latitude: { type: Number, required: true }, longitude: { type: Number, required: true }}],
	carers: [{ type: Schema.Types.ObjectId, ref: 'Carer' }],
	__v: { type: Number, select: false }
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

app.all('/api/*', function (req, res, next) { // This route mitigates CSRF attacks.
	let errors = []
	if (req.header('X-Requested-With') != 'XMLHttpRequest') {
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

app.post('/api/register', function (req, res) {
	delete req.body._id
	let errors = []
	const user_model = get_model_from_account_type(req.body.account_type)
	if (!check_auth_body(req, res, errors, user_model)) {
		return
	}
	bcrypt.hash(req.body.password, salt_rounds)
		.then(function (password_hash) {
			req.body.password_hash = password_hash
			req.body._id = mongoose.Types.ObjectId()
			const user = new user_model(req.body)
			user.save()
				.then(user => login_user(user, req, res))
				.catch(function (db_error) {
					if (!('errors' in insert_error)) { // Mongoose has errors, MongoDB has single error
						insert_error = { errors: [ insert_error ] }
					}
					for (const key in insert_error.errors) {
						let error = insert_error.errors[key]
						if (!('kind' in error)) {
							error.kind = error.name
						}
						error.path = null
						if (!('path' in error) && typeof error.code != 'string') {
							error.path = error.code.toString()
						}
						errors.push({ type: error.kind, key: error.path, message: error.message })
					}
					res.status(400).send({errors: errors})
				})
		})
		.catch(hash_error => handle_hash_error(hash_error, res, errors))
})

app.post('/api/login', function (req, res) { // This allows a user to log in.
	let errors = []
	const user_model = get_model_from_account_type(req.body.account_type)
	if (!check_auth_body(req, res, errors, user_model)) {
		return
	}
	user_model.findOne({ email: req.body.email }, '+password_hash').lean().exec()
		.then(function (user) {
			if (user == null) {
				errors.push({ type: "invalid", key: "login-details", message: "Invalid email or password." })
				res.status(401).send({ errors: errors })
				return
			}
			bcrypt.compare(req.body.password, user.password_hash)
				.then(function (match) {
					if (!match) {
						errors.push({ type: "invalid", key: "login-details", message: "Invalid email or password." })
						res.status(401).send({ errors: errors })
						return
					}
					delete user.password_hash
					login_user(user, req, res)
				})
				.catch(hash_error => handle_hash_error(hash_error, res, errors))
		})
		.catch(db_error => respond_user_error(db_error, res, errors))
})

app.get('/api/logout', function (req, res) {
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

app.all('/api/*', function (req, res) { // In the event that a route is not handled, 404.
	res.status(404).send({errors: [{ type: "not-found", key: "endpoint", message: "Endpoint does not exist." }]})
})

app.get('/login', function (req, res) {
	res.send(pug.renderFile("views/login.pug"))
})

app.get('/register', function (req, res) {
	res.send(pug.renderFile("views/register.pug"))
})

const server = app.listen(80, function() {
	const port = server.address().port
	console.log('Listening on port %d', port)
})

// TODO: Check if logged-in user has permission to view/edit this user's details.
function get_user(model_name, req, res) {
	let errors = []
	const user_model = mongoose.model(model_name)
	user_model.findOne({ _id: req.params.user_id }).lean().exec()
		.then(user => respond_user(user, res, errors))
		.catch(db_error => respond_user_error(db_error, res, errors))
}

function update_user(model_name, req, res) {
	delete req.body.password_hash // We don't want someone trying to modify this.
	let errors = []
	const user_model = mongoose.model(model_name)
	if (req.body.password != null) {
		bcrypt.hash(req.body.password, salt_rounds)
			.then(function (password_hash) {
				req.body.password_hash = password_hash
				user_model.findByIdAndUpdate(req.params.user_id, req.body, {new: true}).exec()
					.then(user => respond_user(user, res, errors))
					.catch(db_error => respond_user_error(db_error, res, errors));
			})
			.catch(function (hash_error) {
				errors.push({ type: "failure", key: "hash", message: hash_error.message })
				res.status(500).send({ errors: errors })
			})
		return
	}
	user_model.findByIdAndUpdate(req.params.user_id, req.body, {new: true}).exec()
		.then(user => respond_user(user, res, errors))
		.catch(db_error => respond_user_error(db_error, res, errors));
}

const account_models = ['Patient', 'Carer', 'CareCompany']

function get_model_from_account_type(account_type) {
	if (account_models.indexOf(account_type) == -1) {
		return null
	}
	return mongoose.model(account_type)
}

function check_auth_body(req, res, errors, user_model) {
	if (typeof req.body.email != 'string') {
		errors.push({ type: "required", key: "email", message: "Path `email` is required." })
	}
	if (typeof req.body.password != 'string') {
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
	req.session.logged_in = true
	req.session.user_id = user._id
	req.session.account_type = req.body.account_type
	res.send(user)
}

function handle_hash_error(hash_error, res, errors) {
	errors.push({ type: "failure", key: "hash", message: hash_error.message })
	res.status(500).send({ errors: errors })
}

function respond_user(user, res, errors) {
	if (user == null) {
		res.status(404).send({errors: [{ type: "not-found", key: "user", message: "User does not exist." }]})
		return
	}
	res.send(user)
}

function respond_user_error(db_error, res, errors) {
	if (db_error.kind == 'ObjectId') {
		errors.push({ type: "not-found", key: "user", message: "User does not exist." })
		res.status(404).send({ errors: errors })
		return
	}
	errors.push({ type: "communication", key: "database", message: "Failed to communicate with database." })
	res.status(503).send({ errors: errors })
}
