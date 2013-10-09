<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Show the user a license which they may grant or reject
  -
  - Attributes to pass in:
  -    license          - the license text to display
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
 
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);    

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);

    String license = (String) request.getAttribute("license");
%>

<dspace:layout locbar="off"
               titlekey="jsp.submit.show-license.title"
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
				<p><fmt:message key="jsp.submit.show-license.info1"/></p>
				<p><dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") +\"#license\"%>"><fmt:message key="jsp.morehelp"/></dspace:popup></p>
				<p><fmt:message key="jsp.submit.show-license.info2"/></p>
			</div>
			<div class="panel-body">
        <%-- <p><strong>Not granting the license will not delete your submission.</strong>
        Your item will remain in your "My DSpace" page.  You can then either remove
        the submission from the system, or agree to the license later once any
        queries you might have are resolved.</p> --%>

        <table class="miscTable" align="center">
            <tr>
                <td class="oddRowEvenCol">
                    <pre><%= license %></pre>
                </td>
            </tr>
        </table>
			</div>
			<div class="panel-footer">
				<div class="pull-right">
					<button class="btn btn-success" type="submit" name="submit_grant"><fmt:message key="jsp.submit.show-license.grant.button"/></button>
					<button class="btn btn-danger" type="submit" name="submit_reject"><fmt:message key="jsp.submit.show-license.notgrant.button"/></button>
				</div>	
				<div class="clearfix"></div>
			</div>
		</div>
    </form>
</dspace:layout>
