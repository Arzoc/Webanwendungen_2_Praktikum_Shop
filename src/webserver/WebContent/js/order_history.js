$.ajax({
  url: "rest/users/order_history",
  data: {
    //Cookie
  },
  success: function( result ) {
        $(  "#noorder").html( "" )
        $.each( data, function( key, value ) {
        $( "main" ).append( "value " );
        });
     
  }
})
