<%@ page import="model.Uc_state" %>
<%@ page import="java.util.List" %>
<%@ page import="utils.SessionManager" %>
<%@ page import="utils.RouteManager" %>
<%@ page import="controller.UCConstants" %>

<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="User Accounts - Urdhvaga Consultancy"/>
</jsp:include>
<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>assets/css/typeahead.bundle.css" />

	<div id="wrapper">
	
	<div class="container top-btm-pad">
		
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
		<div class="row signin-form">
			<div class="col-lg-12">
				<div class="row">
					<div class="col-lg-offset-3 col-lg-6">
					<div class="row">
			<div class="col-lg-12">
				<h3 class="page-title">
					<i class="fa fa-sign-in title-icon"></i> Sign In
				</h3>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
			<% if(request.getParameter("job") != null) { %>
				<form id="signInForm" action="<%=RouteManager.getBasePath() %>user-accounts?job=<%=request.getParameter("job") %>&action=login" method="POST">
			<% } else { %>
				<form id="signInForm" action="<%=RouteManager.getBasePath() %>user-accounts?action=login" method="POST">
			<% } %>
				
							<div class="col-lg-12 form-group">
								<input type="text" autofocus class="form-control" onkeyup="disableError('signin_username')" id="signin_username" name="signin_username" placeholder="Contact Number or Email-Id">
								<label style="color:red;font-weight:normal;display:none" id="signin_username_error"></label>
							</div>
							<div class="col-lg-12 form-group">
								<input type="password" class="form-control" onkeyup="disableError('signin_password')" id="signin_password" name="signin_password" placeholder="Password">
								<label style="color:red;font-weight:normal;display:none" id="signin_password_error"></label>
							</div>
							<div class="col-lg-12 form-group">
								<button type="button" class="btn mybtn" onclick="validateSignIn(this)"><i style="color:#FFF200" class="fa fa-sign-in"></i> &nbsp; Sign In</button>
								<a href="<%=RouteManager.getBasePath() %>forgot-password" style="float:right" class="red-link">Forgot password?</a>
							</div>
						</form>
			</div>
		</div>
		<hr style="border-color:#818181">
		<div class="row">
			<div class="col-lg-12">
				<a onclick="show_signup_form()" style="cursor:pointer" class="btn mybtn col-lg-offset-3"><span style="color:#FFF200">Don't have an account?</span> Register here.</a>
			</div>
		</div>
					</div>
					
				</div>
			</div>
		</div>
		
		<div class="row signup-form">
			<div class="col-lg-12">
				<div class="row">
					<div class="col-lg-offset-3 col-lg-6">
					<div class="row">
			<div class="col-lg-12">
				<h3 class="page-title">
					<span class="glyphicon glyphicon-user title-icon"></span> Sign Up
				</h3>
			</div>
		</div>
		
	<div class="row">
		<section>
        <div class="wizard">
            <div class="wizard-inner">
                <div class="connecting-line"></div>
                <ul id="myTabs" class="nav nav-tabs" role="tablist">

                    <li role="presentation" class="active">
                        <a href="#step1" data-toggle="tab" aria-controls="step1" role="tab" title="Personal Profile">
                            <span class="round-tab">
                                <i class="glyphicon glyphicon-user"></i>
                            </span>
                        </a>
                    </li>

                    <li role="presentation" class="disabled">
                        <a href="#step2" data-toggle="tab" aria-controls="step2" role="tab" title="Education & Experience">
                            <span class="round-tab">
                                <i class="fa fa-graduation-cap"></i>
                            </span>
                        </a>
                    </li>
                    <li role="presentation" class="disabled">
                        <a href="#step3" data-toggle="tab" aria-controls="step3" role="tab" title="Areas of Interest">
                            <span class="round-tab">
                                <i class="fa fa-binoculars"></i>
                            </span>
                        </a>
                    </li>

                    <li role="presentation" class="disabled">
                        <a href="#complete" data-toggle="tab" aria-controls="complete" role="tab" title="Upload your Resume">
                            <span class="round-tab">
                                <i class="glyphicon glyphicon-ok"></i>
                            </span>
                        </a>
                    </li>
                </ul>
            </div>
			<% if(request.getParameter("job") != null) { %>
				<form role="form" id="signup_form" action="<%=RouteManager.getBasePath() %>user-accounts?job=<%=request.getParameter("job") %>&action=signup" enctype="multipart/form-data" method="POST">
			<% } else { %>
				<form role="form" id="signup_form" action="<%=RouteManager.getBasePath() %>user-accounts?action=signup" enctype="multipart/form-data" method="POST">
			<% } %>
            
            <input type="hidden" name="qualification_count" id="qualification_count">
            <input type="hidden" name="experience_count" id="experience_count">
            <input type="hidden" name="interest_count" id="interest_count">
                <div class="tab-content">
                    <div class="tab-pane active" role="tabpanel" id="step1">
                        <div class="col-lg-12 form-group" style="text-align:center;border-bottom:1px solid #818181">
							<h3>Personal Profile</h3>
						</div>
							<div class="col-lg-12 form-group">
								<input type="text" class="form-control" name="user_fullname" onkeyup="disableError('user_fullname')" id="user_fullname" placeholder="* Full Name - Example: Jignesh Khatri">
								<label style="color:red;font-weight:normal;display:none" id="user_fullname_error"></label>
							</div>
							<div class="col-lg-12 form-group">
								<input type="number" name="user_contactno" id="user_contactno" onkeyup="disableError('user_contactno')" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" maxlength="10" class="form-control" placeholder="* Primary Contact Number - Example: 9898989898">
								<label style="color:red;font-weight:normal;display:none" id="user_contactno_error"></label>
								<label style="color:#790000;font-weight:normal">Primary contact number will be your username.</label>
							</div>
							<div class="col-lg-12 form-group">
								<input type="number" name="user_secondarycontactno" id="user_secondarycontactno" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" maxlength="10" class="form-control" placeholder="Secondary Contact Number - Example: 8989898989">
							</div>
							<div class="col-lg-12 form-group">
								<input type="email" name="user_email" id="user_email" onkeyup="disableError('user_email')" class="form-control" placeholder="* Email Id - Example: k3.jignesh@gmail.com">
								<label style="color:red;font-weight:normal;display:none" id="user_email_error"></label>
							</div>
							<div class="col-lg-12 form-group">
								<input type="password" class="form-control" name="user_password" id="user_password" onkeyup="disableError('user_password')" placeholder="* Password">
								<label style="color:red;font-weight:normal;display:none" id="user_password_error"></label>
							</div>
							<div class="col-lg-12 form-group">
								<input type="password" class="form-control" onkeyup="disableError('confirm_password')" id="confirm_password" placeholder="* Confirm Password">
								<label style="color:red;font-weight:normal;display:none" id="confirm_password_error"></label>
							</div>
							<div class="col-lg-12 form-group">
								<label style="cursor:pointer"><input name="user_gender" value="<%=UCConstants.MALE%>" type="radio" checked> Male</label> &nbsp; <label style="cursor:pointer"><input name="user_gender" value="<%=UCConstants.FEMALE %>" type="radio"> Female</label>
							</div>
							<div class="col-lg-12 form-group">
								<input type="number" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" maxlength="4" name="user_birthyear" onkeyup="disableError('user_birthyear')" onclick="disableError('user_birthyear')" class="form-control" placeholder="* Birth Year - Example: 1995" id="user_birthyear">
								<label style="color:red;font-weight:normal;display:none" id="user_birthyear_error"></label>
							</div>
							
							<div class="col-lg-12 form-group">
								<select class="form-control" name="user_state" id="user_state" onchange="state_selected(this); disableError('user_state');">
									<option disabled selected hidden value="select">* Select state in which you live</option>
									<% for(Uc_state state : (List<Uc_state>) request.getAttribute("states")) { %>
										<option value="<%=state.getS_id()%>"><%=state.getS_name() %></option>
									<% } %>
								</select>
								<label style="color:red;font-weight:normal;display:none" id="user_state_error"></label>
							</div>
							<div class="gujarat-selected">
								<div class="form-group col-lg-6">
									<select class="form-control" id="district_options" name="user_district" onchange="district_selected(this); disableError('district_options');">
										
									</select>
									<label style="color:red;font-weight:normal;display:none" id="district_options_error"></label>
								</div>
								<div class="form-group col-lg-6">
									<select class="form-control" id="taluka_options" name="user_taluka" onchange="taluka_selected(this); disableError('taluka_options');">
										
									</select>
									<label style="color:red;font-weight:normal;display:none" id="taluka_options_error"></label>
								</div>
								<div class="form-group col-lg-6">
									<select class="form-control" id="city_options" onchange="disableError('city_options')" name="user_city">
								
									</select>
									<label style="color:red;font-weight:normal;display:none" id="city_options_error"></label>
								</div>
							</div>
							<div class="other-selected">
								<div class="form-group col-lg-6">
									<input type="text" placeholder="* District" id="user_district_other" onkeyup="disableError('user_district_other')" name="user_district_other" class="form-control">
									<label style="color:red;font-weight:normal;display:none" id="user_district_other_error"></label>
								</div>
								<div class="form-group col-lg-6">
									<input type="text" placeholder="* Taluka" id="user_taluka_other" onkeyup="disableError('user_taluka_other')" name="user_taluka_other" class="form-control">
									<label style="color:red;font-weight:normal;display:none" id="user_taluka_other_error"></label>
								</div>
								<div class="form-group col-lg-6">
									<input type="text" placeholder="* City / Village" id="user_city_other" onkeyup="disableError('user_city_other')" name="user_city_other" class="form-control">
									<label style="color:red;font-weight:normal;display:none" id="user_city_other_error"></label>
								</div>
							</div>
                        <div class="form-group col-lg-12">
                            <button type="button" style="float:right" onclick="verify_first_step(this)" class="btn mybtn next-step">Continue &nbsp; <i class="fa fa-angle-double-right" style="color:#FFF200"></i></button>
                        </div>
                    </div>
                    <div class="tab-pane" role="tabpanel" id="step2">
                         <div class="col-lg-12 form-group" style="text-align:center;border-bottom:1px solid #818181">
							<h3>Education & Experience</h3>
						</div>
						<div class="col-lg-12">
								
								<div class="input-group">
    <input type="text" class="form-control" id="user_qualification" onkeyup="disableError('user_qualification')" placeholder="* Qualification - Example: B.Com.">
    
    <div class="input-group-btn">
		<button type="button" onclick="add_qualification_tag()" class="btn mybtn"><i style="color:#FFF200" class="fa fa-plus"></i> &nbsp;Add</button>
	 
    </div><!-- /btn-group -->
	
  </div><!-- /input-group -->
  <label style="color:red;font-weight:normal;display:none" id="user_qualification_error"></label>
							</div>
							<div class="form-group col-lg-12" id="qualification-tags">
							</div>
							<div class="form-group col-lg-12">
								<label style="color:#818181;font-weight:normal">Do you have any experience?</label>
								<label style="cursor:pointer"><input type="radio" onclick="experience_check()" name="user_experience" value="y"> Yes</label> &nbsp; <label style="cursor:pointer"><input type="radio" name="user_experience" onclick="experience_uncheck()" value="n" checked> No</label>
							</div>
							<div class="experience_y form-group col-lg-12">
							
							<div id="more-experience"></div>
							<a class="btn mybtn" data-toggle="tooltip" data-placement="top" id="add_more_quali" title="Click this button to add another experience" style="float:right;cursor:pointer" onclick="load_more_experience()"><i class="fa fa-plus" style="color:#FFF200"></i> &nbsp; Add more</a>
							</div>
							<div class="form-group col-lg-12">
								<input type="text" placeholder="Expected salary in INR (Ex.: 10000-20000) (optional)" id="user_expected_salary" name="user_expected_salary" class="form-control">
								
							</div>
                        <div class="form-group col-lg-12">
							<button type="button" style="float:right" onclick="verify_second_step(this)" class="btn mybtn next-step">Continue &nbsp; <i class="fa fa-angle-double-right" style="color:#FFF200"></i></button>
                            <button type="button" style="float:right;margin-right:10px;" class="btn btn-default prev-step"><i class="fa fa-angle-double-left"></i> &nbsp; Previous</button>
                        </div>
                    </div>
                    <div class="tab-pane" role="tabpanel" id="step3">
					<div class="col-lg-12 form-group" style="text-align:center;border-bottom:1px solid #818181">
							<h3>Areas of Interest</h3>
						</div>
                       <div class="form-group col-lg-12">
								<input type="text" style=\"widht:100%\" class="form-control" id="industry-sector" onkeyup="showTooltip('#post_btn'); disableError('industry-sector');" placeholder="* Industry sector you are interested in">
								<label style="color:red;font-weight:normal;display:none" id="industry-sector_error"></label>
							</div>
							
							<div class="form-group col-lg-12">
								<input type="text" style=\"widht:100%\" class="form-control" id="industry-post" onkeyup="disableError('industry-post')" placeholder="* Post or position you are interested in">
								<label style="color:red;font-weight:normal;display:none" id="industry-post_error"></label>
							</div>
							
							<div class="form-group col-lg-12">
								<a class="btn mybtn" id="post_btn" data-toggle="tooltip" data-placement="left" title="Click here to add industry sector and industry post" onclick="add_tag(); disableError('industry-sector'); disableError('industry-post');"><i class="fa fa-plus" style="color:#FFF200"></i> &nbsp; Add</a>
							</div>
							
							<div class="form-group col-lg-12" id="post-interest-tags">
							</div>
                        <div class="form-group col-lg-12">
							<button type="button" style="float:right" onclick="verify_third_step(this)" class="btn mybtn btn-info-full next-step">Continue &nbsp; <i class="fa fa-angle-double-right" style="color:#FFF200"></i></button>
                            <button type="button" style="float:right;margin-right:10px;" class="btn btn-default prev-step"><i class="fa fa-angle-double-left"></i> &nbsp; Previous</button>
                        </div>
                    </div>
                    <div class="tab-pane" role="tabpanel" id="complete">
					<div class="col-lg-12 form-group" style="text-align:center;border-bottom:1px solid #818181">
							<h3>Upload your Resume & Sign Up</h3>
						</div>
							<div class="form-group col-lg-12">
								<input type="file" id="user_resume" name="user_resume" onchange="disableError('user_resume')" accept=".pdf,.doc,.docx,.txt,.jpeg,.jpg,.gif,.bmp,.png" class="form-control">
								<label style="color:red;font-weight:normal;display:none" id="user_resume_error"></label>
								<label style="color:#790000;font-weight:normal"><span style="color:black">Accepted formats:</span> .pdf  .doc  .docx  .txt  .jpeg  .jpg  .gif  .bmp .png</label>
							</div>
							<div class="form-group col-lg-12">
								<input type="checkbox" id="tc" name="tc"> Agree <a class="blue-link" href="<%=RouteManager.getBasePath() %>terms-conditions" target="_blank">Terms & Conditions</a>
								<label style="color:red;font-weight:normal;display:none" id="tc_error"></label>
							</div>
							<div class="form-group col-lg-12">
								<label class="form-control">Registration fees is Rs.100</label>
							</div>
							 <div class="form-group col-lg-12">
							<button type="button" onclick="verify_fourth_step(this)" style="float:right" class="btn mybtn btn-info-full next-step"><i style="color:#FFF200" class="fa fa-check"></i> &nbsp; Sign Up</button>
                            <button type="button" style="float:right;margin-right:10px;" class="btn btn-default prev-step"><i class="fa fa-angle-double-left"></i> &nbsp; Previous</button>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                </div>
            </form>
        </div>
    </section>
   </div>

		<hr style="border-color:#818181">
		<div class="row">
			<div class="col-lg-12">
				<a onclick="show_signin_form()" style="cursor:pointer" class="btn mybtn col-lg-offset-3"><span style="color:#FFF200">Already have an account?</span> Sign in here.</a></a>
			</div>
		</div>
					</div>
					
				</div>
			</div>
		</div>
		
	</div>
	
	</div>
