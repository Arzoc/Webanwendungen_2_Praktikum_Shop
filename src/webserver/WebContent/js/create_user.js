
$( "#createuser-button").click(function( event ) {
	event.preventDefault();
	//Fill variables
	//ValidateInput()
	var first_name = document.querySelector("#firstName").value;
	var last_name= document.querySelector("#lastName").value;
	var e_mail= document.querySelector("#email").value;
	var pho_ne= document.querySelector("#phone").value;
	var pass_word= document.querySelector("#password").value;
 	//if(!$(".form-control").val(value)){alert( "Alle Felder müssen befüllt sein !");} //empty check
	window.location.replace("login.html");
	if(ValidateInput() == 1){
 	$.ajax({
 		 type: "POST", 
 		 url: "/Yourshop/rest/users/create",
 		 data: { firstname: first_name, lastname: last_name, email: e_mail, phone: pho_ne, password: pass_word 
 		 },
 		  success: function( result, textStatus ) {
 			  console.log("Benutzer erstellt" + result.result);
 			  if(result.result == 0){
 				 console.log("User created");
 				 //alert( "Benutzer erstellt");
 				 window.location.replace("login.html");
 			  }
 			  else if(result.result == 1){
 				 console.log("User exists");
 				 alert( "Benutzer existiert bereits");
 			  }
 			  //1 already exists
 			  //0 user created
 		  },
 		  error: function( text, textStatus ) {
 			 console.log(text);
 		  }//end error
 	});  //end Ajax
	}//end if
}); //end .click

function ValidateInput(){
	var invalid = false;
	$(".form-control").each(function(){
		if ($(this).val() === ''){
			invalid = true;
		}
		});
	if(invalid){
		$( "#createuser-formular" ).append( "<p><strong>Bitte alle Felder befüllen !</strong></p>" );
		//alert( "Bitte alle Felder befüllen !");
		}
	else{
		return 1;
	}
} //end validate