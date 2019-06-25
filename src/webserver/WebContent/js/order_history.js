$(document).ready(function (){
$.ajax({
  url: "rest/users/order-history",
  beforeSend: function(xhr){
		if (localStorage.token) {
		xhr.setRequestHeader('Authorization',  localStorage.token);
		}},
  success: function( order_result ) {
	 var article_id;
	  for(let order_history_index = 0; order_history_index < order_result.length; order_history_index++){
		  for(let article_index = 0; article_index < order_result[order_history_index].orders.length; article_index++){
			  article_id = order_result[order_history_index].orders[article_index].article_id;
			  //Get Article Properties
			  $.ajax({
				  context:this,
				  url: "rest/articles/by-id?article_id=" + article_id,
				  success: function(article_result){
					  $("#order_table tbody").append("<tr> </tr>");
					  $("#order_table tbody tr:last").append("<td>"+ article_result.article_name + "</td>");
					  $("#order_table tbody tr:last").append("<td>"+ order_result[order_history_index].orders[article_index].quantity + "</td>");
					  $("#order_table tbody tr:last").append("<td>"+ order_result[order_history_index].orders[article_index].cost_at_purchase + "</td>");
					  $("#order_table tbody tr:last").append("<td>"+ order_result[order_history_index].buydate + "</td>");
					  $("#order_table tbody tr:last").append("<td>"+ order_result[order_history_index].orders[article_index].article_id + "</td>");
				  }
			  })//end ajax
		  }
	  }
	  
	  
	  	console.log(result);
	  	//  $(  "#noorder").html( "" )
        //$.each( data, function( key, value ) {
        //$(".table tr:last" ).append( "value " );
        //}); 
      
  }//end Success
})//end ajax
})//end onload