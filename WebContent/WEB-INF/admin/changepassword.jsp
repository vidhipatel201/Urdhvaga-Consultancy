<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Change Password - Urdhvaga Consultancy"/>
</jsp:include>

<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">

	<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info home_boxes">
					<div class="panel-heading">Change Password</div>
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
			
			<div class="row">
				<div class="col-lg-12">
					<form id="passwordForm" method="POST" action="<%=RouteManager.getBasePath() %>AdminUserAccounts?action=save_password">
						<div class="form-group">
						<input type="password" class="form-control" onkeyup="disableError('old_password')" id="old_password" name="old_password" placeholder="* Old Password">
						<label style="color:red;font-weight:normal;display:none" id="old_password_error"></label>
						</div>
						<div class="form-group">
						<input type="password" class="form-control" onkeyup="disableError('new_password')" id="new_password" name="new_password" placeholder="* New Password">
						<label style="color:red;font-weight:normal;display:none" id="new_password_error"></label>
						</div>
						<div class="form-group">
						<input type="password" class="form-control" onkeyup="disableError('confirm_password')" id="confirm_password" placeholder="* Confirm New Password">
						<label style="color:red;font-weight:normal;display:none" id="confirm_password_error"></label>
						</div>
						<div class="form-group">
						<button type="button" class="btn btn-primary" onclick="validateChanges(this)">Save Changes</button>
						</div>
					</form>
				</div>
			</div>
			
			</div>
			</div>
			</div>
			</div>
			

</div>

<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="0"/>
</jsp:include>

<script>
function validateChanges(btn) {
	btn.innerHTML = "Please wait";
	if($("#old_password").val() == "" || $("#old_password").val().length < 6) {
		showError("old_password", "Please enter your current password.");
	} else {
		if($("#new_password").val() == "" || $("#new_password").val().length < 6) {
			showError("new_password", "Please enter valid new password with minimum 6 characters.");
		} else {
			if($("#new_password").val() != $("#confirm_password").val()) {
				showError("confirm_password", "New password and confirm password mismatch.");
			} else {
				document.getElementById("passwordForm").submit();
			}
		}
	}
	btn.innerHTML = "Save Changes";
}

function disableError(id) {
	$('#' + id).css({'border-color':''});
	$('#' + id + "_error").css({'display':'none'});
}

function showError(id, msg) {
	$('#' + id).css({'border-color':'red'});
	$('#' + id).focus();
	$('#' + id + "_error").html(msg);
	$('#' + id + "_error").css({'display':'block'});
}
</script>