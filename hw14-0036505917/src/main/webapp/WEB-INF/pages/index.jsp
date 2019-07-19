<%@page import="hr.fer.zemris.java.p12.model.Poll"%>
<%@page import="java.util.List"%>
<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
List<Poll> polls = (List<Poll>)request.getAttribute("dao.polls");
%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
		<title>Index page</title>
	</head>
    <body>
    <h1>Polls</h1>
    <ul style="list-style-type:none;">
     <c:forEach var="poll" items="<%= polls %>">
    
      <li>
        <a href="glasanje?pollID=${poll.id }">${poll.title}</a>
        </li>
        <li>
        <textarea rows="4" cols="100" style="resize: none;" readonly>${poll.message}</textarea>
      </li>
      <br>
      
    
     </c:forEach>
	</ul>
    </body>
</html>