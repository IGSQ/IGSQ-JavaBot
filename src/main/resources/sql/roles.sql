CREATE TABLE IF NOT EXISTS roles
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    userid BIGINT NOT NULL,
    guildid BIGINT NOT NULL REFERENCES guilds(guildid) ON DELETE CASCADE,
    roleid BIGINT NOT NULL
);