<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>Events - Seaside Base</title>
	<link rel="stylesheet" type = "text/css" href = "styles/main_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "styles/text_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "styles/form_style.css"/>
	<script src="main.js"></script>


</head>
<body>
  <div class="wrapper">

<header>
</header>

<nav id = "menu" >
<script>putMenu()</script>

</nav>


<article>

<form method="get" action  = "CanActServlet" >
<input style = "display:none" name = "id" value = <%=request.getParameter("id")%>>
<input type = "checkbox" id = "all_del" name = "del" value = "yes" /> Delete event?
<div id = "not_del">
<br>Change<br>
		<div class = "block_cell">
			<div class = "text">Title</div>
			<div class = "value"><input type="text" name = "EventName"/></div>
		</div>
		
		<div class = "block_cell">
			<div class = "text">Date of beginning</div>
			<div class = "value"><input type = "date" name = "EventDateMain"/></div>
</div>		
	<div class = "block_cell">
			<div class = "text">Duration</div>
			<div class = "value"><input type = "text" pattern = "^[ 0-9]+$" name = "EventDuration"/></div>
</div>			
	
	<div class = "block_cell">
			<div class = "text">Venues</div>
			<div class = "value"><textarea id = "textarea" name = "EventPlace"/></textarea>
			</div>
</div>			
	
	
	<div class ="block_cell">
			<div class = "text">Delete optional parameters, write their names by semicolons</div>
			<div class = "value"><textarea id = "textarea" name = "DEventAdd" placeholder = "type; level"></textarea></div>
	</div>
	<div class ="block_cell">
			<div class = "text">Change optional parameters, write as "name - value" by semicolons</div>
			<div class = "value"><textarea id = "textarea" name = "СEventAdd" placeholder = "quantity of participators - 404"></textarea></div>
	</div>
	<div class ="block_cell">
			<div class = "text">Add optional parameters, write as "name - value" by semicolons</div>
			<div class = "value"><textarea id = "textarea" name = "AEventAdd" placeholder = "quantity of participators - 404; priority - 1"></textarea></div>
	</div>
	
	

</div>

	<div class ="block_cell">
		<input type = "submit" value = "Change">
	</div>


</form>




</article>
<div class="push"></div>
</div>
<footer>
<script>putFoot()</script>

</footer>

</body>
</html>