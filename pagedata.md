##pages
- orders_history
  - daten
    * session cookie (NICHT optional)
  - action on success
    * show orders for LOGGED IN user
  - action on failure 
    * show message
- login
  - daten
    * username
    * password hash (sha256)
  - sets
    * session cookie
  - action on success
    * goto homepage with session cookie set
  - action on failure
    * stay on page, print error message
- buy_article
  - daten
    * session cookie (optional, null if anonymous)
    * article_id
  - action on success
    * send "add item to shopping cart" request to server
- checkout
  - daten:
    * session cookie (optional, null if anonymous),
  - action on success
    * update page with bill, offer homepage
  - action on failure
    * print error message
- index
  - daten: 
    * session cookie (optional)

- neue seite mit warenkorb oder panel an der seite?
