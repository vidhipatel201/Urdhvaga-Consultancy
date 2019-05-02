<%@page import="controller.UCConstants" %>
<%@page import="utils.RouteManager" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Applied Jobs - Urdhvaga Consultancy"/>
</jsp:include>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.15/css/dataTables.bootstrap.min.css">

<div id="wrapper">

<!-- Modal -->
<div id="reasonModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Reason for application rejection</h4>
      </div>
      <div class="modal-body">
        <div id="reason"></div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>

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
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>

<!-- Modal -->
<div id="scheduleModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Interview Schedule</h4>
      </div>
      <div class="modal-body">
        <div id="interviewSchedule"><b>Please wait while interview schedule is loading!</b></div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>
	
	<div class="container top-btm-pad">
		
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
  					<div class="panel-heading"><b>Applied jobs by you</b></div>
  					<div class="panel-body">
  					<% if(request.getAttribute("jobsAvail").toString().equals("1")) { %>
  					<div class="row">
  					<div class="col-lg-12">
  						<div class="col-lg-4" style="text-align:center;border:1px solid #fff;border-radius:20px;background: <%=UCConstants.PENDING_APPLICATION %>;color:#fff">
  							<span> Pending Application</span>
  						</div>
  						<div class="col-lg-4" style="text-align:center;border:1px solid #fff;border-radius:20px;background: <%=UCConstants.ACCEPTED_APPLICATION %>;color:#fff">
  							<span> Accepted Application</span>
  						</div>
  						<div class="col-lg-4" style="text-align:center;border:1px solid #fff;border-radius:20px;background: <%=UCConstants.REJECTED_APPLICATION %>;color:#fff">
  							<span> Rejected Application</span>
  						</div>
  						</div>
  						</div>
  						<br>
  						<table id="jobs" class="table table-bordered" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Position</th>
                <th>Date of Application</th>
                <th>Status</th>
                <th>Job details</th>
                <th>Interview schedule</th>
                <th>Cancel</th>
            </tr>
        </thead>
        <tbody>
            <%=request.getAttribute("jobs") %>
            </tbody>
            </table>
            <% } else { %>
            	<div class="row">
            		<div class="col-lg-12">
            			<b>You haven't applied to any job yet!</b>
            		</div>
            	</div>
            <% } %>
  					
  					</div>
				</div>
			</div>
		</div>
	
	</div>
	
</div>


<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg3"/>
</jsp:include>


<script src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.15/js/dataTables.bootstrap.min.js"></script>

<script>

$(document).ready(function() {
    $('#jobs').DataTable();
});

function showJobDetails(jobId) {
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

function showReason(reason) {
	document.getElementById("reason").innerHTML = reason;
	$('#reasonModal').modal('show');
}

function viewSchedule(jobId) {
	$('#scheduleModal').modal('show');
	$.ajax({
		url: '<%=RouteManager.getBasePath()%>job?action=getInterviewSchedule',
		method: "POST",
		data: {
			"jobId": jobId,
		},
		success: function(data) {
			document.getElementById("interviewSchedule").innerHTML = data;
		},
		error: function(data) {
			document.getElementById("interviewSchedule").innerHTML = "Something went wrong. Please try again after refreshing page.";
		}
	});
}

function cancelApplication(jobId) {
	if(confirm("Are you sure, you want to cancel this application?")) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>applied?action=cancelApplication',
			method: "POST",
			data: {
				"jobId": jobId,
			},
			success: function(data) {
				if(data == "success") {
					alert("Job application is successfully canceled.");
					location.reload();
				} else {
					alert("Something went wrong. Please try again.");
					location.reload();
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			}
		});
	}
}

</script>

</body>
</html>