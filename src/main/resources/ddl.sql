DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS techniques;
DROP TABLE IF EXISTS reviews;

CREATE TABLE clients
    (
        PRIMARY KEY (id),
        id INT NOT NULL AUTO_INCREMENT,
        username VARCHAR(32) NOT NULL,
        password VARCHAR(32) NOT NULL
);

CREATE TABLE techniques
(
    PRIMARY KEY (id),
    id INT NOT NULL AUTO_INCREMENT,
    price DOUBLE NOT NULL,
    company VARCHAR(32) NOT NULL,
    model VARCHAR(32) NOT NULL
);

CREATE TABLE reviews
(
    PRIMARY KEY (id),
    id INT NOT NULL AUTO_INCREMENT,
    owner_id INT NOT NULL,
    technique_id INT NOT NULL,
    text VARCHAR(128) NOT NULL,
    grade INT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);