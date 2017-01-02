INSERT INTO Team (id, created, active, name) VALUES (2001, '2016-11-11', true, 'TMNT');
INSERT INTO Team (id, created, active, name) VALUES (2002, '2016-11-11', true, 'Foot Clan');

INSERT INTO User (id, userNumber, created, active, username, firstName, lastName, team_id)
VALUES (1, 1001, '2016-11-11', true, 'Master Splinter', 'Splinter', 'Yoshi', 2001);
INSERT INTO User (id, userNumber, created, active, username, firstName, lastName, team_id)
VALUES (2, 1002, '2017-01-02', true, 'Mr Smarty pants', 'Donatello', 'di Niccolò di Betto Bardi', 2001);
INSERT INTO User (id, userNumber, created, active, username, firstName, lastName, team_id)
VALUES (3, 1003, '2017-01-02', true, 'Funny guy Mike', 'Michelangelo', 'Buonarroti', 2001);
INSERT INTO User (id, userNumber, created, active, username, firstName, lastName, team_id)
VALUES (4, 1004, '2017-01-02', true, 'Team leader', 'Leonardo', 'da Vinci', 2001);
INSERT INTO User (id, userNumber, created, active, username, firstName, lastName, team_id)
VALUES (5, 1005, '2017-01-02', true, 'Angry big guy', 'Raffaello', 'Sanzio', 2001);
INSERT INTO User (id, userNumber, created, active, username, firstName, lastName)
VALUES (7, 1007, '2016-11-11', true, 'The antagonist', 'Shredder', 'Oroku Saki');

INSERT INTO WorkItem (id, created, description, status, user_id)
VALUES (6, '2016-11-11', 'Be good role model', 'UNSTARTED', 1);