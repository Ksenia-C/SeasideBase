package web.change.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@WebServlet("/CanActServlet")
public class CanActServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] name_act = {"EventName", "EventDateMain", "EventDateEnd"};
	String[] name_basa = {"title", "date_main", "date_end"};
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
    	//считывание остальных параметров
    	String[] aboutEvent = new String[name_act.length];
    	for(int i=0;i<name_act.length;i++) {
    		aboutEvent[i] = request.getParameter(name_act[i]);
    		if(aboutEvent[i].length()==0) {
    			aboutEvent[i] = null;
    		}
    	}
    	CollectChanges inp = new CollectChanges();
    	Integer id_act = 0;
    	//запросы в базу данных    	
    	String result = "ok";
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
                ResultSet rs;
                id_act = Integer.parseInt(request.getParameter("id"));
                if(request.getParameter("del")!=null) {
                	statement.executeUpdate("delete from participation where id_act = "+id_act.toString()+";");
                	rs = statement.executeQuery("select table_name from activity where id_act = "+id_act.toString()+";");
                	if(rs.next()) {
                		String tab = rs.getString(1);
                		if(!tab.equals("null"))
                		statement.executeUpdate("drop table "+tab+";");
                	}
                	statement.executeUpdate("delete from activity where id_act = "+id_act.toString()+";");
                	statement.executeUpdate("delete from moreinf where id_act = "+id_act.toString()+";");
                }else {
                	for(int i = 0;i<name_act.length;i++) {
                		if(aboutEvent[i]!=null) {
                        	statement.executeUpdate("update activity set "+name_basa[i]+" = '"+
                		aboutEvent[i]+"' where id_act = "+id_act.toString()+";");
                		}
                	}
                	if(request.getParameter("EventType").equals("EventOlymp")) {
                		statement.executeUpdate("update activity set type = 0"+
                        		" where id_act = "+id_act.toString()+";");
                	}else if(request.getParameter("EventType").equals("EventSchool")) {
                		statement.executeUpdate("update activity set type = 1"+
                        		" where id_act = "+id_act.toString()+";");
                	}
                	
                	rs = statement.executeQuery("select icontent from moreinf where id_act = "+id_act.toString()+
                			" and iname = 'home128';");
                	String places = "";
                	if(rs.next()) {
                		places = rs.getString(1);
                	
                		if(places == null || places.equals("null")) {
                			places = "";
                		}
                	
                		String wh = request.getParameter("AEventPlace");
                		List<String>f = new ArrayList<String>(Arrays.asList(places.split(";")));
                		f = inp.addPl(wh, f);
                		wh = request.getParameter("DEventPlace");
                		f = inp.delPl(wh, f);
                		places = String.join(";", f);
                		if(places.length()==0) {
                			places = "null";
                		}
                		statement.executeUpdate("update moreinf set icontent ='"+
                				places+ "' where id_act = "+id_act.toString()+
                    			" and iname = 'home128';");
                	}
                    	
                	String add = request.getParameter("DEventAdd");
                	if(add!=null && !add.equals("")) {
	            		PreparedStatement del = con.prepareStatement("delete from moreinf where id_act = ? and iname = ?;");
	                	del.setInt(1, id_act);
	                	for(String s:add.split(";")) {
	                		if(s.trim().equals("home128"))continue;
	                		del.setString(2, s.trim());
	                		del.executeUpdate();
	                	}
                	}
                	add = request.getParameter("СEventAdd");
                	if(add!=null && !add.equals("")) {
	                	PreparedStatement ch = con.prepareStatement("update moreinf set icontent = ? where id_act = ? and iname = ?;");
	                	ch.setInt(2, id_act);
	                	for(String s:add.split(";")) {
	                		String[]s1= s.split("-");
	                		ch.setString(1, s1[1].trim());
	                		ch.setString(3, s1[0].trim());
	                		ch.executeUpdate();
	                	}
                	}

                	add = request.getParameter("AEventAdd");
                	if(add!=null && !add.equals("")) {
	            		PreparedStatement ad = con.prepareStatement("insert into moreinf values(?, ?, ?);");
	                	ad.setInt(1, id_act);
	                	for(String s:add.split(";")) {
	                		String[]s1= s.split("-");
	                		ad.setString(2, s1[0].trim());
	                		ad.setString(3, s1[1].trim());
	                		ad.executeUpdate();
	                	}
                	}
                	
                }
                
                con.close();
            } catch (SQLException e) {
            	e.printStackTrace();
            	result = "somethin wrong with qyery<br>"+ InsertAct;
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
    	if(request.getParameter("del")==null) {
    		request.getRequestDispatcher("writeactivity?id="+id_act).forward(request, response);
    	}else {
    		request.getRequestDispatcher("answer_on_insert.jsp").forward(request, response);
    	}
	}

}
