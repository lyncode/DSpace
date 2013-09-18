<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Initial questions for keeping UI as simple as possible.
  -
  - Attributes to pass in:
  -    submission.info    - the SubmissionInfo object
  -    submission.inputs  - the DCInputSet object
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.submit.AbstractProcessingStep" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.util.DCInputSet" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

        DCInputSet inputSet =
        (DCInputSet) request.getAttribute("submission.inputs");

	// Obtain DSpace context
    Context context = UIUtil.obtainContext(request);

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);
%>

<dspace:layout locbar="off"
               titlekey="jsp.submit.initial-questions.title"
               nocache="true">

    <form class="margin-top" action="<%= request.getContextPath() %>/submit" method="post" onkeydown="return disableEnterKey(event);">
               
		<div class="row margin-top-2">
			<div class="col-lg-12">
	        	<jsp:include page="/submit/progressbar.jsp" />
			</div>
		</div>
		
		<%= SubmissionController.getSubmissionParameters(context, request) %>
        <div class="panel panel-default margin-top-2">
			<div class="panel-heading">
				<h3><fmt:message key="jsp.submit.initial-questions.heading"/></h3>
				<p>
					<fmt:message key="jsp.submit.initial-questions.info" /> 
			        <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#describe1\"%>"><fmt:message key="jsp.morehelp"/></dspace:popup>
				</p>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-lg-offset-1 col-lg-10">
<%
	// Don't display MultipleTitles if no such form box defined
    if (inputSet.isDefinedMultTitles())
    {
%>
					  <div class="form-group">
					    <div class="col-lg-offset-2 col-lg-10">
					      <div class="checkbox">
					        <label>
					          <input type="checkbox" name="multiple_titles" value="true" <%= (subInfo.getSubmissionItem().hasMultipleTitles() ? "checked='checked'" : "") %> />
					          <fmt:message key="jsp.submit.initial-questions.elem1"/>
					        </label>
					      </div>
					    </div>
					  </div>
<%
    }
    // Don't display PublishedBefore if no form boxes defined
    if (inputSet.isDefinedPubBefore())
    {
%>
					  <div class="form-group">
					    <div class="col-lg-offset-2 col-lg-10">
					      <div class="checkbox">
					        <label>
					          <input type="checkbox" name="published_before" value="true" <%= (subInfo.getSubmissionItem().isPublishedBefore() ? "checked='checked'" : "") %> />
					          <fmt:message key="jsp.submit.initial-questions.elem2"/>
					        </label>
					      </div>
					    </div>
					  </div>
<%
    }
    // Don't display file or thesis questions in workflow mode
    if (!subInfo.isInWorkflow())
    {
%>
					  <div class="form-group">
					    <div class="col-lg-offset-2 col-lg-10">
					      <div class="checkbox">
					        <label>
					          <input type="checkbox" name="multiple_files" value="true" <%= (subInfo.getSubmissionItem().hasMultipleFiles() ? "checked='checked'" : "") %> />
					          <fmt:message key="jsp.submit.initial-questions.elem3"/>
					        </label>
					      </div>
					    </div>
					  </div>
<%
        if (ConfigurationManager.getBooleanProperty("webui.submit.blocktheses"))
        {
%>
					  <div class="form-group">
					    <div class="col-lg-offset-2 col-lg-10">
					      <div class="checkbox">
					        <label>
					          <input type="checkbox" name="is_thesis" value="true" />
					          <fmt:message key="jsp.submit.initial-questions.elem4"/>
					        </label>
					      </div>
					    </div>
					  </div>
<%
        }
    }
%>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<div class="pull-right">
				<%  //if not first step, show "Previous" button
					if(!SubmissionController.isFirstStep(request, subInfo))
					{ %>
                        <button class="btn btn-default" type="submit" name="<%=AbstractProcessingStep.PREVIOUS_BUTTON%>"><fmt:message key="jsp.submit.general.previous"/></button>
				<%  } %>
					<button class="btn btn-primary" type="submit" name="<%=AbstractProcessingStep.NEXT_BUTTON%>"><fmt:message key="jsp.submit.general.next"/></button>
					<button class="btn btn-default" type="submit" name="<%=AbstractProcessingStep.CANCEL_BUTTON%>"><fmt:message key="jsp.submit.general.cancel-or-save.button"/></button>
				</div>	
				<div class="clearfix"></div>
			</div>
		</div>
    </form>

</dspace:layout>
