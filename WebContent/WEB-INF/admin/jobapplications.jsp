<%@page import="utils.RouteManager" %>
<%@page import="controller.UCConstants" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Job Applications - Urdhvaga Consultancy"/>
</jsp:include>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">


<!-- Modal -->
<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Please provide reason to reject application</h4>
      </div>
      <div class="modal-body">
        <input type="text" class="form-control" id="rejectReason" placeholder="Reason to reject application">
        <input type="hidden" id="rejectId">
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn btn-danger" onclick="rejectApplication(this)" data-dismiss="modal">Reject</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>

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
  				<div id="jobRequirements"></div>
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
  						<div class="col-lg-12" style="border-bottom:1px solid #c0c0c0;padding-bottom:10px;margin-bottom:10px"><h3>Applications</h3></div>
  					</div>
  					<div class="row">
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
	<jsp:param name="pageId" value="pg2"/>
</jsp:include>
<script type="text/javascript" src="<%=RouteManager.getBasePath() %>admin/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>admin/css/bootstrap-multiselect.css" type="text/css"/>
<script src="<%=RouteManager.getBasePath()%>assets/js/pagination.js"></script>

	<script>
	var criteriaId = 0;
	$(document).ready(function() {
		var filtering = [
	    	['1', 'Show only pending requirements'],
	    	['2', 'Show only accepted requirements'],
	    	['3', 'Show only rejected requirements'],
	    ];
		$("#application_container").hide();
		$("#moreCriteria").append(createSearchCriterias());
		$("#candidateDetails_container").hide();
		 $("#jobRequirements").jPagination({
		    	basePath: "<%=RouteManager.getBasePath()%>",
		    	filter: filtering,
		    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getApplications"
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
		    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getApplications",
		    	extraParameters: criterias
		    });
		
		btn.innerHTML = "Submit search query";
	}
	
	function accept_application(btn, id) {
		
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=acceptApplication",
			data: {
				"id": id,
			},
			method: "POST",
			success: function(data) {
				if(data == "success") $("#appRow" + id).css({"background":"#ABEBC6","color":"black"});
				else alert("Something went wrong. Please try again.");
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			}
		});
		
	}
	function reject_application(btn, id) {
		
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=rejectApplication",
			data: {
				"id": id,
			},
			method: "POST",
			success: function(data) {
				if(data == "success") {
					$("#appRow" + id).css({"background":"#F1948A","color":"black"});
				}
				else alert("Something went wrong. Please try again.");
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			}
		});
		
	}
	function showDetails(btn, id) {
		btn.innerHTML = "Please wait...";
		$("#applicationId").html(id);
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=showDetails",
			method: 'POST',
			data: {
				'jobId':id,
			},
			success: function(data) {
				$("#requirementDetails").html(data.table);
				var criterias = [[id]];
				 var filtering = [
				    	['1', 'Show only pending applications'],
				    	['2', 'Show only accepted applications'],
				    	['3', 'Show only rejected applications'],
				    ];
				 var limitNumber = [
				    	['100', '100'],
				    	['200', '200'],
				    	['300', '300'],
				    	['400', '400'],
				    	['500', '500'],
				    ];
				$("#applicationDetails").jPagination({
			    	basePath: "<%=RouteManager.getBasePath()%>",
			    	extraParameters: criterias,
			    	filter: filtering,
			    	limiting: limitNumber,
			    	limit: 15,
			    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getApplicationList"
			    });
				$("#application_container").show();
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			}
		});
		btn.innerHTML = "Details";
	}
	function close_details() {
		$("#candidateDetails_container").hide();
		$("#application_container").hide();
	}
	function showCandidateDetails(btn, candidateId, applicationId) {
		btn.innerHTML = "Please wait";
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getCandidateDetails',
			method: 'POST',
			data: {
				'candidateId': candidateId,
				'applicationId': applicationId,
			},
			success: function(data) {
				$("#candidateDetails").html(data.table);
				var criterias = [[applicationId]];
				$("#application_comments").jPagination({
			    	basePath: "<%=RouteManager.getBasePath()%>",
			    	extraParameters: criterias,
			    	limit: 5,
			    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getApplicationComments"
			    });
				var criterias = [[candidateId]];
				$("#candidate_comments").jPagination({
			    	basePath: "<%=RouteManager.getBasePath()%>",
			    	extraParameters: criterias,
			    	limit: 5,
			    	source: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getCandidateComments"
			    });
				$("#candidateDetails_container").show();
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			}
		});
		btn.innerHTML = "Details";
	}
	
	function acceptApplication(btn, jobId) {
		btn.innerHTML = "Please wait";
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=acceptApplication',
			method: 'POST',
			data: {
				"jobId": jobId,
			},
			success: function(data) {
				if(data == "success") {
					var bgColor = '<%=UCConstants.ACCEPTED_APPLICATION%>';
					$("#application_row" + jobId).css('background' , bgColor);
				} else {
					alert("Something went wrong. Please try again.");
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			}
		});
		btn.innerHTML = "Accept";
	}
	function rejectApplication(btn) {
		btn.innerHTML = "Please wait";
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=rejectApplication',
			method: 'POST',
			data: {
				"jobId": document.getElementById("rejectId").value,
				"reason": document.getElementById("rejectReason").value,
			},
			success: function(data) {
				if(data == "success") {
					var bgColor = '<%=UCConstants.REJECTED_APPLICATION%>';
					$("#application_row" + document.getElementById("rejectId").value).css('background' , bgColor);
				} else {
					alert("Something went wrong. Please try again.");
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			}
		});
		btn.innerHTML = "Reject";
	}
	function saveComment(btn,id,type) {
		btn.innerHTML = "Please wait";
		if(type == <%=UCConstants.CANDIDATE_COMMENT%>) {
		var comment = $("#candidate_comment_entry").val();
		if(comment != "") {
				$.ajax({
					url: "<%=RouteManager.getBasePath() %>admin/manage-users?action=savecomment",
					data: {
						"comment": comment,
						"id": id,
					},
					method: "POST",
				}).done(function(data) {
					if(data == "error") {
						if(confirm("Comment you just modified is not saved. Want to try again?")) {
							saveComment(btn,id,type);
						}
					} else {
						$("#candidate_comment_entry").val("");
						var criterias = [[id]];
						$("#candidate_comments").jPagination({
					    	basePath: "<%=RouteManager.getBasePath()%>",
					    	extraParameters: criterias,
					    	limit: 5,
					    	source: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getCandidateComments"
					    });
					}
				}).fail(function(data) {
					if(confirm("Comment you just modified is not saved. Want to try again?")) {
						saveComment(btn,id,type);
					}
				});
			} 
		} else {
			var comment = $("#application_comment_entry").val();
			if(comment != "") {
				$.ajax({
					url: "<%=RouteManager.getBasePath() %>admin/manage-job-applications?action=savecomment",
					data: {
						"comment": comment,
						"id": id,
					},
					method: "POST",
				}).done(function(data) {
					if(data == "error") {
						if(confirm("Comment you just modified is not saved. Want to try again?")) {
							saveComment(btn,id,type);
						}
					} else {
						$("#application_comment_entry").val("");
						var criterias = [[id]];
						$("#application_comments").jPagination({
					    	basePath: "<%=RouteManager.getBasePath()%>",
					    	extraParameters: criterias,
					    	limit: 5,
					    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getApplicationComments"
					    });
					}
				}).fail(function(data) {
					if(confirm("Comment you just modified is not saved. Want to try again?")) {
						saveComment(btn,id,type);
					}
				});
			}
	}
		btn.innerHTML = "Submit";
	}
	function saveCommentChanges(txt, id, type) {
		var comment = txt.value;
		if(comment != "") {
			if(type == <%=UCConstants.CANDIDATE_COMMENT%>) {
				$.ajax({
					url: "<%=RouteManager.getBasePath() %>admin/manage-users?action=savecommentchanges",
					data: {
						"comment": comment,
						"id": id,
					},
					method: "POST",
				}).done(function(data) {
					if(data == "error") {
						if(confirm("Comment you just modified is not saved. Want to try again?")) {
							saveCommentChanges(txt, id, type);
						}
					}
				}).fail(function(data) {
					if(confirm("Comment you just modified is not saved. Want to try again?")) {
						saveCommentChanges(txt, id, type);
					}
				});	
			} else {
				$.ajax({
					url: "<%=RouteManager.getBasePath() %>admin/manage-job-applications?action=savecommentchanges",
					data: {
						"comment": comment,
						"id": id,
					},
					method: "POST",
				}).done(function(data) {
					if(data == "error") {
						if(confirm("Comment you just modified is not saved. Want to try again?")) {
							saveCommentChanges(txt, id, type);
						}
					}
				}).fail(function(data) {
					if(confirm("Comment you just modified is not saved. Want to try again?")) {
						saveCommentChanges(txt, id, type);
					}
				});
			}
		}
	}
	function deleteComment(id, commentId, type) {
		if(type == <%=UCConstants.CANDIDATE_COMMENT%>) {
			$.ajax({
				url: '<%=RouteManager.getBasePath() %>admin/manage-users?action=deleteComment',
				method: 'POST',
				data: {
					"commentId": commentId,
				},
				success: function(data) {
					if(data == "error") {
						alert("Something went wrong. Please try again.");
					} else {
						var criterias = [[id]];
						$("#candidate_comments").jPagination({
					    	basePath: "<%=RouteManager.getBasePath()%>",
					    	extraParameters: criterias,
					    	limit: 5,
					    	source: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getCandidateComments"
					    });
					}
				},
				error: function(data) {
					alert("Something went wrong. Please try again.");
				}
			});
		} else {
			$.ajax({
				url: '<%=RouteManager.getBasePath() %>admin/manage-job-applications?action=deleteComment',
				method: 'POST',
				data: {
					"commentId": commentId,
				},
				success: function(data) {
					if(data == "error") {
						alert("Something went wrong. Please try again.");
					} else {
						var criterias = [[id]];
						$("#application_comments").jPagination({
					    	basePath: "<%=RouteManager.getBasePath()%>",
					    	extraParameters: criterias,
					    	limit: 5,
					    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getApplicationComments"
					    });
					}
				},
				error: function(data) {
					alert("Something went wrong. Please try again.");
				}
			});
		}
	}
	
	function acceptJob(btn, jobId) {
		btn.innerHTML = "Please wait";
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=acceptJob',
			method: 'POST',
			data: {
				'jobId': jobId,
			},
			success: function(data) {
				if(data == "success") {
					var bgColor = '<%=UCConstants.ACCEPTED_APPLICATION%>';
					$("#requirement_row" + jobId).css('background' , bgColor);
				} else if(data == "set") {
					alert("Please accept any candidate first, before accepting the job.");
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
			url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=rejectJob',
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
	
	function saveExperienceYears(txt, expId, candidateId) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=saveExperienceYears',
			method: 'POST',
			data: {
				"years": txt.value,
				"expId": expId,
				"candidateId": candidateId
			},
			success: function(data) {
				if(data == "error") alert("Changes are not saved. Please reload page and try again."); 
			},
			error: function(data) {
				 alert("Changes are not saved. Please reload page and try again.");
			}
		});
	}
	
	function saveSalary(txt, expId, candidateId) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=saveSalary',
			method: 'POST',
			data: {
				"salary": txt.value,
				"expId": expId,
				"candidateId": candidateId,
			},
			success: function(data) {
				if(data == "error") alert("Changes are not saved. Please reload page and try again."); 
			},
			error: function(data) {
				 alert("Changes are not saved. Please reload page and try again.");
			}
		});
	}
	
	function saveExpectedSalary(txt, candidateId) {
		alert("called");
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=saveExpectedSalary',
			method: 'POST',
			data: {
				"salary": txt.value,
				"candidateId": candidateId,
			},
			success: function(data) {
				if(data == "error") alert("Changes are not saved. Please reload page and try again."); 
			},
			error: function(data) {
				 alert("Changes are not saved. Please reload page and try again.");
			}
		});
	}
	
	function deleteApplication(btn) {	
		if(confirm("Are you sure you want to remove these application/s? Once removed, data can't be retrived back.")) {
		var checkboxes = $('[name="application_check"]');
		  var checkboxesChecked = [];
		  for (var i=0; i<checkboxes.length; i++) {
		     if (checkboxes[i].checked) {
		        checkboxesChecked.push(checkboxes[i].value);
		     }
		  }
		  if(checkboxesChecked.length > 0) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=deleteApplications',
			method: 'POST',
			data: {
				'applicationIds' : JSON.stringify(checkboxesChecked),
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait";
			},
			success: function(data) {
				if(data == "success") {
					alert("Application/s successfully deleted.");
					location.reload();
				} else {
					alert("Something went wrong. Please try again.");
					location.reload();
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
				location.reload();
			},
			complete: function() {
				btn.innerHTML = "Delete";
			}
		});
		  }
		}
	}
	
	function saveCandidateQualification(txt, candiQualiId) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=saveCandidateQualification',
			method: 'POST',
			data: {
				"qualification": txt.value,
				"candiQualiId": candiQualiId
			},
			success: function(data) {
				if(data == "error") alert("Changes are not saved. Please reload page and try again."); 
			},
			error: function(data) {
				 alert("Changes are not saved. Please reload page and try again.");
			}
		});
	}
	
	$(document).on("click", "#rejectButton", function () {
		document.getElementById("rejectId").value = $(this).data("id"); 
	});
	$(document).on("click", "#rejectJobButton", function () {
		document.getElementById("rejectJobId").value = $(this).data("id"); 
	});
	</script>
</body>
</html>