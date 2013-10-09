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

    // Fetch the document type (dc.type)
    String documentType = "";
    if( (item.getMetadata("dc.type") != null) && (item.getMetadata("dc.type").length >0) )
    {
        documentType = item.getMetadata("dc.type")[0].value;
    }
    
    DCInput input = (DCInput) request.getAttribute("input");
    String fieldName = (String) request.getAttribute("fieldName");
    boolean repeatable = (Boolean) request.getAttribute("repeatable");
    DCValue[] defaults = item.getMetadata(input.getSchema(), input.getElement(), input.getQualifier(), Item.ANY);
    int fieldCount = (Integer) request.getAttribute("fieldCount");

    String fieldInput = (String) request.getAttribute("fieldInput");
    int confidenceValue = (Integer) request.getAttribute("confidenceValue");
    String authorityValue = (String) request.getAttribute("authorityValue");
    int idx = (Integer) request.getAttribute("idx");
    boolean isName = (Boolean) request.getAttribute("isName");
    int collectionID = (Integer) request.getAttribute("collection");

    MetadataAuthorityManager mam = MetadataAuthorityManager.getManager();
    ChoiceAuthorityManager cam = ChoiceAuthorityManager.getManager();
    
    if (cam.isChoicesConfigured(fieldName)) 
    {
    	boolean authority = mam.isAuthorityControlled(fieldName);
		boolean required = authority && mam.isAuthorityRequired(fieldName);
		boolean isSelect = "select".equals(cam.getPresentation(fieldName)) && !isName;
		
		String authorityName = fieldName + "_authority";
		String confidenceName = fieldName + "_confidence";
		if (repeatable && !isSelect && idx != fieldCount-1)
		{
			fieldInput += '_'+String.valueOf(idx+1);
			authorityName += '_'+String.valueOf(idx+1);
			confidenceName += '_'+String.valueOf(idx+1);
		}
		
		String confidenceSymbol = confidenceValue == unknownConfidence ? "blank" : Choices.getConfidenceText(confidenceValue).toLowerCase();
		String confIndID = fieldInput+"_confidence_indicator_id";
		
		if (authority)
		{
%>
<img id="<%=confIndID%>" title="<%=LocaleSupport.getLocalizedMessage(pageContext, "jsp.authority.confidence.description."+confidenceSymbol)%>"
	src="<%= contextPath%>/image/confidence/invisible.gif"
 /> 
<input type="hidden" id="<%=authorityName %>" name="<%=authorityName %>" value="<%=authorityValue!=null?authorityValue:"" %>" />
<input type="hidden" id="<%=confidenceName %>" name="<%=confidenceName %>" value="<%=confidenceSymbol %>" />

<%
		}

		if ("suggest".equals(cam.getPresentation(fieldName)) && !isName)
		{

%>

<span id="<%=fieldInput %>_indicator" style="display: none;">
	<img src="<%=contextPath%>/image/authority/load-indicator.gif" alt="Loading..." />
</span>
<div id="<%=fieldInput%>_autocomplete" style="display: none;"></div>

<script type="text/javascript">
	var gigo = DSpaceSetupAutocomplete('edit_metadata', {
		metadataField: '<%=fieldName%>',
		isClosed: '<%=(required?"true":"false")%>',
		inputName: '<%=fieldInput%>',
		authorityName: '<%=authorityName%>',
		containerID: '<%=fieldInput%>_autocomplete',
		indicatorID: '<%=fieldInput%>_indicator',
		contextPath: '<%=contextPath%>',
		confidenceName: '<%=confidenceName%>',
		confidenceIndicatorID: '<%=confIndID%>',
		collection: '<%=collectionID%>'
	});
</script>
<%
		}
		else if (isSelect)
		{
%>

<select class="form-control" id="<%=fieldInput%>_id" name="<%=fieldInput%>" <% if (repeatable) { %>multiple<% } %>>
<%
	Choices cs = cam.getMatches(fieldName, "", collectionID, 0, 0, null);
	if (!repeatable && cs.defaultSelected < 0 && defaults.length == 0) {
%>
	<option value=""></option>
<%
	}
	for (int i = 0; i < cs.values.length; ++i)
    {
        boolean selected = false;
        for (DCValue dcv : defaults)
        {
            if (dcv.value.equals(cs.values[i].value))
                selected = true;
        }
        
        %>
	<option value="<%=cs.values[i].value.replaceAll("\"", "\\\"")%>" <% if (selected) { %>selected<% } %>><%=cs.values[i].label %></option>
        <%
    }    
%>
</select>
<%
	            }
	            else
	            {
	            	
%>
<button type="button" id="<%=confIndID%>" title="<fmt:message key='jsp.tools.lookup.lookup' />"
	class="btn btn-info" onclick="javascript: return DSpaceChoiceLookup('<%=contextPath%>/tools/lookup.jsp', '<%=fieldName%>','edit_metadata', '<%=fieldInput %>', '<%=authorityName %>', '<%=confIndID %>', <%=collectionID%>, <%=(isName?"true":"false")%>, false);">
	&nbsp;<i class="icon-search"></i>&nbsp;
</button>
<%
	            }
			}
%>