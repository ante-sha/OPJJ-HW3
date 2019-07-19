<%@ page session="true" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String color = (String) session.getAttribute("pickedBgColor");
	if (color == null) {
		color = "ffffff";
	}
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css">
<style>

body {
	background: #<%=color%>;
}
</style>
<title>TrigTable</title>
</head>
<body>
	<table border="1px">
		<thead>
			<tr>
				<th>x</th>
				<th>sin(x)</th>
				<th>cos(x)</th>
			</tr>
		</thead>
		<tbody>
			<%
			for(int x = (Integer)request.getAttribute("a"),n=(Integer)request.getAttribute("b");x<=n;x++){
				%>
				<tr>
				 <td><%= x%></td>
				 <td><%= String.format("%.3f",Math.sin(Math.toRadians(x))) %></td>
				 <td><%= String.format("%.3f",Math.cos(Math.toRadians(x))) %></td>
				</tr>
			<%} %>
		</tbody>
	</table>
</body>
</html>