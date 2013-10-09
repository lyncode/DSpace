<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - License rejected page
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<% request.setAttribute("LanguageSwitch", "hide"); %>

<dspace:layout titlekey="jsp.submit.license-rejected.title">
		
		<div class="panel panel-default margin-top-2">
			<div class="panel-heading">
				<h3><fmt:message key="jsp.submit.license-rejected.heading"/></h3>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-lg-offset-1 col-lg-10">


					    <%-- <h1>Submit: License Rejected</h1> --%>
						
					    <%-- <p>You have chosen not to grant the license to distribute your submission
					    via the DSpace system.  Your submission has not been deleted and can be
					    accessed from the My DSpace page.</p> --%>
						<p class="margin-top-2"><fmt:message key="jsp.submit.license-rejected.info1"/></p>
					    
					    <%-- <p>If you wish to contact us to discuss the license, please use one
					    of the methods below:</p> --%>
						<p><fmt:message key="jsp.submit.license-rejected.info2"/></p>
					    
					    <div class="margin-top-2">
					    	<dspace:include page="/components/contact-info.jsp" />
					    </div>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<center>
					<a class="btn btn-primary" href="<%= request.getContextPath() %>/mydspace"><fmt:message key="jsp.mydspace.general.goto-mydspace"/></a>
				</center>
			</div>
		</div>
</dspace:layout>
