<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Add T&C - Urdhvaga Consultancy"/>
</jsp:include>

<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">

	<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info home_boxes">
					<div class="panel-heading">Add Terms & Conditions</div>
  					<div class="panel-body">
  					<%
						if(SessionManager.sessionExists(request, "error") != null) {
			%>
						<div class="row" id="error-alert">
							<div class="col-lg-offset-3 col-lg-6">
							<div class="alert alert-danger">
							<%= SessionManager.sessionExists(request, "error").toString() %>
							</div>
							</div>
						</div>
						
			<%				
						SessionManager.unsetSession(request, "error");
						}
			%>
			<%
						if(SessionManager.sessionExists(request, "success") != null) {
			%>
						<div class="row" id="success-alert">
							<div class="col-lg-offset-3 col-lg-6">
							<div class="alert alert-success">
							<%= SessionManager.sessionExists(request, "success").toString() %>
							</div>
							</div>
						</div>
						
			<%				
						SessionManager.unsetSession(request, "success");
						}
			%>
			
			<br>
  						<form method="POST" action="<%=RouteManager.getBasePath() %>admin/terms?action=save">
  						<input type="hidden" name="count" id="count" value="5">
  						<div class="form-group col-lg-12">
  							<textarea class="form-control" rows="2" name="tc1" id="tc1" placeholder="Enter"></textarea>
  						</div>
  						<div class="form-group col-lg-12">
  							<textarea class="form-control" rows="2" name="tc2" id="tc2" placeholder="Enter"></textarea>
  						</div>
  						<div class="form-group col-lg-12">
  							<textarea class="form-control" rows="2" name="tc3" id="tc3" placeholder="Enter"></textarea>
  						</div>
  						<div class="form-group col-lg-12">
  							<textarea class="form-control" rows="2" name="tc4" id="tc4" placeholder="Enter"></textarea>
  						</div>
  						<div class="form-group col-lg-12">
  							<textarea class="form-control" rows="2" name="tc5" id="tc5" placeholder="Enter"></textarea>
  						</div>
  						<div id="moreTC"></div>
  						<div class="form-group col-lg-12">
							<div class="col-lg-3">
								<button type="button" onclick="addMore()" class="btn btn-block btn-warning">Add more</button>
							</div>
							<div class="col-lg-3">
								<button type="submit" class="btn btn-block btn-primary">Save</button>
							</div>
						</div>
						</form>
  					</div>
  				</div>
  			</div>
  	</div>
  			

</div>

<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg11"/>
</jsp:include>

<script>
	function addMore() {
		var count = parseInt($("#count").val()) + 1;
		$("#count").val(count);
		var temp = '<div class="form-group col-lg-12">' +
				'<textarea class="form-control" rows="2" name="tc' + count + '" id="tc' + count + '" placeholder="Enter"></textarea>' +
					'</div>';
		$("#moreTC").append(temp);
	}
</script>