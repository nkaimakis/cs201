package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.DataStorage;

/**
 * Servlet implementation class MovieMenuServlet
 */
@WebServlet("/MovieMenuServlet")
public class MovieMenuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieMenuServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		DataStorage ds = (DataStorage)session.getAttribute("dataStorage");
		if(request.getParameter("searchByActor") != null){
			session.setAttribute("moviesBy", "actor");
			response.sendRedirect("moviesBy.jsp");
		}else if(request.getParameter("searchByTitle") != null){
			session.setAttribute("moviesBy", "title");
			response.sendRedirect("moviesBy.jsp");
		}else if(request.getParameter("searchByGenre") != null){
			session.setAttribute("moviesBy", "genre");
			response.sendRedirect("moviesBy.jsp");
		}else if(request.getParameter("mainMenu") != null){
			response.sendRedirect("MainMenu.jsp");
		}else{
			response.sendRedirect("MovieSearchMenu.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}


