<%@page import="model.Uc_firm_details" %>
<jsp:include page="header.jsp" flush="true">
    <jsp:param name="title" value="About - Urdhvaga Consultancy"/>
</jsp:include>

<%
	Uc_firm_details ucFirm = new Uc_firm_details();
	ucFirm = (Uc_firm_details) request.getAttribute("ucFirm");
%>

<div id="wrapper">
	<div class="container top-btm-pad">
	<div class="white-bg about-bg">
	<div class="row">
			<div class="col-lg-12">
				<h3 class="page-title">
					<i class="fa fa-flag title-icon"></i> About <b><%=ucFirm.getFd_name() %></b>
				</h3>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
				<%=ucFirm.getFd_about() %>
			</div>
		</div>
	</div>
	</div>
</div>

<jsp:include page="footer.jsp" flush="true">
	<jsp:param name="pageId" value="pg5"/>
</jsp:include>
