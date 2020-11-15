package web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DataBaseServlet")
public class DataBaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		Integer sw = Integer.parseInt(request.getParameter("switch"));
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
                if(sw==1) {
                	statement.executeUpdate("drop DATABASE IF EXISTS seaside_base;");
                	statement.executeUpdate("create DATABASE seaside_base;");
                	statement.executeUpdate("use seaside_base;");
                	//People
                	statement.executeUpdate("CREATE TABLE People(\r\n" + 
                			"id_homo INT PRIMARY KEY,\r\n" + 
                			"name TINYBLOB,\r\n" + 
                			"surname TINYBLOB,\r\n" + 
                			"patronymic TINYBLOB,\r\n" + 
                			"birthday DATE,\r\n" + 
                			"school BLOB,\r\n" + 
                			"class TINYINT,\r\n" + 
                			"city BLOB)\r\n" + 
                			"ENGINE=InnoDB DEFAULT CHARSET=utf8;");
                	//Activity
                	statement.executeUpdate("CREATE TABLE Activity(\r\n" + 
                			"id_act INT PRIMARY KEY,\r\n" + 
                			"title BLOB,\r\n" + 
                			"date_main DATE,\r\n" + 
                			"date_end Date,\r\n" + 
                			"type INT,\r\n" + 
                			"table_name BLOB)\r\n" + 
                			"ENGINE=InnoDB DEFAULT CHARSET=utf8;");
                	//Participation
                	statement.executeUpdate("CREATE TABLE Participation(\r\n" + 
                			"id_homo INT,\r\n" + 
                			"id_act INT,\r\n" + 
                			"PRIMARY KEY (id_homo, id_act),\r\n" + 
                			"FOREIGN KEY (id_homo) REFERENCES People(id_homo),\r\n" + 
                			"FOREIGN KEY (id_act) REFERENCES Activity(id_act)\r\n" + 
                			")\r\n" + 
                			"ENGINE=InnoDB DEFAULT CHARSET=utf8;");
                	//Additional
                	statement.executeUpdate("CREATE TABLE Additional(\r\n" + 
                			"id_homo INT REFERENCES People,\r\n" + 
                			"aname TINYBLOB,\r\n" + 
                			"acontent BLOB)\r\n" + 
                			"ENGINE=InnoDB DEFAULT CHARSET=utf8;");
                	//MoreInf
                	statement.executeUpdate("CREATE TABLE MoreInf( \r\n" + 
                			"id_act INT REFERENCES Activity, \r\n" + 
                			"iname TINYBLOB, \r\n" + 
                			"icontent BLOB)\r\n" + 
                			"ENGINE=InnoDB DEFAULT CHARSET=utf8;\r\n");
                }
                else if(sw==2) {
                	statement.executeUpdate("drop DATABASE seaside_base;");
                }else if(sw==3) {
                	statement.executeUpdate("use seaside_base;");
                	statement.executeUpdate("update people set class = class + 1;");                	
                }
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
    	request.getRequestDispatcher("answer_on_insert.jsp").forward(request, response);


	}

}
