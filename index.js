const express = require('express')
const app = express()
const url = require('url')
const bodyParser = require('body-parser')
const session = require('express-session')
const mongoose = require('mongoose')
const Schema = mongoose.Schema
const pug = require('pug')

mongoose.Promise = global.Promise
mongoose.connect('mongodb://localhost/care_assistant?authSource=admin', {
    user: 'shane',
    pass: 'mongodb',
    useMongoClient: true
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

app.use(bodyParser.urlencoded({extended: true}))
app.use(bodyParser.json())
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
    new Patient(req.body).save(function (err) {
        if (!err) {
            res.send({})
            return
        }
        let errors = []
        for (const key in err.errors) {
            var error = err.errors[key]
            errors.push({ type: error.kind, key: error.path, message: error.message })
        }
        res.status(400).send({errors: errors})
    })
})

app.post('/api/login', function (req, res) { // This allows a user to log in.
    let errors = []
    if (req.body.email == null) {
        errors.push({ type: "required", key: "email", message: "Path `email` is required." })
    }
    if (req.body.password == null) {
        errors.push({ type: "required", key: "password", message: "Path `password` is required." })
    }
    if (errors.length > 0) {
        res.status(400).send({errors: errors})
        return
    }
    res.send({})
})

app.post('/api/logout', function (req, res) {
    req.session.destroy()
    res.send({})
})

app.get('/api/*', function (req, res) { // In the event that a route is not handled, 404.
    res.status(404).send({'errors': [{ type: "not-found", key: "endpoint", message: "Endpoint does not exist."}]})
})

app.get('/login', function (req, res) {
    res.send(pug.renderFile("test.pug", {
        cache: true,
        name: "Timmy",
        title: "This is a test"
    }))
})

const server = app.listen(80, function() {
    const port = server.address().port
    console.log('Listening on port %d', port)
})
