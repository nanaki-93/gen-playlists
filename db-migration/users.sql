CREATE TABLE users
(
    user_id    UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    email      VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE spotify_tokens
(
    spotify_token_id      UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    user_id               UUID UNIQUE              NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    spotify_id            VARCHAR(255) UNIQUE      NOT NULL,
    access_token          VARCHAR(255)             NOT NULL,
    refresh_token         TEXT                     NOT NULL,
    token_expiration_time TIMESTAMP WITH TIME ZONE NOT NULL,
    scope                 VARCHAR(255)             NOT NULL,
    created_at            TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);