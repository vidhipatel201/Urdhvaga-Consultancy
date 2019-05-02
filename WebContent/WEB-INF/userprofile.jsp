<%@page import="utils.SessionManager" %>
<%@page import="controller.UCConstants" %>
<%@page import="model.Uc_state" %>
<%@ page import="utils.RouteManager" %>
<%@ page import="java.util.List" %>
<%@page import="model.Uc_candidate_details" %>
<%@page import="model.Uc_interest_areas" %>
<%@page import="model.Uc_experience" %>
<%@page import="model.Uc_candidate_qualification" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Profile - Urdhvaga Consultancy"/>
</jsp:include>
<style>
.background {
	background:#fff;padding-top:15px;padding-bottom:15px;border-radius:3px;
}
</style>
<% 
	Uc_candidate_details ucCandidate = new Uc_candidate_details();
   	ucCandidate = (Uc_candidate_details) request.getAttribute("candidateDetails"); 
%>
<div id="wrapper">
	
	<div class="container top-btm-pad">
		<div class="container">
		<div class="row">
		<div class="col-lg-offset-3 col-lg-6">
			
			<div class="row">
				<div class="col-lg-12">
					<h3 class="page-title">
						<i class="fa fa-user title-icon"></i> My Profile
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
                        <a href="#step1" data-toggle="tab" onclick="changed()" aria-controls="step1" role="tab" title="Personal Profile">
                            <span class="round-tab">
                                <i class="glyphicon glyphicon-user"></i>
                            </span>
                        </a>
                    </li>

                    <li role="presentation">
                        <a href="#step2" data-toggle="tab" onclick="changed()" aria-controls="step2" role="tab" title="Education & Experience">
                            <span class="round-tab">
                                <i class="fa fa-graduation-cap"></i>
                            </span>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#step3" data-toggle="tab" onclick="changed()" aria-controls="step3" role="tab" title="Areas of Interest">
                            <span class="round-tab">
                                <i class="fa fa-binoculars"></i>
                            </span>
                        </a>
                    </li>

                    <li role="presentation">
                        <a href="#complete" data-toggle="tab" onclick="changed()" aria-controls="complete" role="tab" title="Upload your Resume">
                            <span class="round-tab">
                                <i class="glyphicon glyphicon-file"></i>
                            </span>
                        </a>
                    </li>
                </ul>
            </div>
			
            <form role="form" id="profile_form" action="" enctype="multipart/form-data" method="POST">
            <input type="hidden" name="qualification_count" id="qualification_count">
            <input type="hidden" name="experience_count" id="experience_count">
            <input type="hidden" name="interest_count" id="interest_count">
                <div class="tab-content">
                    <div class="tab-pane active" role="tabpanel" id="step1">
                        <div class="col-lg-12 form-group" style="text-align:center;border-bottom:1px solid #818181">
							<h3>Personal Profile</h3>
						</div>
							<div class="col-lg-12 form-group background">
								<label style="color:#515151">Full Name</label>
								<input type="text" class="form-control" value="<%=ucCandidate.getCd_name() %>" name="user_fullname" onkeyup="disableError('user_fullname')" id="user_fullname" placeholder="* Full Name - Example: Jignesh Khatri">
								<label style="color:red;font-weight:normal;display:none" id="user_fullname_error"></label>
							</div>
							<div class="col-lg-12 form-group background">
								<label style="color:#515151">Secondary Contact Number</label>
								<input type="number" name="user_secondarycontactno" id="user_secondarycontactno" value="<%=(ucCandidate.getCd_contact_num_secondary() == 0 ? "" : ucCandidate.getCd_contact_num_secondary()) %>" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" maxlength="10" class="form-control" placeholder="Secondary Contact Number - Example: 8989898989">
							</div>
							<div class="col-lg-12 form-group background">
								<label style="color:#515151">Email-Id</label>
								<input type="email" value="<%=ucCandidate.getCd_email() %>" name="user_email" id="user_email" onkeyup="disableError('user_email')" class="form-control" placeholder="* Email Id - Example: k3.jignesh@gmail.com">
								<label style="color:red;font-weight:normal;display:none" id="user_email_error"></label>
							</div>
							<div class="col-lg-12 form-group background">
								<label style="color:#515151">Gender</label>
								<% if(ucCandidate.getCd_gender() == 1) { %>
								<label style="cursor:pointer"><input name="user_gender" value="<%=UCConstants.MALE%>" type="radio" checked> Male</label> &nbsp; <label style="cursor:pointer"><input name="user_gender" value="<%=UCConstants.FEMALE %>" type="radio"> Female</label>
								<% } else { %>
								<label style="cursor:pointer"><input name="user_gender" value="<%=UCConstants.MALE%>" type="radio"> Male</label> &nbsp; <label style="cursor:pointer"><input name="user_gender" value="<%=UCConstants.FEMALE %>" type="radio" checked> Female</label>
								<% } %>
							</div>
							<div class="col-lg-12 form-group background">
							<label style="color:#515151">Birth Year</label>
								<input type="number" value="<%=ucCandidate.getCd_birthyear() %>" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" maxlength="4" name="user_birthyear" onkeyup="disableError('user_birthyear')" onclick="disableError('user_birthyear')" class="form-control" placeholder="* Birth Year - Example: 1995" id="user_birthyear">
								<label style="color:red;font-weight:normal;display:none" id="user_birthyear_error"></label>
							</div>
							<div class="col-lg-12 form-group background">
							<label style="color:#515151">Location: </label>
							<label><%=request.getAttribute("location") %></label>
							</div>
							
							<div class="background col-lg-12 form-group">
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
									<select class="form-control" id="user_district" name="user_district" onchange="district_selected(this); disableError('district_options');">
										
									</select>
									<label style="color:red;font-weight:normal;display:none" id="district_options_error"></label>
								</div>
								<div class="form-group col-lg-6">
									<select class="form-control" id="user_taluka" name="user_taluka" onchange="taluka_selected(this); disableError('taluka_options');">
										
									</select>
									<label style="color:red;font-weight:normal;display:none" id="taluka_options_error"></label>
								</div>
								<div class="form-group col-lg-6">
									<select class="form-control" id="user_city" onchange="disableError('city_options')" name="user_city">
								
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
							</div>
							
                        <div class="form-group col-lg-12">
                            <button type="button" style="float:right" onclick="savePersonalProfile(this)" class="btn mybtn">Save Changes</button>
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
								<%
									for(int i=1 ; i<=((List<String>)request.getAttribute("candidateQualifications")).size() ; i++) {								
								%>
								<div class="single-tag" id="q_tag_count<%=i%>"> &nbsp;<a onclick="remove_q_tag('<%=i%>')" style="cursor:pointer"><i class="fa fa-close" style="color: #790000"></i></a> &nbsp;<label id="user_qualification<%=i%>"><%=((List<String>)request.getAttribute("candidateQualifications")).get(i-1) %></label><input type="hidden" id="user_qualification_hidden<%=i %>" name="user_qualification<%=i %>" value="<%=((List<String>)request.getAttribute("candidateQualifications")).get(i-1) %>"></div>
								<% } %>
							</div>
							
							<div class="form-group col-lg-12">
								<label style="color:#818181;font-weight:normal">Do you have any experience?</label>
								<% if(request.getAttribute("experienceAvail") == "1") { %>
								<label style="cursor:pointer"><input type="radio" onclick="experience_check()" name="user_experience" value="y" checked> Yes</label> &nbsp; <label style="cursor:pointer"><input type="radio" name="user_experience" onclick="experience_uncheck()" value="n"> No</label>
								<% } else { %>
								<label style="cursor:pointer"><input type="radio" onclick="experience_check()" name="user_experience" value="y"> Yes</label> &nbsp; <label style="cursor:pointer"><input type="radio" name="user_experience" onclick="experience_uncheck()" value="n" checked> No</label>
								<% } %>
							</div>
							<div class="experience_y form-group col-lg-12">
							
							<div id="more-experience">
								<% if(request.getAttribute("experienceAvail") == "1") { %>
								<%=request.getAttribute("experienceData") %>
								<% } %>
							</div>
							<a class="btn mybtn" data-toggle="tooltip" data-placement="top" id="add_more_quali" title="Click this button to add another experience" style="float:right;cursor:pointer" onclick="load_more_experience()"><i class="fa fa-plus" style="color:#FFF200"></i> &nbsp; Add more</a>
							</div>
							<div class="form-group col-lg-12">
								<input type="text" value="<%=(ucCandidate.getCd_expected_salary().equalsIgnoreCase("N/A") ? "" : ucCandidate.getCd_expected_salary()) %>" placeholder="Expected salary in INR (Ex.: 10000-20000) (optional)" id="user_expected_salary" name="user_expected_salary" class="form-control">
								
							</div>
                        <div class="form-group col-lg-12">
							<button type="button" style="float:right" onclick="saveQualifications(this)" class="btn mybtn">Save Changes</button>
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
								<%=request.getAttribute("interestAreas") %>
							</div>
                        <div class="form-group col-lg-12">
							<button type="button" style="float:right" onclick="saveInterests(this)" class="btn mybtn">Save Changes</button>
                        </div>
                    </div>
                    <div class="tab-pane" role="tabpanel" id="complete">
					<div class="col-lg-12 form-group" style="text-align:center;border-bottom:1px solid #818181">
							<h3>Download or Modify your Resume</h3>
						</div>
						
							<div class="form-group col-lg-12">
								<input type="file" id="user_resume" name="user_resume" onchange="disableError('user_resume')" accept=".pdf,.doc,.docx,.txt,.jpeg,.jpg,.gif,.bmp,.png" class="form-control">
								<label style="color:red;font-weight:normal;display:none" id="user_resume_error"></label>
								<label style="color:#790000;font-weight:normal"><span style="color:black">Accepted formats:</span> .pdf  .doc  .docx  .txt  .jpeg  .jpg  .gif  .bmp .png</label>
							</div>
							 <div class="form-group col-lg-12">
							<button type="button" style="float:right" onclick="saveResume(this)" class="btn mybtn">Save Changes</button>
							<a href="<%=request.getAttribute("resume")%>"><button style="float:right" type="button" class="btn btn-default">Download Resume</button></a>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                </div>
            </form>
        </div>
    </section>
   </div>
   <div class="row">
				<div class="col-lg-12" id="changesResult">
					
				</div>
			</div>
		</div>
		</div>
		</div>
	</div>	
