<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Show uploaded file (single-file submission mode)
  -
  - Attributes to pass in
  -    just.uploaded    - Boolean indicating whether the user has just
  -                       uploaded a file OK
  -    show.checksums   - Boolean indicating whether to show checksums
  -
  - FIXME: Merely iterates through bundles, treating all bit-streams as
  -        separate documents.  Shouldn't be a problem for early adopters.
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
    
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.submit.AbstractProcessingStep" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.Bitstream" %>
<%@ page import="org.dspace.content.BitstreamFormat" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);    

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);

    boolean justUploaded = ((Boolean) request.getAttribute("just.uploaded")).booleanValue();
    boolean showChecksums = ((Boolean) request.getAttribute("show.checksums")).booleanValue();

    // Get the bitstream
    Bitstream[] all = subInfo.getSubmissionItem().getItem().getNonInternalBitstreams();
    Bitstream bitstream = all[0];
    BitstreamFormat format = bitstream.getFormat();
%>


<dspace:layout locbar="off"
               titlekey="jsp.submit.show-uploaded-file.title"
               nocache="true">

    <form class="margin-top" action="<%= request.getContextPath() %>/submit" method="post" onkeydown="return disableEnterKey(event);">
               
		<div class="row margin-top-2">
			<div class="col-lg-12">
	        	<jsp:include page="/submit/progressbar.jsp" />
			</div>
		</div>
		
<% 
	if (justUploaded) {
%>
	<div class="margin-top-2 alert alert-success">
				<p><strong><fmt:message key="jsp.submit.show-uploaded-file.info1"/></strong></p>
	</div>
<% 
	} 
%>
		
		<%= SubmissionController.getSubmissionParameters(context, request) %>
        <div class="panel panel-default">
			<div class="panel-heading">
<%
    if (justUploaded)
    {
%>
				<h3><fmt:message key="jsp.submit.show-uploaded-file.heading1"/></h3>
<%
    }
    else
    {
%>
		        <%-- <h1>Submit: Uploaded File</h1> --%>
				<h3><fmt:message key="jsp.submit.show-uploaded-file.heading2"/></h3>
<%
    }
%>
		        <%-- <p>Here are the details of the file you have uploaded.  Please check the
		        details before going to the next step.
		        &nbsp;&nbsp;&nbsp;<dspace:popup page="/help/index.html#uploadedfile">(More Help...)</dspace:popup></p> --%>
		
				<p><fmt:message key="jsp.submit.show-uploaded-file.info2"/>
		        &nbsp;&nbsp;&nbsp;<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\")+ \"#uploadedfile\"%>"><fmt:message key="jsp.morehelp"/></dspace:popup></p>
			</div>
			<div class="panel-body">
        <table class="table table-striped" align="center">
            <tr>
                <%-- <th class="oddRowOddCol">File</th>
                <th class="oddRowEvenCol">Size</th>
                <th class="oddRowOddCol">File Format</th> --%>
                
				<th id="t1" class="oddRowOddCol"><fmt:message key="jsp.submit.show-uploaded-file.file"/></th>
                <th id="t2" class="oddRowEvenCol"><fmt:message key="jsp.submit.show-uploaded-file.size"/></th>
                <th id="t3" class="oddRowOddCol"><fmt:message key="jsp.submit.show-uploaded-file.format"/></th>

<%
    if (showChecksums)
    {
%>
                <%-- <th class="oddRowEvenCol">Checksum</th> --%>
				<th id="t4" class="oddRowEvenCol"><fmt:message key="jsp.submit.show-uploaded-file.checksum"/></th>
<%
    }
%>
            </tr>
            <tr>
                <td headers="t1" class="evenRowOddCol"><a href="<%= request.getContextPath() %>/retrieve/<%= bitstream.getID() %>/<%= org.dspace.app.webui.util.UIUtil.encodeBitstreamName(bitstream.getName()) %>" target="_blank"><%= bitstream.getName() %></a></td>
                <td headers="t2" class="evenRowEvenCol"><fmt:message key="jsp.submit.show-uploaded-file.size-in-bytes">
                    <fmt:param><fmt:formatNumber><%= bitstream.getSize() %></fmt:formatNumber></fmt:param>
                </fmt:message></td>
                <td headers="t3" class="evenRowOddCol">
                    <%= bitstream.getFormatDescription() %>
<%    
    if (format.getSupportLevel() == 0)
    { %>
      <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.formats\") +\"#unsupported\"%>">(<fmt:message key="jsp.submit.show-uploaded-file.notSupported"/>)</dspace:popup>
<%  }
    else if (format.getSupportLevel() == 1)
    { %>
      <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.formats\") +\"#known\"%>">(<fmt:message key="jsp.submit.show-uploaded-file.known"/>)</dspace:popup>
<%  }
    else
    { %>
      <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.formats\") +\"#supported\"%>">(<fmt:message key="jsp.submit.show-uploaded-file.supported"/>)</dspace:popup>
<%  } %>
                </td>
<%
    if (showChecksums)
    {
%>
                <td headers="t4" class="evenRowEvenCol">
                    <code><%= bitstream.getChecksum() %> (<%= bitstream.getChecksumAlgorithm() %>)</code>
                </td>
<%
    }
%>
            </tr>
        </table>

        <center>

            <p>
               <%--  <input type="submit" name="submit_format_<%= bitstream.getID() %>" value="Click here if this is the wrong format" /> --%>
			    <button class="btn btn-danger" type="submit" name="submit_format_<%= bitstream.getID() %>"><fmt:message key="jsp.submit.show-uploaded-file.click1.button"/></button>
            </p>
        </center>

        <center>
            <p>
                <%-- <input type="submit" name="submit_remove_<%= bitstream.getID() %>" value="Click here if this is the wrong file"> --%>
				<button class="btn btn-danger" type="submit" name="submit_remove_<%= bitstream.getID() %>" ><fmt:message key="jsp.submit.show-uploaded-file.click2.button"/></button>
            </p>
        </center>

        <br/>

		<p class="uploadHelp"><fmt:message key="jsp.submit.show-uploaded-file.info3"/></p>
        <ul class="uploadHelp">
			<li class="uploadHelp"><fmt:message key="jsp.submit.show-uploaded-file.info4"/></li>
<%
    if (showChecksums)
    {
%>	
			<li class="uploadHelp"><fmt:message key="jsp.submit.show-uploaded-file.info5"/>
            <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#checksum\"%>"><fmt:message key="jsp.submit.show-uploaded-file.info6"/></dspace:popup></li>
<%
    }
    else
    {
%>
  		<li class="uploadHelp"><fmt:message key="jsp.submit.show-uploaded-file.info7"/>
            <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#checksum\"%>"><fmt:message key="jsp.submit.show-uploaded-file.info8"/></dspace:popup></li>
<%
    }
%>
        </ul>
        
        <% if (!showChecksums) { %>
        <center>
        	<button class="btn btn-info" type="submit" name="submit_show_checksums"><fmt:message key="jsp.submit.show-uploaded-file.show.button"/></button>
        </center>
        <% } %>
        
        <br />
        
        
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
