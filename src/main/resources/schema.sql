CREATE TABLE IF NOT EXISTS member (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    modified_at TIMESTAMP DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS holiday (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       date DATE NOT NULL,
                                       name VARCHAR(255) NOT NULL,
    is_holiday BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    modified_at TIMESTAMP DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS room (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(255),
    max_number_of_people INT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    modified_at TIMESTAMP DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS reservation (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           reservation_date DATE NOT NULL,
                                           start_time INT NOT NULL,
                                           end_time INT NOT NULL,
                                           member_id BIGINT NOT NULL,
                                           room_id BIGINT,
                                           number_of_people INT NOT NULL,
                                           created_at TIMESTAMP DEFAULT NOW(),
    modified_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (room_id) REFERENCES room(id)
    );

CREATE TABLE IF NOT EXISTS canceled_reservation (
                                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                    reservation_date DATE,
                                                    start_time INT,
                                                    end_time INT,
                                                    member_id BIGINT,
                                                    number_of_people INT NOT NULL,
                                                    created_at TIMESTAMP DEFAULT NOW(),
    modified_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (member_id) REFERENCES member(id)
    );
