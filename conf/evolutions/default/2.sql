# --- !Ups
INSERT INTO app(id, when_created, when_updated, status, type, name, api_key) VALUES ('63ba80bb-736d-4ec7-9683-456dee0861f7', 1505994653000, 1505994653000,  'APPROVED', 'WEB', 'Сайт', '139e747a-5a75-4d3f-a55c-9b9678f11290');
INSERT INTO app(id, when_created, when_updated, status, type, name, api_key) VALUES ('a3dc7b16-6acd-4696-ab45-288a3a44d57a', 1505994653000, 1505994653000, 'APPROVED', 'MOBILE', 'Приложение', '642a4c73-b2f6-4154-988f-0287326a78e4');

INSERT INTO role(role) VALUES ('edit'), ('create'), ('deleted'), ('view');
# --- !Downs
DELETE FROM app;
DELETE FROM users_role;
DELETE FROM role;