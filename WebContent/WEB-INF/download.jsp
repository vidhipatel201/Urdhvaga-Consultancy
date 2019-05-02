<%@ page trimDirectiveWhitespaces="true" %>
<%@page import="utils.FileManager"%>
<%@page import="controller.UCConstants" %>
<%
    
    String filename= request.getParameter("fileName");
	String fileType= request.getParameter("fileType");
    
	FileManager ftphelper = new FileManager();

    response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    if("image".equals(fileType.toLowerCase())) {
    	ftphelper.downloadFTPFile(UCConstants.UC_FILES + filename, response.getOutputStream());	
    }
    if("file".equals(fileType.toLowerCase())) {
    	ftphelper.downloadFTPFile(UCConstants.UC_RESUMES + filename, response.getOutputStream());
    }
    
    ftphelper.disconnect();

%>