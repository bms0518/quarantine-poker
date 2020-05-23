CREATE TABLE player (
    id   INTEGER      NOT NULL,
    name VARCHAR(128) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE game (
    id   INTEGER      NOT NULL,
    date DATE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE player_game_stats (
    id   INTEGER      NOT NULL,
    player_id   INTEGER      NOT NULL,
    game_id   INTEGER      NOT NULL,
    buy_in DECIMAL NOT NULL,
    total_profit DECIMAL NOT NULL,
    FOREIGN KEY (`player_id`) REFERENCES `player`(`id`),
    FOREIGN KEY (`game_id`) REFERENCES `game`(`id`),
    PRIMARY KEY (id)
);