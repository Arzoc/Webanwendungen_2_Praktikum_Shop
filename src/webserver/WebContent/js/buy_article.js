function getUrlVars() {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}
//ajax request to fill article description
$(document).ready(function (){
	var urlparams = getUrlVars();
	console.log(urlparams);
	if(urlparams.article_id === undefined){
		return;
	}
	console.log("rest/articles/by-id?article_id=" + urlparams.article_id);
	$.ajax({
		
		  url: "rest/articles/by-id?article_id=" + urlparams.article_id,
		 
		  success: function( result) {
			  $("#price_tag").html(result.cost + " €");
			  $("#desc_tag").html(result.descript );
		  },
		  error: function( text, textStatus ) {
		    	console.log(text);
		  	 
		  }//end error
			  
		  })//end ajax
});//end doc ready
//add to cart
$( "#btn-cart").click(function( event ) {
	var urlparams = getUrlVars();
	//create cart if it doesnt exist
	if(localStorage.cart === undefined){
		localStorage.cart = "[]";
		
	}
	var tmp = JSON.parse(localStorage.cart);
	tmp.push({article_id: urlparams.article_id, quanitity: 1});
	localStorage.cart = JSON.stringify(tmp);
	/*
	console.log(localStorage.cart)
	var urlparams = getUrlVars();
	console.log(urlparams.id)
	to_cart = {article_id: parseInt(urlparams.id), quanitity: 1};
	//localStorage.setItem('cart', JSON.stringify(to_cart));
	//localStorage.cart = ({article_id: urlparams.article_id, quanitity: 1});
	*/
}); //end .click
