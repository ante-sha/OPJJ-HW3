<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
		<style>
		</style>
		<title>Error</title>
	</head>
    <body>
    	<span id="title"><h1><%= request.getAttribute("err.msg")%></h1></span>
    	<span><a href="<%= request.getContextPath()%>/servleti/main">Home</a></span>
    </body>
</html>