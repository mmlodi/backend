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

INSERT INTO monthly_data (month, year)
SELECT m.month, y.year
FROM (
    SELECT 1 AS month UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
    UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12
) m
CROSS JOIN (
    SELECT 2020 AS year UNION ALL SELECT 2021 UNION ALL SELECT 2022 UNION ALL SELECT 2023
    UNION ALL SELECT 2024 UNION ALL SELECT 2025 UNION ALL SELECT 2026 UNION ALL SELECT 2027
    UNION ALL SELECT 2028 UNION ALL SELECT 2029 UNION ALL SELECT 2030 UNION ALL SELECT 2031
    UNION ALL SELECT 2032 UNION ALL SELECT 2033 
) y
ORDER BY y.year, m.month;

-- Insert Transactions
INSERT INTO transaction (category_id, transaction_name, description, user_id, created_at, changed_at, transaction_value, transaction_budget, month_data_id) 
VALUES 
(1, 'Electric Bill', 'Monthly electric bill payment', 1, '2024-08-18', '2024-08-18', 120.50, 150.00,53),
(1, 'Netflix Subscription', 'Monthly Netflix subscription', 1, '2024-08-18', '2024-08-18', 15.99, 20.00,54),
(1, 'Grocery Shopping', 'Weekly grocery shopping', 1, '2024-08-18', '2024-08-18', 230.45, 250.00,55),
(1, 'Dining Out', 'Dinner at a restaurant', 1, '2024-08-19', '2024-08-19', 85.00, 100.00,56),
(2, 'Gym Membership', 'Monthly gym membership fee', 1, '2024-08-20', '2024-08-20', 50.00, 60.00,53),
(2, 'Car Maintenance', 'Quarterly car maintenance', 1, '2024-08-21', '2024-08-21', 350.00, 400.00,54),
(2, 'Vacation Savings', 'Monthly savings for vacation', 2, '2024-08-22', '2024-08-22', 200.00, 200.00,55),
(3, 'Book Purchase', 'Purchase of books', 2, '2024-08-23', '2024-08-23', 45.50, 50.00,53),
(3, 'Movie Night', 'Tickets for a movie night', 2, '2024-08-24', '2024-08-24', 25.00, 30.00,54),
(3, 'Pet Supplies', 'Buying supplies for pets', 2, '2024-08-25', '2024-08-25', 60.75, 70.00,55);