</div>

<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg2"/>
</jsp:include>
<script>
var count = <%=(Integer.parseInt(request.getAttribute("experienceCount").toString()) + 1)%>;
var q_tag_count = <%=(Integer.parseInt(request.getAttribute("qualificationCount").toString()) + 1)%>;
var tag_count = <%=(Integer.parseInt(request.getAttribute("interestCount").toString()) + 1)%>;

	$(document).ready(function(){
		$('.gujarat-selected').hide();
		$('.other-selected').hide();
		$('.experience_y').hide();
		if(count == 0) count = 1;
		else $('.experience_y').show();
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
				document.getElementById("user_district").innerHTML = data;
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
			document.getElementById("user_taluka").innerHTML = data;
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
			document.getElementById("user_city").innerHTML = data;
		}).fail(function(data) {
			alert("Something went wrong. Please try again.");
		});
	}
	
	function add_qualification_tag() {
		//disableError("user_qualification");
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
	function experience_uncheck() {
		$('.experience_y').hide('slow');
	}
	function remove_experience(id) {
		$("#exp" + id).remove();
	}
	
	function add_tag() {
		/*var industry = document.getElementById("industry-sector").value;
		var post = document.getElementById("industry-post").value;
		if(industry != "" && post != ""){
		document.getElementById("industry-sector").value = "";
		document.getElementById("industry-post").value = "";
		var tag = '<div class="single-tag" id="tag_count'+tag_count+'"> &nbsp;<a onclick="remove_tag('+tag_count+')" style="cursor:pointer"><i class="fa fa-close" style="color: #790000"></i></a> &nbsp;<label id="interest-industry'+tag_count+'">'+industry+'</label><input type="hidden" name="interest-industry'+tag_count+'" value="'+industry+'"> - <label id="interest-post'+tag_count+'">'+post+'</label><input type="hidden" name="interest-post'+tag_count+'" value="'+post+'"></div>';
		tag_count++;
		document.getElementById("interest_count").value = tag_count;
		$("#post-interest-tags").append(tag);
		}*/
		
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
	
	function changed() {
		document.getElementById("changesResult").innerHTML = "";
	}
	
	function savePersonalProfile(btn) {
		var city = "";
		var taluka = "";
		var district = "";
		var state = "";
		if(document.getElementById("user_state").value != "select") {
			if(document.getElementById("user_state").options[document.getElementById("user_state").selectedIndex].text.toLowerCase() == "other") {
				if(document.getElementById("user_district_other").value != "" && document.getElementById("user_city_other").value != "" && document.getElementById("user_taluka_other").value != "") {
					city = document.getElementById("user_city_other").value;
					taluka = document.getElementById("user_taluka_other").value;
					district = document.getElementById("user_district_other").value;
					state = "other";
				}
			} else {
				if(document.getElementById("user_city").value != "select" && document.getElementById("user_taluka").value != "select" && document.getElementById("user_district").value != "select") {
					city = document.getElementById("user_city").value;
					taluka = document.getElementById("user_taluka").value;
					district = document.getElementById("user_district").value;
					state = "gujarat";	
				} else {
					city = "N/A";
					taluka = "N/A";
					district = "N/A";
					state = "N/A";		
				}
			}
		} else {
			city = "N/A";
			taluka = "N/A";
			district = "N/A";
			state = "N/A";
		}
		
		$.ajax({
			method:'POST',
			url: '<%=RouteManager.getBasePath()%>profile?action=savePersonalProfile',
			beforeSend: function() {
				btn.innerHTML = "Please wait"; 
			},
			data: {
				'user_name': document.getElementById("user_fullname").value,
				'user_email': document.getElementById("user_email").value,
				'user_gender': $('input[name="user_gender"]:checked').val(),
				'user_birthyear': document.getElementById("user_birthyear").value,
				'user_state': state,
				'user_district': district,
				'user_taluka': taluka,
				'user_city': city,
				'user_secondarycontactno': document.getElementById("user_secondarycontactno").value,
			},
			success: function(data) {
				var temp = "";
				if(data == "success") {
					temp = '<div class="row" id="success-alert">' +
						'<div class="col-lg-12">' +
						'<div class="alert alert-success">' + 
							'Changes successfully saved.' +
						'</div>' + 
						'</div>' + 
						'</div>';
				} else if(data == "email_error") {
					temp = '<div class="row" id="error-alert">' +
					'<div class="col-lg-12">' +
					'<div class="alert alert-danger">' + 
						'Email ID you just entered is already registered. Please try another email ID.' +
					'</div>' + 
					'</div>' + 
					'</div>';
				} else {
					temp = '<div class="row" id="error-alert">' +
					'<div class="col-lg-12">' +
					'<div class="alert alert-danger">' + 
						'Something went wrong. Changes are not saved. Please try again.' +
					'</div>' + 
					'</div>' + 
					'</div>';
				}
				document.getElementById("changesResult").innerHTML = temp;
			},
			error: function(data) {
				var temp = '<div class="row" id="error-alert">' +
				'<div class="col-lg-12">' +
				'<div class="alert alert-danger">' + 
					'Something went wrong. Changes are not saved. Please try again.' +
				'</div>' + 
				'</div>' + 
				'</div>';
				document.getElementById("changesResult").innerHTML = temp;
			},
			complete: function() {
				btn.innerHTML = "Save Changes";
			}
		});
	}
	
	function currentChanged(check) {
	    $('input[name="' + check.name + '"]').not(check).prop('checked', false);
	}
	
	function saveQualifications(btn) {
		var quali_avail = 1;
		var qualifications = [];
		var exp_industry = [];
		var exp_position = [];
		var exp_years = [];
		var exp_salary = [];
		var exp_current = [];
		var expected_salary = 0;
		var exp_avail = 0;
		if($('#qualification-tags').has('.single-tag').length == 0) {
			quali_avail = 0;
		} else {
			quali_avail = 1;
			for(var i=1 ; i<q_tag_count ; i++) {
				if($("#user_qualification_hidden" + i).length != 0) {
					qualifications.push($("#user_qualification_hidden" + i).val());
				}
			}
		}
		
		if($('input[name=user_experience]:checked', '#profile_form').val() == 'y') {
			exp_avail = 1;
			for(var i=1 ; i<count ; i++) {
				if($("#user_industry_sector" + i).val() != "" && $("#user_post" + i).val() != "" && $("#user_year_exp" + i).val() != "") {
					exp_industry.push($("#user_industry_sector" + i).val());
					exp_position.push($("#user_post" + i).val());
					exp_years.push($("#user_year_exp" + i).val());
					exp_salary.push($("#user_exp_salary" + i).val() == "" ? 0 : $("#user_exp_salary" + i).val())
					exp_current.push($('input[name=current_job]:checked', '#profile_form').val() == "current_job" + i ? "1" : "0");
				}
			}
		} else {
			exp_avail = 0;
		}
		expected_salary = ($("#user_expected_salary").val() == "" ? 0 : $("#user_expected_salary").val());
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>profile?action=saveQualifications',
			method: "POST",
			beforeSend: function() {
				btn.innerHTML = "Please wait"; 
			},
			data: {
				"quali_avail": quali_avail,
				"qualifications" : qualifications,
				"exp_industry" : exp_industry,
				"exp_position" : exp_position,
				"exp_years" : exp_years,
				"exp_salary" : exp_salary,
				"exp_current" : exp_current,
				"exp_avail" : exp_avail,
				"expected_salary" : expected_salary,
			},
			success: function(data) {
				var temp = "";
				if(data == "success") {
					temp = '<div class="row" id="success-alert">' +
						'<div class="col-lg-12">' +
						'<div class="alert alert-success">' + 
							'Changes successfully saved.' +
						'</div>' + 
						'</div>' + 
						'</div>';
				} else {
					temp = '<div class="row" id="error-alert">' +
					'<div class="col-lg-12">' +
					'<div class="alert alert-danger">' + 
						'Something went wrong. Changes are not saved. Please try again.' +
					'</div>' + 
					'</div>' + 
					'</div>';
				}
				document.getElementById("changesResult").innerHTML = temp;
			},
			error: function(data) {
				var temp = '<div class="row" id="error-alert">' +
				'<div class="col-lg-12">' +
				'<div class="alert alert-danger">' + 
					'Something went wrong. Changes are not saved. Please try again.' +
				'</div>' + 
				'</div>' + 
				'</div>';
				document.getElementById("changesResult").innerHTML = temp;
			},
			complete: function() {
				btn.innerHTML = "Save Changes";
			}
		});
	}
	
	function saveInterests(btn) {
		var industry = [];
		var post = [];
		if($('#post-interest-tags').has('.single-tag').length != 0) {
			for(var i=1 ; i<tag_count ; i++) {
				if($("#interest-industry" + i).length != 0) {
					industry.push($("#interest-industry" + i).val());
					post.push($("#interest-post" + i).val());
				}
			}
			$.ajax({
				url: '<%=RouteManager.getBasePath()%>profile?action=saveInterests',
				method: 'POST',
				beforeSend: function() {
					btn.innerHTML = "Please wait"; 
				},
				data: {
					"industry": industry,
					"post": post,
				},
				success: function(data) {
					var temp = "";
					if(data == "success") {
						temp = '<div class="row" id="success-alert">' +
							'<div class="col-lg-12">' +
							'<div class="alert alert-success">' + 
								'Changes successfully saved.' +
							'</div>' + 
							'</div>' + 
							'</div>';
					} else {
						temp = '<div class="row" id="error-alert">' +
						'<div class="col-lg-12">' +
						'<div class="alert alert-danger">' + 
							'Something went wrong. Changes are not saved. Please try again.' +
						'</div>' + 
						'</div>' + 
						'</div>';
					}
					document.getElementById("changesResult").innerHTML = temp;
				},
				error: function(data) {
					var temp = '<div class="row" id="error-alert">' +
					'<div class="col-lg-12">' +
					'<div class="alert alert-danger">' + 
						'Something went wrong. Changes are not saved. Please try again.' +
					'</div>' + 
					'</div>' + 
					'</div>';
					document.getElementById("changesResult").innerHTML = temp;
				},
				complete: function() {
					btn.innerHTML = "Save Changes";
				}
			});
		} else {
			var temp = '<div class="row" id="error-alert">' +
			'<div class="col-lg-12">' +
			'<div class="alert alert-danger">' + 
				'Please enter atleast one interest area.' +
			'</div>' + 
			'</div>' + 
			'</div>';
			document.getElementById("changesResult").innerHTML = temp;
		}
	}
	
	function saveResume(btn) {
		var validExtensions = ['.pdf', '.doc', '.docx', '.txt', '.jpg', '.jpeg', '.png', '.bmp', '.gif'];
		var file = document.getElementById("user_resume").value;
		if(file.length <= 0) {
			//no file selected
			var temp = '<div class="row" id="error-alert">' +
			'<div class="col-lg-12">' +
			'<div class="alert alert-danger">' + 
				'Please select any valid resume file to upload.' +
			'</div>' + 
			'</div>' + 
			'</div>';
			document.getElementById("changesResult").innerHTML = temp;
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
				 //invalid file
				 var temp = '<div class="row" id="error-alert">' +
					'<div class="col-lg-12">' +
					'<div class="alert alert-danger">' + 
						'You have selected invalid file format. Please select any valid resume file to upload.' +
					'</div>' + 
					'</div>' + 
					'</div>';
					document.getElementById("changesResult").innerHTML = temp;
			 } else {
				 
				 //success
				 var formdata = new FormData();
				 formdata.append("resume", document.getElementById("user_resume").files[0]);
				 $.ajax({
					 url: '<%=RouteManager.getBasePath()%>profile?action=saveResume',
					 method: 'POST',
					 beforeSend: function() {
							btn.innerHTML = "Please wait"; 
					 },
					 data: formdata,
					 contentType: false,
					 cache: false,
				     processData: false,
					 success: function(data) {
						 var temp = "";
							if(data == "success") {
								temp = '<div class="row" id="success-alert">' +
									'<div class="col-lg-12">' +
									'<div class="alert alert-success">' + 
										'Changes successfully saved.' +
									'</div>' + 
									'</div>' + 
									'</div>';
							} else {
								temp = '<div class="row" id="error-alert">' +
								'<div class="col-lg-12">' +
								'<div class="alert alert-danger">' + 
									'Something went wrong. Changes are not saved. Please try again.' +
								'</div>' + 
								'</div>' + 
								'</div>';
							}
							document.getElementById("changesResult").innerHTML = temp;
					 },
					 error: function(data) {
						 var temp = '<div class="row" id="error-alert">' +
							'<div class="col-lg-12">' +
							'<div class="alert alert-danger">' + 
								'Something went wrong. Changes are not saved. Please try again.' +
							'</div>' + 
							'</div>' + 
							'</div>';
							document.getElementById("changesResult").innerHTML = temp;
					 },
					 complete: function() {
							btn.innerHTML = "Save Changes";
						}
				 });	
			 }
		}
	}
	
</script>
</body>
</html>