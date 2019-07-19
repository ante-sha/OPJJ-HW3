<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style.css">
<style>
table{
	align: left;
	text-align: left;
}
</style>
<title>Register</title>
</head>
<body>

	<div>
    	<span id="title"><h2>Register</h2></span>
    	<span class="header"><a href="<%= request.getContextPath()%>/servleti/main">Home</a></span>
    	<form action="register" method="post">
    	<table>
    	<tr>
    		<th>Nick: </th>
    			<th><input type="text" name="nick" value="${form.nick}" size="20"></th>
    			<c:if test="${form.hasError('nick')}">
	    			<th class="error">
    					${form.getError('nick') }
    				</th>
   				</c:if>
    	</tr>
    	<tr>
    		<th>Password: </th>
			<th><input type="password" name="pass" size="20"></th>
			<c:if test="${form.hasError('pass')}">
				<th class="error">
					${form.getError('pass') }
				</th>
			</c:if>
 		</tr>
 		<tr>
 			<th>Email: </th>
    			<th><input type="text" name="email" value="${form.email}" size="50"></th>
    			<c:if test="${form.hasError('email')}">
    				<th class="error">
   						${form.getError('email') }
   					</th>
   				</c:if>
   		</tr>
   		<tr>
   			<th>First name: </th>
	    			<th><input type="text" name="firstName" value="${form.firstName}" size="20"></th>
	    			<c:if test="${form.hasError('firstName')}">
    					<th class="error">
    						${form.getError('firstName') }
    					</th>
    				</c:if>
    	</tr>
    	<tr>
    		<th>Last name: </th>
	    			<th><input type="text" name="lastName" value="${form.lastName}" size="20"></th>
	    			<c:if test="${form.hasError('lastName')}">
    					<th class="error">
    						${form.getError('lastName') }
    					</th>
    				</c:if>
    	</tr>
    	<tr>
    		<th></th><th></th><th><input type="submit" value="Register"></th>
    	</tr>
    	</table>
    	</form>
    </div>
</body>
</html>