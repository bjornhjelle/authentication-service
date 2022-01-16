create TABLE IF NOT EXISTS user_tokens
(
    id            uuid NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    refresh_token varchar(128),
    user_id       uuid NOT NULL,
    created_at  timestamp with time zone,
    updated_at  timestamp with time zone,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
