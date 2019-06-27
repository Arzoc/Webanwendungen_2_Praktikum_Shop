insert into account (first_name, last_name, email, phone, last_login, pwd_hash) values
    ("Hans", "Mueller", "mueller@hans.de", "1122334455", "17:00 04/29/2019", "8881a0528946b82c745f1b8f548821aef60c2d18b67c5a156df02ae9095ee08c"),
    ("Walter", "Meier", "meier@walter", "98729847324", "18:00 04/29/2019", "8881a0528946b82c745f1b8f548821aef60c2d18b67c5a156df02ae9095ee08c"),
    ("ralf", "schnuter", "schnuter@ralf.de", "938743434", "19:00 04/29/2019", "cfdef42a4579a8e2e8fafa6ba9a8cf07afd6a1fa5a64f9c154b75ac92017acf3");
insert into paypal (email) values
    ("irgendwas@test.de"),
    ("andereemail@test.de");
insert into creditcard (card_number, expire, first_name, last_name) values
    ("0948350943850943", "01.01.2020", "anderer", "name"),
    ("7892347894927494", "01.01.2020", "nochein", "name");
insert into article (article_name, cost, category, descript) values
    ("banana", 2.50, "food", "these are bananas"),
    ("apple", 3.00, "food", "these are apples"),
    ("acer pc", 4.00, "electronic", "wonderful acer pc"),
    ("notebook", 0.99, "electronic", "normal notebook"),
    ("shirt", 0.99, "clothes", "fucking shirt"),
    ("pant", 4.20, "clothes", "pant pant"),
    ("hammer", 13.37, "tools", "home improvement");
insert into order_history (order_state, buydate, payment_paypal_id, payment_creditcard_id, account_id) values
     (0, "17:00 04/29/2019", 1, NULL, 1),
     (0, "17:00 04/29/2019", NULL, 2, 1),
     (0, "17:00 04/29/2019", NULL, 1, 2);
insert into orders (quantity, order_history_id, article_id, cost_at_purchase) values
    (10, 1, 3, 2.50),
    (10, 2, 1, 3.00),
    (200, 1, 2, 3.99);
