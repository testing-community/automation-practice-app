CREATE SCHEMA test_schema;
-- Create tables and insert values needed

INSERT INTO test_schema."user"(
    id, email, password, username)
    VALUES (1, 'admin@admin.com', '$2a$10$HkkcaMM6MsOp0lob/iDGIuibyve6wUKU5JQRhzCCOH45JoGSlsN9i', 'admin') ,
    (2, 'mod@email.com', '$2a$10$zjhm6CMZvOg9cwifTDb1EugFCGk8pg9bjHpQhMabdReG4yXZ3vqjm', 'mod') ,
    (3, 'user@email.com', '$2a$10$O4DRQel1qxdSZnHLjD0Xnu8ZkJM8n5PDSNVpeVcCC9K05dIDANNgS', 'user');


INSERT INTO test_schema.role(
	id, name)
	VALUES (1, 'admin') , (2, 'mod') , (3, 'user');

INSERT INTO test_schema.skill(
	id, name)
	VALUES (1, 'Java') , (2, 'Selenium') , (3, 'Automation API Testing'), (4, 'Oracle');

INSERT INTO test_schema.userrole(
	id, roleid, userid)
	VALUES (1, 1, 1) , (2, 2, 1) , (3, 3, 1);

INSERT INTO test_schema.userskill(
	id, skillid, userid)
	VALUES (1, 1, 1) , (2, 3, 1);

-- Java project creates the tables so no needed to run these...

CREATE TABLE test_schema.userskill
(
id			numeric,
skillid		varchar(255),
userid		varchar(255)
);

CREATE TABLE test_schema.user
(
id			numeric,
email		varchar(255),
password	varchar(255),
username	varchar(255)
);

CREATE TABLE test_schema.role
(
id			numeric,
name		varchar(255)
);

CREATE TABLE test_schema.userrole
(
id			numeric,
roleid		varchar(255),
userid		varchar(255)
);

CREATE TABLE test_schema.skill
(
id			numeric,
name		varchar(255)
);
