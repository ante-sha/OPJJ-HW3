<%@page import="java.util.Random"%>
<%@ page import="java.awt.Color" session="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%! 
private String getHexCode(Color color){
		return String.format("%06x",color.getRGB() & 0xffffff);
	}


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
%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.css">
		<style>
			body {
				background: #<%= color%>};
			}
		</style>
		<title>Color chooser</title>
	</head>
   <body>
   	<a href="<%= request.getContextPath() %>/setcolor?color=<%= this.getHexCode(Color.WHITE) %>">WHITE</a>
   	<a href="<%= request.getContextPath() %>/setcolor?color=<%= this.getHexCode(Color.RED) %>">RED</a>
   	<a href="<%= request.getContextPath() %>/setcolor?color=<%= this.getHexCode(Color.GREEN) %>">GREEN</a>
   	<a href="<%= request.getContextPath() %>/setcolor?color=<%= this.getHexCode(Color.CYAN) %>">CYAN</a>
   	<a href="<%= request.getContextPath() %>/setcolor?color=<%= generateRandomColor(new Random())%>">Surprise me</a>
   </body>
</html>