<%@page import="controller.UCConstants" %>
<%@page import="utils.RouteManager" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Approved Applications - Urdhvaga Consultancy"/>
</jsp:include>


<!-- Modal -->
<div id="rejectJobModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Please provide reason to reject job</h4>
      </div>
      <div class="modal-body">
        <input type="text" class="form-control" id="rejectJobReason" placeholder="Reason to reject Job">
        <input type="hidden" id="rejectJobId">
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn btn-danger" onclick="rejectJob(this)" data-dismiss="modal">Reject</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>

<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
<div class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Search Requirements
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
  							<button type="button" onclick="searchRequirements(this)" class="btn btn-block btn-primary">Submit search query</button>
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
				<div class="panel-heading">Job Requirements</div>
  				<div class="panel-body">
  				<div class="row">
  					<div class="col-lg-12">
  						<div class="col-lg-3" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.PENDING_APPLICATION %>;color:#fff">
  							<span> Pending requirement</span>
  						</div>
  						<div class="col-lg-3" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.ACCEPTED_APPLICATION %>;color:#fff">
  							<span> Accepted requirement</span>
  						</div>
  						<div class="col-lg-3" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.REJECTED_APPLICATION %>;color:#fff">
  							<span> Rejected requirement</span>
  						</div>
  						</div>
  						</div>
  				<div id="approvedRequirements"></div>
  				</div>
			</div>
			</div>
		</div>
		<div id="application_container" class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Requirement Id - <span id="applicationId"></span></div>
  				<div class="panel-body">
  					<div class="row">
  						<div class="col-lg-12" style="border-bottom:1px solid #c0c0c0;padding-bottom:10px;margin-bottom:10px"><h3>Requirement details</h3></div>
  					</div>
  					<div class="row"><div class="col-lg-12" id="requirementDetails"></div></div>
  					<div class="row">
  						<div class="col-lg-12" style="border-bottom:1px solid #c0c0c0;padding-bottom:10px;margin-bottom:10px"><h3>Candidates</h3></div>
  					</div>
  					<div class="row">
  						
  						<div class="col-lg-12" id="applicationDetails"></div>
  					</div>
  					<div id="candidateDetails_container" class="row">
  						<div class="col-lg-12" style="border-bottom:1px solid #c0c0c0;padding-bottom:10px;margin-bottom:10px"><h3>Candidate details</h3></div>
  					</div>
  					<div class="row"><div class="col-lg-12" id="candidateDetails"></div></div>
  				</div> 
			</div>
			</div>
		</div>
</div>


<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg5"/>
</jsp:include>
<script type="text/javascript" src="<%=RouteManager.getBasePath() %>admin/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>admin/css/bootstrap-multiselect.css" type="text/css"/>
<script src="<%=RouteManager.getBasePath()%>assets/js/pagination.js"></script>
<script>
var criteriaId = 0;
$(document).ready(function() {
	$("#application_container").hide();
	$("#candidateDetails_container").hide();
	$("#moreCriteria").append(createSearchCriterias());
	var filtering = [
    	['1', 'Show only pending requirements'],
    	['2', 'Show only accepted requirements'],
    	['3', 'Show only rejected requirements'],
    ];
	 $("#approvedRequirements").jPagination({
	    	basePath: "<%=RouteManager.getBasePath()%>",
	    	filter: filtering,
	    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getTelecallerApprovedJobs"
	    });
});

function createSearchCriterias() {
	var temp = '<div class="row" style="border-bottom:1px solid #c0c0c0;padding-bottom:20px;margin-bottom:20px">';
	temp +=	'<div class="col-lg-6">';
	temp += '<select class="form-control" onchange="criteriaChanged('+criteriaId+')" id="searchCriteria'+ criteriaId +'">';
	temp += '<option disabled selected hidden value="select">* Select search criteria</option>';
	temp += '<option value="1">Requirement (Job) Id</option>';
	temp += '<option value="2">Position</option>';
	temp += '</select>';
	temp += '</div>';
	temp += '<div class="col-lg-6">';
	temp += '<input type="text" class="form-control" id="searchCriteriaValue'+ criteriaId +'" placeholder="* Value for the criteria you just selected">';
	temp += '<div id="position'+criteriaId+'">';
	
	temp += '</div>';
	temp += '</div>';
	temp += '</div>';
	document.getElementById("criteriaCount").value = criteriaId;
	criteriaId++;
	return temp;
}

function criteriaChanged(id) {
	if($("#searchCriteria" + id).val() == "2") {
		$("#searchCriteriaValue" + id).hide("slow");
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getPositions",
			data: {
				'criteriaId': id,
			},
			method: "POST",
			success: function(data) {
				$("#position" + id).html(data);
			},
			error: function(data) {
				alert("Something went wrong. Please try again.")
			}
		});
		$("#position" + id).show("slow");
	} else {
		$("#position" + id).hide("slow");
		$("#searchCriteriaValue" + id).show("slow");
	}
}

function addCriteria(btn) {
	btn.innerHTML = "Please wait...";
	$("#moreCriteria").append(createSearchCriterias());
	btn.innerHTML = "Add more criteria";
}

