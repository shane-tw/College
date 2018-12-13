SET autocommit=0;

DROP USER IF EXISTS 'hangmanuser'@'localhost';
CREATE USER 'hangmanuser'@'localhost' IDENTIFIED BY 'mari4db';
DROP DATABASE IF EXISTS hangmandb;
CREATE DATABASE hangmandb;
GRANT ALL PRIVILEGES ON hangmandb.* TO 'hangmanuser'@'localhost';
FLUSH PRIVILEGES;

COMMIT;