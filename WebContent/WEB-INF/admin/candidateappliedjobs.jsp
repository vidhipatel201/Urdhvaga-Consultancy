<%@page import="utils.RouteManager" %>
<%@page import="controller.UCConstants" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Jobs Applied by Candidates - Urdhvaga Consultancy"/>
</jsp:include>

<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">

<div class="row">

				<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Search Candidate
				</div>
  				<div class="panel-body">
  				<form id="searchForm">
  				<div class="form-group col-lg-12">
  					<input type="number" class="form-control" placeholder="Candidate ID" id="candidate_id">
  				</div>
  				<div class="row">
  					<div class="col-lg-6">
  						<div class="col-lg-12">
  							<button type="button" onclick="searchCandidate(this)" class="btn btn-block btn-primary">Submit search query</button>
  						</div>
  					</div>
  				</div>
  				</form>
  				<hr>
  				<div class="row">
  					<div class="col-lg-12">
  						<div class="col-lg-3" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.PENDING_APPLICATION %>;color:#fff">
  							<span> Pending application</span>
  						</div>
  						<div class="col-lg-3" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.ACCEPTED_APPLICATION %>;color:#fff">
  							<span> Accepted application</span>
  						</div>
  						<div class="col-lg-3" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.REJECTED_APPLICATION %>;color:#fff">
  							<span> Rejected application</span>
  						</div>
  						</div>
  						</div>
  				<div id="searchResults"></div>
  				</div>
			</div>
			</div>

</div>

</div>

<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg12"/>
</jsp:include>

<script src="<%=RouteManager.getBasePath()%>assets/js/pagination.js"></script>

<script>
	function searchCandidate(btn) {
		var criterias = [];
		criterias = [[$("#candidate_id").val()]];
		$("#searchResults").jPagination({
	    	basePath: "<%=RouteManager.getBasePath()%>",
	    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=searchCandidateAppliedJobs",
	    	extraParameters: criterias,
	    	limit:100
	    });
	}
</script>