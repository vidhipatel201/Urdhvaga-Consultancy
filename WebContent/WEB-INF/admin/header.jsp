<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Hashtable" %>
<%@ page import="utils.SessionManager" %>
<%@page import="utils.RouteManager" %>
<%@ page import="controller.UCConstants" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" href="<%=RouteManager.getBasePath() %>assets/img/uclogo.png" type="image/x-icon">
<title><%= request.getParameter("title") %></title>

<link href="<%=RouteManager.getBasePath() %>admin/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>assets/font-awesome/css/font-awesome.min.css" />
<link href="<%=RouteManager.getBasePath() %>admin/css/datepicker3.css" rel="stylesheet">
<link href="<%=RouteManager.getBasePath() %>admin/css/styles.css" rel="stylesheet">
<link href="https://cdn.datatables.net/1.10.13/css/dataTables.bootstrap.min.css" rel=stylesheet" />
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<!--Icons-->
<script src="<%=RouteManager.getBasePath() %>admin/js/lumino.glyphs.js"></script>
<script src="<%=RouteManager.getBasePath() %>admin/js/headerManager.js"></script>

<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->

</head>

<body>
<%
	Hashtable sessionAttributes = new Hashtable();
	sessionAttributes = (Hashtable) SessionManager.sessionExists(request, "admin");
