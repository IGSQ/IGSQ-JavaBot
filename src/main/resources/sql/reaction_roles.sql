CREATE TABLE IF NOT EXISTS reaction_roles
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    messageId BIGINT NOT NULL,
    guildId BIGINT NOT NULL REFERENCES guilds(guildId) ON DELETE CASCADE,
    emoteId VARCHAR(25) NULL,
    roleId BIGINT NULL
);