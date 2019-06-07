
$( "#login form" ).submit(function( event ) {
  var na_me = document.querySelector('input[name="username"]').value;
 var p_w= document.querySelector('input[name="password"]').value;
  //$.ajax({
  //url: "/rest/users/login ",
  //data: { "username:" na_me, "password:" p_w
  //  
  //},
  //success: function( result ) {
    //$( "#weather-temp" ).html( "<strong>" + result + "</strong> degrees" );
  //}
        //})  //end Ajax
  alert( "Guten Tag " + na_me + " mit Passwort: " + p_w);
  event.preventDefault();
});
