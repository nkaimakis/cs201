<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="main.ApplicationInterface" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% 
	String fileName = request.getParameter( "file" );
   session.setAttribute( "file", fileName );
   try{
	   ApplicationInterface app = new ApplicationInterface((String)session.getAttribute("file"));
	   
   }catch(Exception e){
	   
   }
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CineMate</title>
</head>
<body>

</body>
</html>