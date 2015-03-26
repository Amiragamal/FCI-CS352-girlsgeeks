<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core"   prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body background="http://bigbackground.com/wp-content/uploads/2013/08/plain-light-blue-backgrounds.jpg">
    <div align='center'>
<h1>Conversation: </h1><br>

<h3>Members of this conversation :</h3>
</font><c:forEach  items="${it.members}" var="member" >
<font size='5'>
#<c:out value="${member}"/><br>
</font>
</c:forEach>

---------------------------------------------------------
<br><br>
<c:forEach  items="${it.msgs}" var="msg"  >
<font size='8'>
 Message : <c:out value="${msg}"/><br>
 
  </font>
 </c:forEach>
<c:forEach items="${it.senders}" var="sender">

   From : <c:out value="${sender}"/><br>
  <br>

</c:forEach>
</font><c:forEach  items="${it.name}" var="name" >
 <form action='/social/replygrp' method='post'>
  <TEXTAREA placeholder="Enter your message here ...." Name="msgbody" ROWS=25 COLS=50></TEXTAREA><br><br>
  <input type=hidden value="${name}" name="convname" >
   <input type='submit' value='Send Message' style="width:100px; height:50px;">
   </form><br>
</c:forEach>
<br><br>

   
</div>

</body>
</html>
