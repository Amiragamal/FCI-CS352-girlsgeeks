<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core"   prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <title>Pages</title>
</head>
<body background="http://bigbackground.com/wp-content/uploads/2013/08/plain-light-blue-backgrounds.jpg">
    <div align='center'>
<h1>Pages: </h1><br>

<c:forEach items="${it.pages}" var="item" >
<font size="5">
    <c:out value="${item}"/><p>  </font>
 <form action='/social/openmypage' method='post'>
 <input type='hidden' value="${item}" name="pagename">
 <input type='submit' value='open page'>
 </form><br>
 

   </form><br>
   
  <br>

</c:forEach>
</div>

</body>
</html>

