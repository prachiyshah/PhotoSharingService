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
<%String message=(String)request.getAttribute("boardName"); %>

<div id="page";">
		<div id="body" style="background-image:url('/MyTacks/images/pinkbackground.gif');min-length:389px;">
		<form name="SignUp" method="post" action="/MyTacks/UserServlet" enctype="multipart/form-data">
				<input type="hidden" name="action" value="addTackDetails"></input>
				<input type="hidden" name="boardName" value=<%=message %>></input>
				<div id="loginbox">
			<div>
			<table>
			<tr>
					
						<td id="loginlabel">
						BoardName
						
						</td>
						
						<td><%=message%></td>
					</tr>
					<tr>
						<td id="loginlabel">
						Tack Name
						
						</td>
						<td><input type="text" name="tackName" id="tackName"></td>
						</tr>
						<tr>
						<td id="loginlabel">
						Tack Image
						
						</td>
						<td><input type="file" name="tackImage" id="tackImage"></td>
						
					</tr>
					
					<tr>
						<td id="loginlabel">
						Tack URL:
						
						</td>
						<td><input type="text" name="tackURL" id="tackURL"></td>
						
					</tr>
					
					<tr>
					
					<td style="align:right;">
					<input type="submit" id="button" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" value="Add">
					
					</td>
					</tr>
					<tr>
					
					<td style="align:right;">
					<% %>
					
					</td>
					</tr>
					
			</table>
			</div>
			</div>	
				
				</form>
		</div>
		</div>	
<%@ include file="footerShow.jsp" %>

</body>
</html>