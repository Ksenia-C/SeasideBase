package web.load.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import web.LocalDataBase;

@WebServlet("/AddPeoServlet")
@MultipartConfig
public class AddPeoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// titles of fields from html
	private String[] namePeoParams = { "StudentName", "StudentSurname", "StudentPatronymic", "StudentBirthday",
			"StudentCity" };
	Integer maxid = 1;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");

		// load table
		Part file = request.getPart("TableSource");

		// getting input parameters
		Integer[] aboutPeople = new Integer[namePeoParams.length];
		for (int i = 0; i < namePeoParams.length; i++) {
			String value = request.getParameter(namePeoParams[i]).trim().toLowerCase();
			if (value.length() == 0 || value.equals("0")) {
				aboutPeople[i] = null;
			} else {
				aboutPeople[i] = Integer.parseInt(value);
			}
		}
		String pe_add = request.getParameter("StudentAdd");
		if (pe_add.length() == 0) {
			pe_add = null;
		}
		CollectInput colli = new CollectInput();
		colli.getForHomo(pe_add);

		// ignore first string
		boolean isT = request.getParameter("ActTitles") == null ? false : true;

		// requests to data base
		String result = "ok"; // the output page. Need to be changed
		String InsertAct = "nonw";

		// class for working with data base
		LocalDataBase ldb = new LocalDataBase();
		try {
			ldb.open_connection();

			// to get id for new persons
			// try to organize in sqrt decomposition in finding min elemnt that dousn't meet
			// in sequence
			// or time to time decrease ids to be stepped one by one
			maxid = ldb.get_max_human_id();

			// read from table
			InputStream inp = file.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
			String line;
			while ((line = reader.readLine()) != null) {
				// string with titles
				if (isT) {
					isT = false;
					continue;
				}

				// one line is table
				String[] tableData = line.split(";");

				// collect parameters that user indicates on the form
				String[] values = new String[namePeoParams.length];
				for (int i = 0; i < namePeoParams.length; i++) {
					if (aboutPeople[i] == null) {
						values[i] = null;
					} else {
						values[i] = tableData[aboutPeople[i] - 1].toLowerCase().trim();
					}
				}

				// adding in base
				ldb.add_human(maxid, values);
				for (Entry<Integer, String> entry : colli.peo_add.entrySet()) {
					ldb.add_human_optionals(maxid, entry.getValue(), tableData[entry.getKey() - 1]);
				}

				// next man
				maxid++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result = "somethin wrong with qyery<br>" + InsertAct;
		} finally {
			// close connection
			// just now I don't know if it correctly open and close it every time in one
			// servlet
			// maybe more correctly open ones with first request and close after time or
			// with the closing tab
			ldb.close_connection();
		}

		// show result to user
		request.setAttribute("exact answer", result);
		request.getRequestDispatcher("answer_on_insert.jsp").forward(request, response);

	}

}
