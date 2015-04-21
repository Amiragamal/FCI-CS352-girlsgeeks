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

<font size='10'>
<br>
<c:forEach items="${it.name}" var="item" >
<font size="10">
  Hashtag : <c:out  value="${item}" escapeXml="false"/> </font>
   <br>

</c:forEach>
<c:forEach items="${it.nop}" var="item" >
<font size="5">
  Number of posts : <c:out  value="${item}" escapeXml="false"/> </font>
   <br>

</c:forEach>
<br><br>

Posts: <br><br>

<c:forEach items="${it.posts}" var="post" >
<font size="5">
  <p style="border-style:solid;border-width: 2px;">  <c:out  value="${post}" escapeXml="false"/></p> </font>
   <br>

</c:forEach>
</div>

</font>
</div>
</body>
</html>
