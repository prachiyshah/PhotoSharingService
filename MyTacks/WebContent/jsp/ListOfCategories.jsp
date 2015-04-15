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
		<%String categoryName=""; %>
			<div id="content">
				<ul class="sale">
					<%for(int i=0;i<count;i++){ %>
<form name="listOfCategories" method="post" action="/MyTacks/UserServlet">

					
					<%String categoryImage=(String)request.getAttribute("categoryImage"+i); %>
					
					
					
					
					<li>
					<%categoryName=categoryImage.substring(0,categoryImage.length()-4); %>
					
					
<a href=""><img src="<%=request.getContextPath() %>/images/<%=categoryImage%>" style="border: 2px solid pink;border-radius: 30px;-moz-border-radius: 30px;
-khtml-border-radius: 30px;
-webkit-border-radius: 30px;
width: 180px;height: 240px;  alt=""></a></li>
					<input type="hidden" name="categoryName" value="<%=categoryName%>"></input>
					<li>
			<%String redirectFrom=(String)request.getAttribute("RedirectFrom"); %>
			<%if(redirectFrom.equals("addBoard")){ %>					
					<input type="hidden" name="action" value="goToAddBoardPage"></input>
					<input type="submit" size="40px" name="addBoards" value="Add Board"></input>
					<%} %>
					<%if(redirectFrom.equals("viewBoards")){ %>					
					<input type="hidden" name="action" value="viewBoardsPage"></input>
					<input type="submit" size="10px" name="viewBoards" value="Boards"></input>
					<%} %>
					<br>
					<br>
				Category:
					<br>
					<br>
					
					<%=categoryName %>
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