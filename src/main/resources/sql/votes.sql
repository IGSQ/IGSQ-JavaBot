CREATE TABLE IF NOT EXISTS votes
(
    voteId BIGSERIAL PRIMARY KEY NOT NULL,
    guildId BIGINT NOT NULL UNIQUE references guilds(guildId) ON DELETE CASCADE,
    timeStamp TIMESTAMP NOT NULL DEFAULT current_timestamp,
    topic TEXT NOT NULL,
    optionOne TEXT NOT NULL,
    optionTwo TEXT NOT NULL,
    voteCountOne INT NOT NULL DEFAULT -1,
    voteCountTwo INT NOT NULL DEFAULT -1
);