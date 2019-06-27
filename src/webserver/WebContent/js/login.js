/**
 * uses functions from login_helper.js
 */

function error_msg(msg) {
	$("#login_messages").empty();
	$("#login_messages" ).append(msg);
}


/**
 * 
 * @param event SUCCESS, WRONG_CREDENTIALS, OTHER_ERROR
 * @returns 
 */
function event_handling(event) {
	switch (event){
	case SUCCESS:
		error_msg("<p><strong>Login successfull</strong></p>");
		window.location.replace("index.html");
		break;
	case WRONG_CREDENTIALS:
		error_msg( "<p><strong>Falsche Anmeldedaten</strong></p>" );
		break;
	case OTHER_ERROR:
		error_msg( "<p><strong>Bitte Support kontaktieren !</strong></p>" );
		break;
	default:
		error_msg( "<p><strong>Unknown event</strong></p>" );
	}
}

function handle_semi_logged_in() {
	error_msg("<p><strong>You were logged out. </strong></p>");
}

function handle_already_logged_in() {
	$("#login").empty();
	$("#login").append("<p><strong>Already logged in.</strong></p>");
}

$(document).ready(function() {
	
	var token_exists = !(localStorage.token === undefined);
	var email_exists = !(localStorage.email === undefined);
	
	/* lazy eval */
	if (token_exists && email_exists && is_logged_in(function(result) {
		handle_already_logged_in();
	}, function() {
		console.log("Failed to check for login");
	})) {
	} else {
		logout();
		if (!(!token_exists && !email_exists)) /* check if we're just not logged in */
			handle_semi_logged_in();
		$("#login-button").click(function() {
		  var email = document.querySelector('input[name="username"]').value;
		  var password = document.querySelector('input[name="password"]').value;
			login(email, password, event_handling);
		});
	}
});
