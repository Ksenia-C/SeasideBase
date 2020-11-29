package web.change.database;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import web.LocalDataBase;

@WebServlet("/CanActServlet")
public class CanActServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] name_act = { "EventName", "EventDateMain", "EventDuration", "EventPlace" };
	String[] name_basa = { "title", "date_main", "date_end" };

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		// getting parameters
		String[] aboutEvent = new String[name_act.length];
		for (int i = 0; i < name_act.length; i++) {
			aboutEvent[i] = request.getParameter(name_act[i]).trim().toLowerCase();
			if (aboutEvent[i].length() == 0) {
				aboutEvent[i] = null;
			}
		}
		Integer id_act = 0;
		// requests to data base
		String result = "ok";
		LocalDataBase ldb = new LocalDataBase();
		try {
			ldb.open_connection();
			id_act = Integer.parseInt(request.getParameter("id"));
			if (request.getParameter("del") != null) {
				ldb.delete_event(id_act);
			} else {
				ldb.change_event_main(id_act, aboutEvent);
				ldb.change_event_optional(id_act, request);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "somethin wrong with connect";
		} finally {
			ldb.close_connection();
		}

		// show results
		request.setAttribute("exact answer", result);
		if (request.getParameter("del") == null) {
			request.getRequestDispatcher("writeactivity?id=" + id_act).forward(request, response);
		} else {
			request.getRequestDispatcher("answer_on_insert.jsp").forward(request, response);
		}
	}

}
