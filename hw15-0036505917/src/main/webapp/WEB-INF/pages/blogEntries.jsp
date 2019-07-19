<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
		<style>
			span{
				display: inline-block;
			}
			.error{
				display: inline-block;
			}
			.entry {
				background: gray;
				font-size: medium;
				text-decoration: bold;
				color: white;
			}
			li {
				margin: 20;
			}
		</style>
		<title>Blog entries</title>
	</head>
    <body>
    <div>
    	<span class="header"><a href="${pageContext.request.contextPath}/servleti/main">Home</a></span>
    	<c:if test="<%= session.getAttribute(\"current.user.id\") != null %>">
    			<span class="header">
    				<a href="<%= request.getContextPath()%>/servleti/logout">Logout</a>
    			</span>
    		</c:if>
    </div>
    	<h2>Blog entries</h2>
    	<ul>
    		<c:forEach var="entry" items="${entries }">
    			<li><a class="entry" href="${entry.creator.nick }/${entry.id }">${entry.title }</a></li>
    		</c:forEach>
    	</ul>
    	<c:if test="${showAdd }">
    	<br>
    		<a href="<%= session.getAttribute("current.user.nick")%>/new">Add blog entry</a>
    	</c:if>
    </body>
</html>