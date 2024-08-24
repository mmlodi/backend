-- Insert Users
INSERT INTO user_main (user_name, created_at, changed_at, password, email) 
VALUES 
('john_doe', '2024-08-18', '2024-08-18', 'password123', 'john.doe@example.com'),
('jane_smith', '2024-08-18', '2024-08-18', 'password456', 'jane.smith@example.com'),
('alice_jones', '2024-08-18', '2024-08-18', 'password789', 'alice.jones@example.com');

-- Insert Categories
INSERT INTO category (category_name, category_type, user_id) 
VALUES 
('Utilities', 'BOLETO', 1),
('Entertainment', 'CREDITO', 1),
('Groceries', 'BOLETO', 2);

-- Insert Transactions
INSERT INTO transaction (category_id, transaction_name, description, user_id, created_at, changed_at) 
VALUES 
(1, 'Electric Bill',  'Monthly electric bill payment', 1, '2024-08-18', '2024-08-18'),
(1, 'Netflix Subscription',  'Monthly Netflix subscription', 1, '2024-08-18', '2024-08-18'),
(2, 'Grocery Shopping', 'Weekly grocery shopping', 2, '2024-08-18', '2024-08-18');
