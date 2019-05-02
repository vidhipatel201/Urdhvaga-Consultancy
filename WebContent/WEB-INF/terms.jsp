<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Terms & Conditions - Urdhvaga Consultancy"/>
</jsp:include>

<div id="wrapper">
	<div class="container top-btm-pad">
	
	<div class="row">
			<div class="col-lg-12">
				<h3 class="page-title">
					<i class="fa fa-paste title-icon"></i> Terms & Conditions
				</h3>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
				<%=request.getAttribute("terms") %>
			</div>
		</div>
	
	</div>
</div>

<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="0"/>
</jsp:include>