<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg4"/>
</jsp:include>
<script type="text/javascript" src="<%=RouteManager.getBasePath()%>assets/js/typeahead.bundle.js"></script>
<script>
var basePath = "<%=RouteManager.getBasePath()%>";
var count = 1;
var tag_count = 1;
var q_tag_count = 1;
 $(document).ready(function() { 
$('.signup-form').hide();
$('.gujarat-selected').hide();
$('.other-selected').hide();
$('.experience_y').hide();
//$("#user_qualification").autocomplete("getQualiSuggestion.jsp");
//var widget = new AutoComplete('user_qualification', "getQualiSuggestion.jsp");
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
 $('#user_qualification').typeahead(null, {
		name: "suggestions_qualification",
        source: suggestions_qualification,
        limit: 10 /* Specify maximum number of suggestions to be displayed */
 });
 
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

});
 
 function validateSignIn(btn) {
	 btn.innerHTML = "Please wait...";
	 if($("#signin_username").val() == "") {
		 showError("signin_username", "Please enter your username (Contact number or Email-Id).");
	 } else if($("#signin_password").val() == "") {
		 showError("signin_password", "Please enter your password.");
	 } else {
		 document.getElementById("signInForm").submit();
	 }
	 btn.innerHTML = '<i style="color:#FFF200" class="fa fa-sign-in"></i> &nbsp; Sign In';
 }
 
