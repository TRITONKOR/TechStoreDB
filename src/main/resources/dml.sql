INSERT INTO clients(username, password)
VALUES ('tritonkor', 'password'),
       ('sasha', 'sasha'),
       ('root', 'root');

INSERT INTO techniques(price, company, model)
VALUES (1200.00, 'ASUS', 'Phen'),
       (1003.00, 'samsung', 'iphone');

INSERT INTO reviews(owner_id, technique_id, text, grade, create_time)
VALUES (1, 1, 'parasha', 2, CURRENT_TIME),
       (2, 1, 'topchik', 10, CURRENT_TIME),
       (3, 2, 'china', 8, CURRENT_TIME);

INSERT INTO techniques_reviews(technique_id, review_id)
VALUES (1, 1),
       (1, 2);