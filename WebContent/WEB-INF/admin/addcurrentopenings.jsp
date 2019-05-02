<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<%@ page import="model.Uc_state" %>
<%@ page import="model.Uc_job_facilities" %>
<%@ page import="model.Uc_employer_details" %>
<%@ page import="java.util.List" %>
<%@ page import="controller.UCConstants" %>

<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Add Current Openings - Urdhvaga Consultancy"/>
</jsp:include>
<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>assets/css/typeahead.bundle.css" />
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">			
	
	<div class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Add Current Openings</div>
  				<div class="panel-body">
  				<% if((Boolean)request.getAttribute("firmsAvail")) { %>
  					<form id="current_jobs_form" action="<%=RouteManager.getBasePath() %>admin/current-openings?action=save" method="POST">
  						<div class="form-group col-lg-12">
  							<div class="col-lg-6">
  							<select class="form-control" name="employer" id="employer" onchange="disableError('employer');">
  								<option selected hidden value="select">* Select Employer</option>
									<% for(Uc_employer_details employer : (List<Uc_employer_details>) request.getAttribute("employers")) { %>
										<option value="<%=employer.getEd_id()%>"><%=employer.getEd_firm_name() %></option>
									<% } %>
  							</select>
  							<label style="color:red;font-weight:normal;display:none" id="employer_error"></label>
  							</div>
  							<div class="col-lg-6">
  								<input type="number" class="form-control" placeholder="Employer Id" id="employer_id" name="employer_id">
  								<label style="color:red;font-weight:normal;display:none" id="employer_id_error"></label>
  							</div>
  						</div>
  						<div class="form-group col-lg-12">
  							
  							<div class="col-lg-6">
  								<input type="text" class="form-control" autocomplete="off" spellcheck="false" onkeyup="disableError('industry-post')" name="industry-post" id="industry-post" placeholder="* Post or position">
  								<label style="color:red;font-weight:normal;display:none" id="industry-post_error"></label>
  							</div>
  							<div class="col-lg-3">
  								<input type="number" class="form-control" onkeyup="disableError('experience_start')" id="experience_start" name="experience_start" placeholder="* Experience range start point">
  								<label style="color:red;font-weight:normal;display:none" id="experience_start_error"></label>
  							</div>
  							<div class="col-lg-3">
  								<input type="number" class="form-control" id="experience_end" name="experience_end" placeholder="* Experience range end point">
  							</div>
  						</div>
  						<div class="form-group col-lg-12">
  							<div class="col-lg-6">
  								<input type="text" class="form-control" name="duty-hours" placeholder="Duty hours">
  							</div>
  							<div class="col-lg-6">
  								<input type="text" class="form-control" name="salary" placeholder="Expected salary">
  							</div>
  						</div>
  						
  						<div class="form-group col-lg-12">
  							<div class="col-lg-6">
  								<div class="col-lg-10">
  									<input type="text" class="form-control" autocomplete="off" spellcheck="false" id="qualification_required" name="qualification_required" placeholder="Qualification Required">
  									<label style="color:red;font-weight:normal;display:none" id="qualification_required_error"></label>
  								</div>
  								<div class="col-lg-2">
  									<button type="button" onclick="add_qualification_tag()" class="btn btn-primary"><i class="fa fa-plus"></i></button>
  								</div>
  							</div>
  							<div class="col-lg-6" id="qualification-tags">
  								 
  							</div>
  							<input type="hidden" name="qualification_count" id="qualification_count">
  						</div>
  						
  						<div class="form-group col-lg-12">
  							<div class="col-lg-6">
  								<input type="number" class="form-control" name="male_quantity" id="male_quantity" placeholder="Number of males required">
  								
  							</div>
  							<div class="col-lg-6">
  								<input type="number" class="form-control" name="female_quantity" id="female_quantity" placeholder="Number of females required">
  							</div>
  						</div>
  						<div class="form-group col-lg-12">
  							<div class="col-lg-6">
  								<label><input type="checkbox" name="no_gender" id="no_gender" value="0"> Check this if no gender information available</label>
  							</div>
  							<div class="col-lg-6">
  								<input type="number" class="form-control" id="total_quantity" name="total_quantity" placeholder="Total number of persons required">
  							</div>
  						</div>
  						<div class="col-lg-12 form-group">
  							<div class="col-lg-6">
								<select class="form-control" name="user_state" id="user_state" onchange="state_selected(this); disableError('user_state');">
									<option disabled selected hidden value="select">* Select state (job location)</option>
									<% for(Uc_state state : (List<Uc_state>) request.getAttribute("states")) { %>
										<option value="<%=state.getS_id()%>"><%=state.getS_name() %></option>
									<% } %>
								</select>
								<label style="color:red;font-weight:normal;display:none" id="user_state_error"></label>
								</div>
							</div>
							<div class="gujarat-selected form-group col-lg-12">
								<div class="col-lg-6">
									<select class="form-control" id="district_options" name="user_district" onchange="district_selected(this); disableError('district_options');">
										
									</select>
									<label style="color:red;font-weight:normal;display:none" id="district_options_error"></label>
								</div>
								<div class="col-lg-6">
									<select class="form-control" id="taluka_options" name="user_taluka" onchange="taluka_selected(this); disableError('taluka_options');">
										
									</select>
									<label style="color:red;font-weight:normal;display:none" id="taluka_options_error"></label>
								</div>
								<div class="col-lg-6">
									<select class="form-control" id="city_options" onchange="disableError('city_options')" name="user_city">
								
									</select>
									<label style="color:red;font-weight:normal;display:none" id="city_options_error"></label>
								</div>
							</div>
							<div class="other-selected form-group col-lg-12">
								<div class="col-lg-6">
									<input type="text" placeholder="* District" id="user_district_other" onkeyup="disableError('user_district_other')" name="user_district_other" class="form-control">
									<label style="color:red;font-weight:normal;display:none" id="user_district_other_error"></label>
								</div>
								<div class="col-lg-6">
									<input type="text" placeholder="* Taluka" id="user_taluka_other" onkeyup="disableError('user_taluka_other')" name="user_taluka_other" class="form-control">
									<label style="color:red;font-weight:normal;display:none" id="user_taluka_other_error"></label>
								</div>
								<div class="col-lg-6">
									<input type="text" placeholder="* City / Village" id="user_city_other" onkeyup="disableError('user_city_other')" name="user_city_other" class="form-control">
									<label style="color:red;font-weight:normal;display:none" id="user_city_other_error"></label>
								</div>
							</div>
							<div class="form-group col-lg-12">
								<div class="col-lg-12">
									<label>Job Facilities: </label>
									<% for(Uc_job_facilities facilities : (List<Uc_job_facilities>) request.getAttribute("jobFacilities")) { %>
										<label><input type="checkbox" name="facilities" value="<%=facilities.getJf_id()%>"> <%=facilities.getJf_name()%></label>
									<% } %>
								</div>
							</div>
							<div class="form-group col-lg-12">
								<div class="col-lg-12">
									<span id="message_msg" style="color:red"></span>
									<textarea id="workprofile" onkeyup="disableError('workprofile')" name="workprofile" class="form-control" rows="3" maxlength="300" placeholder="* Work-profile"></textarea>
									<label style="color:red;font-weight:normal;display:none" id="workprofile_error"></label>
								</div>
							</div>
							<% if((Boolean)request.getAttribute("featured")) { %>
							<div class="form-group col-lg-12">
								<div class="col-lg-12">
									<label><input name="featured" value="1" type="checkbox"> Show this job as featured job on home page</label>
								</div>
							</div>
							<% } %>
							<input type="hidden" name="featured_avail" value="<%=request.getAttribute("featured")%>">
							<div class="form-group col-lg-12">
								<div class="col-lg-3">
									<button type="button" onclick="validate(this)" class="btn btn-primary">Save this job</button>
								</div>
							</div>
  					</form>
  					<% } else { %>
  						No firms (employers) are registered yet. Firstly <a href="<%=RouteManager.getBasePath()%>admin/employer?action=add">register employers</a> and than add current openings.
  					<% } %>
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

