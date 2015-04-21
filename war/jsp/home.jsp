	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core"   prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>Insert title here</title>
</head>
<style type="text/css"> 
 form{ display: inline-block; }
 </style>
<body background="http://bigbackground.com/wp-content/uploads/2013/08/plain-light-blue-backgrounds.jpg">
    <div align='center'>
    
<a href="/social/signout"><font color="black" size='5'> Logout</font></a><br><br>
<br>
<h3>Hashtag Trends : </h3>

<c:forEach items="${it.hashtags}" var="item" >
<font size="5">
       -- <a href='/social/hashtag/${item}'>#<c:out  value="${item}" escapeXml="false"/> </a></font>
   <br>

</c:forEach>
<br>
<font size='10'>
<c:forEach items="${it.name}" var="name" >
<font size="10">
   Welcome <c:out  value="${name}" escapeXml="false"/> </font>
   <br><br>
  
<form  method="post" action="/social/users">
<input type="text" name="userto"></input>
<input type="hidden" name="userfrom" value="${name}"></input>
<input type="submit" value="Search For User" ></input></form>
<br>
<form method="post" action="/social/shownotif">
<input  type="submit" value="Show Notifications" ></input></form>


<br>
<form method="post" action="/social/request" >
<input type="hidden" name="uf" value="${name}"></input>
<input type="submit" value="Show Requests"></input></form>
<form method="post" action="/social/friends" >
<input type="hidden" name="uf" value="${name}"></input>
<input type="submit" value="Show Friends"></input></form>
<br>
<form method="get" action="/social/grp" >
<input type="submit" value="Send Group Msg"></input></form>
<form method="post" action="/social/showgrp">
<input type="submit" value="Show Group Msgs"></input></form>

<form method="post" action="/social/viewmsgs">
<input type="hidden" name="uf" value="${name}"></input>
<input type="submit" value="Inbox"></input></form>

<br>
<form method="get" action="/social/createpage">
<input type="submit" value="Create Page"></input></form>

<form method="post" action="/social/viewepages">
<input type="submit" value="View All Pages"></input></form>

<form method="post" action="/social/viewemypages">
<input type="submit" value="Handle Your Pages"></input></form>

<br>

<form method="post" action="/social/viewtimeline">
<input type="hidden" name="name" value="${name}"></input>
<input type="submit" value="TimeLine"></input></form>
</c:forEach>
</font>
</div>
</body>
</html>
