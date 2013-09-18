<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Footer for home page
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>

<%
    String sidebar = (String) request.getAttribute("dspace.layout.sidebar");
	boolean feedEnabled = ConfigurationManager.getBooleanProperty("webui.feed.enable");
	String feedData = "NONE";
	if (feedEnabled)
	{
	    feedData = "ALL:" + ConfigurationManager.getProperty("webui.feed.formats");
	}
%>

			</div>
			<% if (sidebar != null) { %>
			<div class="col-lg-3">
			<%=sidebar %>
			</div>
			<% } %>

			</div>
			</div>
        </div> <!-- End Container -->
        
        <footer class="container">
        <hr />
        <div class="row">
          <div class="col-lg-12">
            
            <ul class="list-unstyled">
              <li class="pull-right">
              			<a href="#top">Back to top</a>
              </li>
              <li><a href="<%= request.getContextPath() %>/feedback"><fmt:message key="jsp.layout.footer-default.feedback"/></a></li>
              <li><a href="<%= request.getContextPath() %>/subscribe"><fmt:message key="jsp.layout.navbar-default.receive"/></a></li>
            </ul>
            
            <p><fmt:message key="jsp.layout.footer-default.text"/>
                <a href="<%= request.getContextPath() %>/htmlmap"></a>
            </p>
            <p>&nbsp;</p>
            <p>Design by <a href="http://www.lyncode.com"><img width="100" src="<%= request.getContextPath() %>/image/lyncode.png" /></a></p>
            <p>Code licensed under the <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache License v2.0</a>.</p>
            <p>Based on <a href="http://bootswatch.com/readable/">Readable</a> which uses <a href="http://getbootstrap.com">Bootstrap</a>. Icons from <a href="http://fortawesome.github.io/Font-Awesome/">Font Awesome</a>.
            
          </div>
        </div>
        
      </footer>
    </body>
</html>