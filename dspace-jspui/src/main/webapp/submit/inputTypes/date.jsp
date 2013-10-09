<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Edit metadata form
  -
  - Attributes to pass in to this page:
  -    submission.info   - the SubmissionInfo object
  -    submission.inputs - the DCInputSet
  -    submission.page   - the step in submission
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.net.URLEncoder" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="javax.servlet.jsp.tagext.TagSupport" %>
<%@ page import="javax.servlet.jsp.PageContext" %>
<%@ page import="javax.servlet.ServletException" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.jsptag.PopupTag" %>
<%@ page import="org.dspace.app.util.DCInput" %>
<%@ page import="org.dspace.app.util.DCInputSet" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.submit.AbstractProcessingStep" %>
<%@ page import="org.dspace.core.I18nUtil" %>
<%@ page import="org.dspace.app.webui.util.JSPManager" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.DCDate" %>
<%@ page import="org.dspace.content.DCLanguage" %>
<%@ page import="org.dspace.content.DCPersonName" %>
<%@ page import="org.dspace.content.DCSeriesNumber" %>
<%@ page import="org.dspace.content.DCValue" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.content.authority.MetadataAuthorityManager" %>
<%@ page import="org.dspace.content.authority.ChoiceAuthorityManager" %>
<%@ page import="org.dspace.content.authority.Choices" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.core.Utils" %>


<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
	String contextPath = request.getContextPath();
	int unknownConfidence = Choices.CF_UNSET - 100;
    
	// Obtain DSpace context
    Context context = UIUtil.obtainContext(request);

    SubmissionInfo si = SubmissionController.getSubmissionInfo(context, request);

    Item item = si.getSubmissionItem().getItem();

    final int halfWidth = 23;
    final int fullWidth = 50;
    final int twothirdsWidth = 34;

    DCInputSet inputSet =
        (DCInputSet) request.getAttribute("submission.inputs");

    Integer pageNumStr =
        (Integer) request.getAttribute("submission.page");
    int pageNum = pageNumStr.intValue();
    
    // for later use, determine whether we are in submit or workflow mode
    String scope = si.isInWorkflow() ? "workflow" : "submit";

    // owning Collection ID for choice authority calls
    int collectionID = si.getSubmissionItem().getCollection().getID();

    // Fetch the document type (dc.type)
    String documentType = "";
    if( (item.getMetadata("dc.type") != null) && (item.getMetadata("dc.type").length >0) )
    {
        documentType = item.getMetadata("dc.type")[0].value;
    }
    
    String fieldName = (String) request.getAttribute("fieldName");
    DCInput input = (DCInput) request.getAttribute("input");
    Boolean hasWarning = (Boolean) request.getAttribute("hasWarning");
    int fieldCountIncr = (Integer) request.getAttribute("fieldCountIncr");
    boolean repeatable = (Boolean) request.getAttribute("repeatable");
    boolean readonly = (Boolean) request.getAttribute("readonly");
    boolean closedVocabulary = (Boolean) request.getAttribute("closedVocabulary");
    String vocabulary = (String) request.getAttribute("vocabulary");
    boolean hasVocabulary = (Boolean) request.getAttribute("hasVocabulary");
    
			DCValue[] defaults = item.getMetadata(input.getSchema(), input.getElement(), input.getQualifier(), Item.ANY);
			int fieldCount = defaults.length + fieldCountIncr;
			StringBuffer sb = new StringBuffer();
			String val, auth;
			int conf= 0;
			
			if (fieldCount == 0)
				fieldCount = 1;
%>

		<div class="row">
			<div class="col-lg-12 form-group">
				<h4 id="<%=fieldName%>"><%=input.getLabel() %>
					<% if (input.getHints() != null) { // Hint %>
					<small>
						<%=input.getHints() %>
					</small>
					<% } %>
				</h4>
				<% if (hasWarning != null && hasWarning) { %>
				<div class="alert alert-danger">
					<p><%=input.getWarning() %></p>
				</div>
				<% } %>
				<div>
<%

			for (int i = 0; i < fieldCount; i++) {
				DCDate dateIssued;
				if (i < defaults.length)
					dateIssued = new org.dspace.content.DCDate(defaults[i].value);
		        else
		        	dateIssued = new org.dspace.content.DCDate("");
				String fieldNameIdx = fieldName + ((repeatable && i != fieldCount-1)?"_" + (i+1):"");
				val = dateIssued.toString();
%>
				<div class="form-inline">
					<div class="form-group">
						<label class="control-label"><fmt:message key='jsp.submit.edit-metadata.month' /></label>
						<select class="form-control" name="<%=fieldName%>_month" <% if (readonly) { %>readonly<% } %>>
							<option <% if (dateIssued.getMonth() == -1) { %>selected<% } %> value="<%=dateIssued.getMonth()%>"><fmt:message key="jsp.submit.edit-metadata.no_month" /></option>
<% 
							for (int j = 1; j < 13; j++) {
%>
							<option value="<%=j%>" <% if (dateIssued.getMonth() == j) { %>selected<% } %>><%= org.dspace.content.DCDate.getMonthName(j,I18nUtil.getSupportedLocale(request.getLocale()))%></option>
<%
							}
%>
						</select>
					</div>
					<div class="form-group">
						<label><fmt:message key='jsp.submit.edit-metadata.day' /></label>
						<input class="form-control" name="<%=fieldName %>_day<% if (repeatable) { %>_<%=i %><% } %>" id="<%=fieldName %>_day<% if (repeatable) { %>_<%=i %><% } %>" type="text" value="<% if (dateIssued.getDay() > 0 ) { %><%=String.valueOf(dateIssued.getDay()) %><% } %>" <% if ((hasVocabulary && closedVocabulary) || readonly) { %>disabled<% } %> />
					</div>
					<div class="form-group">
						<label><fmt:message key="jsp.submit.edit-metadata.year" /></label>
						<input  class="form-control" name="<%=fieldName %>_year<% if (repeatable) { %>_<%=i %><% } %>" id="<%=fieldName %>_year<% if (repeatable) { %>_<%=i %><% } %>" type="text" value="<% if (dateIssued.getYear() > 0 ) { %><%=String.valueOf(dateIssued.getYear()) %><% } %>" <% if ((hasVocabulary && closedVocabulary) || readonly) { %>disabled<% } %> />
					</div>
					<% if (repeatable && !readonly && i < defaults.length) { %>
					<div class="form-group">
						<button class="btn btn-danger" type="submit" name="submit_<%=fieldName%>_remove_<%=i%>">&nbsp;<i class="icon-trash"></i>&nbsp;</button>
					</div>
					<% } %>
				</div>
				<% if (repeatable && !readonly) { %>
				<div class="row">
					<div class="pull-right">
						<button class="btn btn-default" type="submit" name="submit_<%=fieldName %>_add"><fmt:message key="jsp.submit.edit-metadata.button.add" /></button>
					</div>
					<div class="clearfix"></div>
				</div>
				<% } %>
				
<%	
			}
%>
				</div>
			</div>
		</div>		