<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>Human - Seaside base</title>
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

<form method="get" action  = "CanPeoServlet" >
<input style = "display:none" name = "id" value = <%=request.getParameter("id")%>>
<input type = "checkbox" id = "all_del" name = "del" value = "yes" /> delete the human page?
<div id = "not_del">
<br>

Change
		<div class = "block_cell">
			<div class = "text">Name</div>
			<div class = "value"><input type = "text"  name ="StudentName"></div>
		</div>
		
		<div class = "block_cell">
			<div class = "text">Surname</div>
			<div class = "value"><input type = "text"  name ="StudentSurname"></div>
		</div>
		
		<div class = "block_cell">
			<div class = "text">Patronymic</div>
			<div class = "value"><input type = "text" name ="StudentPatronymic"></div>
		</div>
		 
		<div class = "block_cell">
			<div class = "text">Birthday</div>
			<div class = "value"><input type = "date" name ="StudentBirthday"></div>
		</div>
		
		<div class = "block_cell">
			<div class = "text">City</div>
			<div class = "value"><input type = "text" name ="StudentCity"></div>
		</div>
	
	<div class ="block_cell">
			<div class = "text">Delete optional data (enumerate items by semicolons)</div>
			<div class = "value"><textarea id = "textarea" name = "DPeopleAdd" placeholder = "job; univercity"></textarea></div>
	</div>
	<div class ="block_cell">
			<div class = "text">Change optional data (enumerate items by semicolons)</div>
			<div class = "value"><textarea id = "textarea" name = "СPeopleAdd" placeholder = "job - enginer"></textarea></div>
	</div>
	<div class ="block_cell">
			<div class = "text">Add optional data (enumerate items by semicolons)</div>
			<div class = "value"><textarea id = "textarea" name = "APeopleAdd" placeholder = "job - designer; group - 3"></textarea></div>
	</div>
	
	

</div>

	<div class ="block_cell">
		<input type = "submit" value = "change">
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