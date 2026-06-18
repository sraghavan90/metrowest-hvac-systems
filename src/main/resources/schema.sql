PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS `users` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
    `email` TEXT NOT NULL,
    `username` TEXT NOT NULL,
    `role` TEXT NOT NULL,
    `password_hash` TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS `products` (
   `id` INTEGER PRIMARY KEY AUTOINCREMENT,
   `name` TEXT NOT NULL,
   `type` TEXT NOT NULL,
   `price` DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS `orders` (
   `id` INTEGER PRIMARY KEY AUTOINCREMENT,
   `customer_id` INTEGER NOT NULL,
   `status` TEXT NOT NULL,

    FOREIGN KEY(customer_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS `order_entries` (
    `order_id` INTEGER NOT NULL,
    `product_id` INTEGER NOT NULL,
    `quantity` INTEGER NOT NULL,
    `unit_price` DECIMAL(10,2) NOT NULL,

    FOREIGN KEY(order_id) REFERENCES orders(id),
    FOREIGN KEY(product_id) REFERENCES products(id)
);