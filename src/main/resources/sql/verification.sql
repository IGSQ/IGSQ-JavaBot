CREATE TABLE IF NOT EXISTS verification
(
    id bigserial PRIMARY KEY NOT NULL,
    phrase TEXT NOT NULL UNIQUE,
    guild_id BIGINT NOT NULL REFERENCES guilds(guild_id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL UNIQUE
);