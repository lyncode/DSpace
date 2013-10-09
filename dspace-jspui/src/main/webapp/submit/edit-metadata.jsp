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

<%!
	boolean isSelectable (String fieldKey) {
    	ChoiceAuthorityManager cam = ChoiceAuthorityManager.getManager();
    	return (cam.isChoicesConfigured(fieldKey) &&
        	"select".equals(cam.getPresentation(fieldKey)));
	}

	boolean hasVocabulary (String vocabulary) {
        boolean enabled = ConfigurationManager.getBooleanProperty("webui.controlledvocabulary.enable");
        boolean useWithCurrentField = vocabulary != null && !"".equals(vocabulary);
        boolean has = false;
        
        if (enabled && useWithCurrentField)
        {
                has = true;
        }
        return has;
	}
	
	Boolean hasWarning (String fieldName, SubmissionInfo si, DCInput input) {
		if ((si.getMissingFields() != null) && (si.getMissingFields().contains(fieldName)))
		{
			if(input.getWarning() != null)
			{
				if(si.getJumpToField()==null || si.getJumpToField().length()==0)
	                         si.setJumpToField(fieldName);
				
				return true;
			}
		}
		else
		{
			//print out hints, if not null
			if(input.getHints() != null)
			{
				return false;
			}
		}
		return null;
	}
%>

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
	
	DCInputSet inputSet =  (DCInputSet) request.getAttribute("submission.inputs");
	Integer pageNumStr =  (Integer) request.getAttribute("submission.page");
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
%>

<dspace:layout locbar="off" titlekey="jsp.submit.edit-metadata.title">

	<form class="margin-top-2" action="<%= request.getContextPath() %>/submit#<%= si.getJumpToField()%>" method="post" name="edit_metadata" id="edit_metadata" onkeydown="return disableEnterKey(event);">
		<%= SubmissionController.getSubmissionParameters(context, request) %>
		<jsp:include page="/submit/progressbar.jsp"></jsp:include>
		<div class="panel panel-default margin-top-2">
			<div class="panel-heading">
    			<h3><fmt:message key="jsp.submit.edit-metadata.heading"/></h3>
    			<p>
<%
     //figure out which help page to display
     if (pageNum <= 1)
     {
%>
        			<fmt:message key="jsp.submit.edit-metadata.info1"/>
        			<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#describe2\"%>"><fmt:message key="jsp.submit.edit-metadata.help"/></dspace:popup>
<%
     }
     else
     {
%>
        			<fmt:message key="jsp.submit.edit-metadata.info2"/>
        			<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#describe3\"%>"><fmt:message key="jsp.submit.edit-metadata.help"/></dspace:popup>
    
<%
     }
%>   				
    			</p>
			</div>
			<div class="panel-body">
