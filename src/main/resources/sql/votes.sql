CREATE TABLE IF NOT EXISTS votes
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    voteId BIGINT NOT NULL,
    allowedRoles BIGINT[] NOT NULL,
    receivedVotes INT[] NOT NULL,
    maxVotes INT NOT NULL
);