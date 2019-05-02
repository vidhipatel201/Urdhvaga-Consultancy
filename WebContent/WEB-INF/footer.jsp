<%@page import="utils.RouteManager" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="model.Uc_firm_details" %>
<%@ page import="database.Model" %>
<footer>
	<%
		Model model = new Model();
		Uc_firm_details ucFirm = new Uc_firm_details();
		ucFirm = (Uc_firm_details) model.selectData("model.Uc_firm_details", "1").get(0);
	%>
	<!-- Modal -->
<div id="whatsappModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">WhatsApp</h4>
      </div>
      <div class="modal-body">
      <table class="table table-striped">
        <tr><th>Number: </th><td><div id="whatsapp_number"><%=ucFirm.getFd_whatsapp() == 0 ? "N/A" : "+91 " + ucFirm.getFd_whatsapp()%></div></td></tr>
        <% if(!ucFirm.getFd_wa_grp_one().equals("N/A") && !ucFirm.getFd_wa_grp_two().equals("N/A")) { %>
        <tr><th>Group one:</th><td><a class="blue-link" href="<%=ucFirm.getFd_wa_grp_one() %>"><%=ucFirm.getFd_wa_grp_one() %></a></td></tr>
        <tr><th>Group two:</th><td><a class="blue-link" href="<%=ucFirm.getFd_wa_grp_two() %>"><%=ucFirm.getFd_wa_grp_two() %></a></td></tr>
        <% } %>
        <% if(ucFirm.getFd_wa_grp_one().equals("N/A") && !ucFirm.getFd_wa_grp_two().equals("N/A")) { %>
        <tr><th>Group:</th><td><a class="blue-link" href="<%=ucFirm.getFd_wa_grp_two() %>"><%=ucFirm.getFd_wa_grp_two() %></a></td></tr>
        <% } %>
        <% if(!ucFirm.getFd_wa_grp_one().equals("N/A") && ucFirm.getFd_wa_grp_two().equals("N/A")) { %>
        <tr><th>Group:</th><td><a class="blue-link" href="<%=ucFirm.getFd_wa_grp_one() %>"><%=ucFirm.getFd_wa_grp_one() %></a></td></tr>
        <% } %>
        </table>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>
		<div class="container">
			<div class="row">
				<div class="col-lg-2 footer-col">
					<img src="<%=RouteManager.getBasePath() %>image?fileName=<%=ucFirm.getFd_banner() %>" class="logo" />
				</div>
				<div class="col-lg-3 footer-col">
					<div class="row">
						<div class="col-lg-12 footer-heading">
							<%=ucFirm.getFd_name() %>
						</div>
						<div class="col-lg-12">
							<table>
								<tr>
									<td style="padding-bottom:8px"><i class="fa fa-angle-right footer-icon"></i></td>
									<td style="padding-bottom:8px"><a href="<%=RouteManager.getBasePath()%>">Home</a>
								</tr>
								<tr>
									<td style="padding-bottom:8px"><i class="fa fa-angle-right footer-icon"></i></td>
									<td style="padding-bottom:8px"><a href="<%=RouteManager.getBasePath()%>jobs">Browse Jobs</a>
								</tr>
								<tr>
									<td style="padding-bottom:8px"><i class="fa fa-angle-right footer-icon"></i></td>
									<td style="padding-bottom:8px"><a href="<%=RouteManager.getBasePath()%>about">About</a>
								</tr>
								<tr>
									<td style="padding-bottom:8px"><i class="fa fa-angle-right footer-icon"></i></td>
									<td style="padding-bottom:8px"><a href="<%=RouteManager.getBasePath()%>contact">Contact</a>
								</tr>
							</table>
							
						</div>
					</div>
				</div>
				<div class="col-lg-4 footer-col">
					<div class="col-lg-12 footer-heading">
						Contact
					</div>
					<div class="col-lg-12">
					
						<table>
						<tr>
							<td style="padding-bottom:8px"><i class="fa fa-map-marker footer-icon"></i></td>
							<td style="padding-bottom:8px"><%=ucFirm.getFd_address().replaceAll("\n","<br />") %></td>
						</tr>
						
							<% if(ucFirm.getFd_contact_one() != 0 && ucFirm.getFd_contact_two() != 0) { %>
							<tr title="Contact Number">
								<td style="padding-bottom:8px"><i class="fa fa-phone footer-icon"></i></td>
								<td style="padding-bottom:8px">+91 <%=ucFirm.getFd_contact_one() %><br>+91 <%=ucFirm.getFd_contact_two() %></td>
							</tr>
						<% } %>
						<% if(ucFirm.getFd_contact_one() != 0 && ucFirm.getFd_contact_two() == 0) { %>
							<tr title="Contact Number">
								<td style="padding-bottom:8px"><i class="fa fa-phone footer-icon"></i></td>
								<td style="padding-bottom:8px">+91 <%=ucFirm.getFd_contact_one() %></td>
							</tr>
						<% } %>
						<% if(ucFirm.getFd_contact_one() == 0 && ucFirm.getFd_contact_two() != 0) { %>
							<tr title="Contact Number">
								<td style="padding-bottom:8px"><i class="fa fa-phone footer-icon"></i></td>
								<td style="padding-bottom:8px">+91 <%=ucFirm.getFd_contact_two() %></td>
							</tr>
						<% } %>
						<% if(!ucFirm.getFd_email_one().equals("N/A") && !ucFirm.getFd_email_two().equals("N/A")) { %>
							<tr title="Email-Id">
								<td style="padding-bottom:8px"><i class="fa fa-envelope-o footer-icon"></i></td>
								<td style="padding-bottom:8px"><a href="mailto:<%=ucFirm.getFd_email_one() %>"><%=ucFirm.getFd_email_one() %></a><br><a href="mailto:<%=ucFirm.getFd_email_two() %>"><%=ucFirm.getFd_email_two() %></a></td>
							</tr>
						<% } %>
						<% if(!ucFirm.getFd_email_one().equals("N/A") && ucFirm.getFd_email_two().equals("N/A")) { %>
							<tr title="Email-Id">
								<td style="padding-bottom:8px"><i class="fa fa-envelope-o footer-icon"></i></td>
								<td style="padding-bottom:8px"><a href="mailto:<%=ucFirm.getFd_email_one() %>"><%=ucFirm.getFd_email_one() %></a></td>
							</tr>
						<% } %>
						<% if(ucFirm.getFd_email_one().equals("N/A") && !ucFirm.getFd_email_two().equals("N/A")) { %>
							<tr title="Email-Id">
								<td style="padding-bottom:8px"><i class="fa fa-envelope-o footer-icon"></i></td>
								<td style="padding-bottom:8px"><a href="mailto:<%=ucFirm.getFd_email_two() %>"><%=ucFirm.getFd_email_two() %></a></td>
							</tr>
						<% } %>
						</table>
					
					</div>
				</div>
				<div class="col-lg-3 footer-col">
					<div class="col-lg-12 footer-heading">
						Connect Socially
					</div>
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
	</footer>
		<script src="<%=RouteManager.getBasePath() %>assets/js/jquery-3.1.1.min.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<script src="<%=RouteManager.getBasePath() %>assets/bootstrap/js/bootstrap.min.js"></script>
		<script src="<%=RouteManager.getBasePath() %>assets/js/bootstrap-datepicker.js"></script>
		
		 
		<script>
	

$(document).ready(function() {
	$("#<%=request.getParameter("pageId")%>").addClass("active");
	
	$('.navbar').affix({
	    offset: $('.navbar').position()
		});
		
		// add padding or margin when element is affixed
	$(".navbar").on("affix.bs.affix", function() {
	  return $("#wrapper").addClass("padded");
	});

	// remove it when unaffixed
	$(".navbar").on("affix-top.bs.affix", function() {
	  return $("#wrapper").removeClass("padded");
	});
});
		</script>