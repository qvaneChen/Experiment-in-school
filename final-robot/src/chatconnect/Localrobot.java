package chatconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Localrobot {
	/**
	 * 从本地数据库获得消息回复
	 * 
	 * @param question
	 *            待查询的消息
	 * @return answer 根据消息查询到的回答
	 */
	public String localdriver(String question) {
		// System.out.println("链接本地数据库");
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=javarobot";// 数据源
		String dbname = "数据库用户名";
		String passwd = "数据库密码";
		String answer = "你问点儿我回答的的上来的行不行";
		try {
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(dbURL, dbname, passwd);
			
			Statement state = conn.createStatement();
			
			String sql = "select answer from lorobot where question='" + question + "'";
			
			ResultSet result = state.executeQuery(sql);
			while (result.next()) {
				answer = result.getString("answer");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("连接失败");
		}
		return answer;
	}
}
