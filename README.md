## Prerequisites
NodeJS >= 8.0  
MongoDB

## Setup
Run `npm install -g npm@5.2.0` (because 5.3.0 has a bug)  
Run `npm install`  
Run `mkdir mongodb && mongod --dbpath mongodb`  
Run `mongo < setup_scripts/create_user.js`  

## Running
Run `mongod --dbpath mongodb --auth`  
Run `node index.js`