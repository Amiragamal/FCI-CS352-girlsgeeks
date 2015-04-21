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
<h1>Users With The Name You Entered : </h1><br>
<c:forEach items="${it.userlist}" var="item" >
<c:forEach items="${it.currentuser}" var="user">
   <c:out value="${item}"/><p>
   <form action='/social/req' method='post'>
   <input type='hidden' value="${item}" name='userto'>
   <input type='hidden' value="${user}" name='userfrom'>
   <input type='submit' value='Add friend'>
   </form><br>
      <form action='/social/msgpage' method='post'>
   <input type='hidden' value="${item}" name='userto'>
   <input type='hidden' value="${user}" name='userfrom'>
   <input type='submit' value='Send Message'>
   </form><br>
   <form action='/social/viewtimeline' method='post'>
   <input type='hidden' value="${item}" name='name'>
   <input type='submit' value='Timeline'>
   </form><br>
   
  <br>
</c:forEach>
</c:forEach>
</div>

</body>
</html>
