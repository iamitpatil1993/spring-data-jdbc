-- liquibase formatted sql

-- changeset amipatil:19052019073000

CREATE	TABLE	EMPLOYEE(
	EMPLOYEE_ID	VARCHAR(36)	PRIMARY	KEY,
	FIRST_NAME	VARCHAR(36),
	LAST_NAME	VARCHAR(36),
	DATE_OF_JOINING	date,
	DESIGNATION	VARCHAR(36), 
	created_date timestamp default now(), 
	updated_date timestamp default now()
);

CREATE SEQUENCE actor_pk_generator_seq;

CREATE TABLE actor(
	id bigint  NOT NULL DEFAULT nextval('actor_pk_generator_seq') PRIMARY KEY,
	first_name VARCHAR(40),
	last_name VARCHAR(40)
);

CREATE TABLE address (
	id VARCHAR(36) PRIMARY KEY,
	address VARCHAR(100),
	locality VARCHAR(40),
	region VARCHAR(40),
	city VARCHAR(30) NOT NULL,
	state VARCHAR(30) NOT NULL,
	country VARCHAR(30) NOT NULL,
	zipcode VARCHAR(20) NOT NULL,
	created_date TIMESTAMP DEFAULT now(),
	updated_date TIMESTAMP DEFAULT now(),
	employee_id VARCHAR(36)
);


-- rollback DROP SEQUENCE actor_pk_generator_seq;
-- rollback DROP TABLE address;
-- rollback DROP TABLE actor;
-- rollback DROP TABLE EMPLOYEE;
