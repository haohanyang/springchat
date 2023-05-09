CREATE TABLE IF NOT EXISTS app.user
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) UNIQUE NOT NULL,
    email    VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    password CHAR(32) NOT NULL
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS app.group
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(20) UNIQUE NOT NULL
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS app.membership
(
    member_id   INT NOT NULL,
    group_id    INT NOT NULL,
    joined_time TIME,
    PRIMARY KEY (member_id, group_id),
    FOREIGN KEY (member_id)
        REFERENCES app.user (id)
        ON DELETE NO ACTION ON UPDATE CASCADE ,
    FOREIGN KEY (group_id)
        REFERENCES app.group (id)
        ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS app.group_message
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    group_id  INT NOT NULL,
    sent_time TIME,
    content   TINYTEXT,
    FOREIGN KEY (sender_id)
        REFERENCES app.user (id)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (group_id)
        REFERENCES app.group (id)
        ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS app.user_message
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    sender_id   INT NOT NULL,
    receiver_id INT NOT NULL,
    sent_time   TIME,
    content     TINYTEXT,
    FOREIGN KEY (sender_id)
        REFERENCES app.user (id)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (receiver_id)
        REFERENCES app.user (id)
        ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=INNODB;


