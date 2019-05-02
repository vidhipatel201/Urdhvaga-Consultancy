<%@page import="utils.RouteManager" %>
<%@page import="controller.UCConstants" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Shortlist Candidates - Urdhvaga Consultancy"/>
</jsp:include>
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
		
		<div class="row" id="candidateSearchBox">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Search candidates
				</div>
  				<div class="panel-body">
  				<form id="searchForm">
  				<input type="hidden" id="criteriaCount_candidate" name="criteriaCount_candidate">
  				<div id="moreCriteria_candidate">
  					
  				</div>
  				<div class="row">
  					<div class="col-lg-6">
  						<div class="col-lg-6">
  							<button type="button" onclick="addCriteria_candidate(this)" class="btn btn-block btn-warning">Add more criteria</button>
  						</div>
  						<div class="col-lg-6">
  							<button type="button" onclick="searchRequirements_candidate(this)" class="btn btn-block btn-primary">Submit search query</button>
  						</div>
  					</div>
  				</div>
  				</form>
  				<div id="searchResults_candidate"></div>
  				</div>
			</div>
			</div>
		</div>

<div class="row" id="requirement_container">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Job Requirements</div>
  				<div class="panel-body">
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
		<div id="candidate_container" class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Candidate details <span id="candidateId"></span></div>
  				<div class="panel-body" id="candidateDetails_candidate">
  					
  				</div> 
			</div>
			</div>
		</div>
		
		<div id="shortlist_container" class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Shortlisted Candidates</div>
  				<div class="panel-body">
  					<table class="table table-bordered table-striped">
  					<thead>
  						<tr>
  							<th>Requirement Id
  							<th>Candidate Id
  							<th>Remove
  						</tr>
  						</thead>
  						<tbody id="shortlist">
  						
  						</tbody>
  					</table>
  					<br>
  					<button type="button" class="btn btn-primary" onclick="shortlist()">Submit list</button>
  				</div> 
			</div>
			</div>
		</div>
		
