## pages
- orders_overview.html // warenkorb uebersicht
- rest/users/login
  - send
    - email  
    - password hash (sha256)  
  - server do
    - save session cookie
  - recv
    - jwt token
    - return value
  - client do
    - goto homepage / write error
- rest/users/create
  - send
    - firstname
    - lastname
    - email
    - phone
    - password hash (sha256)
  - server do
    - insert into account
    - save session cookie
  - recv
    - session cookie
    - return value
  - client do
    - goto homepage / write error
- rest/checkout
  - send
    - checkout_params =  {  
        articles : [[  
                      article_id : ....  
                      quantity : ...  
                    ],  
                    ... (mehr artikel)  
                    ]  
        (optional) paypal_email : email; card_number : number  
        payment_new : <[],[...]>  
        reihenfolge: paypal(vorhanden?), card_number(vorhanden?), payment_new(vorhanden?), falls hier ankommen: error
      }  
    - jwt token
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
- rest/articles/by-id
  - send (als queryparam -> url)
    - article_id
  - recv
    - json
- rest/articles/by-category
  - send (als queryparam -> url)
    - kategorie name
    - articles_per_page (>0)
    - page number (start: 1)
    - token
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
- rest/users/order_history
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
return value: json { return: <number>; [if return != 0 -> msg: errorMsg] }
