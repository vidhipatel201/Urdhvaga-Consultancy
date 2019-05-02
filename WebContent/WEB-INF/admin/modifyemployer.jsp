<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<%@page import="controller.UCConstants" %>

<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Modify Employer - Urdhvaga Consultancy"/>
</jsp:include>
		
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
	
	<div class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Search Employer
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
  							<button type="button" onclick="searchEmployer(this)" class="btn btn-block btn-primary">Submit search query</button>
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
				<div class="panel-heading">Registered Employers</div>
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
  					<div id="employerTable"></div>
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
		
		<div id="employer_container" class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Modify employer <span id="employerId"></span></div>
  				<div class="panel-body" id="employer_form">
  					
  				</div> 
			</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="footer.jsp" flush="true">
		<jsp:param name="pageId" value="pg3"/>
	</jsp:include>
	
	<script src="<%=RouteManager.getBasePath()%>admin/js/pagination.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script>
	var criteriaId = 0;
	var a_tag_count = 0;
	$(document).ready(function() {
		  $("#moreCriteria").append(createSearchCriterias());
		$("#employer_container").hide();
	    $("#employerTable").jPagination({
	    	basePath: "<%=RouteManager.getBasePath()%>",
	    	limit: 15,
	    	source: "<%=RouteManager.getBasePath()%>admin/employer?action=getEmployerList"
	    });
	    
	    $( "#industry-sector" ).autocomplete({
	         source: "<%=RouteManager.getBasePath()%>GetSuggestion?tag=industry"
	       });
	});
	
	function createSearchCriterias() {
		var temp = '<div class="row" style="border-bottom:1px solid #c0c0c0;padding-bottom:20px;margin-bottom:20px">';
		temp +=	'<div class="col-lg-6">';
		temp += '<select class="form-control" onchange="criteriaChanged('+criteriaId+')" id="searchCriteria'+ criteriaId +'">';
		temp += '<option disabled selected hidden value="select">* Select search criteria</option>';
		temp += '<option value="1">Employer Id</option>';
		temp += '<option value="2">Employer name</option>';
		temp += '<option value="3">Employer location</option>'; 
		temp += '<option value="4">Employer industry sector</option>';
		temp += '<option value="5">Employer type</option>';
		temp += '<option value="6">Contact person name</option>'; 
		temp += '<option value="7">Contact person phone number</option>';
		temp += '</select>';
		temp += '</div>';
		temp += '<div class="col-lg-6">';
		temp += '<input type="text" class="form-control" id="searchCriteriaValue'+ criteriaId +'" placeholder="* Value for the criteria you just selected">';
		temp += '<div id="location'+criteriaId+'"><div class="col-lg-6" id="stateDiv'+criteriaId+'"></div><div id="districtDiv'+criteriaId+'"></div></div>';
		temp += '<div id="employer_type_container'+criteriaId+'"></div>';
		temp += '</div>';
		temp += '</div>';
		document.getElementById("criteriaCount").value = criteriaId;
		criteriaId++;
		return temp;
	}
	
	function criteriaChanged(id) {
		if($("#searchCriteria" + id).val() == "5") {
			$("#searchCriteriaValue" + id).hide("slow");
			$("#location" + id).hide("slow");
			$.ajax({
				url: "<%=RouteManager.getBasePath()%>admin/employer?action=getType",
				data: {
					'criteriaId': id,
				},
				method: "POST",
				success: function(data) {
					$("#employer_type_container" + id).html(data);
				},
				error: function(data) {
					alert("Something went wrong. Please try again.")
				}
			});
			$("#employer_type_container" + id).show("slow");
		} else if($("#searchCriteria" + id).val() == "3") {
			$("#searchCriteriaValue" + id).hide("slow");
			$("#employer_type_container" + id).hide("slow");
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
		} else {
			$("#employer_type_container" + id).hide("slow");
			$("#location" + id).hide("slow");
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
	
	function searchEmployer(btn) {
		btn.innerHTML = "Please wait...";
		var criterias = [];
		var j = 0;
		var k = 0;
		//experience
		//0-value, 0-infinite, value-infinite, value-value, 0-0
		for(var i = 0 ; i < criteriaId ; i++) {
			if($("#searchCriteria" + i).val() == "5") {
					var temp = [$("#searchCriteria" + i).val(), $("#employer_type" + i).val()];
					criterias.push(temp);
				} else if($("#searchCriteria" + i).val() == "3") {
				var stateDrop = $("#state" + i);
				if($("#state" + i + " option:selected").text().toLowerCase() == "other") {
					
					var temp = [$("#searchCriteria" + i).val(), "*" + stateDrop.val() + "-" + ($("#otherDistrict" + i).val() == "" ? "0" : $("#otherDistrict" + i).val()) + "-" + ($("#otherTaluka" + i).val() == "" ? "0" : $("#otherTaluka" + i).val()) + "-" + ($("#otherCity" + i).val() == "" ? "0" : $("#otherCity" + i).val())];
					criterias.push(temp);
				} else if($("#state" + i + " option:selected").text().toLowerCase() == "gujarat") {
					var temp = [$("#searchCriteria" + i).val(), stateDrop.val() + "-" + (($("#districtSelect" + i).val() == "select" || $("#districtSelect" + i).val() == null) ? "0" : $("#districtSelect" + i).val()) + "-" + (($("#talukaSelect" + i).val() == "select" || $("#talukaSelect" + i).val() == null) ? "0" : $("#talukaSelect" + i).val()) + "-" + (($("#citySelect" + i).val() == "select" || $("#citySelect" + i).val() == null) ? "0" : $("#citySelect" + i).val())];
					criterias.push(temp);
				}
				
			} else {
				if($("#searchCriteriaValue" + i).val() != "") {
					var temp = [$("#searchCriteria" + i).val(),$("#searchCriteriaValue" + i).val()];
					criterias.push(temp);
				}	
			}
		}
		
		    $("#searchResults").jPagination({
		    	basePath: "<%=RouteManager.getBasePath()%>",
		    	extraParameters: criterias,
		    	source: "<%=RouteManager.getBasePath()%>admin/employer?action=getEmployerList"
		    });
		
		btn.innerHTML = "Submit search query";
	}
	
	function modifyEmployer(btn, id) {
		btn.innerHTML = "Please wait...";
		$.ajax({
			url: "<%=RouteManager.getBasePath()%>admin/employer?action=modifySingle",
			method: "POST",
			data: {
				"id": id,
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait...";
			},
			success: function(data) {
				$("#employerId").html("(ID - " + data.employerId + ")");
				$("#employer_form").html(data.form);
				a_tag_count = parseInt($("#admin_count").val())+1;
				$("#employer_container").show("slow");
				$('.gujarat-selected').hide();
				$('.other-selected').hide();
				 $( "#industry-sector" ).autocomplete({
			         source: "<%=RouteManager.getBasePath()%>GetSuggestion?tag=industry"
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
	
	/*************************************************/
	function saveFirmName(txt, id) {
		var firmName = txt.value;
		if(firmName != "") {
			$.ajax({
				url: "<%=RouteManager.getBasePath() %>admin/employer?action=changeFirmName",
				data: {
					"firmName": firmName,
					"id": id,
				},
				method: "POST",
			}).done(function(data) {
				if(data == "error") {
					if(confirm("Firm name you just modified is not saved. Want to try again?")) {
						saveFirmName(txt, id);
					}
				}
			}).fail(function(data) {
				if(confirm("Firm name you just modified is not saved. Want to try again?")) {
					saveFirmName(txt, id);
				}
			});
		}
	}
	function saveIndustrySector(txt, id) {
		var industrySector = txt.value;
		if(industrySector != "") {
			$.ajax({
				url: "<%=RouteManager.getBasePath() %>admin/employer?action=changeIndustrySector",
				data: {
					"industry-sector": industrySector,
					"id": id,
				},
				method: "POST",
			}).done(function(data) {
				if(data == "error") {
					if(confirm("Industry sector you just modified is not saved. Want to try again?")) {
						saveIndustrySector(txt, id);
					}
				}
			}).fail(function(data) {
				if(confirm("Industry sector you just modified is not saved. Want to try again?")) {
					saveIndustrySector(txt, id);
				}
			});
		}
		
	}
	
	function changeCity(div, id) {
		
	}
	/*******************************************************/
	
	function activateEmployers(btn) {
		var checkboxes = $('[name="employer_check"]');
		  var checkboxesChecked = [];
		  for (var i=0; i<checkboxes.length; i++) {
		     if (checkboxes[i].checked) {
		        checkboxesChecked.push(checkboxes[i].value);
		     }
		  }
		  if(checkboxesChecked.length > 0) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/employer?action=activate',
			method: 'POST',
			data: {
				'employerIds' : JSON.stringify(checkboxesChecked),
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait";
			},
			success: function(data) {
				if(data == "success") {
					var bgColor = "<%=UCConstants.APPROVED_ACTIVE%>";
					for (var i=0; i<checkboxesChecked.length; i++) {
						$("#employer_row" + checkboxesChecked[i]).css('background' , bgColor);
					}
					$('[name="employer_check"]').removeAttr('checked');
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
	
	function deactivateEmployers(btn) {
		var checkboxes = $('[name="employer_check"]');
		  var checkboxesChecked = [];
		  for (var i=0; i<checkboxes.length; i++) {
		     if (checkboxes[i].checked) {
		        checkboxesChecked.push(checkboxes[i].value);
		     }
		  }
		  if(checkboxesChecked.length > 0) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/employer?action=deactivate',
			method: 'POST',
			data: {
				'employerIds' : JSON.stringify(checkboxesChecked),
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait";
			},
			success: function(data) {
				if(data == "success") {
					var bgColor = "";
					for (var i=0; i<checkboxesChecked.length; i++) {
						var approved_active = "<%=UCConstants.APPROVED_ACTIVE%>";
						var oldBgColor = rgb2hex($("#employer_row" + checkboxesChecked[i]).css('background-color'));
						if(oldBgColor == approved_active) bgColor = "<%=UCConstants.APPROVED_NOTACTIVE%>";
						else bgColor = oldBgColor;
						$("#employer_row" + checkboxesChecked[i]).css('background' , bgColor);
					}
					$('[name="employer_check"]').removeAttr('checked');
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
	
	function approveEmployers(btn) {
		var checkboxes = $('[name="employer_check"]');
		  var checkboxesChecked = [];
		  for (var i=0; i<checkboxes.length; i++) {
		     if (checkboxes[i].checked) {
		        checkboxesChecked.push(checkboxes[i].value);
		     }
		  }
		  if(checkboxesChecked.length > 0) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/employer?action=approve',
			method: 'POST',
			data: {
				'employerIds' : JSON.stringify(checkboxesChecked),
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
						var oldBgColor = rgb2hex($("#employer_row" + checkboxesChecked[i]).css('background-color'));
						if(oldBgColor == notapproved_notactive || oldBgColor == pending_notactive) bgColor = "<%=UCConstants.APPROVED_NOTACTIVE%>";
						else bgColor = oldBgColor;
						$("#employer_row" + checkboxesChecked[i]).css('background' , bgColor);
					}
					$('[name="employer_check"]').removeAttr('checked');
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
	
	function disapproveEmployers(btn) {
		var checkboxes = $('[name="employer_check"]');
		  var checkboxesChecked = [];
		  for (var i=0; i<checkboxes.length; i++) {
		     if (checkboxes[i].checked) {
		        checkboxesChecked.push(checkboxes[i].value);
		     }
		  }
		  if(checkboxesChecked.length > 0) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/employer?action=disapprove',
			method: 'POST',
			data: {
				'employerIds' : JSON.stringify(checkboxesChecked),
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait";
			},
			success: function(data) {
				if(data == "success") {
					for (var i=0; i<checkboxesChecked.length; i++) {
						var bgColor = "<%=UCConstants.NOTAPPROVED_NOTACTIVE%>";
						$("#employer_row" + checkboxesChecked[i]).css('background' , bgColor);
					}
					$('[name="employer_check"]').removeAttr('checked');
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
	
	function deleteEmployers(btn) {
		//remaining	
		if(confirm("Are you sure you want to remove these employer/s? Once removed, data can't be retrived back.")) {
		var checkboxes = $('[name="employer_check"]');
		  var checkboxesChecked = [];
		  for (var i=0; i<checkboxes.length; i++) {
		     if (checkboxes[i].checked) {
		        checkboxesChecked.push(checkboxes[i].value);
		     }
		  }
		  if(checkboxesChecked.length > 0) {
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/employer?action=deleteEmployers',
			method: 'POST',
			data: {
				'employerIds' : JSON.stringify(checkboxesChecked),
			},
			beforeSend: function() {
				btn.innerHTML = "Please wait";
			},
			success: function(data) {
				if(data == "success") {
					alert("Employer/s successfully deleted.");
					location.reload();
				} else {
					alert("Cannot delete some/all selected employers, because they might have active current openings. Please remove those current openings first, and than remove these employers.");
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
	
	function add_admin_permission(employer) {
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
					url: '<%=RouteManager.getBasePath()%>admin/employer?action=addPermission',
					method: 'POST',
					data: {
						'adminId': admin,
						'employerId':employer,
					},
					success: function(data) {
						if(data == 'success') {
							var tag = '<div class=\"single-tag\" style=\"display:inline-block;margin-left:10px\" id=\"+a_tag_count' + a_tag_count + '\"> &nbsp;<a onclick=\"remove_a_tag(' + a_tag_count + ', ' + employer + ')\" style=\"cursor:pointer\"><i class=\"fa fa-close\" style=\"color: #790000\"></i></a> &nbsp;<label id=\"permitted_admin' + a_tag_count + '\">' + admin_name + '</label><input type=\"hidden\" id=\"permitted_admin_hidden' + a_tag_count + '\" name=\"permitted_admin' + a_tag_count + '\" value=\"' + admin + '\"></div>';
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
	
	function remove_a_tag(id, employer_id) {
		var adminId = $("#permitted_admin_hidden" + id).val();
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>admin/employer?action=removePermission',
			method: 'POST',
			data: {
				'adminId' : adminId,
				'employerId' : employer_id,
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
	</script>
</body>
</html>