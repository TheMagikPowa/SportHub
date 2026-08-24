INSERT INTO user (email, password) VALUES                                                   
('john.smith@example.com', '$2a$10$uQmQm8Yp9uJtFqz8uQ0QmO8rYH1zV7uYv2m6Q8p9Yb1tHqz8uJtFq'), 
('michael.brown@example.com', '$2a$10$wH1zV7uYv2m6Q8p9Yb1tHqz8uQ0Qm6rYH1zV7uJtFqz8uJtFqz8u'), 
('emma.johnson@example.com', '$2a$10$$2a$10$uYv2m6Q8p9Yb1tHqz8uJtFqz8uQ0Qm6rYH1zV7u7h9kPq2sJtFqz8'), 
('olivia.williams@example.com', '$2a$10$$2a$10$uYv2m6Q8p9Yb1tHqz8uJtFqz8uQ0Qm6rYH1zV7u7h9kPq2sJtFqz8'),
('daniel.jones@example.com', '$2a$10$$2a$10$uYv2m6Q8p9Yb1tHqz8uJtFqz8uQ0Qm6rYH1zV7u7h9kPq2sJtFqz8'), 
('sophia.miller@example.com', '$2a$10$uYv2m6Q8p9Yb1tHqz8uJtFqz8uQ0Qm6rYH1zV7u7h9kPq2sJtFqz8'), 
('james.davis@example.com', '$2a$10$uYv2m6Q8p9Yb1tHqz8uJtFqz8uQ0Qm6rYH1zV7u7h9kPq2sJtFqz8'), 
('amelia.garcia@example.com', '$2a$10$uYv2m6Q8p9Yb1tHqz8uJtFqz8uQ0Qm6rYH1zV7u7h9kPq2sJtFqz8'), 
('william.martin@example.com', '$2a$10$uYv2m6Q8p9Yb1tHqz8uJtFqz8uQ0Qm6rYH1zV7u7h9kPq2sJtFqz8'), 
('charlotte.thompson@example.com', '$2a$10$uYv2m6Q8p9Yb1tHqz8uJtFqz8uQ0Qm6rYH1zV7u7h9kPq2sJtFqz8'); 


INSERT INTO people (user_id, username, name, surname, dob, gender, role) VALUES
(1, 'john.smith', 'John', 'Smith', '1990-04-12', 'M', 'ADMIN'),
(2, 'michael.brown', 'Michael', 'Brown', '1988-09-23', 'M', 'STAFF'),
(3, 'emma.johnson', 'Emma', 'Johnson', '1995-01-17', 'F', 'BUYER'),
(4, 'olivia.williams', 'Olivia', 'Williams', '1992-07-05', 'F', 'BUYER'),
(5, 'daniel.jones', 'Daniel', 'Jones', '1985-11-30', 'M', 'BUYER'),
(6, 'sophia.miller', 'Sophia', 'Miller', '1998-03-14', 'F', 'BUYER'),
(7, 'james.davis', 'James', 'Davis', '1991-06-22', 'M', 'BUYER'),
(8, 'amelia.garcia', 'Amelia', 'Garcia', '1996-02-09', 'F', 'BUYER'),
(9, 'william.martin', 'William', 'Martin', '1989-12-01', 'M', 'BUYER'),
(10, 'charlotte.thompson', 'Charlotte', 'Thompson', '1997-08-18', 'F', 'BUYER');



INSERT INTO buyers (user_people_id, active) VALUES
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1);


INSERT INTO events (buyers_user_people_id, title, text, event_date, category) VALUES
(3, 'Park Ride', 'Amateur cycling event in the city park.', '2026-09-10', 'CYCLING'),
(4, 'Charity Match', 'Football match organized to raise funds.', '2026-09-15', 'FOOTBALL'),
(5, 'Indoor Climbing Session', 'Beginner-friendly indoor climbing activity.', '2026-09-20', 'CLIMBING'),
(6, 'MMA Training', 'Introductory mixed martial arts training session.', '2026-09-25', 'COMBAT SPORTS'),
(7, 'Weightlifting Workshop', 'Basic techniques and training for weightlifting.', '2026-10-01', 'WEIGHTLIFTING'),
(8, 'Open Water Swimming', 'Swimming practice session at the lake.', '2026-10-05', 'WATER SPORTS'),
(9, 'Equestrian Competition', 'Amateur-level equestrian competition.', '2026-10-12', 'EQUESTRIAN SPORTS'),
(10, 'Nature Walk', 'Guided hike through the natural reserve.', '2026-10-18', 'OTHER');


INSERT INTO questions_qa (buyers_people_user_id, title, message, state) VALUES
(3, 'Order issue', 'I placed an order but I cannot see the confirmation in my profile.', 'OPEN'),
(4, 'Defective product', 'The product I received has a clear defect. How should I proceed?', 'OPEN'),
(5, 'Shipping times', 'I would like to know the estimated delivery time for my order.', 'OPEN'),
(6, 'Size change', 'Can I change the size of the product I purchased?', 'OPEN'),
(7, 'Payment declined', 'The payment was declined, but my card is working correctly.', 'OPEN'),
(8, 'Refund request', 'I would like to request a refund for a cancelled order.', 'OPEN'),
(9, 'Product information', 'I would like more technical details about the product I am considering.', 'OPEN'),
(10, 'Checkout error', 'I receive an error during checkout. Can you help me?', 'OPEN');

