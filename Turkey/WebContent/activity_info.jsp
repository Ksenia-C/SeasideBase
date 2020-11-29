<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Event - Seaside Base</title>
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

			<a href="change_act.jsp?id=<%=request.getParameter("id")%>">Change
				data</a>
			<div class="main_block">
				title: <%=request.getAttribute("title")%>
			</div>
			<div class="main_block">
				date of beginning: <%=request.getAttribute("date_main")%></div>
			<div class="main_block">duration: <%=request.getAttribute("duration")%></div>
			<div class="main_block">venues: <%=request.getAttribute("places")%></div>


			<%!Integer col = 0;%>
			<%
				col = Integer.parseInt(request.getAttribute("howmany").toString());
				for (Integer i = 1; i <= col; i++) {
			%>
			<div class="add_block1"><%=request.getAttribute("iname" + i.toString())%></div>
			<div class="add_block2"><%=request.getAttribute("icontent" + i.toString())%></div>
			<%
				}
			%>
			table
			<%=request.getAttribute("table")%>
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