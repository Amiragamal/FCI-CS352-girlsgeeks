	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>Insert title here</title>
</head>
<body background="http://bigbackground.com/wp-content/uploads/2013/08/plain-light-blue-backgrounds.jpg">
    <div align='center'>
    
<a href="/social/signout">Logout</a><br>

<font size='10'>
<br><br><p> Welcome ${it.name} </p> 

<form method="post" action="/social/users">
<input type="text" name="userto"></input>
<input type="hidden" name="userfrom" value="${it.name}"></input>
<input type="submit" value="Search For User"></input></form>
<br>
<form method="post" action="/social/notif">
<input type="hidden" name="uf" value="${it.name}"></input>
<input type="submit" value="Show Requests"></input></form>
<br>
<form method="post" action="/social/friends">
<input type="hidden" name="uf" value="${it.name}"></input>
<input type="submit" value="Show Friends"></input></form>
<br>
</font>
</div>
</body>
</html>
