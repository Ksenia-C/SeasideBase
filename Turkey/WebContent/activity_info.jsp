<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>мероприятие - сервис работы с таблицами</title>
	<link rel="stylesheet" type = "text/css" href = "main_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "text_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "own_style.css"/>
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

<a href = "change_act.jsp?id=<%=request.getParameter("id") %>">Изменить данные</a>
<div class = "main_block">
<%=
request.getAttribute("title")
%>
</div>
<div class="main_block">
<%=request.getAttribute("maindate")%></div>
<div class="main_block"><%=
request.getAttribute("enddate")
%></div>


<div class="main_block"><%=
request.getAttribute("type").toString()=="0"?"этап олимпиады":"сборы/школа"
%></div>

<%! Integer col = 0;%> 
<%col = Integer.parseInt(request.getAttribute("howmany").toString()); 
for(Integer i=1;i<=col;i++){
%>
<div class="add_block1"><%=
request.getAttribute("iname"+i.toString()) 
%></div>
<div class="add_block2"><%=
request.getAttribute("icontent"+i.toString()) 
%></div>
<%} %>
Таблица
<%=request.getAttribute("table")%>
</article>
<div class="push"></div>
</div>
<footer>
<script>putFoot()</script>

</footer>

</body>

</html>