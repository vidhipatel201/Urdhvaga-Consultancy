<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Bank Account Details - Urdhvaga Consultancy"/>
</jsp:include>


<div id="wrapper">

<div class="container top-btm-pad">
	
	<div class="row">
			<div class="col-lg-12">
				<h3 class="page-title">
					<i class="fa fa-bank title-icon"></i> Bank Account Details for Registration Payment
				</h3>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<%=request.getAttribute("bankDetails") %>
			</div>
		</div>

</div>
	

</div>


<jsp:include page="footer.jsp" flush="true" />