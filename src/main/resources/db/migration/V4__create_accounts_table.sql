CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    balance NUMERIC(15, 2) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id)
);
