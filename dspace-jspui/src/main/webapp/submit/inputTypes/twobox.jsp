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
    
%>
		<div class="row">
			<div class="col-lg-12 form-group">
				<h4 id="<%=fieldName %>"><%=input.getLabel() %>
					<% if (input.getHints() != null) { // Hint %>
					<small>
						<%=input.getHints() %>
					</small>
					<% } %>
				</h4>
				<% if (hasWarning != null && hasWarning) { // Warning %>
				<div class="alert alert-danger">
					<p><%=input.getWarning() %></p>
				</div>
				<% } %>
				<div>
				<%
				DCValue[] defaults = item.getMetadata(input.getSchema(), input.getElement(), input.getQualifier(), Item.ANY);
				int fieldCount = defaults.length + fieldCountIncr;
				StringBuffer headers = new StringBuffer();
				StringBuffer sb = new StringBuffer();
				org.dspace.content.DCPersonName dpn;
				String auth;
				int conf = 0;
				StringBuffer name = new StringBuffer();
				StringBuffer first = new StringBuffer();
				StringBuffer last = new StringBuffer();
				
				if (fieldCount == 0)
				   fieldCount = 1;
				%>
				<table class="table">
					<% if (input.getElement().equals("relation") && input.getQualifier().equals("ispartofseries")) { %>
					<thead>
						<th><fmt:message key="jsp.submit.edit-metadata.seriesname" /></th>
						<th><fmt:message key="jsp.submit.edit-metadata.paperno" /></th>
						<% if (!readonly) { %>
						<th>
							<button name="submit_<%=fieldName %>_add" class="btn form-control btn-default" type="submit"><fmt:message key="jsp.submit.edit-metadata.button.add" /></button>
						</th>
						<% } %>
					</thead>
					<% } %>
					<tbody>
<%         
					for (int i = 0; i < fieldCount; i++) {
						String fieldParam = fieldName;
						if (i != fieldCount) fieldParam = fieldName + "_" + (i+1);
						
%>
						<tr>
<%
					   	if (i < defaults.length)
					   	{
%>
							<td class="form-group">
								<div class="input-group">
									<input class="form-control" type="text" name="<%=fieldParam %>" value="<%=defaults[i].value.replaceAll("\"", "&quot;") %>" <% if ((hasVocabulary&&closedVocabulary) || readonly) { %>disabled<% } %>  />
									<% if (!readonly || hasVocabulary) { %>
									<span class="input-group-btn">
										<% if (hasVocabulary) { %>
										<jsp:include page="vocabulary/vocabulary.jsp">
											<jsp:param value="<%= fieldParam%>" name="field"/>
										</jsp:include>
										<% } %>
										<% if (!readonly) { %>
											<button name="submit_<%=fieldName%>_remove_<%=i %>" class="btn btn-danger">&nbsp;<i class="icon-trash"></i>&nbsp;</button>
										<% } %>
									</span>
									<% } %>
								</div>
							</td>
<%
					   	} else {
%>
							<td>
									<input class="form-control" type="text" name="<%=fieldParam %>" <% if ((hasVocabulary&&closedVocabulary) || readonly) { %>disabled<% } %>  />
							</td>
<%
					   	}

						i++;
						
						if (i != fieldCount)
							fieldParam = fieldName + "_" + (i+1);
						else
							fieldParam = fieldName;
						
					   	if (i < defaults.length)
					   	{
%>
							<td class="form-group">
								<div class="input-group">
									<input class="form-control" type="text" name="<%=fieldParam %>" value="<%=defaults[i].value.replaceAll("\"", "&quot;") %>" <% if ((hasVocabulary&&closedVocabulary) || readonly) { %>disabled<% } %>  />
									<% if (!readonly || hasVocabulary) { %>
									<span class="input-group-btn">
										<% if (hasVocabulary) { %>
										<jsp:include page="vocabulary/vocabulary.jsp">
											<jsp:param value="<%= fieldParam%>" name="field"/>
										</jsp:include>
										<% } %>
										<% if (!readonly) { %>
											<button name="submit_<%=fieldName%>_remove_<%=i %>" class="btn btn-danger">&nbsp;<i class="icon-trash"></i>&nbsp;</button>
										<% } %>
									</span>
									<% } %>
								</div>
							</td>
<%
					   	} else {
%>
							<td>
								<input class="form-control" type="text" name="<%=fieldParam %>" <% if ((hasVocabulary&&closedVocabulary) || readonly) { %>disabled<% } %>  />
							</td>
<%
					   	}
%>
						<td></td></tr>
<%					} %>

						<% if ((!input.getElement().equals("relation") || !input.getQualifier().equals("ispartofseries")) && !readonly) { %>
						<tr>
							<td></td>
							<td align="right">
								<button name="submit_<%=fieldName %>_add" class="btn btn-default" type="submit"><fmt:message key="jsp.submit.edit-metadata.button.add" /></button>
							</td>
						</tr>
						<% } %>
					</tbody>	
				</table>
				</div>
			</div>
		</div>
