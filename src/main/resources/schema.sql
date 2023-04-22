IF NOT EXISTS(
        SELECT *
        FROM INFORMATION_SCHEMA.TABLES
        WHERE TABLE_SCHEMA = 'app'
          AND TABLE_NAME = 'user')
CREATE TABLE app.[user]
(
    id       INT IDENTITY (1,1) PRIMARY KEY,
    username VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(20)        NOT NULL
);

IF NOT EXISTS(
        SELECT *
        FROM INFORMATION_SCHEMA.TABLES
        WHERE TABLE_SCHEMA = 'app'
          AND TABLE_NAME = 'group')
CREATE TABLE app.[group]
(
    id         INT IDENTITY (1,1) PRIMARY KEY,
    group_name VARCHAR(20) UNIQUE NOT NULL
);

IF NOT EXISTS(
        SELECT *
        FROM INFORMATION_SCHEMA.TABLES
        WHERE TABLE_SCHEMA = 'app'
          AND TABLE_NAME = 'membership')
CREATE TABLE app.membership
(
    member_id   INT NOT NULL,
    group_id    INT NOT NULL,
    joined_time TIME,
    PRIMARY KEY (member_id, group_id),
    FOREIGN KEY (member_id)
        REFERENCES app.[user] (id)
        ON DELETE CASCADE,
    FOREIGN KEY (group_id)
        REFERENCES app.[group] (id)
        ON DELETE CASCADE
);

