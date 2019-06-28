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

function rebuild_articles(category, page, per_page){
	$("#article_container .row").remove();
	$.ajax({
		  url: "rest/articles/by-category?category=" + category + "&page=" + page + "&per_page=" +  per_page,
		  success: function( result) {
			  var num_rows = parseInt(result.length/4);
			  var article;
			  for(let row_index = 0; row_index < num_rows; row_index++){
				  $("#article_container").append("<div class=\"row\"></div>");
				  for (let col_index = 0; col_index < 4; col_index++){
					  article = result[row_index * 4 + col_index];
					  $("#article_container .row:last").append( 
							  "<div class=\"col-lg-3 bg-light category-container\" >" + 
							  "<p>" + article.article_name + "</p>" +
							  "<a href=\"buy_article.html?article_id="+ article.id +"\"><img src=\""+ article.thumbnail + "\" class=\"img-fluid\"/></a>" +
							  "<p>" + article.cost + "&euro;</p>" +
							  "</div>"
					  );
				  }
				  
			  }
			  $("#article_container").append("<div class=\"row\"></div>");
			  for (let col_index = 0; col_index < result.length % 4 ; col_index++){
				  article = result[num_rows * 4 + col_index];
				  $("#article_container .row:last").append( 
						  "<div class=\"col-lg-3 bg-light category-container\" >" + 
						  "<p>" + article.article_name + "</p>" +
						  "<a href=\"buy_article.html?article_id="+ article.id +"\"><img src=\"article_thumbnails/" + article.id + ".jpg\" class=\"img-fluid\"/></a>" +
						  "<p>" + article.cost + "&euro;</p>" +
						  "</div>"
				  );
			  }
		  }
	})
}

$(document).ready(function (){
	var urlparams = getUrlVars();
	var per_page = 10;
	rebuild_articles(urlparams.category, 1, per_page);
	
	$("#category-heading").append(urlparams.category[0].toUpperCase() + urlparams.category.substring(1));
	
	$.ajax({
		type: "GET",
		url: "rest/articles/num-items-in-category?category=" + urlparams.category,
		succcess : function (result) {
			var num_pages = parseInt(result.num_articles / per_page) + 1;
			for(let i = 0; i < num_pages; i++) {
				$("main ul.pagination").append("<li class=\"page-item\"><a class=\"page-link\"></a></li> ");
				$("main ul.pagination li:last ").click(() => rebuild_articles(urlparams.category, i, per_page));
			}
		}
	})
});
