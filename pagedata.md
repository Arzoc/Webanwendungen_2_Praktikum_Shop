## pages
- orders_overview.html // warenkorb uebersicht
- login.html
  - send
    - email (sha256)
    - password hash (sha256)
  - server do
    - save session cookie
  - recv
    - session cookie
    - return value
  - client do
    - goto homepage / write error
- create_user.html
  - send
    - firstname 
    - lastname
    - email
    - phone
    - password
  - server do
    - insert into account
    - save session cookie
  - recv
    - session cookie
    - return value
  - client do
    - goto homepage / write error
- checkout.html
  - send
    - json {
        [ 
          article_id : ....
          quantity : ...
        ], 
        ... (mehr artikel)
    }
    - json {
        id : id,
        art : <paypal,credit,transfer,new>
        new_param : <[],[...]>
      }
    }
    - session cookie
  - server do
    - insert into order_history, orders...
    - insert into pay methods
  - recv
    - return value
  - client do
    - goto next site (overview,homepage?)
- buy_article.html?id=1234
  - send
    - article_id
  - recv
    - json {
        articlename: name
        descr: descr
        imagepath : path
        quantity_avail : avail
      }
  - client do
    - display article
- article_in_category.html
  - send
    - kategorie id
    - session cookie
  - recv
    - json {
        [ 
          artikel name : name
          preis : preis
          thumbnail : nail
          descript: desc
         ]
         ...
      }
- order_history.html
  - send 
    - session cookie
  - recv
    - json {
        [
          article_name : name
          quantity : quantity
          price : price // gesamtpreis
          date : date
          bestellid : id
          thumbnail : nail // optional
        ]
      }


- neue seite mit warenkorb oder panel an der seite?
