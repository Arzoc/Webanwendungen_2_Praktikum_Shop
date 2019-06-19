
$( "#login button").click(function( event ) {
  var na_me = document.querySelector('input[name="username"]').value;
 var p_w= document.querySelector('input[name="password"]').value;
  console.log($("#login button"));
 	$.ajax({
 		type: "POST", 
 		url: "/Yourshop/rest/users/login",
 		data: { email: na_me, password: p_w},
 		//if we have token send this Token
 		beforeSend: function(xhr){
 			if (localStorage.token) {
 			xhr.setRequestHeader('Authorization', 'Bearer' + localStorage.token);
 			}
 		},
 		success: function( result, textStatus ) {
		  //Set JWT token
		  localStorage.token=result.token;
		  console.log(result.token);
		  alert( "Eingeloggt");
 												},
 		error: function( text, textStatus ) {
    	console.log(text.status);
  	  	if (text.status==401){
  		$( "#login" ).append( "<p><strong>Falsche Anmeldedaten</strong></p>" );
  		  //alert( "Falscher Credentials ");
  	  		}//end if 
  		else{$( "#login" ).append( "<p><strong>Bitte Support kontaktieren !</strong></p>" );}
      
  	  	}//end error
	  //$( "#weather-temp" ).html( "<strong>" + result + "</strong> degrees" );

        });  //end Ajax
  alert( "Guten Tag " + na_me + " mit Passwort: " + p_w);
  event.preventDefault();
}); //end .click