</div>

<jsp:include page="footer.jsp" flush="true">
		<jsp:param name="pageId" value="pg2"/>
</jsp:include>

<script src="<%=RouteManager.getBasePath()%>assets/js/typeahead.bundle.js"></script>
<script type="text/javascript">
var basePath = "<%=RouteManager.getBasePath()%>";
$(document).ready(function() { 
	$('.gujarat-selected').hide();
	$('.other-selected').hide();
	
      
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
		
			 
			 var suggestions_position = new Bloodhound({
			        datumTokenizer: Bloodhound.tokenizers.whitespace,
			        queryTokenizer: Bloodhound.tokenizers.whitespace,
			        prefetch: {
			        	url: basePath + 'GetSuggestion?autocomplete=' + <%=UCConstants.POSITION%>,
			        	cache:false,
			        	ttl:0
			        }
			 	});
				suggestions_position.initialize();
				 $('#industry-post').typeahead(null, {
						name: "suggestions_position",
				        source: suggestions_position,
				        limit: 10 /* Specify maximum number of suggestions to be displayed */
				 });
			 
		 
		 
});

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

$( "#workprofile" ).on('input', function() {
    if ($(this).val().length>=300) {
        document.getElementById("message_msg").innerHTML = "Workprofile can't be greater than 300 characters";
		document.getElementById("workprofile").style.borderColor = "red";
    } else {
		document.getElementById("message_msg").innerHTML = "";
		document.getElementById("workprofile").style.borderColor = "";
	}
});

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

