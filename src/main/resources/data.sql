-- clean all records
DELETE
FROM app.membership;
DELETE
FROM app.[group];
DELETE
FROM app.[user];

-- add users and groups
INSERT INTO app.[user] (username, password)
VALUES ('user1', 'password1'),
       ('user2', 'password2'),
       ('user3', 'password3');

INSERT INTO app.[group] (group_name)
VALUES ('group1'),
       ('group2'),
       ('group3');

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
