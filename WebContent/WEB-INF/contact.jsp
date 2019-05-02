<%@page import="model.Uc_firm_details" %>
<%@page import="utils.RouteManager" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Contact Us - Urdhvaga Consultancy"/>
</jsp:include>

<div id="wrapper">
<%
	Uc_firm_details ucFirm = new Uc_firm_details();
	ucFirm = (Uc_firm_details) request.getAttribute("ucFirm");
%>

	<div class="container top-btm-pad">
		<div class="row">
			<div class="col-lg-7">
				<div class="row">
					<div class="col-lg-12">
						<h3 class="page-title">
							<i class="fa fa-map-o title-icon"></i> Locate Us
						</h3>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<%=ucFirm.getFd_map_url() %>
					</div>
				</div>
			</div>
			<div class="col-lg-1"></div>
			<div class="col-lg-4">
				<div class="row">
					<div class="col-lg-12">
						<h3 class="page-title">
							<i class="fa fa-map-marker title-icon"></i> Address
						</h3>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<%=ucFirm.getFd_address().replaceAll("\n","<br />") %>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<h3 class="page-title">
							<i class="fa fa-vcard-o title-icon"></i> Contact
						</h3>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<table>
						<% if(ucFirm.getFd_contact_one() != 0 && ucFirm.getFd_contact_two() != 0) { %>
							<tr title="Contact Number">
								<td><i class="fa fa-phone contact-icon"></i></td>
								<td>+91 <%=ucFirm.getFd_contact_one() %>, +91 <%=ucFirm.getFd_contact_two() %></td>
							</tr>
						<% } %>
						<% if(ucFirm.getFd_contact_one() != 0 && ucFirm.getFd_contact_two() == 0) { %>
							<tr title="Contact Number">
								<td><i class="fa fa-phone contact-icon"></i></td>
								<td>+91 <%=ucFirm.getFd_contact_one() %></td>
							</tr>
						<% } %>
						<% if(ucFirm.getFd_contact_one() == 0 && ucFirm.getFd_contact_two() != 0) { %>
							<tr title="Contact Number">
								<td><i class="fa fa-phone contact-icon"></i></td>
								<td>+91 <%=ucFirm.getFd_contact_two() %></td>
							</tr>
						<% } %>
						
						<% if(!ucFirm.getFd_email_one().equals("N/A") && !ucFirm.getFd_email_two().equals("N/A")) { %>
							<tr title="Email-Id">
								<td><i class="fa fa-envelope-o contact-icon"></i></td>
								<td><a class="blue-link" href="mailto:<%=ucFirm.getFd_email_one() %>"><%=ucFirm.getFd_email_one() %></a>, <a class="blue-link" href="mailto:<%=ucFirm.getFd_email_two() %>"><%=ucFirm.getFd_email_two() %></a></td>
							</tr>
						<% } %>
						<% if(!ucFirm.getFd_email_one().equals("N/A") && ucFirm.getFd_email_two().equals("N/A")) { %>
							<tr title="Email-Id">
								<td><i class="fa fa-envelope-o contact-icon"></i></td>
								<td><a class="blue-link" href="mailto:<%=ucFirm.getFd_email_one() %>"><%=ucFirm.getFd_email_one() %></a></td>
							</tr>
						<% } %>
						<% if(ucFirm.getFd_email_one().equals("N/A") && !ucFirm.getFd_email_two().equals("N/A")) { %>
							<tr title="Email-Id">
								<td><i class="fa fa-envelope-o contact-icon"></i></td>
								<td><a class="blue-link" href="mailto:<%=ucFirm.getFd_email_two() %>"><%=ucFirm.getFd_email_two() %></a></td>
							</tr>
						<% } %>
						</table>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<h3 class="page-title">
							<i class="fa fa-link title-icon"></i> Connect Socially
						</h3>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<% if(!ucFirm.getFd_facebook().equals("N/A")) { %>
							<a href="<%=ucFirm.getFd_facebook()%>" title="Facebook" target="_blank"><img class="social-icon" src="<%=RouteManager.getBasePath() %>assets/img/facebook.png"></a>
						<% } %>
						<% if(!ucFirm.getFd_instagram().equals("N/A")) { %>
							<a href="<%=ucFirm.getFd_instagram()%>" title="Instagram" target="_blank"><img class="social-icon" src="<%=RouteManager.getBasePath() %>assets/img/instagram.png"></a>
						<% } %>
						<% if(!ucFirm.getFd_linkedin().equals("N/A")) { %>
							<a href="<%=ucFirm.getFd_linkedin()%>" title="LinkedIn" target="_blank"><img class="social-icon" src="<%=RouteManager.getBasePath() %>assets/img/linkedin.png"></a>
						<% } %>
						<% if(!ucFirm.getFd_gplus().equals("N/A")) { %>
							<a href="<%=ucFirm.getFd_gplus()%>" title="Google-Plus" target="_blank"><img class="social-icon" src="<%=RouteManager.getBasePath() %>assets/img/google-plus.png"></a>
						<% } %>
						<% if(ucFirm.getFd_whatsapp() != 0) { %>
							<a data-toggle="modal" data-target="#whatsappModal" style="cursor:pointer" title="WhatsApp"><img class="social-icon" src="<%=RouteManager.getBasePath() %>assets/img/whatsapp.png"></a>
						<% } %>
						<% if(!ucFirm.getFd_twitter().equals("N/A")) { %>
							<a href="<%=ucFirm.getFd_twitter()%>" title="Twitter" target="_blank"><img class="social-icon" src="<%=RouteManager.getBasePath() %>assets/img/twitter.png"></a>
						<% } %>
						
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg6"/>
</jsp:include>
