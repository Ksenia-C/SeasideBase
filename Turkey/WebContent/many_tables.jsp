<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>результат - сервис работы с таблицами</title>
	<link rel="stylesheet" type = "text/css" href = "styles/main_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "styles/text_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "styles/table_style.css"/>
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

<%
if(request.getAttribute("link_to_par")!=null){
%>
<a href = <%="ask_par.jsp?fir_cond="+request.getAttribute("link_to_par").toString().replace(" ", "+")
+"&fir_cond_and="+request.getAttribute("link_to_par_and").toString().replace(" ", "+")%>>Узнать еще больше</a>

<%} %>
<%= request.getAttribute("exact answer").toString()%>
</article>
<div class="push"></div>
</div>
<footer>
<script>putFoot()</script>

</footer>

</body>

</html>