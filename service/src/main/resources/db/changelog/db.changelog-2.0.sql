--liquibase formatted sql
--changeset danis:1
INSERT INTO good (id, good_name, price, quantity) VALUES
    (1, 'Paper', 100, 12),
    (2, 'Water', 120, 23),
    (3, 'Bread', 30, 3),
    (4, 'Oil', 49, 9),
    (5, 'Sugar', 17, 25);

SELECT SETVAL('good_id_seq', (SELECT MAX(id) FROM good));
--rollback DELETE FROM good WHERE id in(1, 2, 3, 4, 5);