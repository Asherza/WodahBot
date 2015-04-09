package WodahBot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class mySQL {
	private Connection con = null;
	private Statement st = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	public mySQL(String url,String username,String password){
		joinServer(url,username,password);
	}
	 private  void joinServer(String ur, String username,String pass){
	    	
	    	String url = ur;
	    	String user = username;
	    	String password = pass;
	    	try{
	    		 con = DriverManager.getConnection(url, user, password);
	    		 st = con.createStatement();
	    		 rs = st.executeQuery("SELECT VERSION()");
	    		 
	    		 if(rs.next()){
	    			 System.out.println(rs.getString(1));
	    		 }
	    	}
	    	catch(SQLException ex){
	    		System.out.println(ex);
	    	}
	    }
public ResultSet select(String command){
	try{
		pst = con.prepareStatement(command);
		return pst.executeQuery();
	}
	catch(SQLException ex){
		System.out.println(ex);
	}
	return null;
}
public void insert(String command){
	try{
		pst = con.prepareStatement(command);
		pst.executeUpdate();
	}
	catch(SQLException ex){
		System.out.println(ex);
	}
}
}

