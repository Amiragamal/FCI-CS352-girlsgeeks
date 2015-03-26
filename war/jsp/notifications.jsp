<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body background="http://bigbackground.com/wp-content/uploads/2013/08/plain-light-blue-backgrounds.jpg">
    <div align='center'>
<h1>Notifications: </h1><br>



<br><br>
<c:forEach  items="${it.requests}" var="req"  >
<font size='5'>
 -friend request from : <c:out value="${req}"/>
  <form action='/social/accept' method='post'>
    <input type='hidden' value=${req} name='userfrom'>
  <input type='submit' value='Handle'></form><br>
 
  
 </c:forEach>
<c:forEach items="${it.groups}" var="grp">
<font size='5'>
  -new message in group : <c:out value="${grp}"/>  <form action='/social/showconv' method='post'>
  <input type='hidden' value=${grp} name='convname'>
  <input type='submit' value='Handle'></form><br>
  
  <br>
</font>
</c:forEach>

<c:forEach items="${it.messages}" var="msg">
<font size='5'>
  -new message from : <c:out value="${msg}"/>  <form action='/social/read' method='post'>
  <input type='hidden' value='${msg}' name='userfrom'>
  <input type='submit' value='Handle'></form><br>
  
  <br>
</font>
</c:forEach>
   
</div>

</body>
</html>

