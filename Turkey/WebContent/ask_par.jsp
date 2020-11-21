<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" type = "text/css" href = "styles/main_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "styles/text_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "styles/form_style.css"/>
	<script src="main.js"></script>

<title>Request about participation  - Seaside Base</title>
</head>
<body>
  <div class="wrapper">

<header>
</header>

<nav id = "menu" >
<script>putMenu()</script>

</nav>


<article>
<form method="get" action  = "AskParServlet">
	<input name = "first_cond" value = <%=request.getParameter("fir_cond").replace("+", "%2B").replace(" ", "+") %> style = "display:none">
	<input name = "first_cond_and" value = <%=request.getParameter("fir_cond_and") %> style = "display:none">
	<div class = "block_cell">
			<div class = "text">Title</div>
			<div class = "value"><input type="text" name = "EventName" value = "olympo"/></div>
		</div>
		
	<div class = "block_cell">
			<div class = "text">Data of taking part</div>
			<div class = "value"><input type = "date" name = "EventDate"/></div>
</div>	
<div class = "block_cell">
			<div class = "text">Venue</div>
			<div class = "value"><input type = "text" name = "EventPlace"/></div>
</div>		
		
	<div class = "block_cell">
		<div class = "text">
		Add searching optional parameters by semicolons. <br>
		The title is the first, the second is the value.
		</div>
		<div class = "value">
				<textarea name = "EventAdd" placeholder = "quantity of participators - 403; type of event - It school"
		cols = "50" rows = "2"></textarea>
		</div>
</div>

			<div class = "block_cell">
				<input type = "submit" value = "Request"/>
	
		</div>
	
	</form>










</article>
<div class="push"></div>
</div>
<footer>
<script>putFoot()</script>

</footer>

</body></html>