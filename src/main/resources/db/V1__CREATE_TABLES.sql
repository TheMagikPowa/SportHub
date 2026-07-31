
CREATE TABLE user (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(800) NOT NULL,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    
);

CREATE TABLE people (
	user_id BIGINT UNSIGNED PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    dob DATE NOT NULL,
    gender ENUM("M", "F", "OTHER") NOT NULL,
    role ENUM ("BUYER", "STAFF", "ADMIN") NOT NULL DEFAULT "BUYER",
    FOREIGN KEY (user_id) REFERENCES user(id)
		 ON DELETE CASCADE
         ON UPDATE CASCADE
);

CREATE TABLE buyers (
	user_people_id BIGINT UNSIGNED PRIMARY KEY,
    active TINYINT NOT NULL,
	FOREIGN KEY (user_people_id) REFERENCES people(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE events (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    buyers_user_people_id BIGINT UNSIGNED,
    title VARCHAR (100) NOT NULL,
    text VARCHAR (1000) NOT NULL,
    event_date DATETIME NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    category ENUM("CYCLING", "FOOTBALL", "CLIMBING", "COMBAT SPORTS", "WEIGHTLIFTING", "WATER SPORTS", "EQUESTRIAN SPORTS", "OTHER") NOT NULL,
    FOREIGN KEY (buyers_user_people_id) REFERENCES buyers(user_people_id)
		ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE questions_qa (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    buyers_people_user_id BIGINT UNSIGNED,
    title VARCHAR (50) NOT NULL,
    message VARCHAR (1000) NOT NULL,
    state ENUM("OPEN", "CLOSED") NOT NULL,
	FOREIGN KEY (buyers_people_user_id) REFERENCES buyers(user_people_id)
		ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE TABLE messages_qa (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	question_id BIGINT UNSIGNED NOT NULL,
    staff_people_user_id BIGINT UNSIGNED,
    text VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES questions_qa(id)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
	FOREIGN KEY (staff_people_user_id) REFERENCES people(user_id)
		ON DELETE SET NULL
        ON UPDATE CASCADE
        
);

CREATE TABLE addresses (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	buyers_people_user_id BIGINT UNSIGNED,
    country VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    province VARCHAR(2) NOT NULL,
    street VARCHAR(50) NOT NULL,
    street_number VARCHAR(10) NOT NULL,
    postal_code VARCHAR(5) NOT NULL,
    phone_number BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (buyers_people_user_id) REFERENCES buyers(user_people_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE events_answers (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    buyers_user_people_id BIGINT UNSIGNED,
    text VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (buyers_user_people_id) REFERENCES buyers(user_people_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE event_board (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	events_id BIGINT UNSIGNED,
    events_answers_id BIGINT UNSIGNED,
		FOREIGN KEY (events_id) REFERENCES events(id)
			ON DELETE CASCADE
            ON UPDATE CASCADE,
        FOREIGN KEY (events_answers_id) REFERENCES events_answers(id)
			ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE cart (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	buyers_people_user_id BIGINT UNSIGNED UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (buyers_people_user_id) REFERENCES buyers(user_people_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE products (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    category ENUM("CYCLING", "FOOTBALL", "CLIMBING", "COMBAT SPORTS", "WEIGHTLIFTING", "WATER SPORTS", "EQUESTRIAN SPORTS", "OTHER") NOT NULL,
    gender ENUM('M', 'F', 'UNISEX') NOT NULL,
    age_category ENUM('KIDS', 'ADULTS') NOT NULL,
    price DECIMAL(8,2) NOT NULL CHECK (price >= 0),
    quantity INT UNSIGNED NOT NULL DEFAULT 0,
    description VARCHAR(2000) NOT NULL
);

CREATE TABLE cart_has_products (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    cart_buyers_people_user_id BIGINT UNSIGNED,
    products_id BIGINT UNSIGNED,
    quantity INT UNSIGNED,
    FOREIGN KEY (cart_buyers_people_user_id) REFERENCES cart (buyers_people_user_id)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
	FOREIGN KEY (products_id) REFERENCES products (id)
		ON DELETE CASCADE
        ON UPDATE CASCADE 
);

CREATE TABLE orders(
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    buyers_people_user_id BIGINT UNSIGNED,
    purchase_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    discount_percent INT UNSIGNED NOT NULL DEFAULT 0,
    status ENUM('PROCESSING','SHIPPED', 'DELIVERED'),
    FOREIGN KEY (buyers_people_user_id) REFERENCES buyers(user_people_id)
);

CREATE TABLE orders_has_products (
	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    orders_id BIGINT UNSIGNED,
    products_id BIGINT UNSIGNED,
    name VARCHAR(50) NOT NULL,
    unit_price DECIMAL(8,2) NOT NULL CHECK (unit_price >= 0),
    final_price DECIMAL(8,2) NOT NULL CHECK (final_price >= 0),
    quantity INT UNSIGNED NOT NULL,
    FOREIGN KEY (orders_id) REFERENCES orders(id)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
	FOREIGN KEY (products_id) REFERENCES products(id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

