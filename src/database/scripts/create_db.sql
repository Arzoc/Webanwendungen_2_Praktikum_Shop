-- execute after starting sqlite3 with database: .read <scriptname>
-- enable foreign key support with sqlite3> PRAGMA foreign_keys = ON

CREATE TABLE account (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	first_name TEXT NOT NULL,
	last_name TEXT NOT NULL,
	email TEXT NOT NULL UNIQUE,
	phone TEXT,
	last_login TEXT,
	pwd_hash TEXT NOT NULL
);

-- CREATE TABLE session (
-- 	id INTEGER PRIMARY KEY AUTOINCREMENT,
-- 	token TEXT NOT NULL,
-- 	account_id INTEGER NOT NULL,
-- 	FOREIGN KEY (account_id) REFERENCES account (id)
-- );

CREATE TABLE paypal (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	email TEXT NOT NULL UNIQUE
);

CREATE TABLE creditcard (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	card_number TEXT NOT NULL UNIQUE,
	expire TEXT NOT NULL,
	first_name TEXT NOT NULL,
	last_name TEXT NOT NULL
);

CREATE TABLE article (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	article_name TEXT NOT NULL,
	category TEXT,
    cost REAL,
	descript TEXT
);

CREATE TABLE order_history (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	order_state INTEGER NOT NULL,
    buydate TEXT NOT NULL, -- as unix time -> sample values not correct
    payment_paypal_id INTEGER,
    payment_creditcard_id INTEGER,
    account_id INTEGER NOT NULL,
	FOREIGN KEY (payment_paypal_id) REFERENCES paypal (id),
    FOREIGN KEY (payment_creditcard_id) REFERENCES creditcard (id),
    FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE orders (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	quantity INTEGER NOT NULL,
	order_history_id INTEGER NOT NULL,
	article_id INTEGER NOT NULL,
    cost_at_purchase REAL NOT NULL,
	FOREIGN KEY (order_history_id) REFERENCES order_history (id),
	FOREIGN KEY (article_id) REFERENCES article (id)
);
