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
mongoose.plugin(require('mongoose-hidden')({
    hidden: { _id: true, password_hash: true, __v: true }
}))

mongoose.Promise = global.Promise
mongoose.connect('mongodb://localhost/care_assistant?authSource=admin', {
    user: 'shane',
    pass: 'mongodb',
    useMongoClient: true,
    bufferMaxEntries: 0
})
const CareCompany = mongoose.model('CareCompany', {
    email: { type: String, required: true },
    password_hash: { type: String, required: true },
    name: { type: String, required: true },
    county: { type: String, required: true },
    country: { type: String, required: true },
    carers: [{ type: Schema.Types.ObjectId, ref: 'Carer' }]
})

const Carer = mongoose.model('Carer', {
    email: { type: String, required: true },
    password_hash: { type: String, required: true },
    patients: [{ type: Schema.Types.ObjectId, ref: 'Patient' }],
    companies: [{ type: Schema.Types.ObjectId, ref: 'CareCompany' }]
})

const Patient = mongoose.model('Patient', {
    email: { type: String, required: true, unique: true },
    password_hash: { type: String, required: true },
    allow_location_tracking: { type: Boolean, default: true },
    facebook_token: String,
    twitter_token: String,
    last_location: { latitude: Number, longitude: Number },
    last_location_time: Date,
    allow_remote_camera: { type: Boolean, default: true },
    calendar_events: [{ name: { type: String, required: true }, date: { type: Date, required: true }}],
    enable_geofence: { type: Boolean, default: false },
    geofence_points: [{ latitude: { type: Number, required: true }, longitude: { type: Number, required: true }}],
    carer: { type: Schema.Types.ObjectId, ref: 'Carer' }
})

const account_models = ['Patient', 'Carer', 'CareCompany']

function get_model_from_account_type(account_type) {
    if (account_models.indexOf(account_type) == -1) {
        return null
    }
    return mongoose.model(account_type)
}

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
    next()
})

app.get('/api/me', function (req, res) { // This returns information about the current logged in user.
    if (req.session.user_id == null) {
        res.status(401).send({errors: [{ type: "flow", key: "login", message: "You need to be logged in before you can get your details." }]})
        return
    }
    res.send('TODO: Fill this out.')
    console.log(req.session.user_id)
})

app.post('/api/register', function (req, res) {
    delete req.body._id
    let errors = []
    let status_code = 400
    if (typeof req.body.email != 'string') {
        errors.push({ type: "required", key: "email", message: "Path `email` is required." })
    }
    if (typeof req.body.password != 'string') {
        errors.push({ type: "required", key: "password", message: "Path `password` is required." })
    }
    bcrypt.hash(req.body.password, salt_rounds, function(hash_error, password_hash) {
        if (hash_error) {
            errors.push({ type: "failure", key: "hash", message: hash_error.message })
            status_code = 500
            res.status(status_code).send({ errors: errors })
            return
        }
        req.body.password_hash = password_hash
        const register_model = get_model_from_account_type(req.body.account_type)
        if (register_model == null) {
            errors.push({ type: "required", key: "account_type", message: "Path `account_type` is required." })
        }
        if (errors.length > 0) {
            res.status(status_code).send({ errors: errors })
            return
        }
        new register_model(req.body).save(function (insert_error) {
            if (insert_error == null) {
                res.send({})
                return
            }
            if (!('errors' in insert_error)) { // Highly likely a MongoError, but perhaps not guaranteed.
                insert_error = { errors: [ insert_error ] }
            }
            for (const key in insert_error.errors) {
                let error = insert_error.errors[key]
                if (!('kind' in error)) {
                    error.kind = error.name
                }
                if (!('path' in error)) {
                    error.path = error.code
                }
                errors.push({ type: error.kind, key: error.path, message: error.message })
            }
            res.status(status_code).send({errors: errors})
        })
    })
})

app.post('/api/login', function (req, res) { // This allows a user to log in.
    let errors = []
    let status_code = 400
    if (typeof req.body.email != 'string') {
        errors.push({ type: "required", key: "email", message: "Path `email` is required." })
    }
    if (typeof req.body.password != 'string') {
        errors.push({ type: "required", key: "password", message: "Path `password` is required." })
    }
    const login_model = get_model_from_account_type(req.body.account_type)
    if (login_model == null) {
        errors.push({ type: "required", key: "account_type", message: "Path `account_type` is required." })
    }
    if (errors.length > 0) {
        res.status(status_code).send({ errors: errors })
        return
    }
    bcrypt.hash(req.body.password, salt_rounds, function(hash_error, password_hash) {
        console.log(password_hash)
        if (hash_error) {
            errors.push({ type: "failure", key: "hash", message: hash_error.message })
            status_code = 500
            res.status(status_code).send({ errors: errors })
            return
        }
        login_model.findOne({
            email: req.body.email,
            password_hash: password_hash
        },
        function (db_error, person) {
            if (db_error) {
                errors.push({ type: "communication", key: "database", message: "Failed to communicate with database." })
                status_code = 503
            }
            if (db_error == null && person == null) {
                errors.push({ type: "invalid", key: "login-details", message: "Invalid email or password." })
                status_code = 401
            }
            if (errors.length > 0) {
                res.status(status_code).send({ errors: errors })
                return
            }
            req.session.user_id = person._id
            res.send(person)
        })
    })
})

app.post('/api/logout', function (req, res) {
    req.session.destroy()
    res.send({})
})

app.all('/api/*', function (req, res) { // In the event that a route is not handled, 404.
    res.status(404).send({errors: [{ type: "not-found", key: "endpoint", message: "Endpoint does not exist." }]})
})

app.get('/login', function (req, res) {
    res.send(pug.renderFile("login.pug"))
})

const server = app.listen(80, function() {
    const port = server.address().port
    console.log('Listening on port %d', port)
})
