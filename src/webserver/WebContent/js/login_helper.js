/* static values */
SUCCESS = 1
WRONG_CREDENTIALS = 2
OTHER_ERROR = 3


function get_account_info(finished_handling, error_handling) {
	$.ajax({
		type: "GET",
		url: "rest/users/account-info",
		beforeSend: function(xhr) {
			xhr.setRequestHeader("Authorization", localStorage.token);
		},
		success: function(result) {
			finished_handling(result)
		},
		error: function(result, text) {
			error_handling();
		}
	});
}


function login(email, password, event_handling) {
 	$.ajax({
 		type: "POST", 
 		url: "rest/users/login",
 		data: { email: email, password: password},
 		//if we have token send this Token
 		beforeSend: function(xhr){
 			if (localStorage.token) {
 			xhr.setRequestHeader('Authorization',  localStorage.token);
 			}
 		},
 		success: function( result, textStatus, jqxhr) {
		  //Set JWT token
		  localStorage.token = jqxhr.getResponseHeader("Authorization");
		  get_account_info(function(result){
			  localStorage.email = result.email;
			  event_handling(SUCCESS);
		  }, function() {
			  console.log("failed to get account info");
		  })
 		},
 		error: function( text, textStatus ) {
	  	  	if (text.status == 401)
	  	  		event_handling(WRONG_CREDENTIALS);
	  	  	else
	  			event_handling(OTHER_ERROR);
      
  	  	}//end error	
    });  //end Ajax
}

function is_logged_in_possible() {
	var token_exists = !(localStorage.token === undefined);
	var email_exists = !(localStorage.email === undefined);
	return (token_exists && email_exists);
}

function is_logged_in(positive_handling, negative_handling) {
	if (!is_logged_in_possible()) {
		if (localStorage.token === undefined) localStorage.removeItem("token");
		if (localStorage.email=== undefined) localStorage.removeItem("email");
		negative_handling();
		return;
	}
	$.ajax({
		type: "GET",
		url: "rest/users/account-info",
		beforeSend: function(xhr) {
			xhr.setRequestHeader("Authorization", localStorage.token);
		},
		success: function(result) {
			positive_handling(result);
		},
		error: function(result) {
			negative_handling()
		}
	});
	return true;
}

function logout() {
	if (!(localStorage.token === undefined)) localStorage.removeItem("token");
	if (!(localStorage.email === undefined)) localStorage.removeItem("email");
}

$(document).ready(function() {
	var token_exists = !(localStorage.token === undefined);
	var email_exists = !(localStorage.email === undefined);
	
	token_exists && email_exists && is_logged_in(function(result) {
				
		$("#login_nav_item a").prop("text", "Logout");
		get_account_info(function(result) {
			$("#logged_in_info").append("Logged in as: " + result.email);
		},
		function() {
			$("#logged_in_info").append("Failed to get current account info");
		})
		$("#login_nav_item a").click(function() {
			logout();
			window.location.replace = "login.html";
		})
	}, function() {
		console.log("Failed to get is logged in");
	});
});