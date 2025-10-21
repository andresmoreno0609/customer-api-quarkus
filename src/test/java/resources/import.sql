-- CATALOGS
INSERT INTO catalog_gender (id, name) VALUES (1, 'MALE');
INSERT INTO catalog_gender (id, name) VALUES (2, 'FEMALE');
INSERT INTO catalog_gender (id, name) VALUES (3, 'OTHER');

INSERT INTO catalog_status (id, name) VALUES (1, 'ACTIVE');
INSERT INTO catalog_status (id, name) VALUES (2, 'INACTIVE');

INSERT INTO catalog_country (id, name) VALUES (1, 'COLOMBIA');
INSERT INTO catalog_country (id, name) VALUES (2, 'MEXICO');
INSERT INTO catalog_country (id, name) VALUES (3, 'ARGENTINA');
INSERT INTO catalog_country (id, name) VALUES (4, 'CHILE');
INSERT INTO catalog_country (id, name) VALUES (5, 'PERU');

-- SAMPLE CLIENT
INSERT INTO client
(id, name, birth_date, gender_id, num_cta, status_id, country_id, activated_date, inactivated_date, created_at, updated_at)
VALUES
(1, 'Juan Pérez', '1990-05-14', 1, 'CT1234567890', 1, 1, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
