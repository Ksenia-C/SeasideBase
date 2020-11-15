<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>человек - сервис работы с таблицами</title>
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

<a href = "change_peo.jsp?id=<%=request.getParameter("id") %>">Изменить данные</a>


<div class = "main_block">
<%=
request.getAttribute("name")
%>
</div>
<div class="main_block">
<%=request.getAttribute("surname")%></div>
<div class="main_block"><%=
request.getAttribute("patronymic")
%></div>


<div class="main_block"><%=
request.getAttribute("birthday")
%></div>


<div class="main_block"><%=
request.getAttribute("school")
%></div>

<div class="main_block"><%=
request.getAttribute("class")
%>
</div>
<div class="main_block"><%=
request.getAttribute("city") 
%></div>

<%! Integer col = 0;%> 
<%col = Integer.parseInt(request.getAttribute("howmany").toString()); 
for(Integer i=1;i<=col;i++){
%>
<div class="add_block1"><%=
request.getAttribute("aname"+i.toString()) 
%></div>
<div class="add_block2"><%=
request.getAttribute("acontent"+i.toString()) 
%></div>
<%} %>

<div class = "main_block">
мероприятия
</div>

<%col = Integer.parseInt(request.getAttribute("olhowmany").toString()); 
for(Integer i=1;i<=col;i++){
%>
<div class="add_block1"><%=
request.getAttribute("title"+i.toString()) 
%></div>
<a href = <%=request.getAttribute("idol"+i.toString()) %>>
<span class="add_block2">больше</span></a>
<%} %>

</article>
<div class="push"></div>
</div>
<footer>
<script>putFoot()</script>

</footer>

</body>

</html>