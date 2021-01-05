CREATE TABLE IF NOT EXISTS guilds
(
    guildId BIGINT NOT NULL PRIMARY KEY,
    logChannel BIGINT NULL,
    mutedRole BIGINT NULL,
    verifiedRole BIGINT NULL,
    reportChannel BIGINT NULL,
    voteChannel BIGINT NULL,
    prefix VARCHAR(5) NULL
);