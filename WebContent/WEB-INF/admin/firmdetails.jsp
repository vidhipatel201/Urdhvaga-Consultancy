<%@page import="model.Uc_firm_details" %>
<%@page import="utils.RouteManager" %>
<%@ page import="utils.SessionManager" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Firm Details - Urdhvaga Consultancy"/>
</jsp:include>

<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
	
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info home_boxes">
				<div class="panel-heading">Modify Firm Details
				</div>
  				<div class="panel-body">
  					<div class="row">
  					<div class="col-lg-12">
  					
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
			<br>
  					
  					<% Uc_firm_details ucFirm = new Uc_firm_details();
  						ucFirm = (Uc_firm_details) request.getAttribute("firm");
  					%>
  						<form enctype="multipart/form-data" method="POST" action="<%=RouteManager.getBasePath()%>admin/firm?action=save">
  							<div class="form-group">
  								Firm Name:
  								<input type="text" class="form-control" id="firm_name" name="firm_name" value="<%=ucFirm.getFd_name()%>">
  							</div>
  							<div class="form-group">
  								Contact Number (One):
  								<input type="number" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" maxlength="10" class="form-control" id="firm_contact_one" name="firm_contact_one" value="<%=ucFirm.getFd_contact_one()%>">
  							</div>
  							<div class="form-group">
  								Contact Number (Two):
  								<input type="number" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" maxlength="10" class="form-control" id="firm_contact_two" name="firm_contact_two" value="<%=ucFirm.getFd_contact_two()%>">
  							</div>
  							<div class="form-group">
  								Firm Email (One):
  								<input type="text" class="form-control" id="firm_email_one" name="firm_email_one" value="<%=ucFirm.getFd_email_one()%>">
  							</div>
  							<div class="form-group">
  								Firm Email (Two):
  								<input type="text" class="form-control" id="firm_email_two" name="firm_email_two" value="<%=ucFirm.getFd_email_two()%>">
  							</div>
  							<div class="form-group">
  								Main Logo:
  								<% if(ucFirm.getFd_logo().toString().equals("N/A")) { %>
  									<label>No Logo uploaded!</label>
  								<% } else { %>
  									<img src="<%=RouteManager.getBasePath() %>admin/firm?action=img&fileName=<%=request.getAttribute("logoUrl") %>" style="height:100px">
  								<% } %>
  								<input type="file" class="form-control" name="firm_logo" id="firm_logo">
  							</div>
  							<div class="form-group">
  								Footer Logo:
  								<% if(ucFirm.getFd_banner().toString().equals("N/A")) { %>
  									<label>No Footer Logo uploaded!</label>
  								<% } else { %>
  									<img src="<%=RouteManager.getBasePath() %>admin/firm?action=img&fileName=<%=request.getAttribute("bannerUrl") %>" style="height:100px">
  								<% } %>
  								<input type="file" class="form-control" name="firm_banner" id="firm_banner">
  							</div>
  							<div class="form-group">
  								Slogan:
  								<input type="text" class="form-control" id="firm_slogan" name="firm_slogan" value="<%=ucFirm.getFd_slogan()%>">
  							</div>
  							<div class="form-group">
  								Firm Address:
  								<textarea rows="4" class="form-control" id="firm_address" name="firm_address"><%=ucFirm.getFd_address() %></textarea>
  							</div>
  							<div class="form-group">
  								About:
  								<textarea rows="4" maxlength="1000" class="form-control" id="firm_about" name="firm_about"><%=ucFirm.getFd_about() %></textarea>
  							</div>
  							<div class="form-group">
  								Bank Account Name:
  								<input type="text" class="form-control" id="firm_bank_acc_name" name="firm_bank_acc_name" value="<%=ucFirm.getFd_bank_acc_name()%>">
  							</div>
  							<div class="form-group">
  								Bank Account Number:
  								<input type="text" class="form-control" id="firm_bank_acc_no" name="firm_bank_acc_no" value="<%=ucFirm.getFd_bank_acc_no()%>">
  							</div>
  							<div class="form-group">
  								Bank IFSC Code:
  								<input type="text" class="form-control" id="firm_bank_ifsc" name="firm_bank_ifsc" value="<%=ucFirm.getFd_bank_ifsc()%>">
  							</div>
  							<div class="form-group">
  								Bank Branch Location:
  								<input type="text" class="form-control" id="firm_bank_branch" name="firm_bank_branch" value="<%=ucFirm.getFd_bank_branch()%>">
  							</div>
  							<div class="form-group">
  								Facebook Url:
  								<input type="text" class="form-control" id="firm_facebook" name="firm_facebook" value="<%=ucFirm.getFd_facebook()%>">
  							</div>
  							<div class="form-group">
  								Twitter Url:
  								<input type="text" class="form-control" id="firm_twitter" name="firm_twitter" value="<%=ucFirm.getFd_twitter()%>">
  							</div>
  							<div class="form-group">
  								WhatsApp Number:
  								<input type="number" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" maxlength="10" class="form-control" id="firm_whatsapp" name="firm_whatsapp" value="<%=ucFirm.getFd_whatsapp()%>">
  							</div>
  							<div class="form-group">
  								WhatsApp Group One:
  								<input type="text" class="form-control" id="firm_whatsapp_grp_one" name="firm_whatsapp_grp_one" value="<%=ucFirm.getFd_wa_grp_one()%>">
  							</div>
  							<div class="form-group">
  								WhatsApp Group Two:
  								<input type="text" class="form-control" id="firm_whatsapp_grp_two" name="firm_whatsapp_grp_two" value="<%=ucFirm.getFd_wa_grp_two()%>">
  							</div>
  							<div class="form-group">
  								Google Plus Url:
  								<input type="text" class="form-control" id="firm_gplus" name="firm_gplus" value="<%=ucFirm.getFd_gplus()%>">
  							</div>
  							<div class="form-group">
  								LinkedIn Url:
  								<input type="text" class="form-control" id="firm_linkedin" name="firm_linkedin" value="<%=ucFirm.getFd_linkedin()%>">
  							</div>
  							<div class="form-group">
  								Instagram Url:
  								<input type="text" class="form-control" id="firm_instagram" name="firm_instagram" value="<%=ucFirm.getFd_instagram()%>">
  							</div>
  							<div class="form-group">
  								Google Map Url:
  								<textarea rows="4" class="form-control" id="firm_map" name="firm_map"><%=ucFirm.getFd_map_url() %></textarea>
  							</div>
  							<hr>
  							<div class="form-group">
  								Email ID to send emails:
  								<input type="email" class="form-control" id="firm_send_email" name="firm_send_email" value="<%=ucFirm.getFd_send_email()%>">
  							</div>
  							<div class="form-group">
  								Password:
  								<input type="password" class="form-control" id="firm_send_email_pass" name="firm_send_email_pass">
  							</div>
  							<div class="form-group">
  								Email host:
  								<input type="text" class="form-control" id="firm_email_host" name="firm_email_host" value="<%=ucFirm.getFd_email_host()%>">
  							</div>
  							<div class="form-group">
  								Email port:
  								<input type="number" class="form-control" id="firm_email_port" name="firm_email_port" value="<%=ucFirm.getFd_email_port()%>">
  							</div>
  							<div class="form-group">
  								<input type="submit" class="btn btn-primary" id="saveChanges" name="saveChanges" value="Save Changes">
  							</div>
  						</form>
  					</div>
  					
  				</div>
  				</div>
			</div>
		</div>
	</div>
	</div>


<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg9"/>
</jsp:include>

</body>
</html>