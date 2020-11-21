package web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

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
	private String[] namePeoBase = {"name", "surname", "patronymic",
			"birthday", "city"};
	String[] nameActBase = {"title", "date_main", "duration", "places"};
	
	int sqlType(int ind) { 
		if (ind == indexDate) return Types.DATE;
		return Types.BLOB;
	}
	
	
	// open and close connection with database
	public void open_connection() {
    	try {
            Class.forName("com.mysql.jdbc.Driver");          
            String url = "jdbc:mysql://localhost/mysql?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String name = "root";
            String password = "sowell";
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

	// get id for insert
	public Integer get_max_human_id() throws SQLException {
		ResultSet rs = statement.executeQuery("select max(id_homo) from people;");
		if(rs.next()) {
        	return rs.getInt(1)+1;
        }
		return 0;
	}
	public Integer get_max_act_id() throws SQLException {
		ResultSet rs = statement.executeQuery("select max(id_act) from activity;");
        if(rs.next()) {
        	return rs.getInt(1)+1;
        }
        return 0;
	}
	
	// insert human
	public void add_human(Integer maxid, String[] parameters) throws SQLException {
		PreparedStatement setHuman = con.prepareStatement(
				"Insert Into People values(?, ?, ?, ?, ?, ?);");
		setHuman.setInt(1, maxid);
		for(int i=0;i<parameters.length;i++) {
    		if(parameters[i]==null) {
    			setHuman.setNull(i+2, sqlType(i));
    		}else {
    			setHuman.setString(i+2, parameters[i]);
    		}
    	}
        setHuman.executeUpdate();
	}
	public void add_human_optionals(Integer maxid, String key, String value) throws SQLException {
		PreparedStatement setOptional = con.prepareStatement(
				"Insert into Additional values(?,?,?);");
		setOptional.setInt(1, maxid);
		setOptional.setString(2, key);
		setOptional.setString(3, value);
		setOptional.executeUpdate();
	}
	
	// insert event
	// save whole table
	public void create_table(Integer id, int numColomns) throws SQLException {
		String InsertAct = "Create table table"+id.toString()+"(num_str int, id_homo Int,";
		for(Integer i = 0; i < numColomns; i++) {
			InsertAct+="row"+i.toString()+" blob";
			if(i != numColomns - 1) {
				InsertAct+=",";
			}
		}
		InsertAct+=");";
		statement.executeUpdate(InsertAct);
	}
	private PreparedStatement insertLineOwnTable;
	public void create_prepared_for_own_table(Integer id, int numColomns) throws SQLException {
		String insert = "Insert into table"+id.toString()+" values(?,null,";
		for(Integer i=0;i<numColomns;i++) {
			insert+="?";
			if(i!=numColomns-1) {
				insert+=",";
			}
		}
		insert+=");";
		insertLineOwnTable = con.prepareStatement(insert);		
		
	}
	public void add_string_to_table(int numLine, int numColomns, String[] values) throws SQLException {
		for(Integer i = 0; i < numColomns; i++) {
			insertLineOwnTable.setString(i + 2, values[i]);
		}
		insertLineOwnTable.setInt(1, numLine);
		insertLineOwnTable.executeUpdate();		
	}
	
	// save main data about event
	public void add_act(Integer maxid, String[] parameters, boolean saveAllTable) throws SQLException {
		PreparedStatement setact = con.prepareStatement(
        		"Insert Into Activity values(?,?,?,?,?,?);");
        setact.setInt(1, maxid);
        setact.setString(2, parameters[0]);
        setact.setString(3, parameters[1]);
        setact.setInt(4, Integer.parseInt(parameters[2]));
        setact.setString(5, parameters[3]);
        if(saveAllTable) {
			setact.setString(6, "table"+maxid.toString());	
		}else {
			setact.setNull(6, Types.BLOB);
		}
        setact.executeUpdate();      
        
	}
	public void add_act_optionals(int id, String key, String value) throws SQLException {
		PreparedStatement setadd = con.prepareStatement(
        		"Insert into MoreInf values(?,?,?);");
		setadd.setInt(1, id);
		setadd.setString(2, key);
		setadd.setString(3, value);
		setadd.executeUpdate();        
	}

	// create participation
	public void try_find_and_save(Integer id, int numLine, String[] parameters, boolean hasTable) throws SQLException {
		
		PreparedStatement setpeo = con.prepareStatement(
        		"insert into Participation values(?,?);");
    
		String InsertAct = "select id_homo from People where ";
		boolean first= false;
		for(int i=0;i<parameters.length;i++) {
    		if(parameters[i]!=null) {
    			if(first) {
    				InsertAct+=" and ";
    			}
    			else {
    				first = true;
    			}
    			InsertAct += namePeoBase[i] + " = '" + parameters[i] + "'";   			
    		}
    	}

		InsertAct+=";";
		ResultSet rs = statement.executeQuery(InsertAct);
		if(!rs.next()) return;
		Integer id_homo = rs.getInt(1);
		if(rs.next()) return;
		setpeo.setInt(1, id_homo);
		setpeo.setInt(2, id);
		setpeo.executeUpdate();
		if (hasTable) {
			PreparedStatement setlin = con.prepareStatement(
        		"update table"+id.toString()+" set id_homo = ? where num_str=?;");
		setlin.setInt(1, id_homo);
		setlin.setInt(2, numLine);
		setlin.executeUpdate();
		}
	}

	
	
	// to form table from results
	String create_table_people(ResultSet rs) throws SQLException {
		String writeTable = "<div class = \"MYtable\">";
		Integer num = 1;
		writeTable+="<a><span>№</span><span>Name</span><span>Surname</span><span>Patronics</span>"+
		"<span>Birthday</span><span>City</span></a>";
		while (rs.next()) {
			writeTable+="<a href = \"writepeople?id="+rs.getString(1)+"\"><span>" + (num++).toString()+"</span>";
			for (int i = 0; i < namePeoBase.length; ++i) {
				writeTable += "<span>" + rs.getString(i + 2) + " </span>";
			}
			writeTable+="</a>";
		}
		writeTable+="</div>";
		return writeTable;
	}
	String create_table_events(ResultSet rs) throws SQLException {
		String writeTable = "<div style = \"margin-left:0\" class = \"MYtable\">";
		Integer num = 1;
		writeTable+="<a><span>№</span><span>Title</span><span>Date of begin</span><span>Duration</span>"+
		"<span>Places</span></a>";
		while(rs.next()) {
			writeTable+="<a href = \"writeactivity?id="+rs.getString(1)+"\"><span>" + (num++).toString()+"</span>";
			for(int i = 2; i <= 5; i++) {
				if (i == 4) {
					writeTable += "<span>" + rs.getInt(i) + " </span>";
				}else {
					writeTable += "<span>" + rs.getString(i) + " </span>";
				}
			}
			writeTable+="</a>";
		}
		writeTable+="</div>";
		return writeTable;
	}
	
	// acquire data about people and return table  
	public String search_people(String[] parameters) throws SQLException {
		String InsertAct = "select * from People";
		boolean first= false;
		for(int i=0;i<parameters.length;i++) {
    		if(parameters[i]!=null) {
    			if(first) {
    				InsertAct+=" and ";
    			}
    			else {
    				InsertAct+=" where ";
    				first = true;
    			}
    			InsertAct += namePeoBase[i] + " = '" + parameters[i] + "'";    			
    		}
    	}
        InsertAct+=";";
		ResultSet rs = statement.executeQuery(InsertAct);
		return create_table_people(rs);
	}
	
	public String search_events(String[] parameters) throws SQLException {
		String InsertAct = "select * from Activity";
		boolean first= false;
		for(int i = 0; i < parameters.length; ++i) {
    		if(parameters[i]!=null) {
    			if(first) {
    				InsertAct+=" and ";
    			}
    			else {
    				InsertAct+=" where ";
    				first = true;
    			}
    			if (i == 1) {
    				InsertAct += "'" + parameters[i]+"' between "+ nameActBase[i]
    						+ " and DATE_ADD("+ nameActBase[i] + ", interval " + nameActBase[i + 1]
    					     + " DAY)";
    			} else {
    				InsertAct += nameActBase[i > 1 ? i + 1 : i]+" LIKE '%" + parameters[i] + "%'";
    			}
    		}
    	}
		InsertAct+=";";
		ResultSet rs = statement.executeQuery(InsertAct);
		return create_table_events(rs);
	}
	
	// extract data from people
	public void extract_people_main(int id, HttpServletRequest request) throws SQLException {
		PreparedStatement get_peo = con.prepareStatement("Select * from People where id_homo = ?;");
		get_peo.setInt(1, id);
		ResultSet rs = get_peo.executeQuery();
		rs.next();
		for(int i = 0; i < namePeoBase.length; ++i) {
			request.setAttribute(namePeoBase[i], rs.getString(i + 2));
		}
		PreparedStatement get_add = con.prepareStatement("Select * from Additional where id_homo = ?;");
		get_add.setInt(1, id);
		rs = get_add.executeQuery();
		Integer cnt = 0;
		while(rs.next()) {
			cnt++;
			request.setAttribute("aname"+cnt.toString(), rs.getString(2));
			request.setAttribute("acontent"+cnt.toString(), rs.getString(3));
		}
		request.setAttribute("howmany", cnt);
		PreparedStatement get_par = con.prepareStatement("Select id_act, title from Activity where id_act in "
				+ "(select id_act from Participation where id_homo = ?);");
		get_par.setInt(1, id);
		rs = get_par.executeQuery();
		cnt = 0;
		while(rs.next()) {
			cnt++;
			request.setAttribute("title"+cnt.toString(), rs.getString(2));
			request.setAttribute("idol"+cnt.toString(), "writeactivity?id="+rs.getString(1));
		}
		request.setAttribute("olhowmany", cnt);
	}
	public void extract_events_main(int id, HttpServletRequest request) throws SQLException {
		PreparedStatement get_peo = con.prepareStatement("Select * from Activity where id_act = ?;");
		get_peo.setInt(1, id);
		ResultSet rs = get_peo.executeQuery();
		rs.next();
		for(int i=0;i<nameActBase.length;i++) {
			request.setAttribute(nameActBase[i], rs.getString(i + 2));
		}
		
		if(!rs.getString(6).equals("null")) {
			String table = "<table>";
			String name = rs.getString(6);
			rs = statement.executeQuery("Select * from "+name+";");
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			int num = rsmd.getColumnCount();
			while(rs.next()) {
				table+="<tr>";
				if(rs.getString(2)==null) {
					table+="<td>doesn't exist</td>";
				}else {
					table+= "<td><a href = \"writepeople?id="+rs.getString(2)+"\">exists</a></td>";
				}
				for(Integer i = 2 ;i<num;i++) {
					table+="<td>"+rs.getString(i+1)+"</td>";
				}
				table+="</tr>";
			}
			table+="</table>";
			request.setAttribute("table", table);
		}
		
		PreparedStatement get_add = con.prepareStatement("Select * from MoreInf where id_act = ?;");
		get_add.setInt(1, id);
		rs = get_add.executeQuery();
		Integer cnt = 0;
		while(rs.next()) {
			cnt++;
			request.setAttribute("iname"+cnt.toString(), rs.getString(2));
			request.setAttribute("icontent"+cnt.toString(), rs.getString(3));
		}
		request.setAttribute("howmany", cnt);
	}
	
	

}
