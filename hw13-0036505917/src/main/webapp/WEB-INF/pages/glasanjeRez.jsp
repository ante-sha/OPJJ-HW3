<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map"%>
<%@page import="hr.fer.zemris.java.hw13.servlets.prob7.Band"%>
<%@page import="java.util.List"%>
<%@ page session="true" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%!public List<Band> sortedBands = null;%>
<%
	String color = (String) session.getAttribute("pickedBgColor");
	if (color == null) {
		color = "ffffff";
	}

	sortedBands = (List<Band>) request.getAttribute("bands");
	sortedBands.sort((b1, b2) -> Long.compare(b1.getID(), b2.getID()));

	Map<Long, Long> votes = new TreeMap<>((Map<Long, Long>) request.getAttribute("votes"));
%>
<%!public Band getBandWithKey(long id) {
		for (Band band : sortedBands) {
			if (band.getID() == id) {
				return band;
			}
		}
		return null;
	}%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="style.css">
<style>
body {
	background: #<%=color%>;
}

table.rez td {
	text-align: center;
}
</style>
<title>Rezultati glasanja</title>
</head>
<body>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" cellspacing="0" class="rez">
		<thead>
			<tr>
				<th>Bend</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="band" items="<%=sortedBands%>">
				<tr>
					<td>${band.name }</td>
					<td><%=votes.get(((Band) pageContext.getAttribute("band")).getID())%></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="glasanje-grafika" width="400" height="400" />
	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="glasanje-xls">ovdje</a>
	</p>

	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
	<ul>
		<%
			List<Long> maxVote = votes.entrySet().stream()
					.sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())).map(entry -> entry.getKey())
					.collect(Collectors.toList());
		%>
		<c:forEach var="i" begin="0" end="1">
			<%
				Band band = getBandWithKey(maxVote.get((Integer) pageContext.getAttribute("i")));
			%>
			<li><a href="<%=band.getUrl()%>" target="_blank"><%=band.getName()%></a></li>
		</c:forEach>
	</ul>
</body>
</html>