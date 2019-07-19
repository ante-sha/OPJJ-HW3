<%@page import="hr.fer.zemris.java.hw13.servlets.prob7.Band"%>
<%@page import="java.util.List"%>
<%@ page session="true" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String color = (String) session.getAttribute("pickedBgColor");
	if (color == null) {
		color = "ffffff";
	}

	List<Band> sortedBands = (List<Band>) request.getAttribute("bands");
	sortedBands.sort((b1, b2) -> Long.compare(b1.getID(), b2.getID()));
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css">
<style>
body {
	background: #<%=color%>;
}
</style>
<title>Glasanje</title>
</head>
<body>
	<h1>Glasanje za omiljeni bend:</h1>
	<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na
		link kako biste glasali!</p>
	<ol>
		<c:forEach var="band" items="<%=sortedBands%>">
			<li><a href="glasanje-glasaj?id=${band.ID}">${band.name}</a></li>
		</c:forEach>
	</ol>
</body>
</html>