<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Group Message</title>
</head>
<body background="http://bigbackground.com/wp-content/uploads/2013/08/plain-light-blue-backgrounds.jpg">
    <div align='center'>
<h1>Send a message</h1><br>

   <form action='/social/sendmsg' method='post'>
   TO : ${it.userto}
   <input type='hidden' name='userto' value='${it.userto}'><br><br>
    <input type='hidden' name='userfrom' value='${it.userfrom}'><br><br>
  <TEXTAREA placeholder="Enter your message here ...." Name="msgbody" ROWS=25 COLS=50></TEXTAREA><br><br>
   <input type='submit' value='Send Message' style="width:100px; height:50px;">
   </form><br>
   
  <br>
</div>

</body>
</html>
