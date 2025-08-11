DROP TABLE IF EXISTS spotify_users CASCADE;
CREATE TABLE if not exists spotify_users
(
    spotify_user_id       UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    spotify_id            VARCHAR(255) UNIQUE      NOT NULL,
    role                  VARCHAR(255)             NOT NULL,
    profile_image_url     VARCHAR(255)             NOT NULL,
    access_token          VARCHAR(255)             NOT NULL,
    refresh_token         TEXT                     NOT NULL,
    token_expiration_time TIMESTAMP WITH TIME ZONE NOT NULL,
    scope                 VARCHAR(255)             NOT NULL,
    created_at            TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


DROP TABLE IF EXISTS users;
CREATE TABLE if not exists users
(
    user_id         UUID PRIMARY KEY             DEFAULT gen_random_uuid(),
    password_hash   VARCHAR(255)        NOT NULL,
    email           VARCHAR(255) UNIQUE NOT NULL,
    role            VARCHAR(255)        NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE     DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE     DEFAULT CURRENT_TIMESTAMP,
    is_active       BOOLEAN             NOT NULL DEFAULT TRUE,
    spotify_user_id UUID UNIQUE REFERENCES spotify_users (spotify_user_id) ON DELETE CASCADE
);

