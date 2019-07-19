<%@page import="hr.fer.zemris.java.p12.model.Poll"%>
<%@page import="hr.fer.zemris.java.p12.model.PollOption"%>
<%@page import="java.util.List"%>
<%@ page session="true" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	List<PollOption> sortedOptions = (List<PollOption>) request.getAttribute("dao.options");
	
	Poll poll = (Poll)request.getAttribute("dao.poll");
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
<title>Glasanje</title>
</head>
<body>
	<h1><%= poll.getTitle()%></h1>
	<p><%= poll.getMessage() %></p>
	<ol>
		<c:forEach var="option" items="<%=sortedOptions%>">
			<li><a href="glasanje-glasaj?optionID=${option.optionId}">${option.name}</a></li>
		</c:forEach>
	</ol>
</body>
</html>