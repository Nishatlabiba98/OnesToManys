-- OnesToManys Project
-- Synthetic Seed Data: Customer (master) -> Orders (detail)
--
-- Load AFTER schema.sql:
--   sqlite3 app.db < db/schema.sql
--   sqlite3 app.db < db/data.sql

-- ── Master records: customers ────────────────────────────────────────────────

INSERT INTO customers (first_name, last_name, email, phone, city, state) VALUES
    ('Alice',   'Johnson',  'alice.johnson@example.com',  '555-0101', 'Austin',       'TX'),
    ('Bob',     'Smith',    'bob.smith@example.com',      '555-0102', 'Denver',       'CO'),
    ('Carol',   'Williams', 'carol.williams@example.com', '555-0103', 'Portland',     'OR'),
    ('David',   'Brown',    'david.brown@example.com',    '555-0104', 'Chicago',      'IL'),
    ('Eve',     'Davis',    'eve.davis@example.com',      '555-0105', 'Phoenix',      'AZ'),
    ('Frank',   'Miller',   'frank.miller@example.com',   '555-0106', 'Seattle',      'WA'),
    ('Grace',   'Wilson',   'grace.wilson@example.com',   '555-0107', 'Nashville',    'TN'),
    ('Henry',   'Moore',    'henry.moore@example.com',    '555-0108', 'Boston',       'MA'),
    ('Iris',    'Taylor',   'iris.taylor@example.com',    '555-0109', 'Miami',        'FL'),
    ('Jack',    'Anderson', 'jack.anderson@example.com',  '555-0110', 'San Diego',    'CA');

-- ── Detail records: orders ───────────────────────────────────────────────────
-- Each customer has multiple orders to demonstrate the one-to-many relationship.

-- Alice Johnson (customer_id = 1)
INSERT INTO orders (customer_id, order_date, status,       total_amount, notes) VALUES
    (1, '2024-01-05', 'delivered',  129.99, 'Gift wrap requested'),
    (1, '2024-03-18', 'delivered',   49.95, NULL),
    (1, '2024-07-22', 'shipped',    210.00, 'Expedited shipping');

-- Bob Smith (customer_id = 2)
INSERT INTO orders (customer_id, order_date, status,       total_amount, notes) VALUES
    (2, '2024-02-14', 'delivered',   75.50, 'Valentine''s Day order'),
    (2, '2024-05-01', 'delivered',  320.00, NULL),
    (2, '2024-09-30', 'processing',  89.99, NULL);

-- Carol Williams (customer_id = 3)
INSERT INTO orders (customer_id, order_date, status,       total_amount, notes) VALUES
    (3, '2024-01-20', 'delivered',   55.00, NULL),
    (3, '2024-06-15', 'cancelled',   99.00, 'Customer cancelled');

-- David Brown (customer_id = 4)
INSERT INTO orders (customer_id, order_date, status,       total_amount, notes) VALUES
    (4, '2024-03-03', 'delivered',  175.25, NULL),
    (4, '2024-04-11', 'delivered',   42.00, NULL),
    (4, '2024-08-19', 'shipped',    305.80, 'Leave at door'),
    (4, '2024-10-05', 'pending',     60.00, NULL);

-- Eve Davis (customer_id = 5)
INSERT INTO orders (customer_id, order_date, status,       total_amount, notes) VALUES
    (5, '2024-02-28', 'delivered',  220.00, NULL),
    (5, '2024-07-07', 'delivered',   15.99, 'Small accessory');

-- Frank Miller (customer_id = 6)
INSERT INTO orders (customer_id, order_date, status,       total_amount, notes) VALUES
    (6, '2024-05-20', 'delivered',  510.00, 'Bulk order'),
    (6, '2024-09-12', 'shipped',    185.50, NULL);

-- Grace Wilson (customer_id = 7)
INSERT INTO orders (customer_id, order_date, status,       total_amount, notes) VALUES
    (7, '2024-01-30', 'delivered',   95.00, NULL),
    (7, '2024-04-25', 'delivered',  140.75, NULL),
    (7, '2024-11-01', 'processing',  33.00, NULL);

-- Henry Moore (customer_id = 8)
INSERT INTO orders (customer_id, order_date, status,       total_amount, notes) VALUES
    (8, '2024-03-14', 'delivered',  250.00, 'Pi Day special'),
    (8, '2024-08-08', 'delivered',   78.40, NULL);

-- Iris Taylor (customer_id = 9)
INSERT INTO orders (customer_id, order_date, status,       total_amount, notes) VALUES
    (9, '2024-06-01', 'delivered',  399.00, NULL),
    (9, '2024-10-18', 'pending',     22.50, NULL);

-- Jack Anderson (customer_id = 10)
INSERT INTO orders (customer_id, order_date, status,       total_amount, notes) VALUES
    (10, '2024-02-10', 'delivered',  110.00, NULL),
    (10, '2024-07-14', 'delivered',  295.60, NULL),
    (10, '2024-12-01', 'pending',     48.00, 'Holiday gift');
