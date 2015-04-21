	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core"   prefix="c" %>
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


<br><br>
<c:forEach items="${it.like}" var="item" >
<font size="4">
   Number of likers :  <c:out value="${item}"/>
  </c:forEach>
<br><br>
<c:forEach items="${it.name}" var="item" >
<font size="7">
    <c:out value="${item}"  /><p>  </font>

    <br><br>

<form  method="post" action="/social/savepost">
<h1>Privacy : </h1>
<font size="5">
<input type="radio" name="privacy" value="private">Private
<br><br>

<TEXTAREA placeholder="Enter your post here ...." Name="pagepost" ROWS=10 COLS=50></TEXTAREA><br>
<input type="hidden" value="${item}" name='pname'>
<input type="submit" value="Post to page" ></input></form>
</form>
    </c:forEach>
<br><br>

<font size="7">
Posts: <br><br>

<c:forEach items="${it.post}" var="post" >
<font size="5"><div style="border-style:solid;border-width: 2px;">
   <c:out  value="${post}" escapeXml="false"/> </font>
   <br>

</c:forEach>
</div>
</div>
</font>
</div>
</body>
</html>