INSERT INTO addresses (buyers_people_user_id, country, province, street, street_number, postal_code, phone_number) VALUES
(3, 'USA', 'CA', 'Maple Street', '12A', '90210', 15551234567),
(4, 'USA', 'NY', 'Broadway Avenue', '221', '10001', 15557654321),
(5, 'USA', 'TX', 'Oakwood Drive', '45', '73301', 15559871234),
(6, 'USA', 'FL', 'Sunset Boulevard', '7B', '33101', 15553456789),
(7, 'USA', 'IL', 'Lakeview Road', '89', '60601', 15558765432),
(8, 'USA', 'WA', 'Pine Street', '304', '98101', 15552349876),
(9, 'USA', 'CO', 'Aspen Lane', '16C', '80014', 15555678901),
(10, 'USA', 'MA', 'Harbor Street', '5', '02108', 15551239876);

INSERT INTO events_answers (buyers_user_people_id, event_id, text) VALUES
(3, 1, 'I will join the event. Looking forward to it!'),
(4, 2, 'Sounds great, I am interested in participating.'),
(5, 3, 'I am a beginner, so this session is perfect for me.'),
(6, 4, 'I would like to attend the MMA training.'),
(7, 5, 'I am excited to learn more about weightlifting techniques.'),
(8, 6, 'I love open water swimming, count me in.'),
(9, 7, 'I would like to register for the equestrian competition.'),
(10, 8, 'The nature walk sounds amazing, I will be there.');

INSERT INTO cart (buyers_people_user_id) VALUES
(3),
(4),
(5),
(6),
(7),
(8),
(9),
(10);

INSERT INTO products (name, category, gender, age_category, price, quantity, description) VALUES
('Road Cycling Helmet', 'CYCLING', 'UNISEX', 'ADULTS', 79.99, 25, 'Lightweight road helmet with advanced ventilation and impact protection.'),
('Football Training Shoes', 'FOOTBALL', 'M', 'ADULTS', 59.90, 40, 'Durable football shoes designed for training on grass and turf fields.'),
('Kids Climbing Harness', 'CLIMBING', 'UNISEX', 'KIDS', 45.50, 15, 'Safety-certified climbing harness suitable for children aged 6–12.'),
('MMA Gloves Pro', 'COMBAT SPORTS', 'UNISEX', 'ADULTS', 35.00, 60, 'Professional-grade MMA gloves with reinforced padding for training and sparring.'),
('Weightlifting Belt', 'WEIGHTLIFTING', 'UNISEX', 'ADULTS', 49.99, 30, 'Sturdy leather belt providing strong lumbar support during heavy lifts.'),
('Open Water Swim Goggles', 'WATER SPORTS', 'UNISEX', 'ADULTS', 24.99, 50, 'Anti-fog goggles designed for open water swimming with wide peripheral vision.'),
('Equestrian Riding Boots', 'EQUESTRIAN SPORTS', 'F', 'ADULTS', 120.00, 20, 'High-quality leather riding boots offering comfort and stability.'),
('Kids Football Ball', 'FOOTBALL', 'UNISEX', 'KIDS', 19.99, 80, 'Lightweight football ball designed for children and youth training.'),
('Climbing Chalk Bag', 'CLIMBING', 'UNISEX', 'ADULTS', 14.50, 70, 'Compact chalk bag with adjustable strap and secure closure.'),
('Fitness Resistance Bands Set', 'OTHER', 'UNISEX', 'ADULTS', 29.90, 100, 'Set of five resistance bands suitable for strength training and mobility exercises.');

INSERT INTO cart_has_products (cart_buyers_people_user_id, products_id, quantity) VALUES
(3, 1, 1),   
(3, 10, 2),  

(4, 2, 1),  
(4, 8, 1),   

(5, 3, 1),   
(5, 9, 1),  

(6, 4, 1),   
(6, 10, 1),  

(7, 5, 1),   
(7, 9, 2),   

(8, 6, 1),   
(8, 10, 1),  

(9, 7, 1),   

(10, 8, 1),  
(10, 6, 1);  

INSERT INTO orders (buyers_people_user_id, discount_percent, status, total) VALUES
(3, 0, 'DELIVERED', 109.98),   
(4, 10, 'SHIPPED', 71.91),     
(5, 0, 'DELIVERED', 60.00),    
(6, 5, 'PROCESSING', 57.00),   
(7, 0, 'DELIVERED', 79.99),    
(8, 0, 'SHIPPED', 54.89),     
(9, 15, 'DELIVERED', 102.00),  
(10, 0, 'PROCESSING', 44.98);  

INSERT INTO orders_has_products (orders_id, products_id, name, unit_price, final_price, quantity) VALUES

(1, 1, 'Road Cycling Helmet', 79.99, 79.99, 1),
(1, 10, 'Fitness Resistance Bands Set', 29.90, 29.90, 1),


(2, 2, 'Football Training Shoes', 59.90, 53.91, 1),
(2, 8, 'Kids Football Ball', 19.99, 17.99, 1),


(3, 3, 'Kids Climbing Harness', 45.50, 45.50, 1),
(3, 9, 'Climbing Chalk Bag', 14.50, 14.50, 1),


(4, 4, 'MMA Gloves Pro', 35.00, 33.25, 1),
(4, 10, 'Fitness Resistance Bands Set', 29.90, 28.40, 1),


(5, 5, 'Weightlifting Belt', 49.99, 49.99, 1),
(5, 9, 'Climbing Chalk Bag', 14.50, 14.50, 2),


(6, 6, 'Open Water Swim Goggles', 24.99, 24.99, 1),
(6, 10, 'Fitness Resistance Bands Set', 29.90, 29.90, 1),


(7, 7, 'Equestrian Riding Boots', 120.00, 102.00, 1),


(8, 8, 'Kids Football Ball', 19.99, 19.99, 1),
(8, 6, 'Open Water Swim Goggles', 24.99, 24.99, 1);

