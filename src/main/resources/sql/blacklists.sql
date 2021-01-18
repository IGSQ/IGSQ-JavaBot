CREATE TABLE IF NOT EXISTS blacklists
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    guild_id BIGINT NOT NULL REFERENCES guilds(guild_id),
    phrase TEXT NOT NULL
);