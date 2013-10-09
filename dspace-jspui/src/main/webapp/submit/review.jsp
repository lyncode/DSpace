<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.io.IOException" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="javax.servlet.jsp.PageContext" %>

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.submit.AbstractProcessingStep" %>
<%@ page import="org.dspace.app.util.DCInputSet" %>
<%@ page import="org.dspace.app.util.DCInput" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.Bitstream" %>
<%@ page import="org.dspace.content.BitstreamFormat" %>
<%@ page import="org.dspace.content.DCDate" %>
<%@ page import="org.dspace.content.DCLanguage" %>
<%@ page import="org.dspace.content.DCValue" %>
<%@ page import="org.dspace.content.InProgressSubmission" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.core.Utils" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);    

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);
    
	//get list of review JSPs from VerifyServlet
	HashMap reviewJSPs = (HashMap) request.getAttribute("submission.review");

	//get an iterator to loop through the review JSPs to load
	Iterator reviewIterator = reviewJSPs.keySet().iterator();
%>
          
<dspace:layout locbar="off" titlekey="jsp.submit.review.title" nocache="true">
	<form class="margin-top" action="<%= request.getContextPath() %>/submit" method="post" onkeydown="return disableEnterKey(event);">
               
		<div class="row margin-top-2">
			<div class="col-lg-12">
	        	<jsp:include page="/submit/progressbar.jsp" />
			</div>
		</div>
		
		<%= SubmissionController.getSubmissionParameters(context, request) %>
        <div class="panel panel-default margin-top-2">
			<div class="panel-heading">
				<h3><fmt:message key="jsp.submit.review.heading"/></h3>
       			<p><fmt:message key="jsp.submit.review.info1"/></p>
       			<p><fmt:message key="jsp.submit.review.info2"/>
        		<p><dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#verify\"%>"><fmt:message key="jsp.morehelp"/></dspace:popup></p>
       			<p><fmt:message key="jsp.submit.review.info3"/></p>
        		<p><fmt:message key="jsp.submit.review.info4"/></p>
        	</div>
        	<div class="panel-body">

        <table align="center" class="miscTable" width="80%">
<%
		//loop through the list of review JSPs
		while(reviewIterator.hasNext())
     	{
            //remember, the keys of the reviewJSPs hashmap is in the
            //format: stepNumber.pageNumber
            String stepAndPage = (String) reviewIterator.next();

			//finally get the path to the review JSP (the value)
			String reviewJSP = (String) reviewJSPs.get(stepAndPage);
	%>
		    <tr>
                <td class="evenRowOddCol">
				<%--Load the review JSP and pass it step & page info--%>
				<jsp:include page="<%=reviewJSP%>">
					<jsp:param name="submission.jump" value="<%=stepAndPage%>" />	
				</jsp:include>
				</td>
			</tr>
<%
    	}

%>
                </table>
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
