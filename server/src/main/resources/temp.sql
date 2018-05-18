DELETE FROM availability;

-- RESTAUTANT 1
--

-- DAY 14
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56101', 1, '2018-05-14 10:00:00.000000', '2018-05-14 12:00:00.000000');
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56102', 1, '2018-05-14 14:00:00.000000', '2018-05-14 20:00:00.000000');
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56103', 1, '2018-05-14 21:00:00.000000', '2018-05-14 23:00:00.000000');
-- DAY 15
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56104', 1, '2018-05-15 10:00:00.000000', '2018-05-15 13:00:00.000000');
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56105', 1, '2018-05-15 14:00:00.000000', '2018-05-15 21:00:00.000000');
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56106', 1, '2018-05-15 22:00:00.000000', '2018-05-15 23:00:00.000000');
-- DAY 16
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56108', 1, '2018-05-16 10:30:00.000000', '2018-05-16 22:30:00.000000');
-- DAY 17
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56109', 1, '2018-05-17 10:00:00.000000', '2018-05-17 23:00:00.000000');

--
-- RESTAUTANT 2
--

-- DAY14
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56201', 2, '2018-05-14 10:00:00.000000', '2018-05-14 15:00:00.000000');
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56202', 2, '2018-05-14 18:00:00.000000', '2018-05-14 22:00:00.000000');
-- DAY15
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56204', 2, '2018-05-15 10:00:00.000000', '2018-05-15 12:00:00.000000');
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56205', 2, '2018-05-15 14:00:00.000000', '2018-05-15 21:00:00.000000');
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56206', 2, '2018-05-15 22:00:00.000000', '2018-05-15 23:00:00.000000');
-- DAY16
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56208', 2, '2018-05-16 10:00:00.000000', '2018-05-16 23:00:00.000000');
-- DAY 17
INSERT INTO availability (id, restaurant_id, start_time, end_time) VALUES ('24afd818-fc2a-45c1-a346-d93429b56209', 2, '2018-05-17 10:00:00.000000', '2018-05-17 23:00:00.000000');


SELECT * FROM availability;