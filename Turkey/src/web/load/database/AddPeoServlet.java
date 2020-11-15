package web.load.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


@WebServlet("/AddPeoServlet")
@MultipartConfig
public class AddPeoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] name_peo = {"StudentName", "StudentSurname", "StudentPatronymic",
			"StudentBirthday", "StudentSchool", "StudentClass", "StudentCity"};
	String[] name_basa = {"name", "surname", "patronymic",
			"birthday", "school", "class", "city"};
	Integer maxid = 1;

	int sqlType(int ind) { 
		if(ind==5)return Types.DATE;
		if(ind==4 || ind==7)return Types.INTEGER;
		return Types.BLOB;
	}
	
	ArrayList<Repeat>manut = new ArrayList<Repeat>();
	void ChooseOne(ResultSet rs, ArrayList<String> about, HashMap<String, String> add) throws SQLException {
		Repeat m = new Repeat();
		if(rs.next()) {
			m.id.add(rs.getInt(1));
			m.name.add(rs.getString(2));
			m.surname.add(rs.getString(3));
		}
		m.about = about;
		m.add = add;
		manut.add(m);
	}
	
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
    	//считывание файла первым делом
		Part file = request.getPart("TableSource");
		manut.clear();
    	//считывание остальных параметров
    	Integer[] aboutPeople = new Integer[name_peo.length];
    	for(int i=0;i<name_peo.length;i++) {
    		String what = request.getParameter(name_peo[i]);
    		if(what.length()==0) {
    			aboutPeople[i] = null;
    		}else {
        		aboutPeople[i] =  Integer.parseInt(what);
    		}
    	}
    	String pe_add = request.getParameter("StudentAdd");
    	if(pe_add.length()==0) {
			pe_add = null;
		}
    	CollectInput colli = new CollectInput();
    	colli.getForHomo(pe_add);
    	
    	boolean isT = request.getParameter("ActTitles")==null?false:true;

    	
    	//запросы в базу данных    	
    	String result = "ok";
    	Connection con = null;
        Statement statement = null;
        String InsertAct = "nonw";
    	try {
            Class.forName("com.mysql.jdbc.Driver");          
            String url = "jdbc:mysql://localhost/mysql?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String name = "root";
            String password = "sowell";
            try {
                con = DriverManager.getConnection(url, name, password);
                statement = con.createStatement();
                statement.executeUpdate("USE seaside_base;");
                ResultSet rs = statement.executeQuery("select max(id_homo) from people;");
                if(rs.next()) {
                	maxid = rs.getInt(1)+1;
                }
                
    			
                //read from table
                InputStream inp = file.getInputStream();    	
        		BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
        		String line;
        		PreparedStatement setpeo = con.prepareStatement(
        				"Insert Into People values(?, ?, ?, ?, ?, ?, ?, ?);");
        		PreparedStatement setadd = con.prepareStatement(
        				"Insert into Additional values(?,?,?);");
        		while ((line = reader.readLine()) != null) {
        			if(isT) {
        				isT = false;continue;
        			}
        			
        			String[] count = line.split(";");
        			//know people
        			InsertAct = "select * from People";
        			boolean first= false;
        			ArrayList<String> about = new ArrayList<String>();
        			HashMap<String, String>add = new HashMap<String, String>();
        			for(int i=0;i<name_peo.length;i++) {
        	    		if(aboutPeople[i]!=null) {
        	    			if(first) {
        	    				InsertAct+=" and ";
        	    			}
        	    			else {
        	    				InsertAct+=" where ";
        	    				first = true;
        	    			}
        	    			InsertAct += name_basa[i]+" = ";
        	    			about.add(count[aboutPeople[i]-1].toLowerCase().trim());
        	    			if(i==5) {
        	    				InsertAct+=count[aboutPeople[i]-1].toLowerCase().trim();
        	    			}else {
        	    				InsertAct+="'"+count[aboutPeople[i]-1].toLowerCase().trim()+"'";
        	    			}
        	    			
        	    		}
        	    	}
        			InsertAct+=";";
        			rs = statement.executeQuery(InsertAct);
        			if(rs.next()) {
        				rs.beforeFirst();
        				for(Entry<Integer, String> entry: colli.peo_add.entrySet()) {
                         	add.put(entry.getValue(), count[entry.getKey()-1]);
             			}
        				ChooseOne(rs, about, add);
        				continue;
        			}
        			if(count.length==0)continue;
        			setpeo.setInt(1, maxid);
        			for(int i=0;i<name_peo.length;i++) {
        	    		if(aboutPeople[i]==null) {
        	    			setpeo.setNull(i+2, sqlType(i));
        	    		}else {
        	    			if(i==5) {
        	    				int w = Integer.parseInt(count[aboutPeople[i]-1].toLowerCase().trim());
        	    				setpeo.setInt(i+2, w);
        	    			}
        	    			else{
        	    				setpeo.setString(i+2, count[aboutPeople[i] - 1].toLowerCase().trim());
        	    			}
        	    		}
        	    	}
                    setpeo.executeUpdate();
                    for(Entry<Integer, String> entry: colli.peo_add.entrySet()) {
                    	setadd.setInt(1, maxid);
                    	setadd.setString(2, entry.getValue());
                    	setadd.setString(3, count[entry.getKey()-1]);
                    	setadd.executeUpdate();
        			}
                    maxid++;
        		}
                con.close();
            } catch (SQLException e) {
            	e.printStackTrace();
            	result = "somethin wrong with qyery<br>"+InsertAct;
            }
        } catch (Exception e) {
            e.printStackTrace();
        	result = "somethin wrong with connect";
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                	result = "somethin wrong with close statement";
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                	result = "somethin wrong with close connection";
                }
            }
        }
    	
    	//выход
    	Comparator<Repeat> comparator = new Comparator<Repeat>() {

			@Override
			public int compare(Repeat o1, Repeat o2) {
				if(o1.id.size()<o2.id.size())return -1;
				return o1.id.size()>o2.id.size()?1:0;
			}
    		
    	};

    	manut.sort(comparator);
    	
    	request.setAttribute("classrep", manut);
    	request.setAttribute("exact answer", result);
    	if(manut.size()==0) {
    		request.getRequestDispatcher("answer_on_insert.jsp").forward(request, response);	
    	}else {
    		request.getRequestDispatcher("repeat.jsp").forward(request, response);
    	}
	
	
	}

}
