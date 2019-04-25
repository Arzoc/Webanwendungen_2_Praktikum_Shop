-- execute after starting sqlite3 with database: .read <scriptname>

CREATE TABLE account(
	id integer PRIMARY KEY,
	first_name text NOT NULL,
	last_name text NOT NULL,
	email text NOT NULL UNIQUE,
	phone text,
	last_login text,
	pwd_hash text NOT NULL,
);

CREATE TABLE session(
	id integer PRIMARY KEY,
	cookie text NOT NULL UNIQUE,
	isactive integer NOT NULL,
	FOREIGN KEY (account_id) REFERENCES account (id), 
);

CREATE TABLE payapal(
	id integer PRIMARY KEY,
	email text NOT NULL UNIQUE,
	FOREIGN KEY (account_id) REFERENCES account (id),
);

CREATE TABLE creditcard(
	id integer PRIMARY KEY,
	card_number text NOT NULL UNIQUE,
	expire text NOT NULL,
	first_name text NOT NULL,
	last_name text NOT NULL,
	FOREIGN KEY (account_id) REFERENCES account (id),
);

CREATE TABLE article(
	id integer PRIMARY KEY,
	name text NOT NULL,
	category text,
);


CREATE TABLE order_history(
	id integer 	PRIMARY KEY,
	status integer NOT NULL,
	quantity integer NOT NULL,
	FOREIGN KEY (article_id) REFERENCES article(id),
	FOREIGN KEY (account_id) REFERENCES account (id),
);