function searchRequirements(btn) {
	btn.innerHTML = "Please wait...";
	var criterias = [];
	var j = 0;
	var k = 0;
	//experience
	//0-value, 0-infinite, value-infinite, value-value, 0-0
	for(var i = 0 ; i < criteriaId ; i++) {
		 
		if($("#searchCriteria" + i).val() == "1") {
			if($("#searchCriteriaValue" + i).val() != "") {
				var temp = [$("#searchCriteria" + i).val(),$("#searchCriteriaValue" + i).val()];
				criterias.push(temp);
			}	
		} else {
			if($('[name="position'+i+'"] option:selected').val() != "select") {
				var temp = [$("#searchCriteria" + i).val(),$('[name="position'+i+'"] option:selected').val()];
				criterias.push(temp);
			}
		}
	}
	
	var filtering = [
    	['1', 'Show only pending requirements'],
    	['2', 'Show only accepted requirements'],
    	['3', 'Show only rejected requirements'],
    ];
	    $("#searchResults").jPagination({
	    	basePath: "<%=RouteManager.getBasePath()%>",
	    	filter: filtering,
	    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getTelecallerApprovedJobs",
	    	extraParameters: criterias
	    });
	
	btn.innerHTML = "Submit search query";
}


function showDetails(btn, id) {
	btn.innerHTML = "Please wait...";
	$("#applicationId").html(id);
	$.ajax({
		url: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=showPreFinalDetails",
		method: 'POST',
		data: {
			'jobId':id,
		},
		success: function(data) {
			$("#requirementDetails").html(data.table);
			var criterias = [[id]];
			$("#applicationDetails").jPagination({
		    	basePath: "<%=RouteManager.getBasePath()%>",
		    	extraParameters: criterias,
		    	limit: 9,
		    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getAcceptedApplications"
		    });
			$("#application_container").show();
			$('#interview-date').datepicker({
				format: 'yyyy-mm-dd',
			});
		},
		error: function(data) {
			alert("Something went wrong. Please try again.");
		}
	});
	btn.innerHTML = "Details";
}
function acceptJob(btn, jobId) {
	btn.innerHTML = "Please wait";
	$.ajax({
		url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=acceptJobFinal',
		method: 'POST',
		data: {
			'jobId': jobId,
		},
		success: function(data) {
			if(data == "success") {
				var bgColor = '<%=UCConstants.ACCEPTED_APPLICATION%>';
				$("#requirement_row" + jobId).css('background' , bgColor);
			} else if(data == "set") {
				alert("Please set the schedule first, before accepting the job.");
			} else {
				alert("Something went wrong. Please try again.");
			}
		},
		error: function(data) {
			alert("Something went wrong. Please try again.")
		}
	});
	btn.innerHTML = "Accept";
}

function rejectJob(btn) {
	
	btn.innerHTML = "Please wait";
	$.ajax({
		url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=rejectJobFinal',
		method: 'POST',
		data: {
			'jobId': document.getElementById("rejectJobId").value,
			"reason": document.getElementById("rejectJobReason").value,
		},
		success: function(data) {
			if(data == "success") {
				var bgColor = '<%=UCConstants.REJECTED_APPLICATION%>';
				$("#requirement_row" + document.getElementById("rejectJobId").value).css('background' , bgColor);
			} else {
				alert("Something went wrong. Please try again.");
			}
		},
		error: function(data) {
			alert("Something went wrong. Please try again.")
		}
	});
	btn.innerHTML = "Reject";	
	
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

function formatTime(time) {
    var result = false, m;
    var re = /^\s*([01]?\d|2[0-3]):?([0-5]\d)\s*$/;
    if ((m = time.match(re))) {
        result = (m[1].length === 2 ? "" : "0") + m[1] + ":" + m[2];
    }
    return result;
}

function validate(id) {
	if($("#interview-date").val() == '') {
		showError("interview-date", "Enter date of interview.");	
	} else if($("#interview-time").val() == '' || formatTime($("#interview-time").val()) == false) {
		showError("interview-time", "Enter time of interview in HH:MM format.");
	} else {
		if($("#interview-location").is(':checked')) {
			//success with same
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=saveInterviewSchedule',
				method: 'POST',
				data: {
					'jobId': id,
					'interviewDate': $("#interview-date").val(),
					'interviewTime': $("#interview-time").val(),
					'interviewContact': ($("#interview-contact-person").val() == '' ? "N/A" : $("#interview-contact-person").val()),
					'interviewLocation': "same",
				},
				beforSend: function() {
					$("#saveBtn").html("Please wait");
				},
				success: function(data) {
					if(data == "success") {
						$("#saveBtn").removeClass("btn-primary");
						$("#saveBtn").addClass("btn-success");
						$("#saveBtn").html("Saved");
					} else {
						$("#saveBtn").html("Save interview shedule");
						alert("Something went wrong. Please try again.");
					}
				},
				error: function(data) {
					$("#saveBtn").html("Save interview shedule");
					alert("Something went wrong. Please try again.");
				}
			});
		} else {
			if($("#interview-location_other").val() == '') {
				showError("interview-location", "Please check this checkbox or enter interview location below.");
			} else {
				//success with other
				$.ajax({
					url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=saveInterviewSchedule',
					method: 'POST',
					data: {
						'jobId': id,
						'interviewDate': $("#interview-date").val(),
						'interviewTime': $("#interview-time").val(),
						'interviewContact': ($("#interview-contact-person").val() == '' ? "N/A" : $("#interview-contact-person").val()),
						'interviewLocation': $("#interview-location_other").val(),
					},
					beforSend: function() {
						$("#saveBtn").html("Please wait");
					},
					success: function(data) {
						if(data == "success") {
							$("#saveBtn").removeClass("btn-primary");
							$("#saveBtn").addClass("btn-success");
							$("#saveBtn").html("Saved");
						} else {
							$("#saveBtn").html("Save interview shedule");
							alert("Something went wrong. Please try again.");
						}
					},
					error: function(data) {
						$("#saveBtn").html("Save interview shedule");
						alert("Something went wrong. Please try again.");
					}
				});
			}
		}
	}
}

$(document).on("click", "#rejectJobButton", function () {
	document.getElementById("rejectJobId").value = $(this).data("id"); 
});

</script>