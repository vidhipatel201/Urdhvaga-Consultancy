<%@page import="utils.SessionManager" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Apply - Urdhvaga Consultancy"/>
</jsp:include>

<div id="wrapper">
	
	<div class="container top-btm-pad">
	
	<%
						if(SessionManager.sessionExists(request, "apply_error") != null) {
			%>
						<div class="row" style="margin-top:30px">
							<div class="col-lg-offset-3 col-lg-6">
							<div class="alert alert-danger">
							<%= SessionManager.sessionExists(request, "apply_error").toString() %>
							</div>
							</div>
						</div>
						
			<%				
						SessionManager.unsetSession(request, "apply_error");
						}
			%>
			<%
						if(SessionManager.sessionExists(request, "apply_success") != null) {
			%>
						<div class="row" style="margin-top:30px">
							<div class="col-lg-offset-3 col-lg-6">
							<div class="alert alert-success">
							<%= SessionManager.sessionExists(request, "apply_success").toString() %>
							</div>
							</div>
						</div>
						
			<%				
						SessionManager.unsetSession(request, "apply_success");
						}
			%>
			
			<hr style="border-color:#818181">
		<div class="row">
			<div class="col-lg-12" style="text-align:center">
				<a style="cursor:pointer;" class="red-link">Browse more jobs</a>
			</div>
		</div>
			
	</div>
	
</div>

<jsp:include page="footer.jsp" flush="true" />
</body>
</html>