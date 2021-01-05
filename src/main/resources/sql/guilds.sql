CREATE TABLE IF NOT EXISTS guilds
(
    guildId BIGINT NOT NULL PRIMARY KEY,
    logChannel BIGINT NOT NULL DEFAULT -1,
    mutedRole BIGINT NOT NULL DEFAULT -1,
    verifiedRole BIGINT NOT NULL DEFAULT -1,
    reportChannel BIGINT NOT NULL DEFAULT -1,
    voteChannel BIGINT NOT NULL DEFAULT -1,
    prefix VARCHAR(5) NOT NULL DEFAULT '.'
);