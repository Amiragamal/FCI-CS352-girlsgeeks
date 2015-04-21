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
<c:forEach items="${it.name}" var="name" >
<font size="10">
<c:out  value="${name}" escapeXml="false"/></p> </font>

<hr/>
<form  method="post" action="/social/saveuserpost">
<h1>Privacy : </h1>
<font size="5">
<input type="radio" name="privacy" value="private">Private
<input type="radio" name="privacy" value="custom">Custom
<input type="text" name="custom" placeholder="none" />
<br><br>

<TEXTAREA placeholder="Enter your post here ...." Name="pagepost" ROWS=10 COLS=50></TEXTAREA><br>
<input type="hidden" value="${name}" name="user"></input><br>
Feeling : 
<select id='feeling' name='feeling'>
  <option value="none">None</option>
  <option value="sad">Sad</option>
  <option value="happy">Happy</option>
  <option value="angry">Angry</option>
  <option value="excited">Excited</option>
</select>
<br><br>
<input type="submit" value="Post to Timeline" ></input></form>
</form></font>
</c:forEach>
<br><br>

<font size="7">
Posts: <br><br>

<c:forEach items="${it.post}" var="post" >
<font size="5"><div style="border-style:solid;border-width: 2px;">
  <c:out  value="${post}" escapeXml="false"/></font><br>
  
</c:forEach>
</div></div>
<%-- Comment --%>
</font>
</div>
</body>
</html>
