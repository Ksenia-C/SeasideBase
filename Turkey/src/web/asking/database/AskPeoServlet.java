package web.asking.database;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.LocalDataBase;

@WebServlet("/AskPeoServlet")
public class AskPeoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] name_peo = { "StudentName", "StudentSurname", "StudentPatronymic", "StudentBirthday", "StudentCity" };

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");

		// getting searching information about people
		String[] aboutPeople = new String[name_peo.length];
		for (int i = 0; i < name_peo.length; i++) {
			String what = request.getParameter(name_peo[i]).toLowerCase().trim();
			if (what.length() == 0) {
				aboutPeople[i] = null;
			} else {
				aboutPeople[i] = what;
			}

		}
		String pe_add = request.getParameter("StudentAdd");
		if (pe_add.length() == 0) {
			pe_add = null;
		}
		CollectParameter colli = new CollectParameter();
		colli.getForpeo(pe_add);

		// requests to data base
		String result = "";
		LocalDataBase ldb = new LocalDataBase();
		try {
			ldb.open_connection();

			// part of code to search participations
			// request.setAttribute("link_to_par", "yes");
			// request.setAttribute("people_parameters", saveParametersParticipation);

			String writeTable = ldb.search_people(aboutPeople, colli.peo_add);
			result += writeTable;
		} catch (SQLException e) {
			e.printStackTrace();
			result = "somethin wrong<br>";
		} finally {
			ldb.close_connection();
		}

		// show results to user
		request.setAttribute("exact answer", result);
		request.getRequestDispatcher("many_tables.jsp").forward(request, response);

	}

}
