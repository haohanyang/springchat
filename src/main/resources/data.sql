DELETE app.group_message;
DELETE app.user_message;
DELETE app.membership;
DELETE app.[user];
DELETE app.[group];

-- create users
INSERT INTO app.[user] (username, password)
VALUES ('user1', CONVERT(VARCHAR(32), HASHBYTES('MD5', 'password1'), 2)),
       ('user2', CONVERT(VARCHAR(32), HASHBYTES('MD5', 'password2'), 2)),
       ('user3', CONVERT(VARCHAR(32), HASHBYTES('MD5', 'password3'), 2)),
       ('user4', CONVERT(VARCHAR(32), HASHBYTES('MD5', 'password4'), 2));

-- create groups
INSERT INTO app.[group] (group_name)
VALUES ('group1'),
       ('group2'),
       ('group3'),
       ('group4');

-- user1 joined group1 and group2
INSERT INTO app.membership (member_id, group_id, joined_time)
SELECT u.id, g.id, GETDATE()
FROM app.[group] g
         CROSS JOIN app.[user] u
WHERE u.username = 'user1'
  AND g.group_name IN ('group1', 'group2')

-- user2 joined group2 and group3
INSERT INTO app.membership (member_id, group_id, joined_time)
SELECT u.id, g.id, GETDATE()
FROM app.[group] g
         CROSS JOIN app.[user] u
WHERE u.username = 'user2'
  AND g.group_name IN ('group2', 'group3')

-- user3 joined group1 and group3
INSERT INTO app.membership (member_id, group_id, joined_time)
SELECT u.id, g.id, GETDATE()
FROM app.[group] g
         CROSS JOIN app.[user] u
WHERE u.username = 'user3'
  AND g.group_name IN ('group1', 'group3')

