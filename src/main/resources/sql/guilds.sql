CREATE TABLE IF NOT EXISTS guilds
(
    guildid BIGINT NOT NULL PRIMARY KEY,
    logchannel BIGINT NOT NULL DEFAULT -1,
    mutedrole BIGINT NOT NULL DEFAULT -1,
    verifiedrole BIGINT NOT NULL DEFAULT -1,
    reportchannel BIGINT NOT NULL DEFAULT -1,
    votechannel BIGINT NOT NULL DEFAULT -1,
    suggestionchannel BIGINT NOT NULL DEFAULT -1,
    prefix VARCHAR(5) NOT NULL DEFAULT '.'
);