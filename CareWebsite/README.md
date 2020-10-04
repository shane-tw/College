## Prerequisites
NodeJS >= 8.0  
MongoDB

## Setup
Run `npm install`  
Run `mkdir mongodb && mongod --dbpath mongodb`  
Run `mongo < setup_scripts/create_user.js`  
Run `mongod --dbpath mongodb --auth`  
Run `mongoimport -c businesses -d care_assistant --mode merge --file businesses.json -u mongoadmin -p m0ngodb --authenticationDatabase admin`

## Running
Run `mongod --dbpath mongodb --auth` if it isn't already running.  
Run `node index.js`

## Note
There's a [bug](https://github.com/npm/npm/issues/17858) on several versions of npm, which prevents `npm install` from completing.  
The lazy way of avoiding this bug is to run `npm install -g npm@5.2.0` as it doesn't exist in 5.2.0.