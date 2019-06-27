function set_attr(id) {
	$.ajax({
		type: "GET",
		url: "rest/articles/by-id?article_id=" + id,
		success: (result) => { 
			$("#article_cost_" + id).append(result.cost + " &euro;"); 
			$("#article_name_" + id).append(result.article_name);
		},
		error: () => console.log("failed to get article info")
	})
}

$(document).ready(function() {
	if (localStorage.cart === undefined || localStorage.cart == "[]") {
		$("main").append("<div class=\"container bg-light overview\"> Nothing in cart yet. </div>");
		return;
	}
	var cart = JSON.parse(localStorage.cart);
	for (let i = 0; i < cart.length; i++) {
		if (cart[i].quantity === undefined) {
			cart[i].quantity = 1;
			localStorage.cart = JSON.stringify(cart);
		}
		$("main").append("<div class=\"container bg-light overview\" id=\"order_article_view_" + cart[i].article_id + "\" >" + 
							"<div class=\"row\">" + 
							"<div class=\"col-lg-2 bg-light\">" + 
							"<img src=\"article_thumbnails/" + cart[i].article_id + ".jpg\" class=\"img-fluid\"/>" +
							"</div> " + 
							"<div class=\"col-lg-5 bg-light\">" +
							"<h3 id=\"article_name_" + cart[i].article_id + "\"></h3>" + 
							"<input type=text value=\"" + cart[i].quantity + "\" id=\"article_quantity_" + cart[i].article_id + "\"></input>" +
							"<div id=\"article_quantity_error_" + cart[i].article_id + "\" hidden=\"true\" style=\"color: red\">Please input only numbers</div>" + 
							"</div>" + 
							"<div class=\"col-lg-3 bg-light\">" + 
								"<h3>Gesamtpreis</h3>" + 
								"<p class=\"overview-price\" id=\"article_cost_" + cart[i].article_id + "\"></p>" + 
							"</div>" + 
							"<div class=\"col-lg-2 bg-light\">" + 
								"<button type=\"button\" class=\"btn btn-secondary\">Entfernen</button>" + 
							"</div>" + 
							"</div>" + 
							"</div>"
				)
		set_attr(cart[i].article_id); /* async */
		$("#order_article_view_" + cart[i].article_id + " button").click(function() {
			var id = cart[i].article_id;
			var c = []
			for (let j = 0; j < cart.length; j++) 
				i != j &&  c.push(cart[j]);
			localStorage.cart = JSON.stringify(c);
			$("#order_article_view_" + id).remove();
		})
		
		$("#article_quantity_" + cart[i].article_id).on("input", function() {
			if (isNaN($("#article_quantity_" + cart[i].article_id).prop("value")) || $("#article_quantity_" + cart[i].article_id).prop("value") == "") {
				$("#article_quantity_error_" + cart[i].article_id).prop("hidden", false);
				$("#goto_checkout").click(function(e) {
					e.preventDefault();
				})
			}
			else {
				$("#article_quantity_error_" + cart[i].article_id).prop("hidden", true);
				cart[i].quantity = $("#article_quantity_" + cart[i].article_id).prop("value");
				localStorage.cart = JSON.stringify(cart);
				$("#goto_checkout").unbind("click");
			}
		})
	}
});