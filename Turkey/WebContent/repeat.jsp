<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.load.database.Repeat" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map.Entry" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" type = "text/css" href = "main_style.css"/>
	<link rel="stylesheet" type = "text/css" href = "text_style.css"/>
	<script src="main.js"></script>

<title>добавление... - - сервис работы с таблицами</title>
</head>
<body>
  <div class="wrapper">

<header>
</header>

<nav id = "menu" >
<script>putMenu()</script>

</nav>


<article>
<%=
request.getAttribute("exact answer")
%>

<form action = "AddingPeoServlet" method = "post">

Для всех людей, у кого нашлась ровно одна строчка, уже стоит дополнить эту строчку. У остальных - не добавлять

<table>

<%
ArrayList<Repeat> manut = new ArrayList<Repeat>();
manut  =  (ArrayList<Repeat>)request.getAttribute("classrep");
%>
<input name= "cnt" value = <%=manut.size() %> style = "display:none"/>

<%
Integer cnt = 0;
String[] name_basa = {"name", "surname", "patronymic",
		"birthday", "school", "class", "city"};

for(Repeat m:manut){
	cnt++;
%>
<tr>
<td>
<a href = <%="undone.jsp?name=" + m.about.get(0).replace(" ", "+")+"&surname="+m.about.get(1).replace(" ", "+")+
"&patronymic="+m.about.get(2).replace(" ", "+")+"&birthday="+m.about.get(3).replace(" ", "+")+"&school="+m.about.get(4).replace(" ", "+")+
"&class="+m.about.get(5).replace(" ", "+")+"&city="+m.about.get(6).replace(" ", "+")%>><%=m.about.get(0) +" "+m.about.get(1)%></a>
</td>
<td>
			
			<%Integer j = 0;
			for(Entry<String, String> entry: m.add.entrySet()){
				String ae = "insert into additional values(?, ?, "+entry.getKey()+", "+entry.getValue()+");";
				%>
			<input style = "display:none" name = <%="select"+cnt.toString()+"_"+j.toString()%> value = <%=ae %>>
			
			<%
			j++;
			}
			%>
			
			<input style = "display:none" name = <%="select"+cnt.toString()+"_count"%> value = <%=j %>>
			
			<input name=<%="select"+cnt.toString()%> type="radio" value="none" <%=(m.id.size()!=1?"checked":"") %>> Не добавлять<br>
			<%String ad = "";
				for(int i=0;i<name_basa.length;i++) {
	    			if(i==3 || i==6) {
	    				ad+=", "+m.about.get(i);
	    			}
	    			else{
	    				ad+=", "+" '"+m.about.get(i)+"'";
	    			}
	    		}
				ad+=");";
	    	
			%>
			<input style = "display:none" name = <%="main"+cnt.toString() %> value = <%=ad %>>
			<input name=<%="select"+cnt.toString()%> type="radio" value="new"> Добавить отдельно<br>	
			<%for(Integer i = 0;i<m.id.size();i++){ 
				out.print("<input name=\"select"+cnt.toString()+"\" type=\"radio\" value = "+
			m.id.get(i)+" "+(m.id.size()==1?"checked":"")+"> <a href = \"writepeople?id="+m.id.get(i)+"\">"+m.name.get(i)+" "+m.surname.get(i)+"</a><br>");	
			} %>		
			</td>
</tr>
<%cnt++;} %>


</table>



<div class = "block_cell">
				<input type = "submit" value = "Загрузить"/>
	
		</div>
	

</form>




</article>
<div class="push"></div>
</div>
<footer>
<script>putFoot()</script>

</footer>

</body></html>