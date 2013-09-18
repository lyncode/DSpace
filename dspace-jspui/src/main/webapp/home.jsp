<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Home page JSP
  -
  - Attributes:
  -    communities - Community[] all communities in DSpace
  -    recent.submissions - RecetSubmissions
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="java.io.File" %>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Locale"%>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.I18nUtil" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.app.webui.components.RecentSubmissions" %>
<%@ page import="org.dspace.content.Community" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.core.NewsManager" %>
<%@ page import="org.dspace.browse.ItemCounter" %>
<%@ page import="org.dspace.content.DCValue" %>
<%@ page import="org.dspace.content.Item" %>

<%
    Community[] communities = (Community[]) request.getAttribute("communities");

    Locale[] supportedLocales = I18nUtil.getSupportedLocales();
    Locale sessionLocale = UIUtil.getSessionLocale(request);
    Config.set(request.getSession(), Config.FMT_LOCALE, sessionLocale);
    String topNews = NewsManager.readNewsFile(LocaleSupport.getLocalizedMessage(pageContext, "news-top.html"));
    String sideNews = NewsManager.readNewsFile(LocaleSupport.getLocalizedMessage(pageContext, "news-side.html"));

    boolean feedEnabled = ConfigurationManager.getBooleanProperty("webui.feed.enable");
    String feedData = "NONE";
    if (feedEnabled)
    {
        feedData = "ALL:" + ConfigurationManager.getProperty("webui.feed.formats");
    }
    
    ItemCounter ic = new ItemCounter(UIUtil.obtainContext(request));

    RecentSubmissions submissions = (RecentSubmissions) request.getAttribute("recent.submissions");
%>

<dspace:layout locbar="nolink" titlekey="jsp.home.title" feedData="<%= feedData %>">

	<div class="row">
		<div class="col-lg-12">
    		<form action="<%= request.getContextPath() %>/simple-search" method="get">
    			<div class="form-group">
                  <div class="input-group">
                    <span class="input-group-addon"><i class="icon-search"></i></span>
                    <input type="text" name="query" class="form-control" />
                    <span class="input-group-btn">
                      <button type="submit" class="btn btn-default" type="button"><fmt:message key="jsp.general.search.button"/></button>
                    </span>
                  </div>
                </div>
    		</form>
		</div>
	</div>
    
    <div class="row">
          <div class="col-lg-12">
          <%= topNews %>
          </div>
    </div>
    
    <div class="row">
<%
if (communities != null && communities.length != 0)
{
%>
          <div class="col-lg-6">
            <h3><fmt:message key="jsp.home.com1"/></h3>
            
          	<ul class="list-group">
<%

    for (int i = 0; i < communities.length; i++)
    {
%>

                <li class="list-group-item">
                  
                  <a href="<%= request.getContextPath() %>/handle/<%= communities[i].getHandle() %>"><%= communities[i].getMetadata("name") %></a>
<%
        if (ConfigurationManager.getBooleanProperty("webui.strengths.show"))
        {
%>
            		<span class="badge"><%= ic.getCount(communities[i]) %></span>
<%
        }

%>
                </li>
<%
    }
%>
              </ul>
          </div>
<%
}
%>

<%
if (submissions != null && submissions.count() > 0)
{
%>
          <div class="col-lg-6">
            <h3><fmt:message key="jsp.collection-home.recentsub"/></h3>
          	<ul>
<%
    for (Item item : submissions.getRecentSubmissions())
    {
        DCValue[] dcv = item.getMetadata("dc", "title", null, Item.ANY);
        String displayTitle = "Untitled";
        if (dcv != null & dcv.length > 0)
        {
            displayTitle = dcv[0].value;
        }
%>
                <li>
                            <a href="<%= request.getContextPath() %>/handle/<%=item.getHandle() %>"><%= displayTitle%> </a>
                </li>
<%
     }
%>
          </ul>
          </div>
<%
}
%>
    </div>
    
    
    <div class="row">
    	<div class="col-lg-12">
    		<%@ include file="discovery/static-sidebar-facet.jsp" %>
    	</div>
    </div>
</dspace:layout>
