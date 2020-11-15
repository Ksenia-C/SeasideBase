package web.asking.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/AskPeoServlet")
public class AskPeoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] name_peo = {"StudentName", "StudentSurname", "StudentPatronymic",
			"StudentAge", "StudentBirthday", "StudentSchool", "StudentClass", 
			"StudentCity"};
	String[] name_basa = {"name", "surname", "patronymic",
			"age", "birthday", "school", "class", "city"};
	
	String create_table(ResultSet rs) throws SQLException {
		String writeTable = "<div class = \"MYtable\">";
		Integer num = 1;
		writeTable+="<a><span>№</span><span>Имя</span><span>Фамилия</span><span>Отчество</span>"+
		"<span>школа</span><span>класс</span><span>город</span></a>";
		while(rs.next()) {
			writeTable+="<a href = \"writepeople?id="+rs.getString(1)+"\"><span>" + (num++).toString()+"</span>";
			for(int i=2;i<=8;i++) {
				if(i==5)continue;
				writeTable+="<span>"+rs.getString(i)+" </span>";
			}
			writeTable+="</a>";
		}
		writeTable+="</div>";
		return writeTable;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		String[] aboutPeople = new String[name_peo.length];
    	for(int i=0;i<name_peo.length;i++) {
    		String what = request.getParameter(name_peo[i]);
    		if(what.length()==0) {
    			aboutPeople[i] = null;
    		}else {
        		aboutPeople[i] =  what.toLowerCase().trim();
    		}
    	}
    	String pe_add = request.getParameter("StudentAdd");
    	if(pe_add.length()==0) {
			pe_add = null;
		}
    	CollectParameter colli = new CollectParameter();
    	colli.getForpeo(pe_add);
    	
    	   	
    	//запросы в базу данных    	
    	String result = "";
    	Connection con = null;
        Statement statement = null;
        String InsertAct = "none";
    	try {
            Class.forName("com.mysql.jdbc.Driver");          
            String url = "jdbc:mysql://localhost/mysql?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String name = "root";
            String password = "sowell";
            try {
                con = DriverManager.getConnection(url, name, password);
                statement = con.createStatement();
                statement.executeUpdate("USE seaside_base;");
            	
                InsertAct = "select * from People";
    			boolean first= false;
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
    	    			if(i==5) {
    	    				InsertAct+=aboutPeople[i];
    	    			}else {
    	    				InsertAct+="'"+aboutPeople[i]+"'";
    	    			}
    	    			
    	    		}
    	    	}
                    for(Entry<String, String> entry: colli.peo_add.entrySet()) {
        	    			if(first) {
        	    				InsertAct+=" and ";
        	    			}
        	    			else {
        	    				InsertAct+=" where ";
        	    				first = true;
        	    			}
        	    			InsertAct += "0 < ( select count(*) from additional where "+
        	    			"additional.id_homo = people.id_homo and aname = '"+entry.getKey()+
        	    			"' and acontent = '"+ entry.getValue()+"'";
        	    			InsertAct+=")";
        	    		
        	    	}
               	request.setAttribute("link_to_par", InsertAct);
            	request.setAttribute("link_to_par_and", first);
    			InsertAct+=";";
    			ResultSet rs = statement.executeQuery(InsertAct);
    			String writeTable = create_table(rs);
                result+=writeTable;
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
    	request.setAttribute("exact answer", result);
    	request.getRequestDispatcher("many_tables.jsp").forward(request, response);


	}


}
