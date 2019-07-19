<%@page import="java.util.Random"%>
<%@ page session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%!
/**
 * Metoda koja generira slučajnu broju
 *
 * @param rand objekt za generiranje slučajnih brojeva
 * @return heksa zapis rgb-a boje
 */
private String generateRandomColor(Random rand){
	StringBuilder sb = new StringBuilder();
	for(int i = 0;i<6;i++){
		sb.append(Character.forDigit(rand.nextInt(16), 16));
	}
	return sb.toString();
}
%>
<%
String color = (String)session.getAttribute("pickedBgColor");
if(color == null){
	color = "ffffff";
}

String fontColor = generateRandomColor(new Random());
%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.css">
		<style type="text/css">
			body {
				background: #<%= color%>;
				color: #<%= fontColor%>;
			}
		</style>
		<title>Funny page</title>
	</head>
    <body>
   	 <p>-Knock! Knock!<br>
		Who’s there?<br>
		-Says.<br>
		Says who?<br>
		-Says me, that’s who!
	</p>
	<p>
		Q: How do you stay warm in an empty room?<br>
		A: Go stand in the corner—it’s always 90 degrees.
	</p>
	<p>
		Old mathematicians never die.<br>
		They just disintegrate
	</p>
    </body>
</html>