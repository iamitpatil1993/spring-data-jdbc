CREATE	TABLE	EMPLOYEE(
	EMPLOYEE_ID	VARCHAR(36)	PRIMARY	KEY,
	FIRST_NAME	VARCHAR(30),
	LAST_NAME	VARCHAR(30),
	DATE_OF_JOINING	date,
	DESIGNATION	VARCHAR(30), 
	created_date timestamp default now(), 
	updated_date timestamp default now()
);