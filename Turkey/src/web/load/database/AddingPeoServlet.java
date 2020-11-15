package web.load.database;

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

@WebServlet("/AddingPeoServlet")
public class AddingPeoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Integer maxid = 1;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
    	//считывание файла первым делом
		
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
                
        		PreparedStatement setpeo = null, setad = null;
        		Integer max_cnt = Integer.parseInt(request.getParameter("cnt"));
                for(Integer cnt = 0;cnt<max_cnt;cnt++) {
                	String ans = request.getParameter("select"+cnt.toString());
                	if(ans==null)break;
                	int ad_c = Integer.parseInt(
                			request.getParameter("select"+cnt.toString()+"_count"));
                	int id_cur=0;
                	if(ans.equals("none"))continue;
                	else if(ans.equals("new")){
                		setpeo = con.prepareStatement("Insert into people values(?"+
                	request.getParameter("main"+cnt.toString())+");");
                		setpeo.setInt(1, maxid);
                		setpeo.executeUpdate();
                		id_cur = maxid;
                		maxid++;
                	}else {
                		statement.executeUpdate("update people set "+
                            	request.getParameter("main"+cnt.toString())+"where id_homo = "+
                            	Integer.parseInt(ans)+");");
                		id_cur = Integer.parseInt(ans);
                	}
                	for(Integer j=0;j<ad_c;j++) {
                		String prep =request.getParameter("select"+cnt.toString()+"_"+j.toString());
                		setad = con.prepareStatement(prep);
                		setad.setInt(1, id_cur);
                		setad.executeUpdate();
                	}
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
    	request.getRequestDispatcher("answer_on_insert.jsp").forward(request, response);

	}

}
