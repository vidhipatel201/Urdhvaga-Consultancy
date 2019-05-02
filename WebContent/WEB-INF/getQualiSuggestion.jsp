<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="controller.LoadQualiSuggestion"  %>

<%
	LoadQualiSuggestion obj = new LoadQualiSuggestion();
	String query = request.getParameter("term");
	List<String> suggestions = obj.getData(query);
	String jArray = "[";
		for(int i = 0 ; i<suggestions.size() ; i++) {
		jArray += suggestions.get(i).toString() + ",";
	}
		jArray += "]";
		
%>