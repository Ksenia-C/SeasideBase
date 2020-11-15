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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/AddActServlet")
@MultipartConfig
public class AddActServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String[] name_act = {"EventName", "EventDateMain", "EventDateEnd", "EventType", "EventPlace"};
	String[] name_peo = {"StudentName", "StudentSurname", "StudentPatronymic",
			"StudentBirthday", "StudentSchool", "StudentClass", "StudentCity"};
	String[] name_basa = {"name", "surname", "patronymic",
			"birthday", "school", "class", "city"};
	
	
	Integer maxid = 1;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
    	//считывание файла первым делом
		Part file = request.getPart("TableSource");
   	
    	//считывание остальных параметров
    	String[] aboutEvent = new String[name_act.length];
    	for(int i=0;i<name_act.length;i++) {
    		aboutEvent[i] = request.getParameter(name_act[i]);
    		if(aboutEvent[i].length()==0) {
    			aboutEvent[i] = null;
    		}
    	}
    	if(aboutEvent[2]==null) {
    		aboutEvent[2]=aboutEvent[1];
    	}
    	String ev_add = request.getParameter("EventAdd");
    	if(ev_add.length()==0) {
			ev_add = null;
		}
    	CollectInput colli = new CollectInput();
    	colli.getForAct(ev_add);
		Integer[] aboutPeople = new Integer[0];

    	if(request.getParameter("Conny")!=null) {
    		aboutPeople = new Integer[name_peo.length];
        	for(int i=0;i<name_peo.length;i++) {
        		String what = request.getParameter(name_peo[i]);
        		if(what.length()==0) {
        			aboutPeople[i] = null;
        		}else {
            		aboutPeople[i] =  Integer.parseInt(what);
        		}
        	}
    		
    	}
    	boolean isT = request.getParameter("ActTitles")==null?false:true;
    	
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
                ResultSet rs = statement.executeQuery("select max(id_act) from activity;");
                if(rs.next()) {
                	maxid = rs.getInt(1)+1;
                }
                
              //save table
				if(request.getParameter("all")!=null) {
					InputStream inp = file.getInputStream();    	
            		BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
            		String line;
            		boolean fir = true;
            		int num = 0;
            		String insert;
            		PreparedStatement locadd = null;
                    int line_num =0;
					while ((line = reader.readLine()) != null) {
						line_num++;
						String[] count = line.split(";"); 
						if(fir) {
							insert = "Insert into table"+maxid.toString()+" values(?,null,";
							fir = false;
							num = count.length;
							InsertAct = "create table table"+maxid.toString()+"(num_str int, id_homo Int,";//внешний ключ
							for(Integer i=0;i<num;i++) {
								InsertAct+="row"+i.toString()+" blob";
								insert+="?";
								if(i!=num-1) {
									InsertAct+=",";
									insert+=",";
								}
							}
							InsertAct+=");";
							insert+=");";
							locadd = con.prepareStatement(insert);		
							statement.executeUpdate(InsertAct);
							num+=2;
						}
						for(Integer i=1;i<num-1;i++) {
							locadd.setString(i+1, count[i-1]);
						}
						locadd.setInt(1, line_num);
						locadd.executeUpdate();
						
            		}
				}
				
                
                PreparedStatement setact = con.prepareStatement(
                		"Insert Into Activity values(?,?,?,?,?,?);");
                setact.setInt(1, maxid);
                setact.setString(2, aboutEvent[0]);
                setact.setString(3, aboutEvent[1]);
                setact.setString(4, aboutEvent[2]);
                setact.setInt(5, aboutEvent[3].equals("EventOlymp")?0:1);
				if(request.getParameter("all").equals("yes")) {
					setact.setString(6, "table"+maxid.toString());	
				}else {
					setact.setNull(6, Types.BLOB);
				}
                setact.executeUpdate();
                
                PreparedStatement setadd = con.prepareStatement(
                		"Insert into MoreInf values(?,?,?);");
                
                for(Map.Entry<String, String> entry: colli.act_add.entrySet()) {
    				setadd.setInt(1, maxid);
    				setadd.setString(2, entry.getKey());
    				setadd.setString(3, entry.getValue());
    				setadd.executeUpdate();
    			}
				setadd.setInt(1, maxid);
				setadd.setString(2, "home128");
				if(aboutEvent[4]!=null) {
					setadd.setString(3, aboutEvent[4]);
				}else {
					setadd.setString(3, "null");
				}
				setadd.executeUpdate();
                
				
				
				
                
				 PreparedStatement setpeo = con.prepareStatement(
	                		"insert into Participation values(?,?);");
	                
				//people
				int line_num=0;
                if(aboutPeople.length !=0) {
                	PreparedStatement setlin = con.prepareStatement(
	                		"update table"+maxid.toString()+" set id_homo = ? where num_str=?;");
	             
                	InputStream inp = file.getInputStream();    	
            		BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
                	String line;
            		while ((line = reader.readLine()) != null) {
            			line_num++;
            			if(isT) {
            				isT = false;continue;
            			}
            			String[] count = line.split(";");
            			InsertAct = "select id_homo from People where ";
            			boolean first= false;
            			for(int i=0;i<name_peo.length;i++) {
            	    		if(aboutPeople[i]!=null) {
            	    			if(first) {
            	    				InsertAct+=" and ";
            	    			}
            	    			else {
            	    				first = true;
            	    			}
            	    			InsertAct += name_basa[i]+" = ";
            	    			if(i==3 || i==6) {
            	    				InsertAct+=count[aboutPeople[i] - 1].toLowerCase().trim();
            	    			}else {
            	    				InsertAct+="'"+count[aboutPeople[i] - 1].toLowerCase().trim()+"'";
            	    			}
            	    			
            	    		}
            	    	}
            			InsertAct+=";";
            			rs = statement.executeQuery(InsertAct);
            			if(!rs.next())continue;
            			Integer id_homo = rs.getInt(1);
            			if(rs.next())continue;
            			setpeo.setInt(1, id_homo);
            			setpeo.setInt(2, maxid);
            			setpeo.executeUpdate();
            			setlin.setInt(1, id_homo);
            			setlin.setInt(2, line_num);
            			setlin.executeUpdate();
            		}
                }
                
                maxid++;
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
    	request.getRequestDispatcher("answer_on_insert.jsp").forward(request, response);

	}

}
