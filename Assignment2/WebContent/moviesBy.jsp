<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="data.Movie,java.util.Set" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="main.css">
</head>
<body bgcolor="#E6E6FA">
<h1 id="Title1" >Cinemate</h1>
<h2 id="Title2" >Enter <%out.print(session.getAttribute("moviesBy")); %> to search by</h2>
</head>
<body>
	<form id="form1" method="GET" action="MovieSearchServlet">
		<div id="ButtonHolder">
			<input type="text" name="searchedMovie">
			<input id="MyButton" type="submit" value="Search" name="searchMovie">
		</div>
		<br>
		<div id="ButtonHolder">
			<input id="MyButton" type="submit" value="Back to Movie Search Menu" name="back">
		</div>
	</form>
<%	if(session.getAttribute("searchResults") != null){%>
		<% Set<Movie> movieResults = (Set<Movie>)session.getAttribute("searchResults"); %>
		<div id="ResultsBox">
			<h4 id="Title3">Search Results</h4>
			<% for(Movie m: movieResults){%>
				<h4 id="Results"> <%out.print(m.getTitle()); System.out.println(m.getTitle());%> </h4>
			<%}
			session.setAttribute("searchResults", null); %>	
		</div>
	<%}else if(session.getAttribute("multipleSearch") != null) {%>
			<div id="ResultsBox">
				<h4 id="Title3">No Search Results Found</h4>
			</div>
	<%}%>
</body>
</html>