function show_signup_form() {
	$('.signin-form').hide('slow');
		$('.signup-form').show('slow');
}

function show_signin_form() {
$('.signin-form').show('slow');
		$('.signup-form').hide('slow');
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

 $('#user_birthyear').datepicker({
				format: 'yyyy',
				viewMode: "years", minViewMode: "years",
				endDate: new Date()
	 //changeMonth: false,
     //changeYear: true
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
			$("#error-alert"). fadeTo(5000, 500). slideUp(1000, function(){
				$("#error-alert"). alert('close');
				});
			$("#success-alert"). fadeTo(5000, 500). slideUp(1000, function(){
				$("#success-alert"). alert('close');
				});
			
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
			
			/*function disableError(id) {
				$('#' + id).css({'border-color':''});
				$('#' + id + "_error").css({'display':'none'});
			}
			
			function showError(id, msg) {
				$('#' + id).css({'border-color':'red'});
				$('#' + id).focus();
				$('#' + id + "_error").html(msg);
				$('#' + id + "_error").css({'display':'block'});
			}*/
			
			function verify_first_step(btn) {
				 var re = /\S+@\S+\.\S+/;
				btn.innerHTML = "Please wait...";
				if(document.getElementById("user_fullname").value == "" || $.isNumeric(document.getElementById("user_fullname").value)) {
					showError("user_fullname", "Please enter valid full name.");
				} else if(document.getElementById("user_contactno").value == "" || !$.isNumeric(document.getElementById("user_contactno").value) || document.getElementById("user_contactno").value.length != 10) {
					showError("user_contactno", "Please enter valid contact number of 10 digits.");
				} else if(document.getElementById("user_email").value == "" || !re.test(document.getElementById("user_email").value)) {
					showError("user_email", "Please enter valid email ID.");
				} else if(document.getElementById("user_password").value == "" || document.getElementById("user_password").value.length < 6) {
					showError("user_password", "Please enter minimum 6 characters password.");
				} else if(document.getElementById("user_password").value != document.getElementById("confirm_password").value) {
					showError("confirm_password", "Password and confirm password mismatch.");
				} else if(document.getElementById("user_birthyear").value == "" || !$.isNumeric(document.getElementById("user_birthyear").value) || document.getElementById("user_birthyear").value.length != 4) {
					showError("user_birthyear", "Please enter birthyear in 4 digits.");
				} else if(document.getElementById("user_state").value == "select") {
					showError("user_state", "Please select state.");
				} else {
					if(document.getElementById("user_state").options[document.getElementById("user_state").selectedIndex].text.toLowerCase() == "other") {
						if(document.getElementById("user_district_other").value == "") {
							showError("user_district_other", "Please enter valid district name.");
						} else if(document.getElementById("user_taluka_other").value == "") {
							showError("user_taluka_other", "Please enter valid taluka name.");
						} else if(document.getElementById("user_city_other").value == "") {
							showError("user_city_other", "Please enter valid city or village name.");
						} else {
							var errFlag = false;
							$.ajax({
								url: 'user-accounts?action=verifyContact',
								data: {
									"user_contact": document.getElementById("user_contactno").value,
									"user_email": document.getElementById("user_email").value,
								},
								type: 'POST',
							}).done(function(data) {
								
								if(data == "success") {
									//errFlag = false;
									showNextTab();
								} else if(data == "email") {
									//errFlag = true;
									showError("user_email", "Entered email ID is already registered.");
								} else {
									showError("user_contactno", "Entered contact number is already registered.");
								}
								/*if(errFlag) {
									//contact already exists
									showError("user_contactno", "Entered contact number is already registered. If this is your contact number than, please <a style=\"color:blue;cursor:pointer\" onClick='show_signin_form()'>sign in</a> to your account.");
								} else {
									//success
									showNextTab();
								}*/
							}).fail(function(data) {
								alert("Something went wrong. Please try again.");
							});
							
							
						}
					} else {
						if(document.getElementById("district_options").value == "select") {
							showError("district_options", "Please select district.");
						} else if(document.getElementById("taluka_options").value == "select") {
							showError("taluka_options", "Please select taluka.");
						} else if(document.getElementById("city_options").value == "select") {
							showError("city_options", "Please select city or village.");
						} else {
							var errFlag = false;
							$.ajax({
								url: 'user-accounts?action=verifyContact',
								data: {
									"user_contact": document.getElementById("user_contactno").value,
									"user_email": document.getElementById("user_email").value,
								},
								type: 'POST',
							}).done(function(data) {
								if(data == "success") {
									//errFlag = false;
									showNextTab();
								} else if(data == "email") {
									//errFlag = true;
									showError("user_email", "Entered email ID is already registered.");
								} else {
									showError("user_contactno", "Entered contact number is already registered.");
								}

								/*if(errFlag) {
									//contact already exists
									showError("user_contactno", "Entered contact number is already registered. If this is your contact number than, please <a style=\"color:blue;cursor:pointer\" onClick='show_signin_form()'>sign in</a> to your account.");
								} else {
									//success
									showNextTab();
								}*/
							}).fail(function(data) {
								alert("Something went wrong. Please try again.");
							});
						}
					}
				}
				btn.innerHTML = 'Continue &nbsp; <i class="fa fa-angle-double-right" style="color:#FFF200"></i>';
			}
			
			function showNextTab() {
				  var $active = $('.wizard .nav-tabs li.active');
			        $active.next().removeClass('disabled');
			        $active.addClass('disabled');
			        nextTab($active); 
			}
			
			
			
			function add_qualification_tag() {
				disableError("user_qualification");
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
			
			function experience_check() {
				$('.experience_y').show('slow');
				if(count==1) 
				load_more_experience();
				
			}
			
			/*****************/
			var flag = true;
			function showTooltip(id) {
				if(flag) {
					$(id).tooltip('show');
					flag = false;
				}
			}
			/******************/
			
			function experience_uncheck() {
				$('.experience_y').hide('slow');
			}
			function load_more_experience() {
				if($('.experience_y_inner').length <= 0) {
					var more = '<div class="experience_y_inner form-group col-lg-12" id="exp'+count+'"><div class="form-group col-lg-6"><input type="text" class="form-control" name="user_industry_sector'+count+'" id="user_industry_sector'+count+'" onkeyup="disableError(\'user_industry_sector'+count+'\'); showTooltip(\'#add_more_quali\');" placeholder="* Industry sector"><label style="color:red;font-weight:normal;display:none" id="user_industry_sector'+count+'_error"></label></div><div class="form-group col-lg-6"><input type="text" name="user_post'+count+'" id="user_post'+count+'" onkeyup="disableError(\'user_post'+count+'\')" placeholder="* Postion or post" class="form-control"><label style="color:red;font-weight:normal;display:none" id="user_post'+count+'_error"></label></div><div class="form-group col-lg-6"><input type="number" name="user_year_exp'+count+'" id="user_year_exp'+count+'" onkeyup="disableError(\'user_year_exp'+count+'\')" class="form-control" placeholder="* Years of experience"><label style="color:red;font-weight:normal;display:none" id="user_year_exp'+count+'_error"></label></div><div class="form-group col-lg-6"><input type="number" name="user_exp_salary'+count+'" id="user_exp_salary'+count+'" class="form-control" placeholder="Salary (optional)"></div><div class="form-group col-lg-6"><label><input type="checkbox" name="current_job" onchange="currentChanged(this)" value="current_job'+count+'"> Current job</label></div></div>';
				} else {
					var more = '<div class="experience_y_inner form-group col-lg-12" id="exp'+count+'"><div class="form-group col-lg-12"><a title="Remove" style="float:right;cursor:pointer" onclick="remove_experience('+count+')"><i class="fa fa-close" style="color:#790000"></i></a></div><div class="form-group col-lg-6"><input type="text" class="form-control" name="user_industry_sector'+count+'" id="user_industry_sector'+count+'" onkeyup="disableError(\'user_industry_sector'+count+'\')" placeholder="* Industry sector"><label style="color:red;font-weight:normal;display:none" id="user_industry_sector'+count+'_error"></label></div><div class="form-group col-lg-6"><input type="text" name="user_post'+count+'" id="user_post'+count+'" onkeyup="disableError(\'user_post'+count+'\')" placeholder="* Postion or post" class="form-control"><label style="color:red;font-weight:normal;display:none" id="user_post'+count+'_error"></label></div><div class="form-group col-lg-6"><input type="number" name="user_year_exp'+count+'" id="user_year_exp'+count+'" onkeyup="disableError(\'user_year_exp'+count+'\')" class="form-control" placeholder="* Years of experience"><label style="color:red;font-weight:normal;display:none" id="user_year_exp'+count+'_error"></label></div><div class="form-group col-lg-6"><input type="number" name="user_exp_salary'+count+'" id="user_exp_salary'+count+'" class="form-control" placeholder="Salary (optional)"></div><div class="form-group col-lg-6"><label><input type="checkbox" name="current_job" onchange="currentChanged(this)" value="current_job'+count+'"> Current job</label></div></div>';
				}
				count++;
				document.getElementById("experience_count").value = count;
				$("#more-experience").append(more);
			}
			function remove_experience(id) {
				$("#exp" + id).remove();
			}
			
			function currentChanged(check) {
			    $('input[name="' + check.name + '"]').not(check).prop('checked', false);
			}
			
			function verify_second_step(btn) {
				btn.innerHTML = "Please wait...";
				if($('#qualification-tags').has('.single-tag').length == 0) {
					showError("user_qualification", "Please add your qualification.");
				} 
				else if($('input[name=user_experience]:checked', '#signup_form').val() == 'y') {
					if($('#user_industry_sector1').val() == "") {
						showError('user_industry_sector1', 'Please enter the industry sector in which you have experience.');
					} else if($('#user_post1').val() == "") {
						showError('user_post1', 'Please enter the position on which you have worked in the entered industry sector.');
					} else if($('#user_year_exp1').val() == "") {
						showError('user_year_exp1', 'Please enter the number of years you have experience in entered industry sector.'); 
					} else {
						//success
						showNextTab();
					}
				} else {
					//success
					showNextTab();
				}
				btn.innerHTML = 'Continue &nbsp; <i class="fa fa-angle-double-right" style="color:#FFF200"></i>';
			}
			
			function add_tag() {
				var industry = document.getElementById("industry-sector").value;
				var post = document.getElementById("industry-post").value;
				var flag = true;
				if(industry != "" && post != ""){
				document.getElementById("industry-sector").value = "";
				document.getElementById("industry-post").value = "";
				for(var i = 1 ; i < tag_count ; i++) { 
					if(industry == $("#interest-industry" + i).val() && post == $("#interest-post" + i).val()) flag = false;
				}
				if(flag) {
					var tag = '<div class="single-tag" id="tag_count'+tag_count+'"> &nbsp;<a onclick="remove_tag('+tag_count+')" style="cursor:pointer"><i class="fa fa-close" style="color: #790000"></i></a> &nbsp;<label>'+industry+'</label><input type="hidden" id="interest-industry'+tag_count+'" name="interest-industry'+tag_count+'" value="'+industry+'"> - <label>'+post+'</label><input type="hidden" id="interest-post'+tag_count+'" name="interest-post'+tag_count+'" value="'+post+'"></div>';
					tag_count++;
					document.getElementById("interest_count").value = tag_count;
					$("#post-interest-tags").append(tag);	
				}
				}
				flag = true;
			}
			function remove_tag(id) {
				$("#tag_count" + id).remove();
			}
			
			function verify_third_step(btn) {
				btn.innerHTML = "Please wait...";
				if($('#post-interest-tags').has('.single-tag').length == 0) {
					showError("industry-post", "Please enter and add post on which you are interested in above entered industry sector.");
					showError("industry-sector", "Please enter and add industry sector in which you are interested.");
				} else {
					//success
					showNextTab();
				}
				btn.innerHTML = 'Continue &nbsp; <i class="fa fa-angle-double-right" style="color:#FFF200"></i>';
			}
			
			function verify_fourth_step(btn) {
				btn.innerHTML = "Please wait...";
				var validExtensions = ['.pdf', '.doc', '.docx', '.txt', '.jpg', '.jpeg', '.png', '.bmp', '.gif'];
				var file = document.getElementById("user_resume").value;
				if(file.length <= 0) {
					showError("user_resume", "Please select your resume to be uploaded.");
				} else {
					 var validFlag = false;
					 for (var j = 0; j < validExtensions.length; j++) {
		                    var sCurExtension = validExtensions[j];
		                    if (file.substr(file.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
		                        validFlag = true;
		                        break;
		                    }
		             }
					 if(!validFlag) {
						 showError("user_resume", "Please select resume file in any one of the below mentioned format.");
					 } else {
						 disableError("user_resume");
						 if(document.getElementById("tc").checked) {
							 //success
							 document.getElementById("signup_form").submit();
						 } else {
							 showError("tc", "Please Agree Terms & Conditions to complete Sign-Up.");
						 }
					 }
				}
				btn.innerHTML = '<i style="color:#FFF200" class="fa fa-check"></i> &nbsp; Sign Up';
			}
			
			/**************FORM WIZARD****************/
			
			$(document).ready(function () {
    //Initialize tooltips
    $('.nav-tabs > li a[title]').tooltip();
    
    //Wizard
    $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {

        var $target = $(e.target);
    
        if ($target.parent().hasClass('disabled')) {
            return false;
        }
    });

  /*  $(".next-step").click(function (e) {

        var $active = $('.wizard .nav-tabs li.active');
        $active.next().removeClass('disabled');
        nextTab($active);

    }); */
    $(".prev-step").click(function (e) {

        var $active = $('.wizard .nav-tabs li.active');
        $active.prev().removeClass('disabled');
        $active.addClass('disabled');
        prevTab($active);

    });
});

function nextTab(elem) {
    $(elem).next().find('a[data-toggle="tab"]').click();
}
function prevTab(elem) {
    $(elem).prev().find('a[data-toggle="tab"]').click();
}
			
			/**************FORM WIZARD****************/
		</script>
	</body>
</html>