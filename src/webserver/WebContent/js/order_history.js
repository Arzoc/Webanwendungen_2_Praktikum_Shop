$.ajax({
  url: "rest/users/order_history",
  data: {
    //Cookie
  },
  success: function( result ) {
	      $(  "#noorder").html( "" )
        $.each( data, function( key, value ) {
        $(".table tr:last" ).append( "value " );
        }); 
      
  }//end Success
})
