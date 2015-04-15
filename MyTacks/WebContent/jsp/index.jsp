<%@ include file="header.jsp" %>
	<%String uId=(String)session.getAttribute("userID"); %>
	<%=uId %>
<div id="page";>
<div id="body" style="background-image:url('/MyTacks/images/pinkbackground.gif');min-length:389px;">
			<div class="featured">
				
			Your last login was at <%=session.getAttribute("lastLogin") %>
				<ul>
					<li>
						<a href="index.html"><img src="/MyTacks/images/food-and-dinning.png" alt=""></a>
					</li>
					<li>
						<a href="index.html"><img src="/MyTacks/images/crazy-shopping.png" alt=""></a>
					</li>
					<li>
						<a href="index.html"><img src="/MyTacks/images/discover-ef.png" alt=""></a>
					</li>
					<li>
						<a href="index.html"><img src="/MyTacks/images/big-discount-fever.png" alt=""></a>
					</li>
				</ul>
			</div>
			</div>
			</div>
		<%@ include file="footerShow.jsp" %>	