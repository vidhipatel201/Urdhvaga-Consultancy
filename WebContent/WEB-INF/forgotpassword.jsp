<%@ page import="utils.RouteManager" %>
<%@ page import="utils.SessionManager" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Forgot Password - Urdhvaga Consultancy"/>
</jsp:include>

<div id="wrapper">
	
	<div class="container top-btm-pad">
	
		<div class="container">
		<div class="row">
		<div class="col-lg-offset-3 col-lg-6">
		
			<%
						if(SessionManager.sessionExists(request, "error") != null) {
			%>
						<div class="row" id="error-alert">
							<div class="col-lg-12">
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
							<div class="col-lg-12">
							<div class="alert alert-success">
							<%= SessionManager.sessionExists(request, "success").toString() %>
							</div>
							</div>
						</div>
						
			<%				
						SessionManager.unsetSession(request, "success");
						}
			%>
		
			<div class="row">
				<div class="col-lg-12">
					<h3 class="page-title">
						<i class="fa fa-lock title-icon"></i> Forgot Password
					</h3>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<form id="passwordForm" method="POST" action="<%=RouteManager.getBasePath() %>forgot-password?action=requestNewPassword">
						<div class="form-group">
						<input type="email" class="form-control" id="user_email" name="user_email" placeholder="* Registered Email" required="required">
						</div>
						<div class="form-group">
						<button type="submit" class="btn mybtn">Request New Password</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		</div>
		</div>
	
	</div>
</div>

<jsp:include page="footer.jsp" flush="true" />