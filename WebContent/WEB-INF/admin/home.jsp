<%@page import="java.util.List" %>
<%@page import="java.util.Hashtable" %>
<%@page import="java.util.ArrayList" %>
<%@page import="model.Uc_candidate_details" %>
<%@page import="utils.RouteManager" %>
<%@page import="utils.SessionManager" %>
<%@page import="controller.UCConstants" %>

<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="Admin Home - Urdhvaga Consultancy"/>
</jsp:include>
		<link rel="stylesheet" href="<%=RouteManager.getBasePath() %>assets/css/typeahead.bundle.css" />
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">			
		
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info home_boxes">
				<div class="panel-heading">Registered Users
				</div>
  				<div class="panel-body">
  					<div class="row">
  						<div class="col-lg-4">
  							<div class="form-group">
  								<select id="user_options" onchange="user_option_changed(this)" class="form-control">
  									<option value="0" selected>All</option>
  									<option value="<%=UCConstants.STATE%>">State</option>
  									<option value="<%=UCConstants.DISTRICT%>">District</option>
  									<option value="<%=UCConstants.TALUKA%>">Taluka</option>
  									<option value="<%=UCConstants.CITY%>">City</option>
  									</select>
  							</div>
  						</div>
  						<div class="col-lg-4">
  							<div class="form-group">
  								<input type="text" class="form-control" autocomplete="off" spellcheck="false" id="user_option_value" placeholder="Id or value">
  							</div>
  						</div>
  						<div class="col-lg-4">
  							<div class="form-group">
  								<button type="button" id="user_btn" onclick="generateUserChart()" class="btn btn-primary">Submit</button>
  							</div>
  						</div>
  					</div>
  					<div id="userChart"></div>
  				</div>
  				</div>
			</div>
		</div>
		
		
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-info home_boxes">
				<div class="panel-heading">Registered Employers
				</div>
  				<div class="panel-body">
  					<div id="employersChart"></div>
  				</div>
  				</div>
			</div>
		</div>
	
	</div>	<!--/.main-->

	<jsp:include page="footer.jsp" flush="true">
		<jsp:param name="pageId" value="pg1"/>
	</jsp:include>
	
	
	<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="<%=RouteManager.getBasePath()%>assets/js/typeahead.bundle.js"></script>

<script>
var uChart = "";
var eChart = "";
$(document).ready(function() {
	generateUserChart();
	generateEmployerChart();
	
	uChart = Highcharts.chart('userChart', {
	    title: {
	        text: 'Yearly registered candidates',
	        x: -20 //center
	    },
	    xAxis: {
	        categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
	            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
	    },
	    yAxis: {
	        title: {
	            text: 'Number of candidates registered'
	        },
	        plotLines: [{
	            value: 0,
	            width: 1,
	            color: '#808080'
	        }]
	    },
	   
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle',
	        borderWidth: 0
	    },
	    series: [{
	        name: "",
	        data: []
	    }, {
	    	name: "",
	        data: []
	    }]
	});
	
	
	eChart = Highcharts.chart('employersChart', {
	    title: {
	        text: 'Yearly registered employers',
	        x: -20 //center
	    },
	    xAxis: {
	        categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
	            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
	    },
	    yAxis: {
	        title: {
	            text: 'Number of employers registered'
	        },
	        plotLines: [{
	            value: 0,
	            width: 1,
	            color: '#808080'
	        }]
	    },
	   
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle',
	        borderWidth: 0
	    },
	    series: [{
	        name: "",
	        data: []
	    }, {
	    	name: "",
	        data: []
	    }]
	});
});

var basePath = "<%=RouteManager.getBasePath()%>";

function user_option_changed(select) {
	$('#user_option_value').typeahead('destroy');
	if(select.value == "0") {
		if(!$("#user_option_value").hasClass("disabled")) {
			$("#user_option_value").addClass("disabled");	
		}
		
	} else {
		if($("#user_option_value").hasClass("disabled")) {
			$("#user_option_value").removeClass("disabled");	
		}
		
		var suggestions = new Bloodhound({
	        datumTokenizer: Bloodhound.tokenizers.whitespace,
	        queryTokenizer: Bloodhound.tokenizers.whitespace,
	        prefetch: {
	        	url: basePath + 'GetSuggestion?autocomplete=' + select.value,
	        	cache:false,
	        	ttl:0
	        }
	 	});
		suggestions.initialize();
		 $('#user_option_value').typeahead(null, {
				name: "suggestions",
		        source: suggestions,
		        limit: 10 /* Specify maximum number of suggestions to be displayed */
		    });
	}
}

function generateUserChart() {
	
	$.ajax({
		url: "<%=RouteManager.getBasePath()%>admin/manage-users?action=getInsights",
		method: "POST",
		data : {
			"filter" : $("#user_options").val(),
			"filter_value" : $("#user_option_value").val()
		},
		dataType: "JSON",
		async:true,
		success: function(data) {
			if(data.dataAvail == "0") {
				$("#userChart").html("No data available");
			} else if(data.dataAvail == "1") {
				
				//update
				uChart.series[0].update({
			        name: data.previousYear,
					data: data.previousYearCandidates
			        });
				uChart.series[1].update({
			        name: data.currentYear,
					data: data.currentYearCandidates
			        });
			}
		},
		error: function(data) {
			alert("Something went wrong. Please try again.");
		},
		complete: function() {
			
		}
	});	
	
} 

function generateEmployerChart() {
	$.ajax({
		url: "<%=RouteManager.getBasePath()%>admin/employer?action=getInsights",
		method: "POST",
		dataType: "JSON",
		async:true,
		success: function(data) {
			
				
				//update
				eChart.series[0].update({
			        name: data.previousYear,
					data: data.previousYearEmployers
			        });
				eChart.series[1].update({
			        name: data.currentYear,
					data: data.currentYearEmployers
			        });
			
		},
		error: function(data) {
			alert("Something went wrong. Please try again.");
		},
		complete: function() {
			
		}
	});
}
</script>

</body>

</html>
