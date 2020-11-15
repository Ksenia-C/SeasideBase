<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>человек - сервис работы с таблицами</title>
	<link rel="stylesheet" type = "text/css" href = "main_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "text_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "form_style.css"/>
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
<input type = "checkbox" id = "all_del" name = "del" value = "yes" />удалить человека?
<div id = "not_del">
Изменить
		<div class = "block_cell">
			<div class = "text">Имя</div>
			<div class = "value"><input type = "text"  name ="StudentName"></div>
		</div>
		
		<div class = "block_cell">
			<div class = "text">Фамилия</div>
			<div class = "value"><input type = "text"  name ="StudentSurname"></div>
		</div>
		
		<div class = "block_cell">
			<div class = "text">Отчество</div>
			<div class = "value"><input type = "text" name ="StudentPatronymic"></div>
		</div>
		 
		<div class = "block_cell">
			<div class = "text">День рождения</div>
			<div class = "value"><input type = "date" name ="StudentBirthday"></div>
		</div>
		
		<div class = "block_cell">
			<div class = "text">Школа</div>
			<div class = "value"><input type = "text" name ="StudentSchool"></div>
		</div>
		
		<div class = "block_cell">
			<div class = "text">Класс</div>
			<div class = "value"><input type = "text" name ="StudentClass"></div>
		</div>
		
		<div class = "block_cell">
			<div class = "text">Город</div>
			<div class = "value"><input type = "text" name ="StudentCity"></div>
		</div>
	
	<div class ="block_cell">
			<div class = "text">Удалить дополнительные параметры через точку с запятой</div>
			<div class = "value"><textarea id = "textarea" name = "DPeopleAdd"></textarea></div>
	</div>
	<div class ="block_cell">
			<div class = "text">Изменить дополнительные параметры через точку с запятой</div>
			<div class = "value"><textarea id = "textarea" name = "СPeopleAdd"></textarea></div>
	</div>
	<div class ="block_cell">
			<div class = "text">Добавить дополнительные параметры через точку с запятой</div>
			<div class = "value"><textarea id = "textarea" name = "APeopleAdd"></textarea></div>
	</div>
	
	

</div>

	<div class ="block_cell">
		<input type = "submit" value = "изменить">
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