CREATE TABLE courses(
	id serial PRIMARY KEY,
	title VARCHAR(50) UNIQUE NOT NULL,
	description TEXT NOT NULL,
	price INT NOT NULL
)