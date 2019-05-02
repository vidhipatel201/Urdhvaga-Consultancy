<%@page import="utils.RouteManager" %>
<%@page import="utils.ViewManager" %>
<%@page import="utils.SessionManager" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="model.Uc_firm_details" %>
<%@ page import="database.Model" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
		Model model = new Model();
		Uc_firm_details ucFirm = new Uc_firm_details();
		ucFirm = (Uc_firm_details) model.selectData("model.Uc_firm_details", "1").get(0);
%>
<html>
	<head>
		<title><%= request.getParameter("title") %></title>
		 <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <!-- Google Analytics -->
<script>
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

ga('create', 'UA-103072917-1', 'auto');
ga('send', 'pageview');
</script>
<!-- End Google Analytics -->
  
		<link rel="shortcut icon" href="<%=RouteManager.getBasePath() %>image?fileName=<%=ucFirm.getFd_logo() %>" type="image/x-icon">
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>assets/bootstrap/css/bootstrap.css" />
		<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>assets/font-awesome/css/font-awesome.min.css" />
		<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>assets/css/style.css" />
		<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>assets/css/bootstrap-datepicker.css" />
		<link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Sansita" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Rubik" rel="stylesheet">
	</head>
	<body>
	<%
	Hashtable sessionAttributes = new Hashtable();
	sessionAttributes = SessionManager.sessionExists(request, "uc_client_session") == null ? null : (Hashtable)SessionManager.sessionExists(request, "uc_client_session");
	%>
	
	
	
	<header>
	<% if(sessionAttributes != null && sessionAttributes.get("payment").toString().equals("0")) { %>
	<div class="payment-notification">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					You are not registered yet! Please make payment to complete your registration. (<a class="blue-link" href="<%=RouteManager.getBasePath() %>bank-details">Bank Details</a>)
				</div>
			</div>
		</div>
	</div>
	<% } %>
	<div class="full-width">
		<div class="row top-header">
			<div class="container">
				<div class="row">
					<div class="col-lg-12">
						<span style="margin-right:10px"><i class="fa fa-phone" style="color:#FFF200"> <span style="color:white">+91 <%=ucFirm.getFd_contact_one() %></span></i></span>
						<span><i class="fa fa-envelope mail" style="color:#FFF200"><a href="mailto:<%=ucFirm.getFd_email_one()%>"> <%=ucFirm.getFd_email_one() %></a><i class="fa fa-send mail-fly"></i></i></span>
					</div>
				</div>
			</div>
		</div>
		</div>
		<div class="mid-header">
	<div class="container">
		<div class="row"">
		<div class="col-lg-3 col-centered">
			<img src="<%=RouteManager.getBasePath() %>image?fileName=<%=ucFirm.getFd_logo() %>" class="logo" />
		</div>
		</div>
		</div>
		</div>
		<nav class="navbar navbar-custom navbar-inverse" data-spy="affix">
  <div class="container">
  <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>                        
      </button>
      <a class="navbar-brand hidden-lg" href="#"><%=ucFirm.getFd_name() %></a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
  
      <ul class="nav navbar-nav">
        <li id="pg1"><a href="<%=RouteManager.getBasePath()%>">Home</a></li>
        <li id="pg7"><a href="<%=RouteManager.getBasePath()%>jobs">Browse Jobs</a></li>
        <li id="pg5"><a href="<%=RouteManager.getBasePath()%>about">About</a></li>
        <li id="pg6"><a href="<%=RouteManager.getBasePath()%>contact">Contact Us</a></li>
          <% if(sessionAttributes != null) { %>
      	  	<li id="pg2"><a href="<%=RouteManager.getBasePath()%>profile">Profile</a></li>
      	  	<li id="pg3"><a href="<%=RouteManager.getBasePath()%>applied">Applied Jobs</a></li>
    	  <% } %>
       
      </ul>
      <ul class="nav navbar-nav navbar-right">
      <% if(sessionAttributes != null) { %>
      		<li id="pg4" class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-user"></span> &nbsp; <%=sessionAttributes.get("name") %>
        <span class="caret"></span></a>
        <ul class="dropdown-menu">
          <li><a href="<%=RouteManager.getBasePath() %>profile?action=change_password">Change Password</a></li>
          <li><a href="<%=RouteManager.getBasePath() %>user-accounts?action=logout">Logout</a></li>
        </ul>
      </li>
      	  
      <% } else { %>
      		  <li id="pg4"><a href="<%=RouteManager.getBasePath() %>user-accounts"><span class="glyphicon glyphicon-user"></span> &nbsp; Sign In / Sign Up</a></li>
      <% } %>
        
      </ul>
    </div>
  </div>
</nav>
</header>