<%

	int pageIdx = pageNum - 1;
	DCInput[] inputs = inputSet.getPageRows(pageIdx, si.getSubmissionItem().hasMultipleTitles(),
                                                si.getSubmissionItem().isPublishedBefore() );
	for (int z = 0; z < inputs.length; z++) {

		boolean readonly = false;
		// Omit fields not allowed for this document type
     	if(!inputs[z].isAllowedFor(documentType))
     	{
         	continue;
     	}

     	// ignore inputs invisible in this scope
     	if (!inputs[z].isVisible(scope))
     	{
     		if (inputs[z].isReadOnly(scope))
         	{
     			readonly = true;
         	}
         	else
         	{
         		continue;
  			}
		}

     	String dcElement = inputs[z].getElement();
     	String dcQualifier = inputs[z].getQualifier();
     	String dcSchema = inputs[z].getSchema();
     
     	String fieldName;
     	int fieldCountIncr;
     	boolean repeatable;
     	String vocabulary;

     	vocabulary = inputs[z].getVocabulary();
     
     
		if (dcQualifier != null && !dcQualifier.equals("*"))
			fieldName = dcSchema + "_" + dcElement + '_' + dcQualifier;
		else
			fieldName = dcSchema + "_" + dcElement;


		repeatable = inputs[z].getRepeatable();
		fieldCountIncr = 0;
		if (repeatable && !readonly)
		{
			fieldCountIncr = 1;
			if (si.getMoreBoxesFor() != null && si.getMoreBoxesFor().equals(fieldName))
			{
				fieldCountIncr = 2;
			}
		}

		String inputType = inputs[z].getInputType();
		String label = inputs[z].getLabel();
		boolean closedVocabulary = inputs[z].isClosedVocabulary();
		
		Boolean hasWarning = hasWarning(fieldName, si, inputs[z]);
     
		request.setAttribute("fieldName", fieldName);
		request.setAttribute("input", inputs[z]);
		request.setAttribute("hasWarning", hasWarning);
		request.setAttribute("fieldCountIncr", fieldCountIncr);
		request.setAttribute("repeatable", repeatable);
		request.setAttribute("readonly", readonly);
		request.setAttribute("closedVocabulary", closedVocabulary);
		request.setAttribute("vocabulary", vocabulary);
		request.setAttribute("hasVocabulary", hasVocabulary(vocabulary));
		
		if (inputType.equals("name")) { %>
		<jsp:include page="inputTypes/name.jsp"></jsp:include>
<% 		} else if (isSelectable(fieldName)) { %>
		<jsp:include page="inputTypes/choice.jsp"></jsp:include>
<% 		} else if (inputType.equals("date")) { %>
		<jsp:include page="inputTypes/date.jsp"></jsp:include>
<%		} else if (inputType.equals("series")) { %>
		<jsp:include page="inputTypes/series.jsp"></jsp:include>
<%		} else if (inputType.equals("qualdrop_value")) { %>
		<jsp:include page="inputTypes/quadropvalue.jsp"></jsp:include>
<%		} else if (inputType.equals("textarea")) {  %>
		<jsp:include page="inputTypes/textarea.jsp"></jsp:include>
<%		} else if (inputType.equals("dropdown")) {  %>
		<jsp:include page="inputTypes/dropdown.jsp"></jsp:include>
<%		} else if (inputType.equals("twobox")) { %>
		<jsp:include page="inputTypes/twobox.jsp"></jsp:include>
<%		} else if (inputType.equals("list")) {  %>
		<jsp:include page="inputTypes/list.jsp"></jsp:include>
<%		} else {  %>
		<jsp:include page="inputTypes/onebox.jsp"></jsp:include>
<% 		}
       
		if (hasVocabulary(vocabulary) &&  !readonly) { %>

				<tr>
					<td>&nbsp;</td>
					<td colspan="3" class="submitFormHelpControlledVocabularies">
						<dspace:popup page="/help/index.html#controlledvocabulary">
							<fmt:message
								key="jsp.controlledvocabulary.controlledvocabulary.help-link" />
						</dspace:popup>
					</td>
				</tr>

<%
		}
	}
%>
			</div>
			<div class="panel-footer">
				<div class="pull-right">
<% if(!(SubmissionController.isFirstStep(request, si) && pageNum<=1)) { %>
					<button class="btn btn-default" type="submit" name="<%=AbstractProcessingStep.PREVIOUS_BUTTON%>"><fmt:message key="jsp.submit.edit-metadata.previous"/></button>
<%  } %>
					<button class="btn btn-primary" type="submit" name="<%=AbstractProcessingStep.NEXT_BUTTON%>"><fmt:message key="jsp.submit.edit-metadata.next"/></button>
                	<button class="btn btn-default" type="submit" name="<%=AbstractProcessingStep.CANCEL_BUTTON%>"><fmt:message key="jsp.submit.edit-metadata.cancelsave"/></button>
                </div>
                <div class="clearfix"></div>
			</div>
		</div>
	</form>

</dspace:layout>
