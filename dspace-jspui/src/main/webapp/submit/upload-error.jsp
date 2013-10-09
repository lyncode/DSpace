<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - List of uploaded files
  -
  - Attributes to pass in to this page:
  -    submission.info  - the SubmissionInfo object
  -    submission.inputs  - the DCInputSet object
  -
  - FIXME: Merely iterates through bundles, treating all bit-streams as
  -        separate documents.  Shouldn't be a problem for early adopters.
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.app.util.DCInputSet" %>
<%@ page import="org.dspace.app.util.DCInputsReader" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request); 

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);
%>

<dspace:layout locbar="off"
               titlekey="jsp.submit.upload-error.title"
               nocache="true">
	<form class="margin-top" action="<%= request.getContextPath() %>/submit"  enctype="multipart/form-data" method="post" onkeydown="return disableEnterKey(event);">
               
		<div class="row margin-top-2">
			<div class="col-lg-12">
	        	<jsp:include page="/submit/progressbar.jsp" />
			</div>
		</div>
		
		
		<%= SubmissionController.getSubmissionParameters(context, request) %>
        <div class="panel panel-default margin-top-2">
			<div class="panel-heading">
				<h3 class="text-danger"><fmt:message key="jsp.submit.upload-error.heading"/></h3>
				<p><fmt:message key="jsp.submit.upload-error.info"/></p>
			</div>
			<div class="panel-footer">
				<center>
                	<button type="submit" name="submit_retry" class="btn btn-default"><fmt:message key="jsp.submit.upload-error.retry.button"/></button>
                </center>
            </div>
        </div>
    </form>

</dspace:layout>
