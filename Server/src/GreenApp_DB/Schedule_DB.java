package GreenApp_DB;

/*�씠踰덉＜ �삁�빟 �씪�젙*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import School_Calendar.SchoolCalendar;

public class Schedule_DB {
	private Connection con = null; // DB 占쏙옙占쏙옙
	private PreparedStatement pstmt = null;
	private String driverName = "com.mysql.jdbc.Driver";
	private String dbURL = "jdbc:mysql://localhost/green_app";
	private String db_name = "root";
	private String db_pw = "0123";
	private ResultSet rs = null;
	private Vector<Schedule_Data> schedule_vec;
	private Schedule_Data schedule_obj;
	private int week = 1; // 二쇱감 ���옣 �엫�떆 蹂��닔
	private SchoolCalendar c;
	private String lectureDate = "2014-08-25";

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

	// �씠踰덉＜ �떆媛꾪몴 �뀒�씠釉붿쓽 �뒠�뵆�쓣 �쟾泥� �궘�젣.
	// �쁾 �씠踰덉＜ �뀒�씠釉� �궘�젣 -> �떎�쓬二�-�씠踰덉＜濡� �뀒�씠釉� �뾽�뜲�씠�듃 -> �떎�쓬二� �뀒�씠釉�
	// �궘�젣 -> �떎�쓬二� �삁�빟 �뀒�씠釉� �뒠�뵆 異붽�

	// 硫붿냼�뱶 �샇異� �닚媛� 二쇱감瑜� 寃��궗
	// 二쇨� 諛붾�뚯뿀�떎硫� �뾽�뜲�씠�듃 �썑 媛��졇�삩�떎
	// �븞諛붾�뚯뿀�떎硫� 洹몃깷 媛��졇�삩�떎
	public String[] getSchedule(String team_name) {
		connect();

		StringBuffer SQL;
		String[] arr = new String[35];

		Calendar cc = Calendar.getInstance();
		DateFormat data = new SimpleDateFormat("YYYY-MM-dd");
		String today = data.format(cc.getTime());

		c = new SchoolCalendar(lectureDate, today);

		if (week != c.getW()) {
			week = c.getW();

			SQL = new StringBuffer(
					"replace into schedule select * from schedule;");
			System.err.println("SQL : " + SQL.toString());

			try {
				pstmt = con.prepareStatement(SQL.toString());
				pstmt.execute();
				// schedule replace

				SQL = new StringBuffer(
						"update schedule set t1='f', t2='f', t3='f', t4='f', t5='f', t6='f', t7='f', t8='f', t9='f', t10='f', t11='f', t12='f', t13='f', t14='f', t15='f',  t16='f', t17='f', t18='f', t19='f', t20='f', t21='f', t22='f', t23='f', t24='f', t25='f', t26='f', t27='f', t28='f', t29='f', t30='f', t31='f', t32='f', t33='f', t34='f', t35='f'");
				pstmt.execute();
				// reservation_schdule 紐⑤몢 f濡� update

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // 二쇨� 諛붾�뚯뿀�쑝硫�

		SQL = new StringBuffer(
				"select t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26, t27, t28, t29, t30, t31, t32, t33, t34, t35 from schedule where team_name='");
		SQL.append(team_name);
		SQL.append("'");
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

	// �� �씠由꾩쓣 諛쏆븘�꽌 t/f濡� �빐�떦 ���쓽 tn�쓣 �닔�젙
	// �씠踰덉＜ �삁�빟 痍⑥냼 �삁�빟�븯湲� 硫붿냼�뱶
	public void mod(String team_name, int index, String state) {
		connect();

		StringBuffer SQL = new StringBuffer("update schedule set t");
		SQL.append(index + "='" + state + "' where team_name='" + team_name
				+ "'");
		System.err.println("SQL : " + SQL.toString());

		try {
			pstmt = con.prepareStatement(SQL.toString());
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
	}

	// �뀒�씠釉� �뜲�씠�꽣 �궘�젣
	public void delete() {
		connect();

		StringBuffer SQL = new StringBuffer("delete from schedule");

		try {
			pstmt = con.prepareStatement(SQL.toString());
			pstmt.execute();
		} catch (Exception e) {

		}

		disconnect();
	}

	// �삁�빟 異붽�. �궗�슜�븯吏� �븡�뒗 硫붿냼�뱶.
	public void insert(Schedule_Data data) {
		connect();

		// replace into green_app.schedule select * from
		// green_app.reservation_schedule;
		StringBuffer SQL = new StringBuffer("insert into schedule ");
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
	}

	public String count_room(int index) {

		int temp_count = 0;

		connect();

		try {
			StringBuffer SQL = new StringBuffer(
					"select count(*) from schedule where t");
			SQL.append(index + "='t'");
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();

			rs.next();
			temp_count = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return (temp_count < 14 ? "t" : "f");
	}

}