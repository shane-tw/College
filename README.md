## Setup:
Run `npm install`  
Run `mkdir mongodb && mongod --dbpath mongodb`  
Run `mongo < setup_scripts/create_user.js`  
Run `mongod --dbpath mongodb --auth`

## Running:
Run `mongod --dbpath mongodb --auth`
Run `node index.js`