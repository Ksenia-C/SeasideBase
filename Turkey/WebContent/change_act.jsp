<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>мероприятие - сервис работы с таблицами</title>
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
<input type = "checkbox" id = "all_del" name = "del" value = "yes" />удалить мероприятие?
<div id = "not_del">
Изменить
		<div class = "block_cell">
			<div class = "text">Название</div>
			<div class = "value"><input type="text" name = "EventName"/></div>
		</div>
		
		<div class = "block_cell">
			<div class = "text">дата проведения или начала</div>
			<div class = "value"><input type = "date" name = "EventDateMain"/></div>
</div>		
	<div class = "block_cell">
			<div class = "text">дата окончания, если это продолжительное мероприятие</div>
			<div class = "value"><input type = "date" name = "EventDateEnd"/></div>
</div>		
	<div class = "block_cell">
			<div class = "text">тип мероприятия</div>
			<div class = "value"><select name = "EventType">
			<option value = "EventNone" >---</option>
			<option value = "EventOlymp" >Этап олимпиады</option>
			<option value = "EventSchool">Сборы/Школа</option>			
			</select></div>
	</div>		
	
	<div class ="block_cell">
			<div class = "text">Добавить места проведения через точку с запятой</div>
			<div class = "value"><textarea id = "textarea" name = "AEventPlace"></textarea></div>
	</div>
	
	<div class ="block_cell">
			<div class = "text">Удалить места проведения через точку с запятой</div>
			<div class = "value"><textarea id = "textarea" name = "DEventPlace"></textarea></div>
	</div>
	
	<div class ="block_cell">
			<div class = "text">Удалить дополнительные параметры через точку с запятой</div>
			<div class = "value"><textarea id = "textarea" name = "DEventAdd"></textarea></div>
	</div>
	<div class ="block_cell">
			<div class = "text">Изменить дополнительные параметры через точку с запятой</div>
			<div class = "value"><textarea id = "textarea" name = "СEventAdd"></textarea></div>
	</div>
	<div class ="block_cell">
			<div class = "text">Добавить дополнительные параметры через точку с запятой</div>
			<div class = "value"><textarea id = "textarea" name = "AEventAdd"></textarea></div>
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