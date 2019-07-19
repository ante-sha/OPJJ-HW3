<%@page import="hr.fer.zemris.java.p12.model.Poll"%>
<%@page import="hr.fer.zemris.java.p12.model.PollOption"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page session="true" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%!
public List<PollOption> sortedOptions = null;

public PollOption getBandWithKey(long id) {
		for (PollOption option : sortedOptions) {
			if (option.getOptionId() == id) {
				return option;
			}
		}
		return null;
}
%>
<%

	sortedOptions = (List<PollOption>) request.getAttribute("dao.options");
	sortedOptions.sort((o1,o2)->Long.compare(o2.getVotesCount(),o1.getVotesCount()));
	Poll poll = (Poll)request.getAttribute("dao.poll");
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
<style>
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
				<th>Opcija</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="option" items="<%=sortedOptions%>">
				<tr>
					<td>${option.name }</td>
					<td>${option.votesCount }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="glasanje-grafika?pollID=<%= poll.getId()%>" width="400" height="400" />
	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="glasanje-xls?pollID=<%= poll.getId()%>">ovdje</a>
	</p>

	<h2>Razno</h2>
	<p>Primjeri djela pobjedničkih opcija:</p>
	<ul>
		<%
			long maxVote = sortedOptions.size() == 0 ? 0L : sortedOptions.get(0).getVotesCount();
			List<PollOption> maxList = sortedOptions.stream()
					.filter((opt)->opt.getVotesCount() == maxVote)
					.collect(Collectors.toList());
		%>
		<c:forEach var="option" items="<%= maxList%>">
			<li><a href="${ option.url}" target="_blank">${ option.name}</a></li>
		</c:forEach>
	</ul>
</body>
</html>