</div>
<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg7"/>
</jsp:include>
<script type="text/javascript" src="<%=RouteManager.getBasePath() %>admin/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>admin/css/bootstrap-multiselect.css" type="text/css"/>
<script src="<%=RouteManager.getBasePath()%>assets/js/pagination.js"></script>

	<script>
	var criteriaId = 0;
	var criteriaId_candidate = 0;
	$(document).ready(function() {
		$("#requirement_container").hide();
		$("#application_container").hide();
		$("#shortlist_container").hide();
		$("#candidate_container").hide();
		$("#moreCriteria").append(createSearchCriterias());
		$("#moreCriteria_candidate").append(createSearchCriterias_candidate());
		$("#candidateDetails_container").hide();
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
		$("#searchResults_candidate").html("");
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
		    $("#searchResults").jPagination({
		    	basePath: "<%=RouteManager.getBasePath()%>",
		    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=searchrequirements",
		    	extraParameters: criterias
		    });
		
		btn.innerHTML = "Submit search query";
	}
	
	function createSearchCriterias_candidate() {
		var temp = '<div class="row" style="border-bottom:1px solid #c0c0c0;padding-bottom:20px;margin-bottom:20px">';
		temp +=	'<div class="col-lg-6">';
		temp += '<select class="form-control" onchange="criteriaChanged_candidate('+criteriaId_candidate+')" id="searchCriteria_candidate'+ criteriaId_candidate +'">';
		temp += '<option disabled selected hidden value="select">* Select search criteria</option>';
		temp += '<option value="1">Candidate Id</option>';
		temp += '<option value="2">Candidate Name</option>';
		temp += '<option value="3">Candidate contact number</option>';
		temp += '<option value="4">Candidate email id</option>';
		temp += '<option value="5">Position</option>';
		temp += '<option value="6">Industry Sector</option>'; 
		temp += '<option value="7">Experience</option>';
		temp += '<option value="8">Location</option>';
		temp += '<option value="9">Qualification</option>';
		temp += '</select>';
		temp += '</div>';
		temp += '<div class="col-lg-6">';
		temp += '<input type="text" class="form-control" id="searchCriteriaValue_candidate'+ criteriaId_candidate +'" placeholder="* Value for the criteria you just selected">';
		temp += '<div id="experience_candidate'+criteriaId_candidate+'">';
		
		temp += '</div>';
		temp += '<div id="location_candidate'+criteriaId_candidate+'"><div class="col-lg-6" id="stateDiv'+criteriaId_candidate+'"></div><div id="districtDiv'+criteriaId_candidate+'"></div></div>';
		temp += '</div>';
		temp += '</div>';
		document.getElementById("criteriaCount_candidate").value = criteriaId_candidate;
		criteriaId_candidate++;
		return temp;
	}
	
	function criteriaChanged_candidate(id) {
		if($("#searchCriteria_candidate" + id).val() == "7") {
			$("#searchCriteriaValue_candidate" + id).hide("slow");
			$("#location_candidate" + id).hide("slow");
			
			var temp = "";
			temp += '<div class="col-lg-6"><input type="text" class="form-control" id="exp1CriteriaValue_candidate'+ id +'" placeholder="* Value for the criteria you just selected"></div>';
			temp += '<div class="col-lg-6"><input type="text" class="form-control" id="exp2CriteriaValue_candidate'+ id +'" placeholder="* Value for the criteria you just selected"></div>';
			$("#experience_candidate" + id).html(temp);
			$("#experience_candidate" + id).show("slow");
		} else if($("#searchCriteria_candidate" + id).val() == "8") {
			$("#searchCriteriaValue_candidate" + id).hide("slow");
			$("#experience_candidate" + id).hide("slow");
			
			$.ajax({
				url: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getStates",
				data: {
					'criteriaId': id,
				},
				method: "POST",
				success: function(data) {
					$("#stateDiv" + id).html(data);
				},
				error: function(data) {
					alert("Something went wrong. Please try again.")
				}
			});
			$("#location_candidate" + id).show("slow");
		} else {
			$("#experience_candidate" + id).hide("slow");
			$("#location_candidate" + id).hide("slow");
			
			$("#searchCriteriaValue_candidate" + id).show("slow");
		}
	}
	
	function stateChanged(drop, id) {
		if(drop.options[drop.selectedIndex].text.toLowerCase() == "other") {
			var districtTxt = "<div class=\"col-lg-6\"><input type=\"text\" class=\"form-control\" placeholder=\"Enter District\" id=\"otherDistrict"+id+"\"></div>";
			var talukaTxt = "<div class=\"col-lg-6\"><input type=\"text\" class=\"form-control\" placeholder=\"Enter Taluka\" id=\"otherTaluka"+id+"\"></div>";
			var cityTxt = "<div class=\"col-lg-6\"><input type=\"text\" class=\"form-control\" placeholder=\"Enter City\" id=\"otherCity"+id+"\"></div>";
			$("#districtDiv" + id).html(districtTxt+ talukaTxt + cityTxt);
		} else {
			var districtTxt = "<div class=\"col-lg-6\"><select class=\"form-control\" onchange=\"districtChanged(this,"+id+")\" id=\"districtSelect"+id+"\"></select></div>";
			var talukaTxt = "<div class=\"col-lg-6\"><select class=\"form-control\" onchange=\"talukaChanged(this,"+id+")\" id=\"talukaSelect"+id+"\"></select></div>";
			var cityTxt = "<div class=\"col-lg-6\"><select class=\"form-control\" id=\"citySelect"+id+"\"></select></div>";
			$("#districtDiv" + id).html(districtTxt + talukaTxt + cityTxt);
			$.ajax({
				url: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getDistricts",
				data: {
					'criteriaId': id,
					'state': drop.value
				},
				method: "POST",
				success: function(data) {
					$("#districtSelect" + id).html(data);
				},
				error: function(data) {
					alert("Something went wrong. Please try again.")
				}
			});	
		}
	}
	
	function districtChanged(drop, id) {
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getTalukas",
			data: {
				'criteriaId': id,
				'district': drop.value
			},
			method: "POST",
			success: function(data) {
				$("#talukaSelect" + id).html(data);
			},
			error: function(data) {
				alert("Something went wrong. Please try again.")
			}
		});	
	}
	
	function talukaChanged(drop, id) {
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getCities",
			data: {
				'criteriaId': id,
				'taluka': drop.value
			},
			method: "POST",
			success: function(data) {
				$("#citySelect" + id).html(data);
			},
			error: function(data) {
				alert("Something went wrong. Please try again.")
			}
		});	
	}
	
	function addCriteria_candidate(btn) {
		btn.innerHTML = "Please wait...";
		$("#moreCriteria_candidate").append(createSearchCriterias_candidate());
		btn.innerHTML = "Add more criteria";
	}
	
	function searchRequirements_candidate(btn) {
		if($('input[name=requirement_check]:checked').length > 0) {
		btn.innerHTML = "Please wait...";
		var criterias = [];
		var j = 0;
		var k = 0;
		//experience
		//0-value, 0-infinite, value-infinite, value-value, 0-0
		for(var i = 0 ; i < criteriaId_candidate ; i++) {
			if($("#searchCriteria_candidate" + i).val() == "7") {
				if($("#exp1CriteriaValue_candidate" + i).val() != "" || $("#exp2CriteriaValue_candidate" + i).val() != "") {
					var lValue = $("#exp1CriteriaValue_candidate" + i).val();
					var rValue = $("#exp2CriteriaValue_candidate" + i).val() == "" ? "*" : $("#exp2CriteriaValue_candidate" + i).val();
					var temp = [$("#searchCriteria_candidate" + i).val(), lValue + "-" + rValue];
					criterias.push(temp);
				}
			} else if($("#searchCriteria_candidate" + i).val() == "8"){
				var stateDrop = $("#state" + i);
				if($("#state" + i + " option:selected").text().toLowerCase() == "other") {
					var temp = [$("#searchCriteria_candidate" + i).val(), "*" + stateDrop.val() + "-" + ($("#otherDistrict" + i).val() == "" ? "0" : $("#otherDistrict" + i).val()) + "-" + ($("#otherTaluka" + i).val() == "" ? "0" : $("#otherTaluka" + i).val()) + "-" + ($("#otherCity" + i).val() == "" ? "0" : $("#otherCity" + i).val())];
					criterias.push(temp);
				} else {
					var temp = [$("#searchCriteria_candidate" + i).val(), stateDrop.val() + "-" + (($("#districtSelect" + i).val() == "select" || $("#districtSelect" + i).val() == null) ? "0" : $("#districtSelect" + i).val()) + "-" + (($("#talukaSelect" + i).val() == "select" || $("#talukaSelect" + i).val() == null) ? "0" : $("#talukaSelect" + i).val()) + "-" + (($("#citySelect" + i).val() == "select" || $("#citySelect" + i).val() == null) ? "0" : $("#citySelect" + i).val())];
					criterias.push(temp);
				}
				
			} else {
				if($("#searchCriteriaValue_candidate" + i).val() != "") {
					var temp = [$("#searchCriteria_candidate" + i).val(),$("#searchCriteriaValue_candidate" + i).val()];
					criterias.push(temp);
				}	
			}
		}
		
		var temp = ["reqId",$('input[name=requirement_check]:checked').val()];
		criterias.push(temp);
		
		  var ordering = [
		    	['1', 'Candidate Id (low to high)'],
		    	['2', 'Candidate Id (high to low)'],
		    	['3', 'Candidate Name (low to high)'],
		    	['4', 'Candidate Name (high to low)'],
		    ];
		    var filtering = [
		    	['1', 'Show only males'],
		    	['2', 'Show only females'],
		    	['3', 'Show only unplaced candidates'],
		    	['4', 'Show only placed candidates'],
		    	['5', 'Show only registered candidates'],
		    	['6', 'Show only non-registered candidates'],
		    ];
		    var limitNumber = [
		    	['100', '100'],
		    	['200', '200'],
		    	['300', '300'],
		    	['400', '400'],
		    	['500', '500'],
		    ];
		    
		    $("#searchResults_candidate").jPagination({
		    	basePath: "<%=RouteManager.getBasePath()%>",
		    	sorting: ordering,
		    	order: 2,
		    	filter: filtering,
		    	extraParameters: criterias,
		    	limiting: limitNumber,
		    	limit: 100,
		    	source: "<%=RouteManager.getBasePath()%>admin/manage-users?action=searchShortlistUser"
		    });
		
		btn.innerHTML = "Submit search query";
		} else {
			alert("Please select any requirement before searching for candidate.");
		}
	}
	
	///////
	
	function showCandidateDetails(id) {
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>admin/manage-users?action=showDetails",
			data: {
				"id":id,
			},
			method: "POST",
			success: function(data) {
				$("#candidateId").html("(ID - " + data.id + ")");
				$("#candidateDetails_candidate").html(data.details);
				var criterias = [[id]];
				$("#candidate_comments").jPagination({
			    	basePath: "<%=RouteManager.getBasePath()%>",
			    	extraParameters: criterias,
			    	limit: 5,
			    	source: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getCandidateComments"
			    });
				$("#candidate_container").show();
			},
			error: function(data) {
				$("#candidate_container").hide();
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
				$("#applicationDetails").jPagination({
			    	basePath: "<%=RouteManager.getBasePath()%>",
			    	extraParameters: criterias,
			    	source: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=getApplicationListForShortlist"
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
	function saveComment(btn,id) {
		btn.innerHTML = "Please wait";
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
		
		btn.innerHTML = "Submit";
	}
	
	function deleteComment(id, commentId, type) {
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
	}
	function saveCommentChanges(txt, id, type) {
		var comment = txt.value;
		if(comment != "") {
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
						saveComment(txt, id);
					}
				}
			}).fail(function(data) {
				if(confirm("Comment you just modified is not saved. Want to try again?")) {
					saveComment(txt, id);
				}
			});
		}
	}
		
	
	function addToShortlist(candidateId) {
		var reqId = $('input[name=requirement_check]:checked').val();
		if($("#shortlist_row" + reqId + "_" + candidateId).length <= 0) {
			var temp = '<tr id="shortlist_row'+reqId+'_'+candidateId+'">';
			temp += '<td class="reqId">' + reqId;
			temp += '<td class="candidateId">' + candidateId;
			temp += '<td><button type="button" onclick="removeShortlist('+reqId+','+candidateId+')" class="btn btn-danger">Remove</button>';
			temp += '</tr>';
			$("#shortlist").append(temp);
		}
		$("#shortlist_container").show('slow');
	}
	
	function removeShortlist(reqId, candidateId) {
		$("#shortlist_row" + reqId + "_" + candidateId).remove();
	}
	
	
	function shortlist() {
		var tempArray = [];
		$('#shortlist tr').each(function() {
		    var reqId = $(this).find(".reqId").html();
		    var candidateId = $(this).find(".candidateId").html();
		    var temp = [reqId, candidateId];
		    tempArray.push(temp);
		});
		if(tempArray.length > 0) {
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=saveShortlist',
				method: "POST",
				data: {
					"shortlist": tempArray
				},
				success: function(data) {
					if(data == "success") {
						alert("Shortlisted candidates successfully submitted");
						location.reload();
					} else {
						alert("Something went wrong. Please try again.");
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