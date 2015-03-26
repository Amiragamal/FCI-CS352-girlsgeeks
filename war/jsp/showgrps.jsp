<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core"   prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Show Group msgs</title>
</head>
<body background="http://bigbackground.com/wp-content/uploads/2013/08/plain-light-blue-backgrounds.jpg">
    <div align='center'>
<h1>Conversations you are a member in: </h1><br>
<c:forEach items="${it.grplist}" var="item" >
   <c:out value="${item}"/><p>
   <form action='/social/showconv' method='post'>
   <input type='hidden' value="${item}" name='convname'>
   <input type='submit' value='show conversation'>
   </form><br>
   
  <br>
</c:forEach>
</div>

</body>
</html>
