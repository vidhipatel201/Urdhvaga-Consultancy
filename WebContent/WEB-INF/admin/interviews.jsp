<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<%@page import="controller.UCConstants" %>

<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Interviews - Urdhvaga Consultancy"/>
</jsp:include>
		
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
	
	<div class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Search Interview
				</div>
  				<div class="panel-body">
  				<form id="searchForm">
  				<input type="hidden" id="criteriaCount" name="criteriaCount">
  				<div id="moreCriteria">
  					
  				</div>
  				<div class="row">
  					<div class="col-lg-6">
  						<div class="col-lg-6">
  							<button type="button" onclick="addCriteria(this)" class="btn btn-block btn-warning">Add more criteria</button>
  						</div>
  						<div class="col-lg-6">
  							<button type="button" onclick="searchInterview(this)" class="btn btn-block btn-primary">Submit search query</button>
  						</div>
  					</div>
  				</div>
  				</form>
  				<div id="searchResults"></div>
  				</div>
			</div>
			</div>
		</div>
	
	<div class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Scheduled Interviews</div>
  				<div class="panel-body">
  				<div class="row">
  					<div class="col-lg-12">
  						<div class="col-lg-4" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.PAST_INTERVIEWS %>;color:#fff">
  							<span> Past interviews</span>
  						</div>
  						<div class="col-lg-4" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.TODAY_INTERVIEWS %>;color:#fff">
  							<span> Today's interviews</span>
  						</div>
  						<div class="col-lg-4" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.FUTURE_INTERVIEWS %>;color:#fff">
  							<span> Future interviews</span>
  						</div>
  					</div>
  				</div>
  					<div id="interviewTable"></div>
  					
  				</div>
			</div>
			</div>
		</div>
		
		<div id="interview_container" class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Interview Details <span id="interviewId"></span></div>
  				<div class="panel-body">
  					<div class="row">
  						<div class="col-lg-12" style="border-bottom:1px solid #c0c0c0;padding-bottom:10px;margin-bottom:10px"><h3>Employer Details</h3></div>
  					</div>
  					<div class="row">
  						<div class="col-lg-12" id="employerDetails"></div>
  					</div>
  					<div class="row">
  						<div class="col-lg-12" style="border-bottom:1px solid #c0c0c0;padding-bottom:10px;margin-bottom:10px"><h3>Candidate Details</h3></div>
  					</div>
  					<div class="row">
  						<div class="col-lg-12" id="candidateDetails"></div>
  					</div>
  				</div> 
			</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="footer.jsp" flush="true">
		<jsp:param name="pageId" value="pg8"/>
	</jsp:include>
	<script type="text/javascript" src="<%=RouteManager.getBasePath() %>admin/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>admin/css/bootstrap-multiselect.css" type="text/css"/>
	<script src="<%=RouteManager.getBasePath()%>admin/js/pagination.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script>
	var criteriaId = 0;
	
	$(document).ready(function() {
		var filtering = [
	    	['1', 'Show only Today\'s interviews'],
	    	['2', 'Show only Future interviews'],
	    	['3', 'Show only Past interviews'],
	    ];
		  $("#moreCriteria").append(createSearchCriterias());
		$("#interview_container").hide();
	    $("#interviewTable").jPagination({
	    	basePath: "<%=RouteManager.getBasePath()%>",
	    	limit: 15,
	    	filter: filtering,
	    	source: "<%=RouteManager.getBasePath()%>admin/interviews?action=getInterviewList"
	    });
	});
	
	function createSearchCriterias() {
		var temp = '<div class="row" style="border-bottom:1px solid #c0c0c0;padding-bottom:20px;margin-bottom:20px">';
		temp +=	'<div class="col-lg-6">';
		temp += '<select class="form-control" id="searchCriteria'+ criteriaId +'">';
		temp += '<option disabled selected hidden value="select">* Select search criteria</option>';
		temp += '<option value="1">Employer Id</option>';
		temp += '<option value="2">Employer name</option>';
		temp += '<option value="3">Interview Id</option>';
		temp += '<option value="4">Job Id</option>';
		temp += '<option value="5">Candidate Id</option>';
		temp += '<option value="6">Candidate name</option>';
		temp += '</select>';
		temp += '</div>';
		temp += '<div class="col-lg-6">';
		temp += '<input type="text" class="form-control" id="searchCriteriaValue'+ criteriaId +'" placeholder="* Value for the criteria you just selected">';
		temp += '</div>';
		temp += '</div>';
		document.getElementById("criteriaCount").value = criteriaId;
		criteriaId++;
		return temp;
	}
	
	function addCriteria(btn) {
		btn.innerHTML = "Please wait...";
		$("#moreCriteria").append(createSearchCriterias());
		btn.innerHTML = "Add more criteria";
	}
	
	function searchInterview(btn) {
		btn.innerHTML = "Please wait...";
		var criterias = [];
		var j = 0;
		var k = 0;
		//experience
		//0-value, 0-infinite, value-infinite, value-value, 0-0
		for(var i = 0 ; i < criteriaId ; i++) {

				if($("#searchCriteriaValue" + i).val() != "") {
					var temp = [$("#searchCriteria" + i).val(),$("#searchCriteriaValue" + i).val()];
					criterias.push(temp);
				}	
			
		}
		var filtering = [
	    	['1', 'Show only Today\'s interviews'],
	    	['2', 'Show only Future interviews'],
	    	['3', 'Show only Past interviews'],
	    ];
		
		$("#searchResults").jPagination({
	    	basePath: "<%=RouteManager.getBasePath()%>",
	    	limit: 15,
	    	filter: filtering,
	    	extraParameters: criterias,
	    	source: "<%=RouteManager.getBasePath()%>admin/interviews?action=getInterviewList"
	    });
		
		btn.innerHTML = "Submit search query";
	}
	
	function showDetails(btn, id) {
		btn.innerHTML = "Please wait...";
		$("#interviewId").html(id);
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>admin/interviews?action=showEmployerDetails",
			method: 'POST',
			data: {
				'interviewId':id,
			},
			success: function(data) {
				$("#employerDetails").html(data.details);
				var criterias = [[id]];
				$("#candidateDetails").jPagination({
			    	basePath: "<%=RouteManager.getBasePath()%>",
			    	limit: 15,
			    	extraParameters: criterias,
			    	source: "<%=RouteManager.getBasePath()%>admin/interviews?action=showCandidateDetails"
			    });
				$("#interview_container").show();
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			}
		});
		btn.innerHTML = "Details";
	}
	
	</script>
</body>
</html>