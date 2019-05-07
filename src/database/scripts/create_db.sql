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

CREATE TABLE session (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	cookie TEXT NOT NULL UNIQUE,
	expires_at TEXT NOT NULL,
	account_id INTEGER NOT NULL,
	FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE paypal (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	email TEXT NOT NULL UNIQUE,
	account_id INTEGER NOT NULL,
	FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE creditcard (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	card_number TEXT NOT NULL UNIQUE,
	expire TEXT NOT NULL,
	first_name TEXT NOT NULL,
	last_name TEXT NOT NULL,
	account_id INTEGER NOT NULL,
	FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE article (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	article_name TEXT NOT NULL,
	category TEXT,
	descript TEXT
);

CREATE TABLE order_history (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	order_state INTEGER NOT NULL,
	account_id INTEGER NOT NULL,
	FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE orders (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	quantity INTEGER NOT NULL,
	order_id INTEGER NOT NULL,
	article_id INTEGER NOT NULL,
	FOREIGN KEY (order_id) REFERENCES order_history (id),
	FOREIGN KEY (article_id) REFERENCES article (id)
);
