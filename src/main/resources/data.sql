INSERT INTO member (email, password, created_at, modified_at, role) VALUES
    ('test@email.com', '1234', NOW(), NOW(), 'USER'),
    ('test2@eamil.com', '1234', NOW(), NOW(), 'USER'),
    ('admin@email.com', '1234', NOW(), NOW(), 'ADMIN');

INSERT INTO holiday (date, name, is_holiday, created_at, modified_at) VALUES
    ('2025-01-01', '신정', true, NOW(), NOW()),
    ('2025-01-28', '설날 연휴', true, NOW(), NOW()),
    ('2025-01-29', '설날', true, NOW(), NOW()),
    ('2025-01-30', '설날 연휴', true, NOW(), NOW()),
    ('2025-03-01', '삼일절', true, NOW(), NOW()),
    ('2025-05-01', '근로자의 날', true, NOW(), NOW()),
    ('2025-05-05', '어린이날', true, NOW(), NOW()),
    ('2025-05-06', '부처님 오신 날', true, NOW(), NOW()),
    ('2025-06-06', '현충일', true, NOW(), NOW()),
    ('2025-08-15', '광복절', true, NOW(), NOW()),
    ('2025-10-03', '개천절', true, NOW(), NOW()),
    ('2025-10-05', '추석 연휴', true, NOW(), NOW()),
    ('2025-10-06', '추석', true, NOW(), NOW()),
    ('2025-10-07', '추석 연휴', true, NOW(), NOW()),
    ('2025-10-09', '한글날', true, NOW(), NOW()),
    ('2025-12-25', '크리스마스', true, NOW(), NOW());

INSERT INTO room (name, max_number_of_people, created_at, modified_at) VALUES
                                                                          ('회의실 A', 8, NOW(), NOW()),
                                                                          ('회의실 B', 6, NOW(), NOW()),
                                                                          ('회의실 C', 4, NOW(), NOW()),
                                                                          ('회의실 D', 10, NOW(), NOW());

INSERT INTO reservation (reservation_date, start_time, end_time, member_id, room_id, number_of_people, created_at, modified_at) VALUES
    ('2025-06-13', 14, 16, 1, 1, 4, NOW(), NOW()),
    ('2025-06-14', 14, 16, 2, 1, 4, NOW(), NOW());

INSERT INTO canceled_reservation (reservation_date, start_time, end_time, member_id, number_of_people, created_at, modified_at) VALUES
    ('2025-06-11', 12, 14, 1, 3, NOW(), NOW());
