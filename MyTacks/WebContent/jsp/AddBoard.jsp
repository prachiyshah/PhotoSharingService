<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="/MyTacks/css/style.css" rel="stylesheet" type="text/css">
	<link href="/MyTacks/css/style1.css" rel="stylesheet" type="text/css">
	<link href="/MyTacks/css/jquery-ui-1.10.3.custom.min.css" rel="stylesheet" type="text/css">
	<link href="/MyTacks/css/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css">

</head>
<body>
<%@ include file="header.jsp" %>
<%String message=(String)request.getAttribute("categoryName"); %>

<div id="page";">
		<div id="body" style="background-image:url('/MyTacks/images/pinkbackground.gif');min-length:389px;">
		<form name="SignUp" method="post" action="/MyTacks/UserServlet" enctype="multipart/form-data">
				<input type="hidden" name="action" value="addBoardDetails"></input>
				<input type="hidden" name="categoryName" value=<%=message %>></input>
				<div>
			<div>
	<br>
				<br>
				<br>
				<br>
				<br>
			

			<table align="center">
			<tr>
					
						<td id="loginlabel">
						CategoryName
						
						</td>
						
						<td><%=message%></td>
					</tr>
					<tr>
						<td id="loginlabel">
						Board Name
						
						</td>
						<td><input type="text" name="boardName" id="boardName"></td>
						</tr>
						<tr>
						<td id="loginlabel">
						Board Image
						
						</td>
						<td><input type="file" name="boardImage" id="boardImage"></td>
						
					</tr>
					<tr>
					
					<td style="align:right;">
					<input type="submit" id="button" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" value="Add">
					
					</td>
					</tr>
			</table>
			</div>
			</div>	
				<br>
				<br>
		<br>
		<br>
		<br>	
				</form>
				
		</div>
		</div>	
		
		
<%@ include file="footerShow.jsp" %>

</body>
</html>