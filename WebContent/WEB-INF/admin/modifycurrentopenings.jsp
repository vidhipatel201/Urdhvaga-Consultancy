<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<%@ page import="model.Uc_state" %>
<%@ page import="java.util.List" %>
<%@ page import="controller.UCConstants" %> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Modify Current Openings - Urdhvaga Consultancy"/>
</jsp:include>
<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>assets/css/typeahead.bundle.css" />

<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">			
	
	<div class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Download Data in Excel</div>
  				<div class="panel-body">
  					<form id="excelDownload" action="<%=RouteManager.getBasePath()%>admin/current-openings?action=downloadjobs" method="POST">
  					<div class="row">
  						<div class="col-lg-6 form-group">
  							<input type="text" class="form-control" id="excelStartRange" name="jobStartRange" placeholder="ID start range or seperated by comma, ex: 5,6">
  						</div>
  						<div class="col-lg-6 form-group">
  							<input type="text" class="form-control" id="excelEndRange" name="jobEndRange" placeholder="ID end range, ex: 10">
  						</div>
  					</div>
  					<div class="row">
  						<div class="col-lg-6">
  							<button type="button" onclick="downloadJobs(this)" class="btn btn-block btn-primary">Download</button>
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
				<div class="panel-heading">Search Current Opening
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
  							<button type="button" onclick="searchCurrJobs(this)" class="btn btn-block btn-primary">Submit search query</button>
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
				<div class="panel-heading">Modify Current Openings
				</div>
  				<div class="panel-body">
  				<div class="row">
  					<div class="col-lg-12">
  						<div class="col-lg-3" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.APPROVED_ACTIVE %>;color:#fff">
  							<span> Approved & Active</span>
  						</div>
  						<div class="col-lg-3" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.APPROVED_NOTACTIVE %>;color:#fff">
  							<span> Approved & Not-Active</span>
  						</div>
  						<div class="col-lg-3" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.NOTAPPROVED_NOTACTIVE %>;color:#fff">
  							<span> Rejected & Not-Active</span>
  						</div>
  						<div class="col-lg-3" style="border:1px solid #fff;border-radius:20px;background: <%=UCConstants.PENDING_NOTACTIVE %>;color:#fff">
  							<span> Pending & Not-Active</span>
  						</div>
  					</div>
  				</div>
  				<div id="currentOpenings"></div>
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
  	
  	<div id="current_jobs_container" class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Modify current opening <span id="jobId"></span></div>
  				<div class="panel-body" id="current_jobs_form">
  					
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
<script src="<%=RouteManager.getBasePath()%>admin/js/pagination.js"></script>
<script src="<%=RouteManager.getBasePath()%>assets/js/typeahead.bundle.js"></script>
	<script>
	var basePath = "<%=RouteManager.getBasePath()%>";
	var criteriaId = 0;
	var a_tag_count = 0;
	$(document).ready(function() {
		 $("#moreCriteria").append(createSearchCriterias());
		$("#current_jobs_container").hide();
		var filtering = [
	    	['1', 'Active Jobs'],
	    	['2', 'Not Active Jobs'],
	    ];
		 $("#currentOpenings").jPagination({
		    	basePath: "<%=RouteManager.getBasePath()%>",
		    	filter: filtering,
		    	source: "<%=RouteManager.getBasePath()%>admin/current-openings?action=getCurrentOpenings"
		    });
	});
	
	$( "#workprofile" ).on('input', function() {
	    if ($(this).val().length>=300) {
	        $("#message_msg").html("Workprofile can't be greater than 300 characters");
			$("#workprofile").css("border-color:red");
	    } else {
			$("#message_msg").html("");
			$("#workprofile").css("border-color:inherit");
		}
	});
	
	var q_tag_count = 0;
	function modifyCurrentJob(btn, id) {
		btn.innerHTML = "Please wait...";
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>admin/current-openings?action=modifySingle",
			method: "POST",
			data: {
				"id": id,
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait...";
			},
			success: function(data) {
				$("#jobId").html("(ID - " + data.currJobId + ")");
				$("#current_jobs_form").html(data.form);
				$("#current_jobs_container").show("slow");
				$('.gujarat-selected').hide();
				a_tag_count = parseInt($("#admin_count").val())+1;
				$('.other-selected').hide();
				q_tag_count = parseInt($("#qualification_count").val())+1;
				var suggestions_qualification = new Bloodhound({
			        datumTokenizer: Bloodhound.tokenizers.whitespace,
			        queryTokenizer: Bloodhound.tokenizers.whitespace,
			        prefetch: {
			        	url: basePath + 'GetSuggestion?autocomplete=' + <%=UCConstants.QUALIFICATION%>,
			        	cache:false,
			        	ttl:0
			        }
			 	});
				suggestions_qualification.initialize();
				 $('#qualification_required').typeahead(null, {
						name: "suggestions_qualification",
				        source: suggestions_qualification,
				        limit: 10 /* Specify maximum number of suggestions to be displayed */
				 });
				 $( "#industry-post" ).autocomplete({
			         source: "<%=RouteManager.getBasePath()%>GetSuggestion?tag=post"
			       });
				
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			},
			complete: function() {
				btn.innerHTML = "Modify";
			}
		});
		btn.innerHTML = "Modify";
	}
	function state_selected(state) {
		if(state.options[state.selectedIndex].text.toLowerCase() == "other") {
			$('.other-selected').show('slow');
			$('.gujarat-selected').hide('slow');
		} else {
			$.ajax({
				url: "<%=RouteManager.getBasePath()%>user-accounts?action=getDistrict",
				data: {
					"stateId": state.value,
				},
				method: "POST",
			}).done(function(data) {
				document.getElementById("district_options").innerHTML = data;
			}).fail(function(data) {
				alert("Something went wrong. Please try again.");
			});
			$('.other-selected').hide('slow');
			$('.gujarat-selected').show('slow');
		}
	}

	function district_selected(district) {
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>user-accounts?action=getTaluka",
			data: {
				"districtId": district.value,
			},
			method: "POST",
		}).done(function(data) {
			document.getElementById("taluka_options").innerHTML = data;
		}).fail(function(data) {
			alert("Something went wrong. Please try again.");
		});
	}
	
	function taluka_selected(taluka) {
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>user-accounts?action=getCity",
			data: {
				"talukaId": taluka.value,
			},
			method: "POST",
		}).done(function(data) {
			document.getElementById("city_options").innerHTML = data;
		}).fail(function(data) {
			alert("Something went wrong. Please try again.");
		});
	}

	function createSearchCriterias() {
		var temp = '<div class="row" style="border-bottom:1px solid #c0c0c0;padding-bottom:20px;margin-bottom:20px">';
		temp +=	'<div class="col-lg-6">';
		temp += '<select class="form-control" onchange="criteriaChanged('+criteriaId+')" id="searchCriteria'+ criteriaId +'">';
		temp += '<option disabled selected hidden value="select">* Select search criteria</option>';
		temp += '<option value="1">Job Id</option>';
		temp += '<option value="2">Position</option>';
		temp += '<option value="3">Employer Id</option>'; 
		temp += '<option value="4">Employer name</option>';
		temp += '<option value="5">Location</option>';
		temp += '</select>';
		temp += '</div>';
		temp += '<div class="col-lg-6">';
		temp += '<input type="text" class="form-control" id="searchCriteriaValue'+ criteriaId +'" placeholder="* Value for the criteria you just selected">';
		temp += '<div id="location'+criteriaId+'"><div class="col-lg-6" id="stateDiv'+criteriaId+'"></div><div id="districtDiv'+criteriaId+'"></div></div>';
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
		} else if($("#searchCriteria" + id).val() == "5") {
			$("#searchCriteriaValue" + id).hide("slow");
			$("#position" + id).hide("slow");
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
		}  else {
			$("#position" + id).hide("slow");
			$("#searchCriteriaValue" + id).show("slow");
		}
	}
	
	function addCriteria(btn) {
		btn.innerHTML = "Please wait...";
		$("#moreCriteria").append(createSearchCriterias());
		btn.innerHTML = "Add more criteria";
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
	
	function searchCurrJobs(btn) {
		btn.innerHTML = "Please wait...";
		var criterias = [];
		for(var i = 0 ; i < criteriaId ; i++) {
			if($("#searchCriteria" + i).val() == "2") {
				if($('[name="position'+i+'"] option:selected').val() != "select") {
					var temp = [$("#searchCriteria" + i).val(),$('[name="position'+i+'"] option:selected').val()];
					criterias.push(temp);
				}
			} else if($("#searchCriteria" + i).val() == "5") {
				var stateDrop = $("#state" + i);
				if($("#state" + i + " option:selected").text().toLowerCase() == "other") {
					
					var temp = [$("#searchCriteria" + i).val(), "*" + stateDrop.val() + "-" + ($("#otherDistrict" + i).val() == "" ? "0" : $("#otherDistrict" + i).val()) + "-" + ($("#otherTaluka" + i).val() == "" ? "0" : $("#otherTaluka" + i).val()) + "-" + ($("#otherCity" + i).val() == "" ? "0" : $("#otherCity" + i).val())];
					criterias.push(temp);
				} else if($("#state" + i + " option:selected").text().toLowerCase() == "gujarat") {
					var temp = [$("#searchCriteria" + i).val(), stateDrop.val() + "-" + (($("#districtSelect" + i).val() == "select" || $("#districtSelect" + i).val() == null) ? "0" : $("#districtSelect" + i).val()) + "-" + (($("#talukaSelect" + i).val() == "select" || $("#talukaSelect" + i).val() == null) ? "0" : $("#talukaSelect" + i).val()) + "-" + (($("#citySelect" + i).val() == "select" || $("#citySelect" + i).val() == null) ? "0" : $("#citySelect" + i).val())];
					criterias.push(temp);
				}
				
			} 
			else {
				if($("#searchCriteriaValue" + i).val() != "") {
					var temp = [$("#searchCriteria" + i).val(),$("#searchCriteriaValue" + i).val()];
					criterias.push(temp);
				}	
				
				}
		}
		
		// add filter here
		
		var filtering = [
	    	['1', 'Active Jobs'],
	    	['2', 'Not Active Jobs'],
	    ];
		
		    $("#searchResults").jPagination({
		    	basePath: "<%=RouteManager.getBasePath()%>",
		    	filter: filtering,
		    	extraParameters: criterias,
		    	source: "<%=RouteManager.getBasePath()%>admin/current-openings?action=getCurrentOpenings"
		    });
		
		btn.innerHTML = "Submit search query";
	}
	
	
	function add_qualification_tag() {
		var qualification = $("#qualification_required").val();
		var flag = true;
		if(qualification != ""){
		$("#qualification_required").val("");
		if(q_tag_count > 1) {
			for(var i = 1 ; i < q_tag_count ; i++) {
				if(qualification == $("#qualification_required_hidden" + i).val()) flag = false;
			}
		}
		if(flag) {
			var tag = '<div class="single-tag" style=\"display:inline-block;margin-left:10px\" id="q_tag_count'+q_tag_count+'"> &nbsp;<a onclick="remove_q_tag('+q_tag_count+')" style="cursor:pointer"><i class="fa fa-close" style="color: #790000"></i></a> &nbsp;<label id="qualification_required'+q_tag_count+'">'+qualification+'</label><input type="hidden" id="qualification_required_hidden'+q_tag_count+'" name="qualification_required'+q_tag_count+'" value="'+qualification+'"></div>';
			q_tag_count++;
			$("#qualification_count").val(q_tag_count);
			$("#qualification-tags").append(tag);
		}
		
		}
		flag = true;
	}
	function remove_q_tag(id) {
		$("#q_tag_count" + id).remove();	
	}
	
	function activateJobs(btn) {
		var checkboxes = $('[name="job_check"]');
		  var checkboxesChecked = [];
		  for (var i=0; i<checkboxes.length; i++) {
		     if (checkboxes[i].checked) {
		        checkboxesChecked.push(checkboxes[i].value);
		     }
		  }
		  if(checkboxesChecked.length > 0) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/current-openings?action=activate',
			method: 'POST',
			data: {
				'jobIds' : JSON.stringify(checkboxesChecked),
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait";
			},
			success: function(data) {
				if(data == "success") {
					var bgColor = "<%=UCConstants.APPROVED_ACTIVE%>";
					for (var i=0; i<checkboxesChecked.length; i++) {
						$("#job_row" + checkboxesChecked[i]).css('background' , bgColor);
					}
					$('[name="job_check"]').removeAttr('checked');
				} else {
					alert("Something went wrong. Please try again.");
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			},
			complete: function() {
				btn.innerHTML = "Activate";
			}
		});
		  }
	}
	
	var hexDigits = new Array
    ("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"); 

	//Function to convert rgb color to hex format
	function rgb2hex(rgb) {
		rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
		return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
	}

	function hex(x) {
		return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
	}
	
	function deactivateJobs(btn) {
		var checkboxes = $('[name="job_check"]');
		  var checkboxesChecked = [];
		  for (var i=0; i<checkboxes.length; i++) {
		     if (checkboxes[i].checked) {
		        checkboxesChecked.push(checkboxes[i].value);
		     }
		  }
		  if(checkboxesChecked.length > 0) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/current-openings?action=deactivate',
			method: 'POST',
			data: {
				'jobIds' : JSON.stringify(checkboxesChecked),
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait";
			},
			success: function(data) {
				if(data == "success") {
					var bgColor = "";
					for (var i=0; i<checkboxesChecked.length; i++) {
						var approved_active = "<%=UCConstants.APPROVED_ACTIVE%>";
						var oldBgColor = rgb2hex($("#job_row" + checkboxesChecked[i]).css('background-color'));
						if(oldBgColor == approved_active) bgColor = "<%=UCConstants.APPROVED_NOTACTIVE%>";
						else bgColor = oldBgColor;
						$("#job_row" + checkboxesChecked[i]).css('background' , bgColor);
					}
					$('[name="job_check"]').removeAttr('checked');
				} else {
					alert("Something went wrong. Please try again.");
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			},
			complete: function() {
				btn.innerHTML = "De-Activate";
			}
		});
		  }
	}
	
	function approveJobs(btn) {
		var checkboxes = $('[name="job_check"]');
		  var checkboxesChecked = [];
		  for (var i=0; i<checkboxes.length; i++) {
		     if (checkboxes[i].checked) {
		        checkboxesChecked.push(checkboxes[i].value);
		     }
		  }
		  if(checkboxesChecked.length > 0) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/current-openings?action=approve',
			method: 'POST',
			data: {
				'jobIds' : JSON.stringify(checkboxesChecked),
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait";
			},
			success: function(data) {
				if(data == "success") {
					var bgColor = "";
					for (var i=0; i<checkboxesChecked.length; i++) {
						var notapproved_notactive = "<%=UCConstants.NOTAPPROVED_NOTACTIVE%>";
						var pending_notactive = "<%=UCConstants.PENDING_NOTACTIVE%>";
						var oldBgColor = rgb2hex($("#job_row" + checkboxesChecked[i]).css('background-color'));
						if(oldBgColor == notapproved_notactive || oldBgColor == pending_notactive) bgColor = "<%=UCConstants.APPROVED_NOTACTIVE%>";
						else bgColor = oldBgColor;
						$("#job_row" + checkboxesChecked[i]).css('background' , bgColor);
					}
					$('[name="job_check"]').removeAttr('checked');
				} else {
					alert("Something went wrong. Please try again.");
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			},
			complete: function() {
				btn.innerHTML = "Approve";
			}
		});
		  }
	}
	
	function disapproveJobs(btn) {
		var checkboxes = $('[name="job_check"]');
		  var checkboxesChecked = [];
		  for (var i=0; i<checkboxes.length; i++) {
		     if (checkboxes[i].checked) {
		        checkboxesChecked.push(checkboxes[i].value);
		     }
		  }
		  if(checkboxesChecked.length > 0) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/current-openings?action=disapprove',
			method: 'POST',
			data: {
				'jobIds' : JSON.stringify(checkboxesChecked),
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait";
			},
			success: function(data) {
				if(data == "success") {
					for (var i=0; i<checkboxesChecked.length; i++) {
						var bgColor = "<%=UCConstants.NOTAPPROVED_NOTACTIVE%>";
						$("#job_row" + checkboxesChecked[i]).css('background' , bgColor);
					}
					$('[name="job_check"]').removeAttr('checked');
				} else {
					alert("Something went wrong. Please try again.");
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			},
			complete: function() {
				btn.innerHTML = "Dis-Approve";
			}
		});
		  }
	}
	
	function deleteJobs(btn) {	
		if(confirm("Are you sure you want to remove these current opening/s? Once removed, data can't be retrived back.")) {
		var checkboxes = $('[name="job_check"]');
		  var checkboxesChecked = [];
		  for (var i=0; i<checkboxes.length; i++) {
		     if (checkboxes[i].checked) {
		        checkboxesChecked.push(checkboxes[i].value);
		     }
		  }
		  if(checkboxesChecked.length > 0) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/current-openings?action=deleteJobs',
			method: 'POST',
			data: {
				'jobIds' : JSON.stringify(checkboxesChecked),
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait";
			},
			success: function(data) {
				if(data == "success") {
					alert("Current Opening/s successfully deleted.");
					location.reload();
				} else {
					alert("Cannot delete some/all selected current openings, because they might have active job applications. Please remove those applications first, and than remove these current openings.");
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

	function add_admin_permission(job) {
		var admin = $("#admin_list").val();
		var admin_name = $("#admin_list option[value='" + admin + "']").text()
		var flag = true;
		if(admin != null && admin != "select") {
			if(a_tag_count > 1) {
				for(var i = 1 ; i < a_tag_count ; i++) {
					if(admin == $("#permitted_admin_hidden" + i).val()) flag = false;
				}
			}
			if(flag) {
				$.ajax({
					url: '<%=RouteManager.getBasePath()%>admin/current-openings?action=addPermission',
					method: 'POST',
					data: {
						'adminId': admin,
						'jobId':job,
					},
					success: function(data) {
						if(data == 'success') {
							var tag = '<div class=\"single-tag\" style=\"display:inline-block;margin-left:10px\" id=\"+a_tag_count' + a_tag_count + '\"> &nbsp;<a onclick=\"remove_a_tag(' + a_tag_count + ', ' + job + ')\" style=\"cursor:pointer\"><i class=\"fa fa-close\" style=\"color: #790000\"></i></a> &nbsp;<label id=\"permitted_admin' + a_tag_count + '\">' + admin_name + '</label><input type=\"hidden\" id=\"permitted_admin_hidden' + a_tag_count + '\" name=\"permitted_admin' + a_tag_count + '\" value=\"' + admin + '\"></div>';
							a_tag_count++;
							$("#admin_count").val(a_tag_count);
							$("#admin-tags").append(tag);		
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
		flag = true;
	}
	
	function remove_a_tag(id, job_id) {
		var adminId = $("#permitted_admin_hidden" + id).val();
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/current-openings?action=removePermission',
			method: 'POST',
			data: {
				'adminId' : adminId,
				'jobId' : job_id,
			},
			success: function(data) {
				if(data == "success") {
					$("#a_tag_count" + id).remove();	
				} else {
					alert("Something went wrong. Please try again.");
				}
			},
			error: function(data) {
				alert("Something went wrong. Please try again.");
			}
		});
	}
	
	function downloadJobs(btn) {
		if(document.getElementById("excelStartRange").value == "") {
			alert("Please provide start range of id");
		} else {
			document.getElementById("excelDownload").submit();				
		}
	}
	
	</script>
	

</body>
</html>