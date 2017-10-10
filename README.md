## Prerequisites:
NodeJS >= 8.0
MongoDB

## Setup:
Run `npm install`  
Run `mkdir mongodb && mongod --dbpath mongodb`  
Run `mongo < setup_scripts/create_user.js`  

## Running:
Run `mongod --dbpath mongodb --auth`  
Run `node index.js`