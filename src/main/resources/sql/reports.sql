CREATE TABLE IF NOT EXISTS reports
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    messageid BIGINT NOT NULL UNIQUE,
    reportmessageid BIGINT NOT NULL UNIQUE,
    channelid BIGINT NOT NULL,
    guildid BIGINT NOT NULL REFERENCES guilds(guildid) ON DELETE CASCADE,
    userid BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT current_timestamp,
    reporttext TEXT NOT NULL
);