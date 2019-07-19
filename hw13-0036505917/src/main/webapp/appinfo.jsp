<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String color = (String)session.getAttribute("pickedBgColor");
if(color == null){
	color = "ffffff";
}

long elapsedTime = System.currentTimeMillis() - Long.parseLong(application.getAttribute("startUpTime").toString());

long millis = elapsedTime % 1000;
long sec = elapsedTime / 1000 % 60;
long min = elapsedTime / (1000 * 60) % 60;
long hour = elapsedTime / (1000 * 60 * 60) % 24;
long days = elapsedTime / (1000*60*60*24);

String interval = String.format("%d days %d hours %d minutes %d seconds %d milliseconds",days,hour,min,sec,millis);
%>
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="style.css">
		<style>
			body {
				background: #<%= color%>;
			}
		</style>
	</head>
    <body>
   	 <p>
   	 	Application has been active for: <%= interval%>
   	 </p>
    </body>
</html>