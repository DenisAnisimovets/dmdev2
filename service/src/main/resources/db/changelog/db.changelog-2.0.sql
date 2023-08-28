--liquibase formatted sql
--changeset danis:1
INSERT INTO good (id, good_name, price, quantity, image) VALUES
    (1, 'Paper', 100, 12, '/api/v1/goods/1/avatar'),
    (2, 'Water', 120, 23, '/api/v1/goods/2/avatar'),
    (3, 'Bread', 30, 3, '/api/v1/goods/3/avatar'),
    (4, 'Oil', 49, 9, '/api/v1/goods/4/avatar'),
    (5, 'Sugar', 17, 25, '/api/v1/goods/5/avatar');

SELECT SETVAL('good_id_seq', (SELECT MAX(id) FROM good));
--rollback DELETE FROM good WHERE id in(1, 2, 3, 4, 5);