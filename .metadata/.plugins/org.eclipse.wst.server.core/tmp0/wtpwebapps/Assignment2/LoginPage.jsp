<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="main.css">
</head>
<body bgcolor="#E6E6FA">
<h1 id="Title1" >Cinemate</h1>
<h2 id="Title2" >File parsed! Please log in.</h2>
</head>
<body>
	<form id="form2" method="GET" action="LoginServlet">
		<h2 id="Title4">Username: <input type="text" name="username"></h2>
		<h2 id="Title4">Password: <input type="text" name="password"></h2>
		<div id="ButtonHolder">
			<input id="MyButton" type="submit" value="Log In" name="Submit">
		</div>
	</form>
<%if(session.getAttribute("error") != null){%>
	<h2 id="Title3">ERROR: <%out.print(session.getAttribute("error")); %></h2>
<%} session.setAttribute("error", null);%>
</body>
</html>

<%-- /Users/Nick/Desktop/cs201/Assignment2/WebContent/samplexml --%>