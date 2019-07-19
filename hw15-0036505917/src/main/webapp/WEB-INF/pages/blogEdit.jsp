<%@page import="hr.fer.zemris.java.tecaj_13.model.BlogEntry"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
BlogEntry entry = (BlogEntry)request.getAttribute("entry");

String method = request.getAttribute("modify.method").toString();
String title = "edit".equals(method) ? "Edit" : "Insert";
%>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
<title><%= title%> blog entry</title>
</head>
<body>

	<div>
    	<span id="title"><h2><%= title%> blog entry</h2></span>
    	<span class="header"><a href="<%= request.getContextPath()%>/servleti/main">Home</a></span>
    	<c:if test="${entry ne null }"><span class="header"><a href="<%= entry.getId()%>">Entry page</a></span></c:if>
    	<span class="header"><a href="<%= request.getContextPath()%>/servleti/author/<%= session.getAttribute("current.user.nick") %>">My entries</a></span>
    	<br>
    	<c:if test="${entry ne null }"><span class="inf">Created at: <%= formatter.format(entry.getCreatedAt())%></span></c:if>
    	<c:if test="${entry ne null and entry.lastModifiedAt ne null }"><span class="inf">Last modified at: <%= formatter.format(entry.getLastModifiedAt())%></span></c:if>
    	<form action="" method="post" id="blogForm">
    		<div>
	    		<div>
	    			<span>Title: </span>
	    			<input type="text" name="title" value="${form.title}" size="20">
	    			<div class="error">
    					<c:if test="${form.hasError('title')}">
    						${form.getError('title') }
    					</c:if>
    				</div>
	    		</div>
    		</div>
    		<div>
    			<div>
    				<div><span>Text: </span></div>
    				<textarea form="blogForm" name="text" cols="150" rows="15">${form.text}</textarea>
    				<div class="error">
    					<c:if test="${form.hasError('text')}">
    						${form.getError('text') }
    					</c:if>
    				</div>
    			</div>
    		</div>
    	
    		<div>
    			<input type="hidden" name="EID" value="${entry.id }">
    			<input type="submit" value="Submit">
    		</div>
    	</form>
    </div>
</body>
</html>