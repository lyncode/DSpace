<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Submission complete message
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.content.InProgressSubmission" %>
<%@ page import="org.dspace.content.Collection"%>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>


<%
    request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);

	//get collection
    Collection collection = subInfo.getSubmissionItem().getCollection();
%>

<dspace:layout locbar="off" titlekey="jsp.submit.complete.title">
		<div class="row margin-top-2">
			<div class="col-lg-12">
	        	<jsp:include page="/submit/progressbar.jsp" />
			</div>
		</div>
        <div class="panel panel-default margin-top-2">
			<div class="panel-heading">
				<%-- <h1>Submit: Submission Complete!</h1> --%>
				<h3><fmt:message key="jsp.submit.complete.heading"/></h3>
    
			    <%-- FIXME    Probably should say something specific to workflow --%>
			    <%-- <p>Your submission will now go through the workflow process designated for 
			    the collection to which you are submitting.    You will receive e-mail
			    notification as soon as your submission has become a part of the collection,
			    or if for some reason there is a problem with your submission. You can also
			    check on the status of your submission by going to the My DSpace page.</p> --%>
				<p><fmt:message key="jsp.submit.complete.info"/></p> 
    		</div>
    		<div class="panel-body">
    			<p><a href="<%= request.getContextPath() %>/mydspace"><fmt:message key="jsp.submit.complete.link"/></a></p>
				<p><a href="<%= request.getContextPath() %>/community-list"><fmt:message key="jsp.community-list.title"/></a></p>
     		</div>
    		<div class="panel-footer">
    			<center>	
			    <form action="<%= request.getContextPath() %>/submit" method="post" onkeydown="return disableEnterKey(event);">
			        <input type="hidden" name="collection" value="<%= collection.getID() %>"/>
				    <button class="btn btn-primary" type="submit" name="submit"><fmt:message key="jsp.submit.complete.again"/></button>
			    </form>
			    </center>
    		</div>
    	</div>	
     
</dspace:layout>
