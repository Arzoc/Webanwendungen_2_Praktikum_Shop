
$( "#createuser-button").click(function( event ) {
	event.preventDefault();
	//Fill variables
	ValidateInput()
	var first_name = document.querySelector("#firstName").value;
	var last_name= document.querySelector("#lastName").value;
	var e_mail= document.querySelector("#email").value;
	var pho_ne= document.querySelector("#phone").value;
	var pass_word= document.querySelector("#password").value;
 	//if(!$(".form-control").val(value)){alert( "Alle Felder müssen befüllt sein !");} //empty check
	
 	$.ajax({
 		 type: "POST", 
 		 url: "/Yourshop/rest/users/create",
 		  data: { firstname: first_name, lastname: last_name, email: e_mail, phone: pho_ne, password: pass_word 
 		   },
 		  success: function( result, textStatus ) {
 			  
 		  },
 		  error: function( text, textStatus ) {
 		  
 		  }//end error
 	});  //end Ajax
  
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
} //end validate