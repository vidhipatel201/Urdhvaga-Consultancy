<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<%@page import="model.Uc_position" %>
<%@page import="java.util.List" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Search Results - Urdhvaga Consultancy"/>
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
	
	<div class="container top-btm-pad">
		<div class="row" style="padding-top:15px">
		<form id="search_form">
			<div class="col col-lg-4 col-lg-offset-2 search-position" style="">
   				 <input type="text" id="search_position" onkeydown = "if (event.keyCode == 13) document.getElementById('btnSearch').click()" onkeyup="disableError('search_position');disableError('search_location');" name="position" class="form-control" placeholder="Position or Qualification">
   				 <label style="color:red;font-weight:normal;display:none" id="search_position_error"></label>
			</div>
			<div class="col col-lg-3 search-location" style="">
				<input type="text" name="location" onkeydown = "if (event.keyCode == 13) document.getElementById('btnSearch').click()" onkeyup="disableError('search_position');disableError('search_location');" id="search_location" class="form-control" placeholder="City or Town (optional)">
			</div>
			<div class="col-lg-2 search-button" style="">
				<button type="button" id="btnSearch" onclick="validate_search(this)" class="btn mybtn"><i style="color:#FFC300" class="fa fa-search"></i> &nbsp; Search</button>
			</div>
		</form>
		</div>
	</div>
	
	<%
	String resultsFor = "";
	if(SessionManager.sessionExists(request, "searchError") == null) {
		String pos = request.getParameter("position").toString();
		String loc = request.getParameter("location").toString();
		if(!pos.equals("") && !loc.equals("")) {
			resultsFor = " for Position '" + pos + "' and Location '" + loc + "'";
		} else if(pos.equals("") && !loc.equals("")) {
			resultsFor = " for Location '" + loc + "'";
		} else if(!pos.equals("") && loc.equals("")) {
			resultsFor = " for Position '" + pos + "'";
		}
	}
	%>
	
	
	
	<div class="white-bg top-btm-pad">

	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h3 class="page-title">
					<i class="fa fa-search title-icon"></i> Search Results<%=resultsFor %>
				</h3>
			</div>
		</div>
		
		<div class="row">
		
		<div class="col-lg-12" id="main-content">
			<div id="search-results"></div>
			<%
						if(SessionManager.sessionExists(request, "searchError") != null) {
			%>
						<div class="row" id="error-alert">
							<div class="col-lg-12">
							<div class="alert alert-danger">
							<%= SessionManager.sessionExists(request, "searchError").toString() %>
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
</div>

<jsp:include page="footer.jsp" flush="true" />
<script src="<%=RouteManager.getBasePath()%>assets/js/pagination.js"></script>

<script>
<% if(SessionManager.sessionExists(request, "searchError") != null) {
		SessionManager.unsetSession(request, "searchError");
	} else {
%>

$(document).ready(function() {
	 $("#search-results").jPagination({
	    	basePath: "<%=RouteManager.getBasePath()%>",
	    	source: "<%=RouteManager.getBasePath()%>search?action=find&position=<%=request.getParameter("position")%>&location=<%=request.getParameter("location")%>"
	    });
	// $("#sidebar").hide();
});
<% } %>
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
function validate_search(btn) {
	btn.innerHTML = "Please wait...";
	if($("#search_position").val() == "" && $("#search_location").val() == "") {
		showError("search_position", "Please enter the position or location to search for.");
	} else {
		//document.getElementById("search_form").submit();
		var base = "<%=RouteManager.getBasePath()%>";
		window.location.href = base + 'search?position=' + $("#search_position").val() + '&location=' + $("#search_location").val();
	}
	btn.innerHTML = '<i style="color:#FFC300" class="fa fa-search"></i> &nbsp; Search';
}

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