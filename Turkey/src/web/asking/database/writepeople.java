package web.asking.database;

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

@WebServlet("/writepeople")
public class writepeople extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] name_basa = {"name", "surname", "patronymic",
			"age", "birthday", "school", "class", "city"};
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		Integer id = Integer.parseInt(request.getParameter("id"));
    	//запросы в базу данных    	
		String result = "ok";
		Connection con = null;
        Statement statement = null;
    	try {
            Class.forName("com.mysql.jdbc.Driver");          
            String url = "jdbc:mysql://localhost/mysql?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String name = "root";
            String password = "sowell";
            try {
                con = DriverManager.getConnection(url, name, password);
                statement = con.createStatement();
                statement.executeUpdate("USE seaside_base;");
        		PreparedStatement get_peo = con.prepareStatement("Select * from People where id_homo = ?;");
        		get_peo.setInt(1, id);
    			ResultSet rs = get_peo.executeQuery();
    			rs.next();
    			for(int i=2;i<=8;i++) {
    				request.setAttribute(name_basa[i-2], rs.getString(i));
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
    			
                con.close();
            } catch (SQLException e) {
            	e.printStackTrace();
            	result = "somethin wrong with qyery<br>";
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
    	request.getRequestDispatcher("people_info.jsp").forward(request, response);



	}

}
