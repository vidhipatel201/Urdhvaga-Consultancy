<%@page import="model.Uc_current_jobs" %>
<%@page import="utils.RouteManager" %>
<%@page import="java.util.List" %>
<%@ page import="model.Uc_firm_details" %>
<%@ page import="database.Model" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Urdhvaga Consultancy"/>
</jsp:include>

<div id="wrapper">

<!-- Modal -->
<div id="jobModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Job Details</h4>
      </div>
      <div class="modal-body">
        
        <div id="jobDetails"><b>Please wait while job details are loading!</b></div>
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn mybtn" onclick="showTC()" data-dismiss="modal">Apply</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>

<!-- Modal -->
<div id="termsModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Terms & Conditions</h4>
      </div>
      <div class="modal-body">
        <input type="hidden" id="jobId">
        <div id="termsAndConditions"><b>Please wait while terms & conditions are loading!</b></div>
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn mybtn" onclick="applyJob()" data-dismiss="modal">Accept T&C and Apply</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>


<%
		Model model = new Model();
		Uc_firm_details ucFirm = new Uc_firm_details();
		ucFirm = (Uc_firm_details) model.selectData("model.Uc_firm_details", "1").get(0);
%>

	<div class="full-width home-slogan">
		<div class="row" style="">
			<div class="col-lg-offset-3 col-lg-6 slogan">
			<div style="text-align:left"><i class="fa fa-quote-left" style="color:black;"></i></div>
			<div style="text-align:center"><%=ucFirm.getFd_slogan() %></div>
			<div style="text-align:right"><i class="fa fa-quote-right" style="color:black;"></i></div>
			</div>
		</div>
	</div>
	
	
	<div class="container top-btm-pad">
		<div class="row">
			<div class="col-lg-12">
				<h3 class="page-title">
					<i class="fa fa-search title-icon"></i> Find Your Dream Job
				</h3>
			</div>
		</div>
		<div class="row">
		<form id="search_form">
			<div class="col col-lg-4 col-lg-offset-2 search-position" style="">
   				 <input type="text" id="search_position" onkeyup="disableError('search_position');disableError('search_location');" onkeydown = "if (event.keyCode == 13) document.getElementById('btnSearch').click()" name="position" class="form-control" placeholder="Position or Qualification">
   				 <label style="color:red;font-weight:normal;display:none" id="search_position_error"></label>
			</div>
			<div class="col col-lg-3 search-location" style="">
				<input type="text" name="location" onkeyup="disableError('search_position');disableError('search_location');" id="search_location" onkeydown = "if (event.keyCode == 13) document.getElementById('btnSearch').click()" class="form-control" placeholder="City or Town (optional)">
			</div>
			<div class="col-lg-2 search-button" style="">
				<button type="button" id="btnSearch" onclick="validate_search(this)" class="btn mybtn"><i style="color:#FFC300" class="fa fa-search"></i> &nbsp; Search</button>
			</div>
		</form>
		</div>
	</div>
	
	<% if(request.getAttribute("recommended") != null) { %>
	<div class="white-bg top-btm-pad">
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h3 class="page-title">
					<i class="fa fa-briefcase title-icon"></i> Recommended Jobs for You
				</h3>
			</div>
		</div>
		
		<div class="row">
		<div class="col-lg-12" id="main-content">
				<%= request.getAttribute("recommended") %>
		</div>
		</div>
	</div>
	</div>
	<% } %>
	
	<% if((Boolean) request.getAttribute("featuredAvail")) { %>
	
	<div class="white-bg top-btm-pad">
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h3 class="page-title">
					<i class="fa fa-briefcase title-icon"></i> Featured Jobs
				</h3>
			</div>
		</div>
		
		<div class="row">
		<div class="col-lg-12" id="main-content">
				<%= request.getAttribute("featured") %>
		</div>
		</div>
	</div>
	</div>
	<% } %>
	
		<div class="container top-btm-pad">
		<div class="row" style="padding-top:15px">
		<!--<div class="col-lg-2 col-md-4 col-xs-4 col-sm-4 col-lg-offset-2" style="text-align:center">
			<div class="round-count"><span class="count"></span><br><span class="count-text">Candidates Placed</span></div>
		</div>-->
		<div class="col-lg-2 col-md-4 col-xs-4 col-sm-4 col-lg-offset-5" style="text-align:center">
			<div class="round-count"><span class="count"><%=request.getAttribute("jobCount") %></span><br><span class="count-text">Current Openings</span></div>
		</div>
		<!-- <div class="col-lg-2 col-md-4 col-xs-4 col-sm-4 col-lg-offset-1" style="text-align:center">
			<div class="round-count"><span class="count"></span><br><span class="count-text">Registered Employers</span></div>
		</div>-->
		</div>
	</div>
	</div>
	
	<jsp:include page="footer.jsp" flush="true">
		<jsp:param name="pageId" value="pg1"/>
	</jsp:include>
		
	<script>
	/*$(document).ready(function() { 
      /*  $('.counter').counterUp({
            delay: 10,
            time: 1000
        }); */
	/*	var options = {
       		useEasing : true, 
        	useGrouping : true, 
        	separator : ',', 
        	decimal : '.', 
        	prefix : '', 
        	suffix : ''
        };
		var c1 = new CountUp(".c1", 0, , 0, 2.5, options);
		var c2 = new CountUp(".c2", 0, , 0, 2.5, options);
		var c3 = new CountUp(".c3", 0, , 0, 2.5, options);
		c1.start();
		c2.start();
		c3.start();*/
   /* });*/
	
	$(window).scroll(startCounter);
	function startCounter() {
	var hT = $('.count').offset().top,
	       hH = $('.count').outerHeight(),
	       wH = $(window).height(),
	       wS = $(this).scrollTop();
	    if (wS > (hT+hH-wH)) {
	        $(window).off("scroll", startCounter);
			$('.count').each(function () {
	    $(this).prop('Counter',0).animate({
	        Counter: $(this).text()
	    }, {
	        duration: 2000,
	        easing: 'swing',
	        step: function (now) {
	            $(this).text(Math.ceil(now));
	        }
	    });
	});
		}
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
	
	function validate_search(btn) {
	//	if(btn.which==1 || btn.keycode==13) {
		btn.innerHTML = "Please wait...";
		if($("#search_position").val() == "" && $("#search_location").val() == "") {
			showError("search_position", "Please enter the position or location to search for.");
		} else {
			//document.getElementById("search_form").submit();
			var base = "<%=RouteManager.getBasePath()%>";
			window.location.href = base + 'search?position=' + $("#search_position").val() + '&location=' + $("#search_location").val();
		}
		btn.innerHTML = '<i style="color:#FFC300" class="fa fa-search"></i> &nbsp; Search';
		//}
	}
	
	function showJobDetails(jobId) {
		document.getElementById("jobId").value = jobId;
		$('#jobModal').modal('show');
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>job',
			method: "POST",
			data: {
				"jobId": jobId,
			},
			success: function(data) {
				document.getElementById("jobDetails").innerHTML = data;
			},
			error: function(data) {
				document.getElementById("jobDetails").innerHTML = "Something went wrong. Please try again after refreshing page.";
			}
		});
	}
	
	function showTC() {
		$('#jobModal').modal('hide');
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>terms-conditions?action=getTC',
			method: 'POST',
			success: function(data) {
				document.getElementById("termsAndConditions").innerHTML = data;
			},
			error: function(data) {
				document.getElementById("termsAndConditions").innerHTML = "Something went wrong. Please try again after refreshing page.";
			}
		});
		$("#termsModal").modal('show');
	}
	
	function showTCDirect(jobId) {
		document.getElementById("jobId").value = jobId;
		$.ajax({
			url: '<%=RouteManager.getBasePath()%>terms-conditions?action=getTC',
			method: 'POST',
			success: function(data) {
				document.getElementById("termsAndConditions").innerHTML = data;
			},
			error: function(data) {
				document.getElementById("termsAndConditions").innerHTML = "Something went wrong. Please try again after refreshing page.";
			}
		});
		$("#termsModal").modal('show');
	}
	
	function applyJob() {
		var basePath = '<%=RouteManager.getBasePath()%>';
		window.location.href = basePath + 'apply?job=' + document.getElementById("jobId").value;
	}
	
	
	
</script>
	</body>
</html>