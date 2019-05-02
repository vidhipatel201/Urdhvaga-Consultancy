<%@page import="java.util.List" %>
<%@page import="java.util.Hashtable" %>
<%@page import="java.util.ArrayList" %>
<%@page import="model.Uc_candidate_details" %>
<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<%@ page import="controller.UCConstants" %>

<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Registered Candidates - Urdhvaga Consultancy"/>
</jsp:include>
		<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>assets/css/typeahead.bundle.css" />
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">	
	
	<div class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Download Data in Excel</div>
  				<div class="panel-body">
  					<form id="excelDownload" action="<%=RouteManager.getBasePath()%>admin/manage-users?action=downloadcandidatedetails" method="POST">
  					<div class="row">
  						<div class="col-lg-6 form-group">
  							<input type="text" class="form-control" id="excelStartRange" name="candidateStartRange" placeholder="ID start range or seperated by comma, ex: 5,6">
  						</div>
  						<div class="col-lg-6 form-group">
  							<input type="text" class="form-control" id="excelEndRange" name="candidateEndRange" placeholder="ID end range, ex: 10">
  						</div>
  					</div>
  					<div class="row">
  						<div class="col-lg-6">
  							<button type="button" onclick="downloadCandidateDetails(this)" class="btn btn-block btn-primary">Download</button>
  						</div>
  					</div>
  					</form>
  				</div>
			</div>
			</div>
		</div>
			
		<div class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Search Users
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
  							<button type="button" onclick="searchCandidate(this)" class="btn btn-block btn-primary">Submit search query</button>
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
				<div class="panel-heading">Registered Users</div>
  				<div class="panel-body">
  					<div id="userTable"></div>
  				</div>
			</div>
			</div>
		</div>
		
		<div id="candidate_container" class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Candidate details <span id="candidateId"></span></div>
  				<div class="panel-body" id="candidateDetails">
  					
  				</div> 
			</div>
			</div>
		</div>
	</div>	<!--/.main-->

	<jsp:include page="footer.jsp" flush="true">
		<jsp:param name="pageId" value="pg4"/>
	</jsp:include>
	
