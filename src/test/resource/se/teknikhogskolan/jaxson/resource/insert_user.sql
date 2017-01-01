INSERT INTO Team (id, created, active, name) VALUES (1, '2016-11-11', true, 'Light side');
INSERT INTO Team (id, created, active, name) VALUES (2, '2016-11-11', true, 'Dark side');

INSERT INTO User (id, userNumber, active, username, firstName, lastName) VALUES (10, 1, true, 'Robotarm Luke', 'Luke', 'Skywalker');
INSERT INTO User (id, userNumber, active, username, firstName, lastName) VALUES (11, 2, true, 'I am your father', 'Darth', 'Vader');
INSERT INTO User (id, userNumber, active, username, firstName, lastName) VALUES (12, 3, true, 'I am your sister', 'Leia', 'Skywalker');
INSERT INTO User (id, userNumber, active, username, firstName, lastName) VALUES (13, 4, true, 'Master Yoda', 'Yoda', '');

INSERT INTO WorkItem (id, created, description, user_id) VALUES (1, '2016-11-11', 'Destroy deathstar', 10);
INSERT INTO WorkItem (id, created, description, user_id) VALUES (2, '2016-11-11', 'Train to be a yedi', 10);