%>
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#sidebar-collapse">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#"><span>Urdhvaga</span>Consultancy</a>
				<ul class="user-menu">
					<li class="dropdown pull-right">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown"><svg class="glyph stroked male-user"><use xlink:href="#stroked-male-user"></use></svg> <%=sessionAttributes.get("uusername") %> <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							
							<li><a href="<%=RouteManager.getBasePath() %>AdminUserAccounts?action=changePassword"><svg class="glyph stroked gear"><use xlink:href="#stroked-gear"></use></svg> Change Password</a></li>
							<li><a href="<%=RouteManager.getBasePath() %>AdminUserAccounts?action=logout"><svg class="glyph stroked cancel"><use xlink:href="#stroked-cancel"></use></svg> Logout</a></li>
						</ul>
					</li>
				</ul>
			</div>
							
		</div><!-- /.container-fluid -->
	</nav>
		
	<div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">
		<form role="search">
			<div class="form-group">
				<input type="text" class="form-control" placeholder="Search">
			</div>
		</form>
		<% if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.MAIN_ADMIN) { %>
		<ul class="nav menu">
			<li id="pg1"><a href="<%=RouteManager.getBasePath()%>admin/home"><i class="fa fa-home"></i> Home</a></li>
			<li id="pg4"><a href="<%=RouteManager.getBasePath()%>admin/admin-manager"><i class="fa fa-address-book-o"></i> Admins</a></li>
			<li id="pg1"><a href="<%=RouteManager.getBasePath()%>admin/manage-users"><i class="fa fa-user"></i> Registered Candidates</a></li>
			<li id="pg2" class="parent " data-toggle="collapse" href="#sub-item-1"><a href="#"><i class="fa fa-suitcase"></i> Jobs</a></li>
			<li><ul class="children collapse" id="sub-item-1">
					<li>
						<a href="<%=RouteManager.getBasePath()%>admin/manage-job-applications">
							<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> All Job Applications
						</a>
					</li>
					<li>
						<a class="" href="<%=RouteManager.getBasePath()%>admin/current-openings?action=add">
							<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> Add Current Openings
						</a>
					</li>
					<li>
						<a class="" href="<%=RouteManager.getBasePath()%>admin/current-openings?action=modify">
							<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> Modify Current Openings
						</a>
					</li>
				</ul></li>
			
			<li id="pg3" class="parent " data-toggle="collapse" href="#sub-item-2"><a href="#"><i class="fa fa-address-book-o"></i> Employers</a></li>
			<li><ul class="children collapse" id="sub-item-2">
					<li>
						<a href="<%=RouteManager.getBasePath()%>admin/employer?action=add">
							<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> Add Employer
						</a>
					</li>
					<li>
						<a class="" href="<%=RouteManager.getBasePath()%>admin/employer?action=modify">
							<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> Modify Employer
						</a>
					</li>
				</ul></li>
				<li id="pg5"><a href="<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=telecallerApprovedApplications"><i class="fa fa-suitcase"></i> Telecaller Approved Job</a></li>
				<li id="pg6"><a href="<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=salesApprovedApplications"><i class="fa fa-suitcase"></i> Sales Approved Job</a></li>
				<li id="pg7"><a href="<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=shortlistCandidates"><i class="fa fa-suitcase"></i> Shortlist candidates</a></li>
				<li id="pg12"><a href="<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=candidateAppliedJobs"><i class="fa fa-suitcase"></i> Jobs Applied by Candidates</a></li>
				<li id="pg8"><a href="<%=RouteManager.getBasePath()%>admin/interviews"><i class="fa fa-suitcase"></i> Scheduled Interviews</a></li>
				<li id="pg9"><a href="<%=RouteManager.getBasePath()%>admin/firm"><i class="fa fa-suitcase"></i> Firm Details</a></li>
				<li id="pg11" class="parent " data-toggle="collapse" href="#sub-item-11"><a href="#"><i class="fa fa-address-book-o"></i> Terms & Conditions</a></li>
				<li><ul class="children collapse" id="sub-item-11">
					<li>
						<a href="<%=RouteManager.getBasePath()%>admin/terms?action=add">
							<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> Add new T&C
						</a>
					</li>
					<li>
						<a class="" href="<%=RouteManager.getBasePath()%>admin/terms?action=modify">
							<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> Modify T&C
						</a>
					</li>
				</ul></li>
		</ul>
		<% } %>
		<% if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.SALES_ADMIN) { %>
		<ul class="nav menu">
			<li id="pg1"><a href="<%=RouteManager.getBasePath()%>admin/home"><i class="fa fa-home"></i> Home</a></li>
			
			
			<li id="pg2" class="parent " data-toggle="collapse" href="#sub-item-1"><a href="#"><i class="fa fa-suitcase"></i> Jobs</a></li>
			<li><ul class="children collapse" id="sub-item-1">
					<li>
						<a class="" href="<%=RouteManager.getBasePath()%>admin/current-openings?action=add">
							<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> Add Current Openings
						</a>
					</li>
					<li>
						<a class="" href="<%=RouteManager.getBasePath()%>admin/current-openings?action=modify">
							<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> Modify Current Openings
						</a>
					</li>
				</ul></li>
			
			<li id="pg3" class="parent " data-toggle="collapse" href="#sub-item-2"><a href="#"><i class="fa fa-address-book-o"></i> Employers</a></li>
			<li><ul class="children collapse" id="sub-item-2">
					<li>
						<a href="<%=RouteManager.getBasePath()%>admin/employer?action=add">
							<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> Add Employer
						</a>
					</li>
					<li>
						<a class="" href="<%=RouteManager.getBasePath()%>admin/employer?action=modify">
							<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> Modify Employer
						</a>
					</li>
				</ul></li>
				
				<li id="pg5"><a href="<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=telecallerApprovedApplications"><i class="fa fa-suitcase"></i> Approved Job</a></li>
				<li id="pg8"><a href="<%=RouteManager.getBasePath()%>admin/interviews"><i class="fa fa-suitcase"></i> Scheduled Interviews</a></li>
		</ul>
		<% } %>
		<% if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.TELECALLER_ADMIN) { %>
		<ul class="nav menu">
			<li id="pg1"><a href="<%=RouteManager.getBasePath()%>admin/home"><i class="fa fa-home"></i> Home</a></li>
			<li id="pg1"><a href="<%=RouteManager.getBasePath()%>admin/manage-users"><i class="fa fa-user"></i> Registered Candidates</a></li>
			<li id="pg7"><a href="<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=shortlistCandidates"><i class="fa fa-suitcase"></i> Shortlist candidates</a></li>
			<li id="pg2"><a href="<%=RouteManager.getBasePath()%>admin/manage-job-applications"><i class="fa fa-home"></i> Job Applications</a></li>
			<li id="pg6"><a href="<%=RouteManager.getBasePath()%>admin/manage-job-applications?action=salesApprovedApplications"><i class="fa fa-suitcase"></i> Sales Approved Job</a></li>
			<li id="pg8"><a href="<%=RouteManager.getBasePath()%>admin/interviews"><i class="fa fa-suitcase"></i> Scheduled Interviews</a></li>
		</ul>
		<% } %>
		<% if(Integer.parseInt(sessionAttributes.get("utype").toString()) == UCConstants.ADD_REQUIREMENT_ADMIN) { %>
			<ul class="nav menu">
			<li id="pg2" class="parent " data-toggle="collapse" href="#sub-item-1"><a href="#"><i class="fa fa-suitcase"></i> Jobs</a></li>
			<li><ul class="children collapse" id="sub-item-1">
				<li>
					<a class="" href="<%=RouteManager.getBasePath()%>admin/current-openings?action=add">
						<svg class="glyph stroked chevron-right"><use xlink:href="#stroked-chevron-right"></use></svg> Add Current Openings
					</a>
				</li>
			</li>
			</ul>
					
			</li>
			</ul>
		<% } %>

	</div><!--/.sidebar-->
	<script>
	
	</script>