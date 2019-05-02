<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<%@ page import="model.Uc_state" %>
<%@page import="model.Uc_employer_type" %>
<%@ page import="java.util.List" %>
<%@ page import="controller.UCConstants" %>

<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Add Employer - Urdhvaga Consultancy"/>
</jsp:include>
		
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
	
		
		<div class="row">
			<div class="col-lg-12">
			<div class="panel panel-info home_boxes">
				<div class="panel-heading">Add Employer</div>
  				<div class="panel-body">
  					<form id="employer_form" action="<%=RouteManager.getBasePath() %>admin/employer?action=save" method="POST">
  						<div class="form-group col-lg-12">
  							<div class="col-lg-12">
  								<input type="text" class="form-control" onkeyup="disableError('firm_name')" name="firm_name" id="firm_name" placeholder="* Firm Name">
  								<label style="color:red;font-weight:normal;display:none" id="firm_name_error"></label>
  							</div>
  						</div>
  						<div class="form-group col-lg-12">
  							<div class="col-lg-12">
  								<select name="employer_type" onchange="disableError('employer_type')" class="form-control" id="employer_type">
  									<option selected disabled hidden value="select">* Select firm type</option>
  									<% for(Uc_employer_type type : (List<Uc_employer_type>) request.getAttribute("types")) { %>
  										<option value="<%=type.getEt_id()%>"><%=type.getEt_name() %></option>
  									<% } %>
  								</select>
  								<label style="color:red;font-weight:normal;display:none" id="employer_type_error"></label>
  							</div>
  						</div>
  						<div class="form-group col-lg-12">
  						<div class="col-lg-12">
  							<input type="text" class="form-control" onkeyup="disableError('industry-sector')" name="industry-sector" id="industry-sector" placeholder="* Industry sector">
  							<label style="color:red;font-weight:normal;display:none" id="industry-sector_error"></label>
  						</div>
  						</div>
  						
  						<div class="col-lg-12 form-group">
  							<div class="col-lg-6">
								<select class="form-control" name="user_state" id="user_state" onchange="state_selected(this); disableError('user_state');">
									<option disabled selected hidden value="select">* Select state</option>
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
									<textarea rows="3" class="form-control" onkeyup="disableError('employer_address')" id="employer_address" name="employer_address" placeholder="* Full address of employer"></textarea>
									<label style="color:red;font-weight:normal;display:none" id="employer_address_error"></label>
								</div>
							</div>
							<div class="form-group col-lg-12">
								<div class="col-lg-6">
									<input type="text" class="form-control" onkeyup="disableError('contact_person_name')" placeholder="* Contact person name" id="contact_person_name" name="contact_person_name">
									<label style="color:red;font-weight:normal;display:none" id="contact_person_name_error"></label>
								</div>
								<div class="col-lg-6">
									<input type="number" onkeyup="disableError('contact_person_no')" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" maxlength="10" class="form-control" placeholder="* Contact person phone number" id="contact_person_no" name="contact_person_no">
									<label style="color:red;font-weight:normal;display:none" id="contact_person_no_error"></label>
								</div>
							</div>
							<div class="form-group col-lg-12">
								<div class="col-lg-6">
									<input type="text" class="form-control" onkeyup="disableError('contact_person_email')" placeholder="* Contact person email-ID" id="contact_person_email" name="contact_person_email">
									<label style="color:red;font-weight:normal;display:none" id="contact_person_email_error"></label>
								</div>
							</div>
							<div class="form-group col-lg-12">
								<div class="col-lg-6">
									<input type="text" class="form-control" onkeyup="disableError('employer_date')" placeholder="Joining date of employer" id="employer_date" name="employer_date">
									<label style="color:red;font-weight:normal;display:none" id="employer_date_error"></label>
								</div>
							</div>
							<div class="form-group col-lg-12">
								<div class="col-lg-3">
									<button type="button" onclick="validate(this)" class="btn btn-primary">Save employer</button>
								</div>
							</div>
  					</form>
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
		<jsp:param name="pageId" value="pg3"/>
	</jsp:include>
	
	<script type="text/javascript" src="<%=RouteManager.getBasePath()%>assets/js/typeahead.bundle.js"></script>
<script type="text/javascript">
$(document).ready(function() { 
	$('.gujarat-selected').hide();
	$('.other-selected').hide();
	var suggestions_industry = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.whitespace,
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        prefetch: {
        	url: basePath + 'GetSuggestion?autocomplete=' + <%=UCConstants.INDUSTRY_SECTOR%>,
        	cache:false,
        	ttl:0
        }
 	});
	suggestions_industry.initialize();
	 $('#industry-sector').typeahead(null, {
			name: "suggestions_industry",
	        source: suggestions_industry,
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
	 $('#employer_date').datepicker({
		 format: 'yyyy-mm-dd',
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
	if($("#firm_name").val() == "") {
		showError("firm_name", "Enter firm name");
	} else if(document.getElementById("employer_type").value == "select") {
		showError("employer_type", "Select employer type");
	} else if($("#industry-sector").val() == "") {
		showError("industry-sector", "Enter industry sector of firm");
	} else if(document.getElementById("user_state").value == "select") {
		showError("user_state", "Select any state");
	} else {
		if(document.getElementById("user_state").options[document.getElementById("user_state").selectedIndex].text.toLowerCase() == "other") {
			if(document.getElementById("user_district_other").value == "") {
				showError("user_district_other", "Enter valid district name.");
			} else if(document.getElementById("user_city_other").value == "") {
				showError("user_city_other", "Enter valid city or village name.");
			} else if($("#employer_address").val() == "") {
				showError("employer_address", "Enter employer full address");
			} else if($("#contact_person_name").val() == "") {
				showError("contact_person_name", "Enter contact person name");
			} else if($("#contact_person_no").val() == "" || !$.isNumeric(document.getElementById("contact_person_no").value) || document.getElementById("contact_person_no").value.length != 10) {
				showError("contact_person_no", "Enter contact person valid phone number");
			} else if($("#contact_person_email").val() == "") {
				showError("contact_person_email", "Enter contact person email-ID");
			} else {
				//success
				document.getElementById("employer_form").submit();
			}
		} else {
			if(document.getElementById("district_options").value == "select") {
				showError("district_options", "Select district.");
			} else if(document.getElementById("city_options").value == "select") {
				showError("city_options", "Select city or village.");
			} else if($("#employer_address").val() == "") {
				showError("employer_address", "Enter employer full address");
			} else if($("#contact_person_name").val() == "") {
				showError("contact_person_name", "Enter contact person name");
			} else if($("#contact_person_no").val() == "") {
				showError("contact_person_no", "Enter contact person phone number");
			} else if($("#contact_person_email").val() == "") {
				showError("contact_person_email", "Enter contact person email-ID");
			} else {
				//success
				document.getElementById("employer_form").submit();
			}
		}
	}
	btn.innerHTML = "Save employer";
}
</script>
	
</body>
</html>