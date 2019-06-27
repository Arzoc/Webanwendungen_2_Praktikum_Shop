function creditcard_changed() {
	$("#Cardnumber").prop("disabled", false);
	$("#firstCreditCardName").prop("disabled", false);
	$("#lastCreditCardName").prop("disabled", false);
	$("#CardExpire").prop("disabled", false);
	$("#PayPalAdress").prop("disabled", true);
}

function paypal_changed() {
	$("#Cardnumber").prop("disabled", true);
	$("#firstCreditCardName").prop("disabled", true);
	$("#lastCreditCardName").prop("disabled", true);
	$("#CardExpire").prop("disabled", true);
	$("#PayPalAdress").prop("disabled", false);
}

$(document).ready(function() {
	if ($("#paypal").prop("checked")) {
		paypal_changed();
	} else if ($("#credit").prop("checked")) {
		creditcard_changed();
	}
	
	$("#paypal").change(paypal_changed);
	$("#credit").change(creditcard_changed);
	
	$.ajax({
		type: "GET",
		url: "rest/users/payment-methods",
		beforeSend: function(xhr){
 			if (localStorage.token) {
 				xhr.setRequestHeader('Authorization',  localStorage.token);
 			}
 		},
 		success: function(result, text_status, jqxhr) {
 			
 			/* set new jwt token with new timeout */
 			localStorage.token=jqxhr.getResponseHeader("authorization");
 			
 			for (let i = 0; i < result.creditcards.length; i++) {
 				
 				$("#known-payment-methods .btn-group").append(
 						"<label class=\"btn btn-secondary active\" data-toggle=\"collapse\" type=\"button\" id=\"creditcard_btn_" + i + "\" data-target=\"#creditcard_info_" + i + "\">" + 
 						"<input type=\"radio\" name=\"options\" id=\"creditcard_radio_" + i + "\" autocomplete=\"off\" checked> Creditcard: " + result.creditcards[i].card_number + 
 						"</label><p></p>"
 					)
				$("#known-payment-methods .btn-group").append(
 						"<div class=\"collapse\" id=\"creditcard_info_" + i + "\"> <div class=\"card card-body\"> " + 
 						"<p>Card number : " + result.creditcards[i].card_number + "</p>" + 
 						"<p>Expire : " + result.creditcards[i].expire + "</p>" + 
 						"<p>First Name : " + result.creditcards[i].first_name + "</p>" + 
 						"<p>Last Name : " + result.creditcards[i].last_name + "</p>" + 
 						"</div> </div><p></p"
 					);
 			}
 			for (let i = 0; i < result.paypals.length; i++) {
 				
 				$("#known-payment-methods .btn-group").append(
 						"<label class=\"btn btn-secondary active\" data-toggle=\"collapse\" type=\"button\" id=\"paypal_btn_" + i + "\" data-target=\"#paypal_info_" + i + "\">" + 
 						"<input type=\"radio\" name=\"options\" id=\"paypal_radio_" + i + "\" autocomplete=\"off\" checked> Paypal: " + result.paypals[i].email + 
 						"</label><p></p>"
 					)
 					
 				$("#known-payment-methods .btn-group").append(
 						"<div class=\"collapse\" id=\"paypal_info_" + i + "\"> <div class=\"card card-body\"> " + 
 						"Email: " + result.paypals[i].email + 
 						"</div> </div><p></p"
 					);
 			}
 			
 			
 			$("#known-payment-methods .btn-group").append(
						"<label class=\"btn btn-secondary active\" data-toggle=\"collapse\" type=\"button\" id=\"new_payment_method\" data-target=\".new-paymethod\">" + 
						"<input type=\"radio\" name=\"options\" id=\"new_payment_method_radio\" autocomplete=\"off\" checked> New payment method: " + 
						"</label><p></p>"
					)
 		
			known_payment_methods = result;
 		}
	});
	
	
	$("#checkout-button").click(function() {
		var checked_option = $("#known-payment-methods input[type=radio]:checked")[0];
		var checked_info = $("#known-payment-methods input[type=radio]:checked")[0].offsetParent.dataset.target;
		var paypal_email, articles, card_number, payment_new;
		
		if (checked_option.id.startsWith("paypal")) {
			let index = checked_option.id.substring(13, 14);
			paypal_email = known_payment_methods.paypals[index].email;
		} else if (checked_option.id.startsWith("creditcard")) {
			let index = checked_option.id.substring(17, 18);
			card_number = known_payment_methods.creditcards[index].card_number;
		} else {
			if ($("#paypal").prop("checked")) {
				payment_new = {  paypal_email : $("#PayPalAdress")[0].value }
			} else if ($("#credit").prop("checked")) {
				payment_new = {
						creditcard_card_number : $("#Cardnumber")[0].value,
						creditcard_expire : $("#CardExpire")[0].value,
						creditcard_first_name: $("#firstCreditCardName")[0].value,
						creditcard_last_name : $("#lastCreditCardName")[0].value
				}
			}
		}
		
		var articles = JSON.parse(localStorage.cart);
		
		var post_data = { articles, paypal_email, card_number, payment_new }
		$.ajax({
			type: "POST",
			url: "rest/checkout",
			beforeSend: function(xhr){
	 			if (localStorage.token) {
	 			xhr.setRequestHeader('Authorization',  localStorage.token);
	 			}
	 		},
			data : { checkout_params: JSON.stringify(post_data) },
			success: function() {
				localStorage.cart = "[]";
				window.location.replace("order_history.html");
			}
		})
	});
	
});