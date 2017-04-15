package GreenApp_DB;

/*�떎�쓬二� �삁�빟 �씪�젙*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Reservation_Schedule_DB {
	private Connection con = null; // DB 占쏙옙占쏙옙
	private PreparedStatement pstmt = null;
	private String driverName = "com.mysql.jdbc.Driver";
	private String dbURL = "jdbc:mysql://localhost/green_app";
	private String db_name = "root";
	private String db_pw = "0123";
	private ResultSet rs = null;
	private Vector<Reservation_Schedule_Data> res_schedule_vec;
	private Reservation_Schedule_Data res_schedule_obj;

	// /

	// �뿰寃�
	public void connect() {
		try {
			Class.forName(driverName);
			con = DriverManager.getConnection(dbURL, db_name, db_pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {

		}
	}

	// �뿰寃� �빐�젣
	public void disconnect() {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// �삁�빟 異붽�. �벐吏� �븡�쓬.
	public void insert(Reservation_Schedule_Data data) {

		connect();

		StringBuffer SQL = new StringBuffer("insert into reservation_schedule ");
		SQL.append("values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		try {
			pstmt = con.prepareStatement(SQL.toString());

			pstmt.setString(1, data.getTeam_name()); // �� �씠由�
			for (int a = 0; a < 35; a++) {
				pstmt.setString(a + 2, data.getT(a));
			} // �떆媛� 諛� 踰덊샇

			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
		/*
		 * connect();
		 * 
		 * StringBuffer SQL = new
		 * StringBuffer("insert into reservation_schedule ");
		 * SQL.append("values (?, ?, ?, ?)");
		 * 
		 * try { pstmt = con.prepareStatement(SQL.toString());
		 * 
		 * pstmt.setInt(1, 0); // AI �옄�룞 �븷�떦 pstmt.setString(2,
		 * data.getDay()); pstmt.setInt(3, data.getPeriod()); pstmt.setString(4,
		 * data.getTeam_name());
		 * 
		 * pstmt.execute(); } catch (SQLException e) { e.printStackTrace(); }
		 * 
		 * String temp_day = data.getDay(); int temp_period = data.getPeriod();
		 * 
		 * switch(temp_period) { case 1: if(temp_day.equals("1")) arr[0]++; else
		 * if(temp_day.equals("2")) arr[1]++; else if(temp_day.equals("3"))
		 * arr[2]++; else if(temp_day.equals("4")) arr[3]++; else
		 * if(temp_day.equals("5")) arr[4]++; break; case 2:
		 * if(temp_day.equals("1")) arr[5]++; else if(temp_day.equals("2"))
		 * arr[6]++; else if(temp_day.equals("3")) arr[7]++; else
		 * if(temp_day.equals("4")) arr[8]++; else if(temp_day.equals("5"))
		 * arr[9]++; break; case 3: if(temp_day.equals("1")) arr[10]++; else
		 * if(temp_day.equals("2")) arr[11]++; else if(temp_day.equals("3"))
		 * arr[12]++; else if(temp_day.equals("4")) arr[13]++; else
		 * if(temp_day.equals("5")) arr[14]++; break; case 4:
		 * if(temp_day.equals("1")) arr[15]++; else if(temp_day.equals("2"))
		 * arr[16]++; else if(temp_day.equals("3")) arr[17]++; else
		 * if(temp_day.equals("4")) arr[18]++; else if(temp_day.equals("5"))
		 * arr[19]++; break; case 5: if(temp_day.equals("1")) arr[20]++; else
		 * if(temp_day.equals("2")) arr[21]++; else if(temp_day.equals("3"))
		 * arr[22]++; else if(temp_day.equals("4")) arr[23]++; else
		 * if(temp_day.equals("5")) arr[24]++; break; case 6:
		 * if(temp_day.equals("1")) arr[25]++; else if(temp_day.equals("2"))
		 * arr[26]++; else if(temp_day.equals("3")) arr[27]++; else
		 * if(temp_day.equals("4")) arr[28]++; else if(temp_day.equals("5"))
		 * arr[29]++; break; case 7: if(temp_day.equals("1")) arr[30]++; else
		 * if(temp_day.equals("2")) arr[31]++; else if(temp_day.equals("3"))
		 * arr[32]++; else if(temp_day.equals("4")) arr[33]++; else
		 * if(temp_day.equals("5")) arr[34]++; break; case 8:
		 * if(temp_day.equals("1")) arr[35]++; else if(temp_day.equals("2"))
		 * arr[36]++; else if(temp_day.equals("3")) arr[37]++; else
		 * if(temp_day.equals("4")) arr[38]++; else if(temp_day.equals("5"))
		 * arr[39]++; break; } disconnect();
		 */
	}

	// �� �씠由꾩쓣 諛쏆븘�꽌 t/f濡� �빐�떦 ���쓽 tn�쓣 �닔�젙
	// �씠踰덉＜ �삁�빟 痍⑥냼 �삁�빟�븯湲� 硫붿냼�뱶
	public void mod(String team_name, int index, String state) {
		connect();

		StringBuffer SQL = new StringBuffer("update reservation_schedule set t");
		SQL.append(index + "='" + state + "' where team_name='" + team_name
				+ "'");
		System.err.println("SQL : " + SQL.toString());

		try {
			pstmt = con.prepareStatement(SQL.toString());
			System.err.println("pstmt : " + pstmt.toString());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
	}

	// 濡쒓렇�씤 �썑, �굹�쓽 �떎�쓬二� �삁�빟 �쁽�솴 媛��졇�샂
	public String[] getNextSchedule(String team_name) {
		String[] arr = new String[35];

		connect();

		StringBuffer SQL = new StringBuffer(
				"select t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29, t30, t31, t32, t33, t34, t35 from reservation_schedule where team_name='");
		SQL.append(team_name);
		SQL.append("'");
		System.err.println("SQL : " + SQL.toString());

		// �빐�떦 ���쓽 35媛� �떆媛꾪몴 t, f瑜� 媛��졇�샂

		try {
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();

			for (int a = 0; a < 35; a++) {
				arr[a] = rs.getString(a + 1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return arr;
	}

	// 媛� �슂�씪�쓽 媛� 援먯떆�뿉 �떊泥��븳 ���씠 紐� ���씤吏�瑜� 諛섑솚. 紐⑤뱺 援먯떆(珥� 35)媛쒕�� �젙�닔�삎
	// 諛곗뿴�뿉 �꽔�뼱�꽌 諛섑솚.
	public int[] count_res() {
		int[] arr = new int[35];

		connect();

		try {
			for (int a = 0; a < 35; a++) {
				StringBuffer SQL = new StringBuffer(
						"select count(*) from green_app.reservation_schedule where t");
				SQL.append((a + 1) + "='t'");
				pstmt = con.prepareStatement(SQL.toString());
				rs = pstmt.executeQuery();

				rs.next();
				arr[a] = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return arr;
	}
	
	public String count_room(int index) {

		int temp_count = 0;
		
		connect();

		try {
			StringBuffer SQL = new StringBuffer(
						"select count(*) from reservation_schedule where t");
				SQL.append(index + "='t'");
				pstmt = con.prepareStatement(SQL.toString());
				rs = pstmt.executeQuery();

				rs.next();
				temp_count = rs.getInt(1);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return (temp_count<10 ? "t" : "f");
	}

	// �삁�빟 �뀒�씠釉붿쓽 �뒠�뵆�쓣 �쟾泥� �궘�젣.
	// 留ㅺ컻蹂��닔 �뾾�쓬
	public void delete() {
		connect();

		StringBuffer SQL = new StringBuffer("delete from reservation_schedule");

		try {
			pstmt = con.prepareStatement(SQL.toString());
			pstmt.execute();
		} catch (Exception e) {

		}

		disconnect();
	}
}
