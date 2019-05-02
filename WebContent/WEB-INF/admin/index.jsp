<%@page import="utils.RouteManager" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login - Urdhvaga Consultancy</title>

<link href="<%=RouteManager.getBasePath() %>admin/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=RouteManager.getBasePath() %>admin/css/datepicker3.css" rel="stylesheet">
<link href="<%=RouteManager.getBasePath() %>admin/css/styles.css" rel="stylesheet">

<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->

</head>

<body>
	<div class="container">
	<div class="row">
		<div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 col-md-4 col-md-offset-4">
			<div class="login-panel panel panel-default">
				<% if(Integer.parseInt(request.getAttribute("registered").toString()) == 1) { %>
				
				<div class="panel-heading">Login</div>
				<div class="panel-body">
					<form role="form" action="<%=RouteManager.getBasePath() %>AdminUserAccounts?action=login" method="POST">
						<fieldset>
							<div class="form-group">
								<input class="form-control" placeholder="Username" name="username_login" type="text" autofocus="">
							</div>
							<div class="form-group">
								<input class="form-control" placeholder="Password" name="password_login" type="password" value="">
							</div>
							<div class="checkbox">
								<label>
									<input name="remember" type="checkbox" value="Remember Me">Remember Me
								</label>
							</div>
							<input type="submit" value="Login" class="btn btn-primary">
							<% if(session.getAttribute("login_error") != null) { %>
							<div class="form-group">
								<div class="alert alert-danger">
  									<%= session.getAttribute("login_error").toString() %>
  									<% session.removeAttribute("login_error"); %>
								</div>
							</div>
							<% } %>
							<% if(session.getAttribute("login_success") != null) { %>
							<div class="form-group">
								<div class="alert alert-success">
  									<%= session.getAttribute("login_success").toString() %>
  									<% session.removeAttribute("login_success"); %>
								</div>
							</div>
							<% } %>
						</fieldset>
					</form>
				</div>
				
				<% } else { %>
				
				<div class="panel-heading">Create Account</div>
				<div class="panel-body">
					<form role="form" action="<%=RouteManager.getBasePath() %>AdminUserAccounts?action=signup" method="POST" onSubmit="event.preventDefault(); verify_password(this);">
						<fieldset>
							<div class="form-group">
								<input class="form-control" placeholder="Username" name="username_signup" type="text" autofocus="">
							</div>
							<div class="form-group">
								<input class="form-control" placeholder="Password" id="password" name="password_signup" type="password" value="">
							</div>
							<div class="form-group">
								<input class="form-control" placeholder="Confirm Password" id="confirm_password" type="password" value="">
							</div>
							<input type="submit" value="Sign Up" class="btn btn-primary">
							<% if(session.getAttribute("signup_error") != null) { %>
							<div class="form-group">
								<div class="alert alert-danger">
  									<%= session.getAttribute("signup_error").toString() %>
  									<% session.removeAttribute("signup_error"); %>
								</div>
							</div>
							<% } %>
							<% if(session.getAttribute("signup_success") != null) { %>
							<div class="form-group">
								<div class="alert alert-success">
  									<%= session.getAttribute("signup_success").toString() %>
  									<% session.removeAttribute("signup_success"); %>
								</div>
							</div>
							<% } %>
							<div class="form-group" id="pass_error">
								<div class="alert alert-danger">
									Password & confirm password mismatch.
								</div>
							</div>
						</fieldset>
					</form>
				</div>
				
				<% } %>
				
			</div>
		</div><!-- /.col-->
	</div><!-- /.row -->	
	</div>
	
		

	<script src="<%=RouteManager.getBasePath() %>admin/js/jquery-1.11.1.min.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/bootstrap.min.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/chart.min.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/chart-data.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/easypiechart.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/easypiechart-data.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/bootstrap-datepicker.js"></script>
	<script>
		!function ($) {
			$(document).on("click","ul.nav li.parent > a > span.icon", function(){		  
				$(this).find('em:first').toggleClass("glyphicon-minus");	  
			}); 
			$(".sidebar span.icon").find('em:first').addClass("glyphicon-plus");
		}(window.jQuery);

		$(window).on('resize', function () {
		  if ($(window).width() > 768) $('#sidebar-collapse').collapse('show')
		})
		$(window).on('resize', function () {
		  if ($(window).width() <= 767) $('#sidebar-collapse').collapse('hide')
		})
	</script>
	<script>
	$(document).ready(function(){
		$("#pass_error").hide();
	});
		function verify_password(form) {
			var pass = document.getElementById("password").value;
			var confirm_pass = document.getElementById("confirm_password").value;
			if(pass == confirm_pass) {
				$("#pass_error").hide();
				form.submit();
			}
			else {
				$("#pass_error").show('slow');
			}
		}
	</script>	
</body>

</html>
