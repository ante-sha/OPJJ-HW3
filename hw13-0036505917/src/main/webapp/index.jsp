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
		<title>HW13</title>
	</head>
    <body>
    <div><h1>HomeWork 13</h1></div>
    <div>
     <a href="<%= request.getContextPath() %>/colors.jsp">Background color chooser</a>
   	 <a href="<%= request.getContextPath() %>/trigonometric?a=0&b=90">Trigonometric functions of first 90 degrees</a>
    </div>
    <div>
     <form action="trigonometric" method="GET">
 		Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
 		Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
 		<input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
	</form>
    </div>
	<div>
	 <a href="stories/funny.jsp">Funny stories</a>
	<a href="powers?a=1&b=100&n=3">Power xsl</a>
	<a href="appinfo.jsp">App runtime info</a>
	<a href="glasanje">Glasanje</a>
	<a href="report.jsp">Report</a>
	</div>
	
    </body>
</html>