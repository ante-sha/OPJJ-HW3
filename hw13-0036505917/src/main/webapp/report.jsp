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
		<title>Report</title>
	</head>
    <body>
   	 <header><h2>OS Usage</h2></header>
   	 <p>Here are the results of OS usage in survey that we completed.</p>
   	 <img src="reportImage" />
    </body>
</html>