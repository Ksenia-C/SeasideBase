package web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

/*
 * For a current moment they work and exist:
 * open_connection close_connection
 * get_max_human_id get_max_act_id
 * add_human add_human_optionals
 * create_table create_prepared+for_own_table add_string_to_table
 * add_act add_act_optionals
 * try_find_and_save
 * search_people search_events
 */

public class LocalDataBase {
	private Connection con = null;
	private Statement statement = null;

	int indexDate = 3;
	private String[] namePeoBase = { "name", "surname", "patronymic", "birthday", "city" };
	String[] nameActBase = { "title", "date_main", "duration", "places" };

	int sqlType(int ind) {
		if (ind == indexDate)
			return Types.DATE;
		return Types.BLOB;
	}

	// open and close connection with database
	public void open_connection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/mysql?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
			String name = "root";
			String password = "";
			try {
				con = DriverManager.getConnection(url, name, password);
				statement = con.createStatement();
				statement.executeUpdate("USE seaside_base;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	public void close_connection() {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	void create_data_base() throws SQLException {
		statement.executeUpdate("create DATABASE seaside_base;");
		statement.executeUpdate("use seaside_base;");
		// People
		statement.executeUpdate("CREATE TABLE People(\r\n" + "id_homo INT PRIMARY KEY,\r\n" + "name TINYBLOB,\r\n"
				+ "surname TINYBLOB,\r\n" + "patronymic TINYBLOB,\r\n" + "birthday DATE,\r\n" + "city BLOB);");
		// Activity
		statement.executeUpdate("CREATE TABLE Activity(\r\n" + "id_act INT PRIMARY KEY,\r\n" + "title BLOB,\r\n"
				+ "date_main DATE,\r\n" + "duration INT,\r\n" + "places BLOB,\r\n" + "table_name BLOB);");
		// Participation
		statement.executeUpdate("CREATE TABLE Participation(\r\n" + "id_homo INT,\r\n" + "id_act INT,\r\n"
				+ "PRIMARY KEY (id_homo, id_act),\r\n" + "FOREIGN KEY (id_homo) REFERENCES People(id_homo),\r\n"
				+ "FOREIGN KEY (id_act) REFERENCES Activity(id_act)\r\n" + ");");
		// Additional
		statement.executeUpdate("CREATE TABLE Additional(\r\n" + "id_homo INT REFERENCES People,\r\n"
				+ "aname TINYBLOB,\r\n" + "acontent BLOB);");
		// MoreInf
		statement.executeUpdate("CREATE TABLE MoreInf( \r\n" + "id_act INT REFERENCES Activity, \r\n"
				+ "iname TINYBLOB, \r\n" + "icontent BLOB);");
	}

	// delete database
	public void delete_data_base() throws SQLException {
		statement.executeUpdate("drop DATABASE IF EXISTS seaside_base;");
	}

	// get id for insert
	public Integer get_max_human_id() throws SQLException {
		ResultSet rs = statement.executeQuery("select max(id_homo) from people;");
		if (rs.next()) {
			return rs.getInt(1) + 1;
		}
		return 0;
	}

	public Integer get_max_act_id() throws SQLException {
		ResultSet rs = statement.executeQuery("select max(id_act) from activity;");
		if (rs.next()) {
			return rs.getInt(1) + 1;
		}
		return 0;
	}

	// insert human
	public void add_human(Integer maxid, String[] parameters) throws SQLException {
		String InsertAct = "select * from People";
		boolean first = false;
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] != null) {
				if (first) {
					InsertAct += " and ";
				} else {
					InsertAct += " where ";
					first = true;
				}
				InsertAct += namePeoBase[i] + " = '" + parameters[i] + "'";
			}
		}
		InsertAct += ";";
		ResultSet rs = statement.executeQuery(InsertAct);
		if (rs.next())
			return;

		PreparedStatement setHuman = con.prepareStatement("Insert Into People values(?, ?, ?, ?, ?, ?);");
		setHuman.setInt(1, maxid);
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] == null) {
				setHuman.setNull(i + 2, sqlType(i));
			} else {
				setHuman.setString(i + 2, parameters[i]);
			}
		}
		setHuman.executeUpdate();
	}

	public void add_human_optionals(Integer maxid, String key, String value) throws SQLException {
		PreparedStatement setOptional = con.prepareStatement("Insert into Additional values(?,?,?);");
		setOptional.setInt(1, maxid);
		setOptional.setString(2, key);
		setOptional.setString(3, value);
		setOptional.executeUpdate();
	}

	// insert event
	// save whole table
	public void create_table(Integer id, int numColomns) throws SQLException {
		String InsertAct = "Create table table" + id.toString() + "(num_str int, id_homo Int,";
		for (Integer i = 0; i < numColomns; i++) {
			InsertAct += "row" + i.toString() + " blob";
			if (i != numColomns - 1) {
				InsertAct += ",";
			}
		}
		InsertAct += ");";
		statement.executeUpdate(InsertAct);
	}

	private PreparedStatement insertLineOwnTable;

	public void create_prepared_for_own_table(Integer id, int numColomns) throws SQLException {
		String insert = "Insert into table" + id.toString() + " values(?,null,";
		for (Integer i = 0; i < numColomns; i++) {
			insert += "?";
			if (i != numColomns - 1) {
				insert += ",";
			}
		}
		insert += ");";
		insertLineOwnTable = con.prepareStatement(insert);

	}

	public void add_string_to_table(int numLine, int numColomns, String[] values) throws SQLException {
		for (Integer i = 0; i < numColomns; i++) {
			insertLineOwnTable.setString(i + 2, values[i]);
		}
		insertLineOwnTable.setInt(1, numLine);
		insertLineOwnTable.executeUpdate();
	}

	// save main data about event
	public void add_act(Integer maxid, String[] parameters, boolean saveAllTable) throws SQLException {
		PreparedStatement setact = con.prepareStatement("Insert Into Activity values(?,?,?,?,?,?);");
		setact.setInt(1, maxid);
		setact.setString(2, parameters[0]);
		setact.setString(3, parameters[1]);
		setact.setInt(4, Integer.parseInt(parameters[2]));
		setact.setString(5, parameters[3]);
		if (saveAllTable) {
			setact.setString(6, "table" + maxid.toString());
		} else {
			setact.setNull(6, Types.BLOB);
		}
		setact.executeUpdate();

	}

	public void add_act_optionals(int id, String key, String value) throws SQLException {
		PreparedStatement setadd = con.prepareStatement("Insert into MoreInf values(?,?,?);");
		setadd.setInt(1, id);
		setadd.setString(2, key);
		setadd.setString(3, value);
		setadd.executeUpdate();
	}

	// create participation
	public void try_find_and_save(Integer id, int numLine, String[] parameters, boolean hasTable) throws SQLException {

		PreparedStatement setpeo = con.prepareStatement("insert into Participation values(?,?);");

		String InsertAct = "select id_homo from People where ";
		boolean first = false;
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] != null) {
				if (first) {
					InsertAct += " and ";
				} else {
					first = true;
				}
				InsertAct += namePeoBase[i] + " = '" + parameters[i] + "'";
			}
		}

		InsertAct += ";";
		ResultSet rs = statement.executeQuery(InsertAct);
		if (!rs.next())
			return;
		Integer id_homo = rs.getInt(1);
		if (rs.next())
			return;
		setpeo.setInt(1, id_homo);
		setpeo.setInt(2, id);
		setpeo.executeUpdate();
		if (hasTable) {
			PreparedStatement setlin = con
					.prepareStatement("update table" + id.toString() + " set id_homo = ? where num_str=?;");
			setlin.setInt(1, id_homo);
			setlin.setInt(2, numLine);
			setlin.executeUpdate();
		}
	}

	// to form table from results
	String create_table_people(ResultSet rs) throws SQLException {
		String writeTable = "<div class = \"MYtable\" style = \"margin-left: auto; margin-right: auto;\">";
		Integer num = 1;
		writeTable += "<a><span>№</span><span>Name</span><span>Surname</span><span>Patronics</span>"
				+ "<span>Birthday</span><span>City</span></a>";
		while (rs.next()) {
			writeTable += "<a href = \"writepeople?id=" + rs.getString(1) + "\"><span>" + (num++).toString()
					+ "</span>";
			for (int i = 0; i < namePeoBase.length; ++i) {
				writeTable += "<span>" + rs.getString(i + 2) + " </span>";
			}
			writeTable += "</a>";
		}
		writeTable += "</div>";
		return writeTable;
	}

	String create_table_events(ResultSet rs) throws SQLException {
		String writeTable = "<div style = \"margin-left:0\" class = \"MYtable\" style = \"margin-left: auto; margin-right: auto;\">";
		Integer num = 1;
		writeTable += "<a><span>№</span><span>Title</span><span>Date of begin</span><span>Duration</span>"
				+ "<span>Places</span></a>";
		while (rs.next()) {
			writeTable += "<a href = \"writeactivity?id=" + rs.getString(1) + "\"><span>" + (num++).toString()
					+ "</span>";
			for (int i = 2; i <= 5; i++) {
				if (i == 4) {
					writeTable += "<span>" + rs.getInt(i) + " </span>";
				} else {
					writeTable += "<span>" + rs.getString(i) + " </span>";
				}
			}
			writeTable += "</a>";
		}
		writeTable += "</div>";
		return writeTable;
	}

	// acquire data about people and return table
	public String search_people(String[] parameters, HashMap<String, String> optional) throws SQLException {
		String InsertAct = "select * from People";
		boolean first = false;
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] != null) {
				if (first) {
					InsertAct += " and ";
				} else {
					InsertAct += " where ";
					first = true;
				}
				InsertAct += namePeoBase[i] + " = '" + parameters[i] + "'";
			}
		}
		if (!optional.isEmpty()) {
			if (first) {
				InsertAct += " and ";
			} else {
				InsertAct += " where ";
				first = true;
			}
			InsertAct += String.valueOf(optional.size()) + " = (SELECT COUNT(*) FROM additional"
					+ " WHERE additional.id_homo = people.id_homo and (";
			first = true;
			for (HashMap.Entry<String, String> entry : optional.entrySet()) {
				if (!first) {
					InsertAct += " or ";
				} else {
					first = true;
				}
				InsertAct += "aname = '" + entry.getKey() + "' and acontent = '" + entry.getValue() + "'";
			}
			InsertAct += "))";
		}
		InsertAct += ";";
		ResultSet rs = statement.executeQuery(InsertAct);
		return create_table_people(rs);
	}

	public String search_events(String[] parameters, HashMap<String, String> optional) throws SQLException {
		String InsertAct = "select * from Activity";
		boolean first = false;
		for (int i = 0; i < parameters.length; ++i) {
			if (parameters[i] != null) {
				if (first) {
					InsertAct += " and ";
				} else {
					InsertAct += " where ";
					first = true;
				}
				if (i == 1) {
					InsertAct += "'" + parameters[i] + "' between " + nameActBase[i] + " and DATE_ADD(" + nameActBase[i]
							+ ", interval " + nameActBase[i + 1] + "- 1 DAY)";
				} else {
					InsertAct += nameActBase[i > 1 ? i + 1 : i] + " LIKE '%" + parameters[i] + "%'";
				}
			}
		}
		if (!optional.isEmpty()) {
			if (first) {
				InsertAct += " and ";
			} else {
				InsertAct += " where ";
				first = true;
			}
			InsertAct += String.valueOf(optional.size()) + " = (SELECT COUNT(*) FROM moreinf"
					+ " WHERE moreinf.id_act = activity.id_act and (";
			first = true;
			for (HashMap.Entry<String, String> entry : optional.entrySet()) {
				if (!first) {
					InsertAct += " or ";
				} else {
					first = true;
				}
				InsertAct += "iname = '" + entry.getKey() + "' and icontent = '" + entry.getValue() + "'";
			}
			InsertAct += "))";
		}
		InsertAct += ";";
		ResultSet rs = statement.executeQuery(InsertAct);
		return create_table_events(rs);
	}

	// extract data from people
	public void extract_people_main(int id, HttpServletRequest request) throws SQLException {
		PreparedStatement get_peo = con.prepareStatement("Select * from People where id_homo = ?;");
		get_peo.setInt(1, id);
		ResultSet rs = get_peo.executeQuery();
		rs.next();
		for (int i = 0; i < namePeoBase.length; ++i) {
			request.setAttribute(namePeoBase[i], rs.getString(i + 2));
		}
		PreparedStatement get_add = con.prepareStatement("Select * from Additional where id_homo = ?;");
		get_add.setInt(1, id);
		rs = get_add.executeQuery();
		Integer cnt = 0;
		while (rs.next()) {
			cnt++;
			request.setAttribute("aname" + cnt.toString(), rs.getString(2));
			request.setAttribute("acontent" + cnt.toString(), rs.getString(3));
		}
		request.setAttribute("howmany", cnt);
		PreparedStatement get_par = con.prepareStatement("Select id_act, title from Activity where id_act in "
				+ "(select id_act from Participation where id_homo = ?);");
		get_par.setInt(1, id);
		rs = get_par.executeQuery();
		cnt = 0;
		while (rs.next()) {
			cnt++;
			request.setAttribute("title" + cnt.toString(), rs.getString(2));
			request.setAttribute("idol" + cnt.toString(), "writeactivity?id=" + rs.getString(1));
		}
		request.setAttribute("olhowmany", cnt);
	}

	// extract data from events
	public void extract_events_main(int id, HttpServletRequest request) throws SQLException {
		PreparedStatement get_peo = con.prepareStatement("Select * from Activity where id_act = ?;");
		get_peo.setInt(1, id);
		ResultSet rs = get_peo.executeQuery();
		rs.next();
		for (int i = 0; i < nameActBase.length; i++) {
			request.setAttribute(nameActBase[i], rs.getString(i + 2));
		}

		if (rs.getString(6) != null) {
			ResultSet rs_pe;
			PreparedStatement person_existance = con.prepareStatement("Select * from People where id_homo = ?;");
			String table = "<table>";
			String name = rs.getString(6);
			rs = statement.executeQuery("Select * from " + name + ";");
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			int num = rsmd.getColumnCount();
			while (rs.next()) {
				table += "<tr>";
				if (rs.getString(2) == null) {
					table += "<td>empty</td>";
				} else {
					person_existance.setString(1, rs.getString(2));
					rs_pe = person_existance.executeQuery();
					if (rs_pe.next()) {
						table += "<td><a href = \"writepeople?id=" + rs.getString(2) + "\">exists</a></td>";
					} else {
						table += "<td>empty</td>";
					}
				}
				for (Integer i = 2; i < num; i++) {
					table += "<td>" + rs.getString(i + 1) + "</td>";
				}
				table += "</tr>";
			}
			table += "</table>";
			request.setAttribute("table", table);
		}

		PreparedStatement get_add = con.prepareStatement("Select * from MoreInf where id_act = ?;");
		get_add.setInt(1, id);
		rs = get_add.executeQuery();
		Integer cnt = 0;
		while (rs.next()) {
			cnt++;
			request.setAttribute("iname" + cnt.toString(), rs.getString(2));
			request.setAttribute("icontent" + cnt.toString(), rs.getString(3));
		}
		request.setAttribute("howmany", cnt);
	}

	// change person
	public void delete_person(Integer id) throws SQLException {
		statement.executeUpdate("delete from participation where id_homo = " + id.toString() + ";");
		statement.executeUpdate("delete from people where id_homo = " + id.toString() + ";");
		statement.executeUpdate("delete from additional where id_homo = " + id.toString() + ";");
	}

	public void change_person_main(Integer id, String[] parameters) throws SQLException {
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] != null) {
				statement.executeUpdate("update people set " + namePeoBase[i] + " = '" + parameters[i]
						+ "' where id_homo = " + id.toString() + ";");
			}
		}
	}

	public void change_person_optional(Integer id, HttpServletRequest request) throws SQLException {
		String add = request.getParameter("DPeopleAdd");
		if (add != null && !add.equals("")) {
			PreparedStatement del = con.prepareStatement("delete from additional where id_homo = ? and aname = ?;");
			del.setInt(1, id);
			for (String s : add.split(";")) {
				del.setString(2, s.trim());
				del.executeUpdate();
			}
		}
		add = request.getParameter("СPeopleAdd");
		if (add != null && !add.equals("")) {
			PreparedStatement ch = con
					.prepareStatement("update additional set acontent = ? where id_homo = ? and aname = ?;");
			ch.setInt(2, id);
			for (String s : add.split(";")) {
				String[] s1 = s.split("-");
				ch.setString(1, s1[1].trim());
				ch.setString(3, s1[0].trim());
				ch.executeUpdate();
			}
		}

		add = request.getParameter("APeopleAdd");
		if (add != null && !add.equals("")) {
			PreparedStatement ad = con.prepareStatement("insert into additional values(?, ?, ?);");
			ad.setInt(1, id);
			for (String s : add.split(";")) {
				String[] s1 = s.split("-");
				ad.setString(2, s1[0].trim());
				ad.setString(3, s1[1].trim());
				ad.executeUpdate();
			}
		}
	}

	// change event
	public void delete_event(Integer id) throws SQLException {
		statement.executeUpdate("delete from participation where id_act = " + id.toString() + ";");
		ResultSet rs = statement.executeQuery("select table_name from activity where id_act = " + id.toString() + ";");
		if (rs.next()) {
			String tab = rs.getString(1);
			if (!tab.equals("null"))
				statement.executeUpdate("drop table " + tab + ";");
		}
		statement.executeUpdate("delete from activity where id_act = " + id.toString() + ";");
		statement.executeUpdate("delete from moreinf where id_act = " + id.toString() + ";");
	}

	public void change_event_main(Integer id, String[] parameters) throws SQLException {
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] != null) {
				statement.executeUpdate("update activity set " + nameActBase[i] + " = '" + parameters[i]
						+ "' where id_act = " + id.toString() + ";");
			}
		}
	}

	public void change_event_optional(Integer id, HttpServletRequest request) throws SQLException {
		String add = request.getParameter("DEventAdd");
		if (add != null && !add.equals("")) {
			PreparedStatement del = con.prepareStatement("delete from moreinf where id_act = ? and iname = ?;");
			del.setInt(1, id);
			for (String s : add.split(";")) {
				del.setString(2, s.trim());
				del.executeUpdate();
			}
		}
		add = request.getParameter("СEventAdd");
		if (add != null && !add.equals("")) {
			PreparedStatement ch = con
					.prepareStatement("update moreinf set icontent = ? where id_act = ? and iname = ?;");
			ch.setInt(2, id);
			for (String s : add.split(";")) {
				String[] s1 = s.split("-");
				ch.setString(1, s1[1].trim());
				ch.setString(3, s1[0].trim());
				ch.executeUpdate();
			}
		}

		add = request.getParameter("AEventAdd");
		if (add != null && !add.equals("")) {
			PreparedStatement ad = con.prepareStatement("insert into moreinf values(?, ?, ?);");
			ad.setInt(1, id);
			for (String s : add.split(";")) {
				String[] s1 = s.split("-");
				ad.setString(2, s1[0].trim());
				ad.setString(3, s1[1].trim());
				ad.executeUpdate();
			}
		}

	}

}
