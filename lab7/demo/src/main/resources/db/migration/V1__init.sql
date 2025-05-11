CREATE TABLE team (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL
);

CREATE TABLE player (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(50),
    age INT,
    current_team_id INT REFERENCES team(id)
);

CREATE TABLE transfer (
    id SERIAL PRIMARY KEY,
    player_id INT NOT NULL REFERENCES player(id),
    from_team_id INT REFERENCES team(id),
    to_team_id INT REFERENCES team(id),
    transfer_date DATE,
    transfer_fee DECIMAL(15, 2)
);
