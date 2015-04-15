<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="/MyTacks/css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@ include file="header.jsp" %>
<%int count=(Integer)request.getAttribute("totalcount"); %>

<div id="page";>
<div id="body" style="background-image:url('/MyTacks/images/pinkbackground.gif');min-length:389px;">
		<%String tackName=""; %>
			<div id="content">
				<ul class="sale">
					<%for(int i=0;i<count;i++){ %>
<form name="listOfCategories" method="post" action="/MyTacks/UserServlet">

					
					<%String tackImage=(String)request.getAttribute("tackImage"+i); %>
					<%String tackURL=(String)request.getAttribute("tackURL"+i); %>
					
					
					
					<li>
					<%tackName=tackImage.substring(0,tackImage.length()-4); %>
					
					
<a href="<%=tackURL %>"><img src="<%=request.getContextPath() %>/images/<%=tackImage%>" style="border: 2px solid pink;border-radius: 30px;-moz-border-radius: 30px;
-khtml-border-radius: 30px;
-webkit-border-radius: 30px;
width: 180px;height: 240px;  alt=""></a></li>
					<input type="hidden" name="tackName" value="<%=tackName%>"></input>
					<li>
					<%String redirectFrom=(String)request.getAttribute("RedirectFrom"); %>
					<%if(redirectFrom.equals("updateTack")){ %>
					<input type="hidden" name="action" value="updateTack"></input>
					<input type="submit" name="updateTack" value="Update Tack"></input>
					<%} %>
					<%if(redirectFrom.equals("deleteTack")){ %>
					<input type="hidden" name="action" value="deleteTack"></input>
					<input type="submit" name="deleteTack" value="Delete Tack"></input>
					<%} %>
					<%if(redirectFrom.equals("likeTacks")){ %>
					<input type="hidden" name="action" value="likeTack"></input>
					<input type="submit" name="likeTack" value="Like"></input>
					<%} %>
										
					
					<br>
					<br>
				Tack Name:
					<br>
					<br>
					
					<%=tackName %>
					</li>
		</form>
					
				
					<%} %>
							</ul>
		
			</div>
		</div>
		</div>
		<%@ include file="footerShow.jsp" %>
</body>
</html>