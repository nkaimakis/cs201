/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/9.0.0.M15
 * Generated at: 2017-05-06 16:49:38 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.Set;
import java.util.HashSet;
import data.Event;
import data.MySQLDriver;
import data.User;
import data.StringConstants;
import data.MySQLDriver;

public final class big_005ffeed_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(1);
    _jspx_dependants.put("/jsp/navbar.jsp", Long.valueOf(1492467370000L));
  }

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("java.util.HashSet");
    _jspx_imports_classes.add("data.Event");
    _jspx_imports_classes.add("java.util.Set");
    _jspx_imports_classes.add("data.User");
    _jspx_imports_classes.add("data.MySQLDriver");
    _jspx_imports_classes.add("data.StringConstants");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    final java.lang.String _jspx_method = request.getMethod();
    if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
      return;
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("\t<link rel=\"stylesheet\" href=\"../css/main.css\">\n");
      out.write("\t<link rel=\"stylesheet\" href=\"../css/navbar.css\">\n");
      out.write("\t<link href=\"https://fonts.googleapis.com/css?family=Lato:700i\" rel=\"stylesheet\">\n");
      out.write("\t<script src = \"../js/navbar.js\"></script>\n");
      out.write("</head>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("<title>Cinemate</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");
 MySQLDriver sql = (MySQLDriver)session.getAttribute("sql");
	User loggedInUser = sql.getLoggedInUser();

      out.write("\n");
      out.write("\t<div id = \"navBar\">\n");
      out.write("\t\t<a href = \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/jsp/big_feed.jsp\">\n");
      out.write("\t\t\t<img src = \"../img/navbar/feed_icon.png\" id = \"feed_icon\" class = \"clickable\" alt = \"feed\" title = \"View feed\">\n");
      out.write("\t\t</a>\n");
      out.write("\t\t<a href = \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/jsp/user_profile.jsp?");
      out.print(StringConstants.USERNAME);
      out.write('=');
      out.print(loggedInUser.getUsername());
      out.write("\">\n");
      out.write("\t\t\t<img src = ");
      out.print( loggedInUser.getImage() );
      out.write(" id = \"logged_in_user_icon\" class = \"clickable\" alt = \"user_icon\" title = \"View profile\">\n");
      out.write("\t\t</a>\n");
      out.write("\t\t<!-- container for our search bar -->\n");
      out.write("\t\t<div id = \"search_bar\">\n");
      out.write("\t\t\t<!-- Actual search input which will be dynamically updated (search type image and placeholder text)  -->\n");
      out.write("\t\t\t<input type = \"search\" id = \"search_input\" placeholder = \"Search movies\" size = \"3\" width = \"40px\">\n");
      out.write("\t\t\t<input id = \"search_type\" style = \"display:none;\" value = \"");
      out.print( StringConstants.MOVIE );
      out.write("\"></input>\n");
      out.write("\t\t\t<!-- the search type image -->\n");
      out.write("\t\t\t<img src=\"../img/navbar/clapperboard_icon.png\" id = \"toggleable_search_type\" class = \"clickable\" onclick = \"toggleSearch()\" title = \"Toggle search type\">\n");
      out.write("\t\t</div>\n");
      out.write("\t\t<!-- pass in the string used for the parameter key in the ajax request -->\n");
      out.write("\t\t<img src = \"../img/navbar/search_icon.png\" id = \"search_icon\" class = \"clickable\" alt = \"search\" title = \"Search\" onclick = \"search('");
      out.print(StringConstants.SEARCH_PARAM);
      out.write("', 'search_type')\">\n");
      out.write("\t\t<a href = \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/jsp/kaimakis_login.jsp\">\n");
      out.write("\t\t\t<img src = \"../img/navbar/exit_icon.jpg\" id = \"exit_icon\" class = \"clickable\" alt = \"exit\" title = \"Exit\">\n");
      out.write("\t\t</a>\n");
      out.write("\t\t<a href = \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/jsp/kaimakis_login.jsp\">\n");
      out.write("\t\t\t<img src = \"../img/navbar/logout_icon.png\" id = \"logout_icon\" class = \"clickable\" alt = \"logout\" title = \"Log out\">\n");
      out.write("\t\t</a>\n");
      out.write("\t\t<div id = \"cinemate_title\">\n");
      out.write("\t\t\tCinemate\n");
      out.write("\t\t</div>\n");
      out.write("\t</div>\n");
      out.write("</body>\n");
      out.write("</html>");
      out.write('\n');
 sql = (MySQLDriver)session.getAttribute("sql");
      out.write("\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("\t<title>Feed</title>\n");
      out.write("\t<link rel=\"stylesheet\" href=\"../css/main.css\">\n");
      out.write("\t<link rel=\"stylesheet\" href=\"../css/big_feed.css\">\n");
      out.write("\t<link href=\"https://fonts.googleapis.com/css?family=Lato:700i\" rel=\"stylesheet\">\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("\n");
      out.write("\t<div id = \"big_feed_container\">\n");
      out.write("\t\t<table id = \"big_feed\">\n");
      out.write("\t\t\t<tbody>\n");
      out.write("\t\t\t");

				Set<Event> events = new HashSet<>();
				sql = (MySQLDriver)session.getAttribute("sql");	
				User u = sql.getLoggedInUser();
				events.addAll(sql.getFeedByUsername(u.getUsername(), u.getFollowing()));

				for (Event event : events) {
					User user = sql.getUserByUsername(event.getUsername());
			
      out.write("\n");
      out.write("\t\t\t\t<tr>\n");
      out.write("\t\t\t\t\t<td>\n");
      out.write("\t\t\t\t\t<a href = \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/jsp/user_profile.jsp?");
      out.print( StringConstants.USERNAME );
      out.write('=');
      out.print(user.getUsername());
      out.write("\">\n");
      out.write("\t\t\t\t\t\t<img src = \"");
      out.print( user.getImage() );
      out.write("\" class = \"round clickable\" alt = \"Profile Image Not Found\" title = \"");
      out.print(user.getFullname() );
      out.write("\">\n");
      out.write("\t\t\t\t\t</a>\n");
      out.write("\t\t\t\t\t</td>\n");
      out.write("\t\t\t\t\t<td><img src = \"");
      out.print( StringConstants.ACTION_IMG_EXT+ event.getActionIcon() );
      out.write("\" title = \"");
      out.print( event.getAction().toLowerCase() );
      out.write("\"></td>\n");
      out.write("\t\t\t\t\t<td>\n");
      out.write("\t\t\t\t\t<a href = \"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/jsp/movie_profile.jsp?");
      out.print(StringConstants.TITLE);
      out.write('=');
      out.print(event.getMovieTitle());
      out.write("\">\n");
      out.write("\t\t\t\t\t<img src = \"");
      out.print( event.getMovie().getImage() );
      out.write("\" class = \"clickable\" title = \"");
      out.print( event.getMovieTitle() );
      out.write("\">\n");
      out.write("\t\t\t\t\t</a>\n");
      out.write("\t\t\t\t\t</td>\n");
      out.write("\t\t\t\t\t<td>\n");
      out.write("\t\t\t\t\t<a>");
      out.print(event.getTime());
      out.write("</a>\n");
      out.write("\t\t\t\t\t</td>\n");
      out.write("\t\t\t\t</tr>\n");
      out.write("\t\t\t");
 } 
      out.write("\n");
      out.write("\t\t\t</tbody>\n");
      out.write("\t\t</table>\n");
      out.write("\t</div>\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}