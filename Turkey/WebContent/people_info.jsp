<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>One man - Seaside Base</title>
<link rel="stylesheet" type="text/css" href="styles/main_style.css" />
<link rel="stylesheet" type="text/css" href="styles/text_style.css" />
<link rel="stylesheet" type="text/css" href="styles/own_style.css" />
<script src="main.js"></script>

</head>
<body>
	<div class="wrapper">

		<header> </header>

		<nav id="menu">
			<script>
				putMenu()
			</script>

		</nav>


		<article>

			<a href="change_peo.jsp?id=<%=request.getParameter("id")%>">Change
				data</a>


			<div class="main_block">
				name:
				<%=request.getAttribute("name")%>
			</div>
			<div class="main_block">
				surname:
				<%=request.getAttribute("surname")%></div>

			<div class="main_block">
				patronymic:
				<%=request.getAttribute("patronymic")%></div>

			<div class="main_block">
				birthday:
				<%=request.getAttribute("birthday")%></div>

			<div class="main_block">
				city:
				<%=request.getAttribute("city")%></div>

			<%!Integer col = 0;%>
			<%
				col = Integer.parseInt(request.getAttribute("howmany").toString());
				for (Integer i = 1; i <= col; i++) {
			%>
			<div class="add_block1"><%=request.getAttribute("aname" + i.toString())%></div>
			<div class="add_block2"><%=request.getAttribute("acontent" + i.toString())%></div>
			<%
				}
			%>
			
			<div class="main_block"> events taken part in</div>
			<%
				col = Integer.parseInt(request.getAttribute("olhowmany").toString());
				for (Integer i = 1; i <= col; i++) {
			%>
			<div class="add_block1"><%=request.getAttribute("title" + i.toString())%></div>
			<a href=<%=request.getAttribute("idol" + i.toString())%>> <span
				class="add_block2">more</span></a>
			<%
				}
			%>
			<div class="main_block"> <br></div>
			
		</article>
		<div class="push"></div>
	</div>
	<footer>
		<script>
			putFoot()
		</script>

	</footer>

</body>

</html>