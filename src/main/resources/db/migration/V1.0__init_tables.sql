CREATE TABLE "user" (
	id BIGSERIAL NOT NULL,
	name VARCHAR(64) NOT NULL,
	email VARCHAR(64) NOT NULL,
	age SMALLINT,
	PRIMARY KEY(id)
);

CREATE TABLE user_bootcamp (
    id BIGSERIAL NOT NULL,
	user_id BIGINT NOT NULL,
	bootcamp_id BIGINT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY (user_id) REFERENCES "user"(id),
	CONSTRAINT uq_user_bootcamp UNIQUE (user_id, bootcamp_id)
);
