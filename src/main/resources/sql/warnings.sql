CREATE TABLE IF NOT EXISTS warnings
(
    warnid BIGSERIAL NOT NULL PRIMARY KEY,
    guildid BIGINT NOT NULL REFERENCES guilds(guildid) ON DELETE CASCADE,
    userid BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT current_timestamp,
    warntext TEXT NOT NULL
);