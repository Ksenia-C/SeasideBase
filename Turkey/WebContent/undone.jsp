<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>добавление... - сервис работы с таблицами</title>
	<link rel="stylesheet" type = "text/css" href = "styles/main_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "styles/text_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "styles/own_style.css"/>
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
<div class = "main_block">
<%=
request.getParameter("name")
%>
</div>
<div class="main_block">
<%=request.getParameter("surname")%></div>
<div class="main_block"><%=
request.getParameter("patronymic")
%></div>


<div class="main_block"><%=
request.getParameter("birthday")
%></div>


<div class="main_block"><%=
request.getParameter("school")
%></div>

<div class="main_block"><%=
request.getParameter("class")
%>
</div>
<div class="main_block"><%=
request.getParameter("city") 
%></div>


</article>
<div class="push"></div>
</div>
<footer>
<script>putFoot()</script>

</footer>

</body>

</html>