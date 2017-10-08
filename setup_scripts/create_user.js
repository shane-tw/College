use admin
db.createUser(
	{
		user: 'mongoadmin',
		pwd: 'm0ngodb',
		roles: [ 'root' ]
	}
)
db.shutdownServer()
exit