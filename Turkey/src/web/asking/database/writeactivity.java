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

import com.mysql.cj.jdbc.result.ResultSetMetaData;

@WebServlet("/writeactivity")
public class writeactivity extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	String[] name_basa = {"title", "maindate", "enddate",
			"type"};
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
        		PreparedStatement get_peo = con.prepareStatement("Select * from Activity where id_act = ?;");
        		get_peo.setInt(1, id);
    			ResultSet rs = get_peo.executeQuery();
    			rs.next();
    			for(int i=2;i<=5;i++) {
    				request.setAttribute(name_basa[i-2], rs.getString(i));
    			}
    			
    			if(!rs.getString(6).equals("null")) {
        			String table = "<table>";
        			name = rs.getString(6);
        			rs = statement.executeQuery("Select * from "+name+";");
        			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
        			int num = rsmd.getColumnCount();
        			while(rs.next()) {
        				table+="<tr>";
        				if(rs.getString(2)==null) {
        					table+="<td>нет</td>";
        				}else {
        					table+= "<td><a href = \"writepeople?id="+rs.getString(2)+"\">есть</a></td>";
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
    	request.getRequestDispatcher("activity_info.jsp").forward(request, response);

	}

}
