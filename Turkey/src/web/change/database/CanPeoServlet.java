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

@WebServlet("/CanPeoServlet")
public class CanPeoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] name_peo = {"StudentName", "StudentSurname", "StudentPatronymic",
			"StudentBirthday", "StudentSchool", "StudentClass", "StudentCity"};
	String[] name_basa = {"name", "surname", "patronymic",
			"birthday", "school", "class", "city"};
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
    	//считывание остальных параметров
    	String[] aboutEvent = new String[name_peo.length];
    	for(int i=0;i<name_peo.length;i++) {
    		aboutEvent[i] = request.getParameter(name_peo[i]);
    		if(aboutEvent[i].length()==0) {
    			aboutEvent[i] = null;
    		}
    	}
    	Integer id_homo = 0;
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
                id_homo = Integer.parseInt(request.getParameter("id"));
                if(request.getParameter("del")!=null) {
                	statement.executeUpdate("delete from participation where id_homo = "+id_homo.toString()+";");
                	statement.executeUpdate("delete from people where id_homo = "+id_homo.toString()+";");
                	statement.executeUpdate("delete from additional where id_homo = "+id_homo.toString()+";");
                }else {
                	for(int i = 0;i<name_peo.length;i++) {
                		if(aboutEvent[i]!=null) {
                        	statement.executeUpdate("update people set "+name_basa[i]+" = '"+
                		aboutEvent[i]+"' where id_homo = "+id_homo.toString()+";");
                		}
                	}
                	String add = request.getParameter("DPeopleAdd");
                	if(add!=null && !add.equals("")) {
	            		PreparedStatement del = con.prepareStatement("delete from additional where id_homo = ? and aname = ?;");
	                	del.setInt(1, id_homo);
	                	for(String s:add.split(";")) {
	                		del.setString(2, s.trim());
	                		del.executeUpdate();
	                	}
                	}
                	add = request.getParameter("СPeopleAdd");
                	if(add!=null && !add.equals("")) {
	                	PreparedStatement ch = con.prepareStatement("update additional set acontent = ? where id_homo = ? and aname = ?;");
	                	ch.setInt(2, id_homo);
	                	for(String s:add.split(";")) {
	                		String[]s1= s.split("-");
	                		ch.setString(1, s1[1].trim());
	                		ch.setString(3, s1[0].trim());
	                		ch.executeUpdate();
	                	}
                	}

                	add = request.getParameter("APeopleAdd");
                	if(add!=null && !add.equals("")) {
	            		PreparedStatement ad = con.prepareStatement("insert into additional values(?, ?, ?);");
	                	ad.setInt(1, id_homo);
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
    		request.getRequestDispatcher("writepeople?id="+id_homo).forward(request, response);
    	}else {
    		request.getRequestDispatcher("answer_on_insert.jsp").forward(request, response);
    	}

	}

}
