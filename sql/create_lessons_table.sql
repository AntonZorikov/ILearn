CREATE TABLE lessons(
	id SERIAL PRIMARY KEY,
	user_id BIGINT NOT NULL,
	title VARCHAR(50) NOT NULL,
	text TEXT NOT NULL,
	rating INT,
	video_id TEXT,
	file_id TEXT
)