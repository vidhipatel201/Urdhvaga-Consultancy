<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Jobs - Urdhvaga Consultancy"/>
</jsp:include>

<div id="wrapper">

	<!-- Modal -->
<div id="jobModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Job Details</h4>
      </div>
      <div class="modal-body">
        <div id="jobDetails"><b>Please wait while job details are loading!</b></div>
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn mybtn" onclick="showTC()" data-dismiss="modal">Apply</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>

<!-- Modal -->
<div id="termsModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Terms & Conditions</h4>
      </div>
      <div class="modal-body">
        <input type="hidden" id="jobId">
        <div id="termsAndConditions"><b>Please wait while terms & conditions are loading!</b></div>
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn mybtn" onclick="applyJob()" data-dismiss="modal">Accept T&C and Apply</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>

<div class="white-bg top-btm-pad">

	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h3 class="page-title">
					<i class="fa fa-briefcase title-icon"></i> Jobs
				</h3>
			</div>
		</div>
		<div class="row">
		
		<div class="col-lg-12" id="main-content">
			<div id="allJobs"></div>
			<%
						if(SessionManager.sessionExists(request, "noJobs") != null) {
			%>
						<div class="row" id="error-alert">
							<div class="col-lg-12">
							<div class="alert alert-danger">
							<%= SessionManager.sessionExists(request, "noJobs").toString() %>
							</div>
							</div>
						</div>
						
			<%				
						}
			%>
		</div>
		
		</div>
	</div>
</div>

</div>

<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg7"/>
</jsp:include>

<script src="<%=RouteManager.getBasePath()%>assets/js/pagination.js"></script>
<script>
<% if(SessionManager.sessionExists(request, "noJobs") != null) {
	SessionManager.unsetSession(request, "noJobs");
} else {
%>

$(document).ready(function() {
 $("#allJobs").jPagination({
    	basePath: "<%=RouteManager.getBasePath()%>",
    	source: "<%=RouteManager.getBasePath()%>jobs?action=all"
    });
// $("#sidebar").hide();
});
<% } %>

function showJobDetails(jobId) {
	document.getElementById("jobId").value = jobId;
	$('#jobModal').modal('show');
	$.ajax({
		url: '<%=RouteManager.getBasePath()%>job',
		method: "POST",
		data: {
			"jobId": jobId,
		},
		success: function(data) {
			document.getElementById("jobDetails").innerHTML = data;
		},
		error: function(data) {
			document.getElementById("jobDetails").innerHTML = "Something went wrong. Please try again after refreshing page.";
		}
	});
}

function showTC() {
	$('#jobModal').modal('hide');
	$.ajax({
		url: '<%=RouteManager.getBasePath()%>terms-conditions?action=getTC',
		method: 'POST',
		success: function(data) {
			document.getElementById("termsAndConditions").innerHTML = data;
		},
		error: function(data) {
			document.getElementById("termsAndConditions").innerHTML = "Something went wrong. Please try again after refreshing page.";
		}
	});
	$("#termsModal").modal('show');
}

function showTCDirect(jobId) {
	document.getElementById("jobId").value = jobId;
	$.ajax({
		url: '<%=RouteManager.getBasePath()%>terms-conditions?action=getTC',
		method: 'POST',
		success: function(data) {
			document.getElementById("termsAndConditions").innerHTML = data;
		},
		error: function(data) {
			document.getElementById("termsAndConditions").innerHTML = "Something went wrong. Please try again after refreshing page.";
		}
	});
	$("#termsModal").modal('show');
}

function applyJob() {
	var basePath = '<%=RouteManager.getBasePath()%>';
	window.location.href = basePath + 'apply?job=' + document.getElementById("jobId").value;
}
</script>
