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

@WebServlet("/AskActServlet")
public class AskActServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] name_act = {"EventName", "EventDateMain", "EventDateEnd", "EventType"};
	String[] name_basa = {"title", "date_main", "date_end", "type"};
	String create_table(ResultSet rs) throws SQLException {
		String writeTable = "<div style = \"margin-left:0\" class = \"MYtable\">";
		Integer num = 1;
		writeTable+="<a><span>№</span><span>Название</span><span>Начало</span><span>Конец</span>"+
		"<span>Что же это</span></a>";
		while(rs.next()) {
			writeTable+="<a href = \"writeactivity?id="+rs.getString(1)+"\"><span>" + (num++).toString()+"</span>";
			for(int i=2;i<=4;i++) {
				writeTable+="<span>"+rs.getString(i)+" </span>";
			}
			writeTable+="<span>"+(rs.getInt(5)==0?"этап олимпиады":"сборы/школа")+" </span>";
			writeTable+="</a>";
		}
		writeTable+="</div>";
		return writeTable;
	}
    	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		request.setCharacterEncoding("UTF-8");
    		response.setContentType("text/html;charset=utf-8");
    		String[] aboutevent = new String[name_act.length];
        	for(int i=0;i<name_act.length;i++) {
        		String what = request.getParameter(name_act[i]);
        		if(i==3) {
        			if(what.equals("None")) {
        				aboutevent[i] = null;
        			}else {
        				aboutevent[i] = what.equals("EventOlymp")?"0":"1";
        			}
        		}else {
	        		if(what.length()==0) {
	        			aboutevent[i] = null;
	        		}else {
	            		aboutevent[i] =  what.toLowerCase().trim();
	        		}
        		}
        	}
        	String act_add = request.getParameter("EventAdd");
        	if(act_add.length()==0) {
        		act_add = null;
    		}
        	CollectParameter colli = new CollectParameter();
        	colli.getForact(act_add);
        	
        	   	
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
                	
                    InsertAct = "select * from Activity";
        			boolean first= false;
        			for(int i=0;i<name_act.length;i++) {
        	    		if(aboutevent[i]!=null) {
        	    			if(first) {
        	    				InsertAct+=" and ";
        	    			}
        	    			else {
        	    				InsertAct+=" where ";
        	    				first = true;
        	    			}
        	    			InsertAct += name_basa[i]+" = ";
        	    			if(i==4) {
        	    				InsertAct+=aboutevent[i];
        	    			}else {
        	    				InsertAct+="'"+aboutevent[i]+"'";
        	    			}
        	    			
        	    		}
        	    	}
                        for(Entry<String, String> entry: colli.act_add.entrySet()) {
            	    			if(first) {
            	    				InsertAct+=" and ";
            	    			}
            	    			else {
            	    				InsertAct+=" where ";
            	    				first = true;
            	    			}
            	    			InsertAct += "0 < ( select count(*) from MoreInf where "+
            	    			"MoreInf.id_act = Activity.id_act and iname = '"+entry.getKey()+
            	    			"' and icontent = '"+ entry.getValue()+"'";
            	    			InsertAct+=")";
            	    		
            	    	}
        			InsertAct+=";";
        			ResultSet rs = statement.executeQuery(InsertAct);
        			String writeTable = create_table(rs);
                    if(writeTable.length()==0) {
                    	result+="found nothing<br>"+InsertAct;
                    }else {
                    	result+=writeTable;
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
        	request.setAttribute("exact answer", result);
        	request.getRequestDispatcher("many_tables.jsp").forward(request, response);



    	}


}
