<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Admin Manager - Urdhvaga Consultancy"/>
</jsp:include>
		
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
	
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info home_boxes">
				<div class="panel-heading">Registered Admins
				</div>
  				<div class="panel-body">
  					<div class="row">
  					<div class="col-lg-12">
  					<% if(request.getAttribute("adminList") != null) { %>
  						<table class="table table-striped table-bordered">
  							<tr>
  								<th>Sr No.
  								<th>Username
  								<th>Type
  								<th>Deactivate
  								<th>Remove
  							</tr>
  							<%= request.getAttribute("adminList") %>
  						</table>
  					<% } else { %>
  						No other admins registered.
  					<% } %>
  					</div>
  					
  				</div>
  				</div>
			</div>
		</div>
	</div>
	
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info home_boxes">
				<div class="panel-heading">Register New Admin
				</div>
  				<div class="panel-body">
  					<div class="row">
  					<div class="col-lg-12">
  						<form action="<%=RouteManager.getBasePath()%>admin/admin-manager?action=add" method="POST" id="adminRegistrationForm">
  							<div class="form-group">
  								<%= request.getAttribute("adminTypes") %>
  							</div>
  							<div class="form-group">
  								<input class="form-control" type="text" id="adminUsername" name="adminUsername" placeholder="Username"/>
  								<label style="color:red;font-weight:normal;display:none" id="adminUsername_error"></label>
  							</div>
  							<div class="form-group">
  								<label class="form-control">Default password: 123456</label>
  							</div>
  							<div class="form-group">
  								<button type="button" onclick="validate(this)" class="btn btn-primary">Submit</button>
  							</div>
  						</form>
  						<br>
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
  					</div>
  					</div>
  				</div>
  				</div>
			</div>
		</div>
	</div>
	
	
	
<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg4"/>
</jsp:include>

<script>
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
	function validate(btn) {
		btn.innerHTML = "Please wait";
		if(document.getElementById("adminTypes").value == "select") {
			showError("adminTypes", "Please select admin type");
		} else if($("#adminUsername").val() == "") {
			showError("adminUsername", "Please enter admin username");
		} else if(/\s/.test($("#adminUsername").val())) {
			showError("adminUsername", "Username cannot contain spaces");
		} else {
			document.getElementById("adminRegistrationForm").submit();
		}
		btn.innerHTML = "Submit";
	}
	
	function saveChanges(drop, id) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/admin-manager?action=saveTypeChanges',
			method: 'POST',
			data: {
				'typeId': drop.value,
				'adminId': id,
			},
			success: function(data) {
				if(data == "error") {
					alert("Something went wrong. Please refresh the page and try again.");
				}
			},
			error: function(data) {
				alert("Something went wrong. Please refresh the page and try again.");
			}
		});
	}
	
	function deactivateAdmin(btn, id) {
		btn.innerHTML = "Please wait";
		var success = false;
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/admin-manager?action=deactivateAdmin',
			data: {
				'adminId': id,
			},
			method: 'POST',
			success: function(data) {
				if(data == "error") {
					alert("Something went wrong. Please try again after refreshing this page.");
					success = false;
				} else {
					success = true;
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again after refreshing this page.");
				btn.innerHTML = "Deactivate";
			},
			complete: function() {
				if(success) {
					btn.setAttribute('onclick', 'activateAdmin(this,' + id + ')');
					btn.classList.remove("btn-warning");
					btn.classList.add("btn-success");
					btn.innerHTML = "Activate";
				} else {
					btn.innerHTML = "Deactivate";
				}
			}
		});
	}
	
	function activateAdmin(btn, id) {
		btn.innerHTML = "Please wait";
		var success = false;
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/admin-manager?action=activateAdmin',
			data: {
				'adminId': id,
			},
			method: 'POST',
			success: function(data) {
				if(data == "error") {
					alert("Something went wrong. Please try again after refreshing this page.");
					success = false;
				} else {
					success = true;		
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again after refreshing this page.");
				btn.innerHTML = "Activate";
			},
			complete: function() {
				if(success) {
					btn.setAttribute('onclick', 'deactivateAdmin(this,' + id + ')');
					btn.classList.remove("btn-success");
					btn.classList.add("btn-warning");
					btn.innerHTML = "Deactivate";
				} else {
					btn.innerHTML = "Activate";
				}
			}
		});
	}
	
	function deleteAdmin(btn, id) {
		btn.innerHTML = "Please wait";
		if(confirm("Are you sure you want to remove this admin?")) {
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>admin/admin-manager?action=deleteAdmin',
				method: 'POST',
				data: {
					"adminId": id,
				},
				success: function(data) {
					if(data == "success") {
						location.reload();
					} else if(data == "dependency") {
						alert("Other data is dependent on this admin. First remove dependent data and than remove this admin.");
					} else {
						alert("Something went wrong. Please try again after refreshing this page.");
					}
				},
				error: function(data) {
					alert("Something went wrong. Please try again after refreshing this page.");
				}
			});
		}
	}
</script>

</body>
</html>