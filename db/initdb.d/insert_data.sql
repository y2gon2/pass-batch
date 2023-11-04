INSERT INTO package (package_name, count, period, created_at)
VALUES ('Starter PT 10', 10, 60, '2022-08-01 00:00:00'),
       ('Starter PT 20', 20, 120, '2022-08-01 00:00:00'),
       ('Starter PT 30', 30, 180, '2022-08-01 00:00:00'),
       ('Event!! Free Pilates 1', 1, NULL, '2022-08-01 00:00:00'),
       ('Body Profile Challenge PT 4 weeks', NULL, 28, '2022-08-01 00:00:00'),
       ('Body Profile Challenge PT 8 weeks', NULL, 48, '2022-08-01 00:00:00'),
       ('Consulting - about your inbody', NULL, NULL, '2022-08-01 00:00:00');

INSERT INTO `user` (user_id, user_name, status, phone, meta, created_at)
VALUES ('A1000000', 'Yeong Woo', 'ACTIVE', '01011112222', NULL, '2022-08-01 00:00:00'),
       ('A1000001', 'Soo Yeon', 'ACTIVE', '01033334444', NULL, '2022-08-01 00:00:00'),
       ('A1000002', 'Jun Ho', 'INACTIVE', '01055556666', NULL, '2022-08-01 00:00:00'),
       ('B1000010', 'Min Woo', 'ACTIVE', '01077778888', NULL, '2022-08-01 00:00:00'),
       ('B1000011', 'Gra Mi', 'INACTIVE', '01088889999', NULL, '2022-08-01 00:00:00'),
       ('B2000000', 'Sun', 'ACTIVE', '01099990000', NULL, '2022-08-01 00:00:00'),
       ('B2000001', 'Su Mi', 'ACTIVE', '01000001111', NULL, '2022-08-01 00:00:00');

INSERT INTO user_group_mapping (user_group_id, user_id, user_group_name, description, created_at)
VALUES ('HANBADA', 'A1000000', 'BADA', 'BDDA Corp. Group', '2022-08-01 00:00:00'),
       ('HANBADA', 'A1000001', 'BADA', 'BDDA Corp. Group', '2022-08-01 00:00:00'),
       ('HANBADA', 'A1000002', 'BADA', 'BDDA Corp. Group', '2022-08-01 00:00:00'),
       ('HANBADA', 'B1000010', 'BADA', 'BDDA Corp. Group', '2022-08-01 00:00:00'),
       ('HANBADA', 'B2000000', 'BADA', 'BDDA Corp. Group', '2022-08-01 00:00:00'),
       ('TAESAN', 'B2000001', 'Big Mount.', 'TEASAN corp.Group', '2022-08-01 00:00:00');

INSERT INTO pass (package_seq, user_id, status, remaining_count, started_at, ended_at, expired_at, created_at)
VALUES ('1', 'A1000000', 'PROGRESSED', '5', '2023-10-01 00:00:00', NULL, '2023-12-01 00:00:00', '2023-10-01 00:00:00'),
       ('4', 'A1000000', 'READY', '1', '2023-11-11 00:00:00', NULL, NULL, '2023-10-01 00:00:00'),
       ('6', 'A1000000', 'EXPIRED', NULL, '2023-08-01 00:00:00', '2023-09-25 00:00:00', '2023-09-30 00:00:00', '2023-10-01 00:00:00')


