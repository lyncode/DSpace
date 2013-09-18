<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Default navigation bar
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="/WEB-INF/dspace-tags.tld" prefix="dspace" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.Community" %>
<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.browse.BrowseIndex" %>
<%@ page import="org.dspace.browse.BrowseInfo" %>
<%@ page import="java.util.Map" %>
<%
    // Is anyone logged in?
    EPerson user = (EPerson) request.getAttribute("dspace.current.user");
    String siteName = ConfigurationManager.getProperty("dspace.name");

    // Is the logged in user an admin
    Boolean admin = (Boolean)request.getAttribute("is.admin");
    boolean isAdmin = (admin == null ? false : admin.booleanValue());

    // Get the current page, minus query string
    String currentPage = UIUtil.getOriginalURL(request);
    int c = currentPage.indexOf( '?' );
    if( c > -1 )
    {
        currentPage = currentPage.substring( 0, c );
    }

    // E-mail may have to be truncated
    String navbarEmail = null;

    if (user != null)
    {
        navbarEmail = user.getEmail();
        if (navbarEmail.length() > 18)
        {
            navbarEmail = navbarEmail.substring(0, 17) + "...";
        }
    }
    
    // get the browse indices
    
	BrowseIndex[] bis = BrowseIndex.getBrowseIndices();
    BrowseInfo binfo = (BrowseInfo) request.getAttribute("browse.info");
    String browseCurrent = "";
    if (binfo != null)
    {
        BrowseIndex bix = binfo.getBrowseIndex();
        // Only highlight the current browse, only if it is a metadata index,
        // or the selected sort option is the default for the index
        if (bix.isMetadataIndex() || bix.getSortOption() == binfo.getSortOption())
        {
            if (bix.getName() != null)
    			browseCurrent = bix.getName();
        }
    }
%>

	<div class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a href="<%= request.getContextPath() %>/" class="logo navbar-brand"><img src="<%= request.getContextPath() %>/image/logo2.gif" /> </a>
          <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <div class="navbar-collapse collapse" id="navbar-main">
          <ul class="nav navbar-nav">
            <li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="dspace-browse"><fmt:message key="jsp.layout.navbar-default.browse"/> <span class="caret"></span></a>
              <ul class="dropdown-menu" aria-labelledby="dspace-browse">
                <li><a tabindex="-1" href="<%= request.getContextPath() %>/community-list"><fmt:message key="jsp.layout.navbar-default.communities-collections"/></a></li>
                <li class="divider"></li>
                <%
					for (int i = 0; i < bis.length; i++)
					{
						BrowseIndex bix = bis[i];
						String key = "browse.menu." + bix.getName();
				%>
                <li><a tabindex="-1" href="<%= request.getContextPath() %>/browse?type=<%= bix.getName() %>"><fmt:message key="<%= key %>"/></a></li>
				<%	
					}
				%>
              </ul>
            </li>
          </ul>

          <ul class="nav navbar-nav navbar-right">
<%
    if (user != null)
    {
%>
			<li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="dspace-user">
              	<fmt:message key="jsp.layout.navbar-default.loggedin">
					<fmt:param><%= navbarEmail %></fmt:param>
  				</fmt:message> <span class="caret"></span>
  			  </a>
              <ul class="dropdown-menu" aria-labelledby="dspace-browse">
                <li><a tabindex="-1" href="<%= request.getContextPath() %>/logout"><i class="icon-off"></i> <fmt:message key="jsp.layout.navbar-default.logout"/></a></li>
                <li class="divider"></li>
                <li><a tabindex="-1" href="<%= request.getContextPath() %>/mydspace"><i class="icon-briefcase"></i> <fmt:message key="jsp.layout.navbar-default.users"/></li>
  				
<%
  if (isAdmin)
  {
%>  
				<li><a tabindex="-1" href="<%= request.getContextPath() %>/dspace-admin"><i class="icon-list"></i> <fmt:message key="jsp.administer"/></a></li>
<%
  }
%>
				<li class="divider"></li>
				<li><a tabindex="-1" href="<%= request.getContextPath() %>/profile"><i class="icon-user"></i> <fmt:message key="jsp.layout.navbar-default.edit"/></a></li>
                <li><a tabindex="-1" href="<%= request.getContextPath() %>/subscribe"><i class="icon-envelope"></i> <fmt:message key="jsp.layout.navbar-default.receive"/></a></li>
				
              </ul>
            </li>
<%
    } else {
%>
            <li><a href="<%= request.getContextPath() %>/mydspace"><fmt:message key="jsp.layout.navbar-default.users"/></a></li>
<% } %>
          </ul>
        </div>
      </div>
    </div>