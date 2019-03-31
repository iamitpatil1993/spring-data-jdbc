DROP TABLE EMPLOYEE;

DROP TABLE actor;

CREATE	TABLE	EMPLOYEE(
	EMPLOYEE_ID	VARCHAR(36)	PRIMARY	KEY,
	FIRST_NAME	VARCHAR(36),
	LAST_NAME	VARCHAR(36),
	DATE_OF_JOINING	date,
	DESIGNATION	VARCHAR(36), 
	created_date timestamp default now(), 
	updated_date timestamp default now()
);

CREATE TABLE actor(
	id bigint auto_increment,
	first_name VARCHAR(40),
	last_name VARCHAR(40)
);