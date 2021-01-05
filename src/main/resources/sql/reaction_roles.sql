CREATE TABLE IF NOT EXISTS reaction_roles
(
    guildId BIGINT NOT NULL REFERENCES guilds(guildId) ON DELETE CASCADE,
    emoteId VARCHAR(25) NULL,
    roleId BIGINT NULL,

    PRIMARY KEY (emoteId, roleId)
);