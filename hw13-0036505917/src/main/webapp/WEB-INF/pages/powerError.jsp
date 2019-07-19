<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String color = (String)session.getAttribute("pickedBgColor");
if(color == null){
	color = "ffffff";
}
%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.css">
		<style>
			body {
				background: #<%= color%>;
			}
		</style>
		<title>Error</title>
	</head>
    <body>
   	 <h2><%= request.getAttribute("errorMessage")%></h2>
   	 <a href="index.jsp">IndexPage</a>
    </body>
</html>