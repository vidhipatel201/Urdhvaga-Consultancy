<%@page import="utils.RouteManager" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Modify T&C - Urdhvaga Consultancy"/>
</jsp:include>

<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">

	<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info home_boxes">
					<div class="panel-heading">Modify Terms & Conditions</div>
  					<div class="panel-body">
  						<%=request.getAttribute("terms") %>
  					</div>
  				</div>
  			</div>
  	</div>

</div>

<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg11"/>
</jsp:include>

<script>
	function saveChanges(txt, id) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/terms?action=saveChanges',
			method: "POST",
			data: {
				'term': txt.value,
				'id': id,
			},
			success: function(data) {
				if(data == "error") {
					alert("Something went wrong. Please try again after refreshing the page.");
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again after refreshing the page.");
			}
		});
	}
</script>