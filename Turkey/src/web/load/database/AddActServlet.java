package web.load.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import web.LocalDataBase;

@WebServlet("/AddActServlet")
@MultipartConfig
public class AddActServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] nameActParams = { "EventName", "EventDateMain", "EventDuration", "EventPlace" };
	private String[] namePeoParams = { "StudentName", "StudentSurname", "StudentPatronymic", "StudentBirthday",
			"StudentCity" };

	Integer maxid = 1;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		// load table
		Part file = request.getPart("TableSource");

		// read main parameters
		String[] aboutEvent = new String[nameActParams.length];
		for (int i = 0; i < nameActParams.length; i++) {
			aboutEvent[i] = request.getParameter(nameActParams[i]).trim().toLowerCase();
			if (aboutEvent[i].length() == 0) {
				aboutEvent[i] = null;
			}
		}
		if (aboutEvent[2] == null || Integer.parseInt(aboutEvent[2]) < 1) {
			aboutEvent[2] = "1";
		}
		String ev_add = request.getParameter("EventAdd");
		if (ev_add.length() == 0) {
			ev_add = null;
		}
		CollectInput colli = new CollectInput();
		colli.getForAct(ev_add);
		Integer[] aboutPeople = new Integer[0];

		if (request.getParameter("Conny") != null) {
			aboutPeople = new Integer[namePeoParams.length];
			for (int i = 0; i < namePeoParams.length; i++) {
				String what = request.getParameter(namePeoParams[i]);
				if (what.length() == 0) {
					aboutPeople[i] = null;
				} else {
					aboutPeople[i] = Integer.parseInt(what);
				}
			}
		}

		// if titles are in
		boolean isT = request.getParameter("ActTitles") == null ? false : true;

		// requests to data base
		String result = "ok";
		LocalDataBase ldb = new LocalDataBase();
		try {
			ldb.open_connection();
			maxid = ldb.get_max_act_id();

			// save table
			if (request.getParameter("all") != null) {
				InputStream inp = file.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
				String line;
				boolean fir = true;
				int numLine = 0;
				while ((line = reader.readLine()) != null) {
					numLine++;
					String[] tableValues = line.split(";");
					int num = tableValues.length;
					if (fir) {
						fir = false;
						ldb.create_prepared_for_own_table(maxid, num);
						ldb.create_table(maxid, num);
					}
					ldb.add_string_to_table(numLine, num, tableValues);
				}
			}

			// save data about event
			ldb.add_act(maxid, aboutEvent, request.getParameter("all") != null);
			for (Map.Entry<String, String> entry : colli.act_add.entrySet()) {
				ldb.add_act_optionals(maxid, entry.getKey(), entry.getValue());
			}

			// save participators
			int numLine = 0;
			if (aboutPeople.length != 0) {
				InputStream inp = file.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
				String line;
				while ((line = reader.readLine()) != null) {
					numLine++;
					if (isT) {
						isT = false;
						continue;
					}
					String[] tableValues = line.split(";");
					String[] values = new String[namePeoParams.length];
					for (int i = 0; i < namePeoParams.length; i++) {
						if (aboutPeople[i] == null) {
							values[i] = null;
						} else {
							values[i] = tableValues[aboutPeople[i] - 1].toLowerCase().trim();
						}
					}
					ldb.try_find_and_save(maxid, numLine, values, request.getParameter("all") != null);

				}
			}
			maxid++;

		} catch (Exception e) {
			e.printStackTrace();
			result = "somethin wrong with connect";
		} finally {
			ldb.close_connection();
		}
		// выход
		request.setAttribute("exact answer", result);
		request.getRequestDispatcher("answer_on_insert.jsp").forward(request, response);

	}

}