function validate(btn) {
	btn.innerHTML = "Please wait...";
	if(document.getElementById("employer").value == "select") {
		if($("#employer_id").val() == "") {
			showError("employer", "Select employer");
			showError("employer_id", "Enter employer Id");
		} else {
			if($("#industry-sector").val() == "") {
				showError("industry-sector", "Industry sector can't be empty");
			} else if($("#industry-post").val() == "") {
				showError("industry-post", "Position can't be empty");
			} else if($("#experience_start").val() == "") {
				showError("experience_start", "Experience can't be empty");
			} else if($('#qualification-tags').has('.single-tag').length == 0) {
				showError("qualification_required", "Enter atleast one qualification (if not specific than enter Any)");
			} else if(document.getElementById("user_state").value == "select") {
				showError("user_state", "Select any state");
			} else {
				if(document.getElementById("user_state").options[document.getElementById("user_state").selectedIndex].text.toLowerCase() == "other") {
					if(document.getElementById("user_district_other").value == "") {
						showError("user_district_other", "Enter valid district name.");
					} else if(document.getElementById("user_city_other").value == "") {
						showError("user_city_other", "Enter valid city or village name.");
					} else {
						if($("#workprofile").val() == "") {
							showError("workprofile", "Enter some detail about the job");
						} else {
							//success
							if ($("#experience_end").val() == "") {
								$("#experience_end").val($("#experience_start").val());
							}
							document.getElementById("current_jobs_form").submit();
						}
					}
				} else {
					if(document.getElementById("district_options").value == "select") {
						showError("district_options", "Select district.");
					} else if(document.getElementById("city_options").value == "select") {
						showError("city_options", "Select city or village.");
					} else {
						if($("#workprofile").val() == "") {
							showError("workprofile", "Enter some detail about the job");
						} else {
							if($("#experience_end").val() == "") $("#experience_end").val($("#experience_start").val());
							if($("#experience_start").val() > $("#experience_end").val()) {
								//error	
								showError("experience_start", "Starting experience point can't be greater than ending point");
							} else {
								//success
								if ($("#experience_end").val() == "") {
									$("#experience_end").val($("#experience_start").val());
								}
								document.getElementById("current_jobs_form").submit();	
							}
						}
					}
				}
			}
		}
	} else if($("#industry-sector").val() == "") {
		showError("industry-sector", "Industry sector can't be empty");
	} else if($("#industry-post").val() == "") {
		showError("industry-post", "Position can't be empty");
	} else if($("#experience_start").val() == "") {
		showError("experience_start", "Experience can't be empty");
	} else if($('#qualification-tags').has('.single-tag').length == 0) {
		showError("qualification_required", "Enter atleast one qualification (if not specific than enter Any)");
	} else if(document.getElementById("user_state").value == "select") {
		showError("user_state", "Select any state");
	} else {
		if(document.getElementById("user_state").options[document.getElementById("user_state").selectedIndex].text.toLowerCase() == "other") {
			if(document.getElementById("user_district_other").value == "") {
				showError("user_district_other", "Enter valid district name.");
			} else if(document.getElementById("user_city_other").value == "") {
				showError("user_city_other", "Enter valid city or village name.");
			} else {
				if($("#workprofile").val() == "") {
					showError("workprofile", "Enter some detail about the job");
				} else {
					//success
					if ($("#experience_end").val() == "") {
						$("#experience_end").val($("#experience_start").val());
					}
					document.getElementById("current_jobs_form").submit();
				}
			}
		} else {
			if(document.getElementById("district_options").value == "select") {
				showError("district_options", "Select district.");
			} else if(document.getElementById("city_options").value == "select") {
				showError("city_options", "Select city or village.");
			} else {
				if($("#workprofile").val() == "") {
					showError("workprofile", "Enter some detail about the job");
				} else {
					//success
					if ($("#experience_end").val() == "") {
						$("#experience_end").val($("#experience_start").val());
					}
					document.getElementById("current_jobs_form").submit();
				}
			}
		}
	}
	btn.innerHTML = "Save this job";
}

var q_tag_count = 1;
function add_qualification_tag() {
	disableError("qualification_required");
	var qualification = document.getElementById("qualification_required").value;
	var flag = true;
	if(qualification != ""){
	document.getElementById("qualification_required").value = "";
	if(q_tag_count > 1) {
		for(var i = 1 ; i < q_tag_count ; i++) {
			if(qualification == $("#qualification_required_hidden" + i).val()) flag = false;
		}
	}
	if(flag) {
		var tag = '<div class="single-tag" style=\"display:inline-block;margin-left:10px\" id="q_tag_count'+q_tag_count+'"> &nbsp;<a onclick="remove_q_tag('+q_tag_count+')" style="cursor:pointer"><i class="fa fa-close" style="color: #790000"></i></a> &nbsp;<label id="qualification_required'+q_tag_count+'">'+qualification+'</label><input type="hidden" id="qualification_required_hidden'+q_tag_count+'" name="qualification_required'+q_tag_count+'" value="'+qualification+'"></div>';
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

</script>

</body>
</html>