package web.asking.database;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.LocalDataBase;

@WebServlet("/AskActServlet")
public class AskActServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] name_act = { "EventName", "EventDate", "EventPlace" };

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");

		// getting data about event
		String[] aboutevent = new String[name_act.length];
		for (int i = 0; i < name_act.length; i++) {
			String what = request.getParameter(name_act[i]);
			if (what.length() == 0) {
				aboutevent[i] = null;
			} else {
				aboutevent[i] = what.toLowerCase().trim();
			}
		}
		String act_add = request.getParameter("EventAdd");
		if (act_add.length() == 0) {
			act_add = null;
		}
		CollectParameter colli = new CollectParameter();
		colli.getForact(act_add);

		// requests to data base
		String result = "";
		LocalDataBase ldb = new LocalDataBase();
		try {
			ldb.open_connection();
			result += ldb.search_events(aboutevent, colli.act_add);
		} catch (SQLException e) {
			e.printStackTrace();
			result = "somethin wrong with qyery";
		} finally {
			ldb.close_connection();
		}

		// show results to user
		request.setAttribute("exact answer", result);
		request.getRequestDispatcher("many_tables.jsp").forward(request, response);

	}

}
