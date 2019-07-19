<%@page import="hr.fer.zemris.java.tecaj_13.forms.CommentForm"%>
<%@page import="hr.fer.zemris.java.tecaj_13.model.BlogComment"%>
<%@page import="hr.fer.zemris.java.tecaj_13.model.BlogEntry"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
BlogEntry entry = (BlogEntry)request.getAttribute("entry");

String email = (String)session.getAttribute("current.user.email");
boolean logged = false;
if(email != null){
	logged = true;
} else {
	CommentForm form = (CommentForm)request.getAttribute("form");
	email = form == null ? "" : form.getEmail();
}
%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
		<title>Blog entry page</title>
	</head>
    <body>
    	<div>
    		<span id="title"><h2>Title: ${entry.title}</h2><h3>EMail: ${entry.creator.email }</h3></span>
    		<c:if test="<%= session.getAttribute(\"current.user.id\") != null%>">
    			<span class="header">
    				<a href="<%= request.getContextPath()%>/servleti/logout">Logout</a>
    			</span>
    		</c:if>
    		<span class="header"><a href="<%= request.getContextPath()%>/servleti/main">Home</a></span>
    		<span class="inf">Created: <%= formatter.format(entry.getCreatedAt())%></span>
    		<c:if test="${entry.lastModifiedAt ne null }">
    			<span class="inf">Modified: <%= formatter.format(entry.getLastModifiedAt())%></span>
    		</c:if>
    		
    		
    		
    		<p>${entry.text }</p>
    		<c:if test="${editable }">
    		<form action="edit" method="post">
    			<input type="hidden" name="EID" value="${entry.id }">
    			<input type="submit" value="Edit">
    		</form>
    		</c:if>
    	</div>
    	<hr>
    	<div>
    		<c:forEach var="com" items="${entry.comments }">
    			<div class="comment">
    				<br>
    				<span>${com.usersEMail}</span><span class="inf">Posted on: <%= formatter.format(((BlogComment)pageContext.findAttribute("com")).getPostedOn()) %></span>
    				<hr>
    				<div>
    					<p>${com.message }</p>
    				</div>
    			</div>
    		</c:forEach>
    		<div>
   			<form id="comForm" action="" method="post" align="right">
   				<label>Email</label>
   				<br>
   				<input type="text" name="email" value="<%= email%>" <% if(logged) out.write("readonly");%>>
   				<c:if test="${form ne null and form.hasError('email') }">
   					<div class="error">${form.getError('email') }</div>
   				</c:if>
   				<br>
   				<label>Comment</label><br>
   				<textarea form="comForm" name="message" rows="4" cols="40">${form.message }</textarea>
   				<c:if test="${form ne null and form.hasError('message') }">
   					<div class="error">${form.getError('message') }</div>
   				</c:if>
   				<br>
   				<input type="submit" value="Comment">
   			</form>
    		</div>
    	</div>
    	
    </body>
</html>