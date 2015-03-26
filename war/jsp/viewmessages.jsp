<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core"   prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <title>Inbox</title>
</head>
<body background="http://bigbackground.com/wp-content/uploads/2013/08/plain-light-blue-backgrounds.jpg">
    <div align='center'>
<h1>Your Messages: </h1><br>
<c:forEach items="${it.msgs}" var="item" >
   Message from <c:out value="${item}"/><p>
 <form action='/social/read' method='post'>
  
 <input type='hidden' value="${item}" name='userfrom'>
 <input type='submit' value='read message'>
 </form><br>
 

   </form><br>
   
  <br>
</c:forEach>
</div>

</body>
</html>


