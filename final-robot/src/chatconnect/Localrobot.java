package chatconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Localrobot {
	/**
	 * �ӱ������ݿ�����Ϣ�ظ�
	 * 
	 * @param question
	 *            ����ѯ����Ϣ
	 * @return answer ������Ϣ��ѯ���Ļش�
	 */
	public String localdriver(String question) {
		// System.out.println("���ӱ������ݿ�");
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=javarobot";// ����Դ
		String dbname = "���ݿ��û���";
		String passwd = "���ݿ�����";
		String answer = "���ʵ���һش�ĵ��������в���";
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
			System.out.println("����ʧ��");
		}
		return answer;
	}
}
