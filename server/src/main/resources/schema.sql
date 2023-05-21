CREATE TABLE IF NOT EXISTS app.user
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(20) UNIQUE NOT NULL,
    email      VARCHAR(50) NOT NULL,
    first_name VARCHAR(20)        NOT NULL,
    last_name  VARCHAR(20)        NOT NULL,
    avatar_url VARCHAR(50)        NOT NULL,
    password   CHAR(60)           NOT NULL
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS app.group
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(20) UNIQUE NOT NULL,
    creator_id   INT                NOT NULL,
    avatar_url VARCHAR(50)        NOT NULL,
    created_at DATETIME DEFAULT NOW(),
    FOREIGN KEY (creator_id)
        REFERENCES app.user (id)
        ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS app.membership
(
    member_id  INT NOT NULL,
    group_id   INT NOT NULL,
    created_at DATETIME DEFAULT NOW(),
    PRIMARY KEY (member_id, group_id),
    FOREIGN KEY (member_id)
        REFERENCES app.user (id)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (group_id)
        REFERENCES app.group (id)
        ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS app.group_message
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    sender_id   INT NOT NULL,
    receiver_id INT NOT NULL,
    created_at  DATETIME DEFAULT NOW(),
    content     TINYTEXT,
    FOREIGN KEY (sender_id)
        REFERENCES app.user (id)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (receiver_id)
        REFERENCES app.group (id)
        ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS app.user_message
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    sender_id   INT NOT NULL,
    receiver_id INT NOT NULL,
    created_at  DATETIME DEFAULT NOW(),
    content     TINYTEXT,
    FOREIGN KEY (sender_id)
        REFERENCES app.user (id)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (receiver_id)
        REFERENCES app.user (id)
        ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = INNODB;


