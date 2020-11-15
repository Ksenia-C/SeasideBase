<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" type = "text/css" href = "main_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "text_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "form_style.css"/>
	<script src="main.js"></script>

<title>спросить про людей - сервис работы с таблицами</title>
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
			<div class = "text">Название</div>
			<div class = "value"><input type="text" name = "EventName" value = "olympo"/></div>
		</div>
		
	<div class = "block_cell">
			<div class = "text">дата проведения или начала</div>
			<div class = "value"><input type = "date" name = "EventDateMain"/></div>
</div>		
<!-- 
<div class = "block_cell">
			<div class = "text">дата из срока длительности</div>
			<div class = "value"><input type = "date" name = "EventDateIn"/></div>
</div>	
 -->	
	<div class = "block_cell">
			<div class = "text">дата окончания, если это продолжительное мероприятие</div>
			<div class = "value"><input type = "date" name = "EventDateEnd"/></div>
</div>		
	<div class = "block_cell">
			<div class = "text">тип мероприятия</div>
			<div class = "value"><select name = "EventType">
			<option value = "None" >---</option>
			<option value = "EventOlymp" >Этап олимпиады</option>
			<option value = "EventSchool">Сборы/Школа</option>			
			</select></div>
</div>		
		
	<div class = "block_cell">
		<div class = "text">
		если хотите искать по дополнительному параметру, то укажите их здесь через точку с запятой. <br>
		сначала название параметра, а затем его значение. Помните, пока все хранится как строки.
		</div>
		<div class = "value">
				<textarea name = "EventAdd" placeholder = "количество участников - 403; уровень олимпиады - 1; лучший приз - книга 'люди и камни'"
		cols = "50" rows = "2"></textarea>
		</div>
</div>

			<div class = "block_cell">
				<input type = "submit" value = "Узнать"/>
	
		</div>
	
	</form>










</article>
<div class="push"></div>
</div>
<footer>
<script>putFoot()</script>

</footer>

</body></html>