<script type="text/javascript" src="<%=RouteManager.getBasePath() %>admin/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>admin/css/bootstrap-multiselect.css" type="text/css"/>
<script type="text/javascript" src="<%=RouteManager.getBasePath()%>assets/js/typeahead.bundle.js"></script>
	<script src="<%=RouteManager.getBasePath()%>admin/js/pagination.js"></script>
	<script>
	var criteriaId = 0;
	var q_tag_count = 1;
	$(document).ready(function() {
		$("#candidate_container").hide();
	   $("#moreCriteria").append(createSearchCriterias());
	   $("#experience0").hide();
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
	    	['7', 'Show only active candidates'],
	    	['8', 'Show only non-active candidates'],
	    ];
	    var limitNumber = [
	    	['100', '100'],
	    	['200', '200'],
	    	['300', '300'],
	    	['400', '400'],
	    	['500', '500'],
	    ];
	    $("#userTable").jPagination({
	    	basePath: "<%=RouteManager.getBasePath()%>",
	    	sorting: ordering,
	    	order: 2,
	    	filter: filtering,
	    	limiting: limitNumber,
	    	limit: 100,
	    	source: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getUserList"
	    });
	});
	
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
		
		function acceptJob(btn, id) {
			btn.innerHTML = "Please wait...";
			$.ajax({
				url: "<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=accept",
				data: {
					"id" : id,
				},
				method: "POST",
			}).done(function(data) {
				if(data == "success") {
					btn.disabled = true;
					btn.innerHTML = "Accepted";
				} else {
					btn.innerHTML = "Accept";
					alert("Something went wrong. Please try again");
				}
			}).fail(function(data) {
				btn.innerHTML = "Accept";
			});
		}
		
		function createSearchCriterias() {
			var temp = '<div class="row" style="border-bottom:1px solid #c0c0c0;padding-bottom:20px;margin-bottom:20px">';
			temp +=	'<div class="col-lg-6">';
			temp += '<select class="form-control" onchange="criteriaChanged('+criteriaId+')" id="searchCriteria'+ criteriaId +'">';
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
			temp += '<input type="text" class="form-control" id="searchCriteriaValue'+ criteriaId +'" placeholder="* Value for the criteria you just selected">';
			temp += '<div id="experience'+criteriaId+'">';
			temp += '</div>';
			temp += '<div id="searchPosition'+criteriaId+'"></div>';
			temp += '<div id="searchQualification'+criteriaId+'"></div>';
			temp += '<div id="location'+criteriaId+'"><div class="col-lg-6" id="stateDiv'+criteriaId+'"></div><div id="districtDiv'+criteriaId+'"></div></div>';
			temp += '</div>';
			temp += '</div>';
			document.getElementById("criteriaCount").value = criteriaId;
			criteriaId++;
			return temp;
		}
		
		function criteriaChanged(id) {
			if($("#searchCriteria" + id).val() == "7") {
				$("#searchCriteriaValue" + id).hide("slow");
				$("#searchPosition" + id).hide("slow");
				$("#location" + id).hide("slow");
				$("#searchQualification" + id).hide("slow");
				var temp = "";
				temp += '<div class="col-lg-6"><input type="text" class="form-control" id="exp1CriteriaValue'+ id +'" placeholder="* Value for the criteria you just selected"></div>';
				temp += '<div class="col-lg-6"><input type="text" class="form-control" id="exp2CriteriaValue'+ id +'" placeholder="* Value for the criteria you just selected"></div>';
				$("#experience" + id).html(temp);
				$("#experience" + id).show("slow");
			} else if($("#searchCriteria" + id).val() == "8") {
				$("#searchCriteriaValue" + id).hide("slow");
				$("#experience" + id).hide("slow");
				$("#searchPosition" + id).hide("slow");
				$("#searchQualification" + id).hide("slow");
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
				$("#location" + id).show("slow");
			} else if($("#searchCriteria" + id).val() == "5") {
				$("#searchCriteriaValue" + id).hide("slow");
				$("#experience" + id).hide("slow");
				$("#location" + id).hide("slow");
				$("#searchQualification" + id).hide("slow");
				$.ajax({
					url: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getPositions",
					data: {
						'criteriaId': id,
					},
					method: "POST",
					success: function(data) {
						$("#searchPosition" + id).html(data);
					},
					error: function(data) {
						alert("Something went wrong. Please try again.")
					}
				});
				$("#searchPosition" + id).show("slow");
			} else if($("#searchCriteria" + id).val() == "9") {
				$("#searchCriteriaValue" + id).hide("slow");
				$("#experience" + id).hide("slow");
				$("#location" + id).hide("slow");
				$("#searchPosition" + id).hide("slow");
				$.ajax({
					url: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getQualifications",
					data: {
						'criteriaId': id,
					},
					method: "POST",
					success: function(data) {
						$("#searchQualification" + id).html(data);
					},
					error: function(data) {
						alert("Something went wrong. Please try again.")
					}
				});
				$("#searchQualification" + id).show("slow");
			} else {
				$("#experience" + id).hide("slow");
				$("#location" + id).hide("slow");
				$("#searchPosition" + id).hide("slow");
				$("#searchQualification" + id).hide("slow");
				$("#searchCriteriaValue" + id).show("slow");
			}
		}
		
		function stateChanged(drop, id) {
			if(drop.options[drop.selectedIndex].text.toLowerCase() == "other") {
				var districtTxt = "<div class=\"col-lg-6\"><input type=\"text\" class=\"form-control\" placeholder=\"Enter District\" id=\"otherDistrict"+id+"\"></div>";
				var talukaTxt = "<div class=\"col-lg-6\"><input type=\"text\" class=\"form-control\" placeholder=\"Enter Taluka\" id=\"otherTaluka"+id+"\"></div>";
				var cityTxt = "<div class=\"col-lg-6\"><input type=\"text\" class=\"form-control\" placeholder=\"Enter City\" id=\"otherCity"+id+"\"></div>";
				$("#districtDiv" + id).html(districtTxt + talukaTxt + cityTxt);
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
		
		function addCriteria(btn) {
			btn.innerHTML = "Please wait...";
			$("#moreCriteria").append(createSearchCriterias());
			btn.innerHTML = "Add more criteria";
		}
		
		function searchCandidate(btn) {
			btn.innerHTML = "Please wait...";
			var criterias = [];
			var j = 0;
			var k = 0;
			//experience
			//0-value, 0-infinite, value-infinite, value-value, 0-0
			for(var i = 0 ; i < criteriaId ; i++) {
				if($("#searchCriteria" + i).val() == "7") {
					if($("#exp1CriteriaValue" + i).val() != "" || $("#exp2CriteriaValue" + i).val() != "") {
						var lValue = $("#exp1CriteriaValue" + i).val();
						var rValue = $("#exp2CriteriaValue" + i).val() == "" ? "*" : $("#exp2CriteriaValue" + i).val();
						var temp = [$("#searchCriteria" + i).val(), lValue + "-" + rValue];
						criterias.push(temp);
					}
				} else if($("#searchCriteria" + i).val() == "8"){
					var stateDrop = $("#state" + i);
					if($("#state" + i + " option:selected").text().toLowerCase() == "other") {
						var temp = [$("#searchCriteria" + i).val(), "*" + stateDrop.val() + "-" + ($("#otherDistrict" + i).val() == "" ? "0" : $("#otherDistrict" + i).val()) + "-" + ($("#otherTaluka" + i).val() == "" ? "0" : $("#otherTaluka" + i).val()) + "-" + ($("#otherCity" + i).val() == "" ? "0" : $("#otherCity" + i).val())];
						criterias.push(temp);
					} else {
						var temp = [$("#searchCriteria" + i).val(), stateDrop.val() + "-" + (($("#districtSelect" + i).val() == "select" || $("#districtSelect" + i).val() == null) ? "0" : $("#districtSelect" + i).val()) + "-" + (($("#talukaSelect" + i).val() == "select" || $("#talukaSelect" + i).val() == null) ? "0" : $("#talukaSelect" + i).val()) + "-" + (($("#citySelect" + i).val() == "select" || $("#citySelect" + i).val() == null) ? "0" : $("#citySelect" + i).val())];
						criterias.push(temp);
					}
					
				} else if($("#searchCriteria" + i).val() == "5") {
					var temp = [$("#searchCriteria" + i).val(),$("#positionValue" + i).val()];
					criterias.push(temp);
				} else if($("#searchCriteria" + i).val() == "9") {
					var temp = [$("#searchCriteria" + i).val(),$("#qualificationValue" + i).val()];
					criterias.push(temp);
				} else {
					if($("#searchCriteriaValue" + i).val() != "") {
						var temp = [$("#searchCriteria" + i).val(),$("#searchCriteriaValue" + i).val()];
						criterias.push(temp);
					}	
				}
			}
			
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
			    	['7', 'Show only active candidates'],
			    	['8', 'Show only non-active candidates'],
			    ];
			    var limitNumber = [
			    	['100', '100'],
			    	['200', '200'],
			    	['300', '300'],
			    	['400', '400'],
			    	['500', '500'],
			    ];
			    $("#searchResults").jPagination({
			    	basePath: "<%=RouteManager.getBasePath()%>",
			    	sorting: ordering,
			    	order: 2,
			    	filter: filtering,
			    	extraParameters: criterias,
			    	limiting: limitNumber,
			    	limit: 100,
			    	source: "<%=RouteManager.getBasePath()%>admin/manage-users?action=searchUser"
			    });
			
			btn.innerHTML = "Submit search query";
		}
		
		function showDetails(id) {
			$.ajax({
				url: "<%=RouteManager.getBasePath()%>admin/manage-users?action=showDetails",
				data: {
					"id":id,
				},
				method: "POST",
				success: function(data) {
					$("#candidateId").html("(ID - " + data.id + ")");
					$("#candidateDetails").html(data.details);
					var criterias = [[id]];
					$("#candidate_comments").jPagination({
				    	basePath: "<%=RouteManager.getBasePath()%>",
				    	extraParameters: criterias,
				    	limit: 5,
				    	source: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getCandidateComments"
				    });
					$("#candidate_container").show();
					q_tag_count = 1;
					var suggestions_qualification = new Bloodhound({
					    datumTokenizer: Bloodhound.tokenizers.whitespace,
					    queryTokenizer: Bloodhound.tokenizers.whitespace,
					    prefetch: {
					    	url: "<%=RouteManager.getBasePath()%>" + 'GetSuggestion?autocomplete=' + <%=UCConstants.QUALIFICATION%>,
					    	cache:false,
					    	ttl:0
					    }
						});
					suggestions_qualification.initialize();
					 $('#user_qualification').typeahead(null, {
							name: "suggestions_qualification",
					        source: suggestions_qualification,
					        limit: 10 /* Specify maximum number of suggestions to be displayed */
					 });
				},
				error: function(data) {
					$("#candidate_container").hide();
					alert("Something went wrong. Please try again.");
				}
			});
		}
		function saveExperienceYears(txt, id) {
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=saveExperienceYears',
				method: 'POST',
				data: {
					"years": txt.value,
					"id": id,
				},
				success: function(data) {
					if(data == "error") alert("Changes are not saved. Please reload page and try again."); 
				},
				error: function(data) {
					 alert("Changes are not saved. Please reload page and try again.");
				}
			});
		}
		function changePlaced(check, id) {
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>admin/manage-users?action=changePlaced',
				method: 'POST',
				data: {
					"placed": (check.checked ? "1" : "0"),
					"id": id,
				},
				success: function(data) {
					if(data == "error") alert("Changes are not saved. Please reload page and try again."); 
				},
				error: function(data) {
					 alert("Changes are not saved. Please reload page and try again.");
				}
			});
		}
		
		function changePayment(check, id) {
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>admin/manage-users?action=changePayment',
				method: 'POST',
				data: {
					"payment": (check.checked ? "1" : "0"),
					"id": id,
				},
				success: function(data) {
					if(data == "error") alert("Changes are not saved. Please reload page and try again."); 
				},
				error: function(data) {
					 alert("Changes are not saved. Please reload page and try again.");
				}
			});
		}
		
		function downloadCandidateDetails(btn) {
			if(document.getElementById("excelStartRange").value == "") {
				alert("Please provide start range of id");
			} else {
				document.getElementById("excelDownload").submit();				
			}
		}
		
		function removeUser(id) {
			if(confirm("Are you sure you want to remove this candidate? Once removed, its details can't be retrived again.")) {
				$.ajax({
					method: "POST",
					url: '<%=RouteManager.getBasePath()%>admin/manage-users?action=removeUser',
					data: {
						"candidateId": id,
					},
					success: function(data) {
						if(data == "success") {
							alert("Candidate successfully removed.");
						} else {
							alert("Something went wrong. Please try again.");
						}
						location.reload();
					},
					error: function(data) {
						alert("Something went wrong. Please try again.");
						location.reload();
					}
				});	
			}
		}
		
		function deactivateUser(id) {
			$.ajax({
				method: "POST",
				url: '<%=RouteManager.getBasePath()%>admin/manage-users?action=deactivateUser',
				data: {
					"candidateId": id,
				},
				success: function(data) {
					if(data == "success") {
						alert("Candidate successfully deactivated.");
					} else {
						alert("Something went wrong. Please try again.");
					}
					location.reload();
				},
				error: function(data) {
					alert("Something went wrong. Please try again.");
					location.reload();
				}
			});
		}
		
		function activateUser(id) {
			$.ajax({
				method: "POST",
				url: '<%=RouteManager.getBasePath()%>admin/manage-users?action=activateUser',
				data: {
					"candidateId": id,
				},
				success: function(data) {
					if(data == "success") {
						alert("Candidate successfully activated.");
					} else {
						alert("Something went wrong. Please try again.");
					}
					location.reload();
				},
				error: function(data) {
					alert("Something went wrong. Please try again.");
					location.reload();
				}
			});
		}
		
		function saveInterestIndustry(txt, id) {
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>admin/manage-users?action=saveInterestIndustry',
				method: 'POST',
				data: {
					'value': txt.value,
					'id': id,
				},
				success: function(data) {
					if(data == 'error') {
						alert("Something went wrong. Please try again after reloading this page.");
					}
				},
				error: function(data) {
					alert("Something went wrong. Please try again after reloading this page.");
				}
			});
		}
		
		function saveInterestPosition(txt, id) {
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>admin/manage-users?action=saveInterestPosition',
				method: 'POST',
				data: {
					'value': txt.value,
					'id': id,
				},
				success: function(data) {
					if(data == 'error') {
						alert("Something went wrong. Please try again after reloading this page.");
					}
				},
				error: function(data) {
					alert("Something went wrong. Please try again after reloading this page.");
				}
			});
		}
		
		function deleteInterest(id) {
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>admin/manage-users?action=deleteInterest',
				method: 'POST',
				data: {
					'id': id,
				},
				success: function(data) {
					if(data == 'error') {
						alert("Something went wrong. Please try again after reloading this page.");
					} else {
						$("#interestRow" + id).css('background', 'red');
					}
				},
				error: function(data) {
					alert("Something went wrong. Please try again after reloading this page.");
				}
			});
		}
		
		function changeGender(radio, id, gender) {
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>admin/manage-users?action=saveModifiedGender',
				method: 'POST',
				data: {
					'id': id,
					'gender': gender
				},
				success: function(data) {
					if(data == 'error') {
						alert("Something went wrong. Please try again after reloading this page.");
					}
				},
				error: function(data) {
					alert("Something went wrong. Please try again after reloading this page.");
				}
			});
		}
		
		function savePaymentDate(txt, id) {
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>admin/manage-users?action=savePaymentDate',
				method: 'POST',
				data: {
					'id': id,
					'date': txt.value
				},
				success: function(data) {
					if(data == 'error') {
						alert("Something went wrong. Please try again after reloading this page.");
					}
				},
				error: function(data) {
					alert("Something went wrong. Please try again after reloading this page.");
				}
			});
		}
		
		function add_qualification_tag() {
			var qualification = document.getElementById("user_qualification").value;
			var flag = true;
			if(qualification != ""){
			document.getElementById("user_qualification").value = "";
			if(q_tag_count > 1) {
				for(var i = 1 ; i < q_tag_count ; i++) {
					if(qualification == $("#user_qualification_hidden" + i).val()) flag = false;
				}
			}
			if(flag) {
				var tag = '<div class="single-tag" id="q_tag_count'+q_tag_count+'"> &nbsp;<a onclick="remove_q_tag('+q_tag_count+')" style="cursor:pointer"><i class="fa fa-close" style="color: #790000"></i></a> &nbsp;<label id="user_qualification'+q_tag_count+'">'+qualification+'</label><input type="hidden" id="user_qualification_hidden'+q_tag_count+'" name="user_qualification'+q_tag_count+'" value="'+qualification+'"></div>';
				q_tag_count++;
				document.getElementById("qualification_count").value = q_tag_count;
				$("#qualification-tags").append(tag);
			
			}
			}
			flag = true;
		}
		
		function remove_q_tag(id) {
			$("#q_tag_count" + id).remove();	
		}
		
		/*$("#userTable-sorting-select select").on('change', function() {
			  alert("called");
			});*/
			
			 
		
		/* var $rows = $('.table tr');
		$('.table-search').keyup(function() {
		    var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
		    
		    $rows.show().filter(function() {
		        var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
		        return !~text.indexOf(val);
		    }).hide();
		}); */
		
	</script>
</body>

</html>
