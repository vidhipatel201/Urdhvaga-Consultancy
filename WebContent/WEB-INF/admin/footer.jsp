<%@page import="utils.RouteManager" %>

	<script src="<%=RouteManager.getBasePath() %>admin/js/jquery-3.1.1.min.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/bootstrap.min.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/chart.min.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/chart-data.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/easypiechart.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/easypiechart-data.js"></script>
	<script src="<%=RouteManager.getBasePath() %>admin/js/bootstrap-datepicker.js"></script>
	<script>
		$('#calendar').datepicker({
		});
		
		$(document).ready(function() {
			$("#<%=request.getParameter("pageId")%>").addClass("active");
		});

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