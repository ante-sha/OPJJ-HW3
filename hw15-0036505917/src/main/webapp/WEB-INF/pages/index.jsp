<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
		<style>
			.login{
				display: inline-block;
				margin: 0 5;
				align: right;
			}
			#header {
				border-style: solid;
				border-witdth: 2px;
				justify-content: end;
			}
		</style>
		<title>Index page</title>
	</head>
    <body>
    <div id="header" align="center">
    	<div id="title" style="padding-right: 10;"><h1>Blog site</h1></div>
    		<c:if test="<%= session.getAttribute(\"current.user.id\") != null %>">
    			<div class="header">
    				<form action="logout" align="right">
    					<button>Logout</button>
    				</form>
    			</div>
    		</c:if>
    		<c:if test="<%= session.getAttribute(\"current.user.id\") == null %>">
    		<div class="header">
    	
		    	<form action="" method="post" align="right" id="loginForm">
		    			<div class="login">
			    			<div>Nick:
			    				<input type="text" name="nick" value="${form.nick}" size="20">
			    			</div>
			    			<div class="error">
			   					<c:if test="${form.hasError('nick')}">
			   						${form.getError('nick') }
			   					</c:if>
		   					</div>
		   				</div>
		   				<div class="login">
		   					<div>Pass:
		   						<input type="password" name="pass" size="20">
		   					</div>
		   					<div class="error">
		   						<c:if test="${form.hasError('pass')}">
		   							${form.getError('pass') }
		   						</c:if>
		   					</div>
		    			</div>
		    			<div class="login"><input type="submit" value="Login"></div>
		    			<span><a href="register">Register</a></span>
		    			
		    	</form>
		    	
   			 	</div>
		   </c:if>
    	</div>
    
    
    <hr>
    
    <h2>List of authors</h2>
    	<ol>
    		<c:forEach var="author" items="<%= request.getAttribute(\"users\")%>">
    			<li><a href="author/${author.nick }">${author.firstName } ${author.lastName}</a></li>
    		</c:forEach>
    	</ol>
    </body>
</html>