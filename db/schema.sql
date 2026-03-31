-- OnesToManys Project
-- Master-Detail Schema: Customer (master) -> Orders (detail)
--
-- Master table: customers
-- Detail table: orders
-- Relationship: One customer can have many orders

-- Drop tables in reverse dependency order to allow re-running the script
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;

-- Master table
CREATE TABLE customers (
    customer_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name    TEXT    NOT NULL,
    last_name     TEXT    NOT NULL,
    email         TEXT    NOT NULL UNIQUE,
    phone         TEXT,
    city          TEXT,
    state         TEXT,
    created_at    TEXT    NOT NULL DEFAULT (datetime('now'))
);

-- Detail table (references the master table via foreign key)
CREATE TABLE orders (
    order_id      INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_id   INTEGER NOT NULL,
    order_date    TEXT    NOT NULL DEFAULT (date('now')),
    status        TEXT    NOT NULL DEFAULT 'pending'
                          CHECK (status IN ('pending', 'processing', 'shipped', 'delivered', 'cancelled')),
    total_amount  REAL    NOT NULL DEFAULT 0.00,
    notes         TEXT,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Index on the foreign key column for efficient lookups
CREATE INDEX idx_orders_customer_id ON orders (customer_id);
