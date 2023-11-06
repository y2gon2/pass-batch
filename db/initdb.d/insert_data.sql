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
VALUES ('BADA', 'A1000000', 'BADA', 'BDDA Corp. Group', '2022-08-01 00:00:00'),
       ('BADA', 'A1000001', 'BADA', 'BDDA Corp. Group', '2022-08-01 00:00:00'),
       ('BADA', 'A1000002', 'BADA', 'BDDA Corp. Group', '2022-08-01 00:00:00'),
       ('BADA', 'B1000010', 'BADA', 'BDDA Corp. Group', '2022-08-01 00:00:00'),
       ('BADA', 'B2000000', 'BADA', 'BDDA Corp. Group', '2022-08-01 00:00:00'),
       ('TAESAN', 'B2000001', 'Big Mount.', 'TEASAN corp.Group', '2022-08-01 00:00:00');

INSERT INTO pass (package_seq, user_id, status, remaining_count, started_at, ended_at, expired_at, created_at)
VALUES ('1', 'A1000000', 'PROGRESSED', '5', '2023-10-01 00:00:00', NULL, '2023-12-01 00:00:00', '2023-10-01 00:00:00'),
       ('4', 'A1000000', 'READY', '1', '2023-11-11 00:00:00', NULL, NULL, '2023-10-01 00:00:00'),
       ('6', 'A1000000', 'EXPIRED', NULL, '2023-08-01 00:00:00', '2023-09-25 00:00:00', '2023-09-30 00:00:00', '2023-10-01 00:00:00');

INSERT INTO bulk_pass (package_seq, user_group_id, status, count, started_at, ended_at, created_at, modified_at)
VALUES ('1', 'BADA', 'READY', NULL, '2023-12-01 00:00:00', '2024-12-01 00:00:00', '2023-12-01 00:00:00', NULL),
       ('2', 'BADA', 'READY', 1, '2023-12-01 00:00:00', '2024-12-01 00:00:00', '2023-12-01 00:00:00', NULL),
       ('3', 'TAESAN', 'COMPLETED', 8, '2022-12-01 00:00:00', '2024-10-01 00:00:00', '2023-12-01 00:00:00', NULL),
       ('4', 'BADA', 'COMPLETED', 10, '2023-01-01 00:00:00', '2024-02-01 00:00:00', '2023-12-01 00:00:00', NULL),
       ('5', 'BADA', 'COMPLETED', NULL, '2023-02-01 00:00:00', '2024-09-01 00:00:00', '2023-12-01 00:00:00', NULL),
       ('6', 'TAESAN', 'READY', 16, '2023-12-01 00:00:00', '2024-02-01 00:00:00', '2023-12-01 00:00:00', NULL),
       ('7', 'BADA', 'READY', 20, '2023-12-01 00:00:00', '2024-08-01 00:00:00', '2023-12-01 00:00:00', NULL);

-- web 통계 화면 확인용
INSERT INTO statistics (statistics_seq, statistics_at, all_count, attended_count, cancelled_count)
VALUES ('1', '2023-11-01 00:00:00', '130', '100', '30'),
       ('2', '2023-11-02 00:00:00', '119', '79', '40'),
       ('3', '2023-11-03 00:00:00', '116', '101', '15'),
       ('4', '2023-11-04 00:00:00', '155', '130', '25'),
       ('5', '2023-11-05 00:00:00', '134', '80', '54'),
       ('6', '2023-11-06 00:00:00', '156', '136', '20'),
       ('7', '2023-11-07 00:00:00', '90', '80', '10'),
       ('8', '2023-11-09 00:00:00', '0', '0', '0'),
       ('9', '2023-11-10 00:00:00', '149', '146', '13'),
       ('10', '2023-11-11 00:00:00', '130', '100', '30'),
       ('11', '2023-11-12 00:00:00', '140', '110', '30'),
       ('12', '2023-11-13 00:00:00', '160', '134', '26'),
       ('13', '2023-11-14 00:00:00', '114', '100', '14');

-- JobLauncher 를 적용, REST API 로 취소 요청이 추가됨을 확인하기 위한 data
INSERT INTO booking (pass_seq, user_id, status, used_pass, attended, started_at, ended_at, cancelled_at, created_at, modified_at)
VALUES ('1', 'A1000000', 'CANCELLED', '0', '0', '2023-11-01 00:00:00', '2023-11-09 01:01:01', '2023-11-09 00:00:00', '2023-11-09 00:00:00', NULL),
       ('2', 'A1000000', 'COMPLETED', '1', '1', '2023-11-01 00:00:00', '2023-11-09 02:02:02', NULL, '2023-11-09 00:00:00', NULL)
