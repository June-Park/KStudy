package GreenApp_DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import School_Calendar.SchoolCalendar;

public class Time_Check_DB {

	private Connection con = null;
	private PreparedStatement pstmt = null;
	private String driverName = "com.mysql.jdbc.Driver";
	private String dbURL = "jdbc:mysql://localhost/green_app";
	private String db_name = "root";
	private String db_pw = "0123";
	private ResultSet rs = null;
	private SchoolCalendar c = new SchoolCalendar();
	String lecture = "2014-08-25";

	// /setLecture setToday getW

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

	public void insert(Time_Check_Data data) {
		connect();

		StringBuffer SQL = new StringBuffer("insert into time_check ");
		SQL.append("(team_name, study_type, assigned_time, sum, left_time, no)"
				+ " values (?, ?, ?, ?, ?, ?)");
		System.err.println("SQL : " + SQL.toString());
		try {

			pstmt = con.prepareStatement(SQL.toString());

			pstmt.setString(1, data.getTeam_name());
			pstmt.setInt(2, data.getStudy_type());
			pstmt.setInt(3, data.getAssigned_time());
			pstmt.setInt(4, data.getTotal_time());
			pstmt.setInt(5, data.getLeft_time());
			pstmt.setInt(6, 0);

			pstmt.execute();
			System.err.println("pstmt : " + pstmt.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
	}

	// default 1踰�. 紐⑤뱺 ���쓽 吏��궃二� �떆媛� / �� 媛쒖닔
	public int default_getLastWeek_Aver() {
		c.setLecture(lecture);
		c.setToday(c.get_today());

		String last_week = (c.get_day(c.get_today()) < 4 ? c.getW() - 2 : c
				.getW() - 1) + "";
		int total_sum = 0; // 紐⑤뱺 二쇱감�쓽 �빀
		int team_count = 0; // 紐⑤뱺 ���쓽 媛쒖닔

		// �뒪�꽣�뵒�뒗 3二쇰��꽣 �떆�옉. 留뚯빟 3二� �씠�쟾�쓽 �뜲�씠�꽣瑜� �슂援ы븯硫� 硫붿냼�뱶�뒗 0�쓣
		// 諛섑솚�븳�떎.
		if (Integer.parseInt(last_week) < 3) {
			return 0;
		}

		// �뿰寃�
		connect();

		StringBuffer SQL = new StringBuffer("select sum(week");
		SQL.append(last_week);
		SQL.append(") from time_check");

		try {

			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			total_sum = rs.getInt(1); // 紐⑤뱺 二쇱감�쓽 �빀�쓣 媛��졇�삩�떎.

			SQL = new StringBuffer("select count(*) from time_check");
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			team_count = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return total_sum / team_count; // �쟾泥� 二쇱감 �빀 / �� 媛��닔
	}

	// default 3踰�. 紐⑤뱺 ���쓽 �늻�쟻�떆媛� / �� 媛쒖닔
	public int default_getAccumulate_Aver() {
		int total_sum = 0; // 紐⑤뱺 ���뱾�쓽 �늻�쟻�떆媛꾩쓣 �뜑�븳 媛�
		int team_count = 0; // �쟾泥� �� �닔
		// �뿰寃�
		connect();

		StringBuffer SQL = new StringBuffer("select sum(sum) from time_check");

		try {

			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			total_sum = rs.getInt(1); // 紐⑤뱺 �늻�쟻�떆媛꾩쓽 �빀

			SQL = new StringBuffer("select count(*) from time_check");
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			team_count = rs.getInt(1); // �� 媛쒖닔

		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return total_sum / team_count; // �쟾泥� �늻�쟻 �빀 / �� 媛��닔
	}

	// default 2踰�. 紐⑤뱺 ���쓽 �늻�쟻�떆媛� / 二쇱감 / �� 媛쒖닔
	public int default_getWeekAccumulate_Aver() {
		c.setLecture(lecture);
		c.setToday(c.get_today());

		int week = c.getW() - 2; // 二쇱감
		int total_sum = 0; // 紐⑤뱺 二쇱감�쓽 �빀
		int team_count = 0; // 紐⑤뱺 ���쓽 媛쒖닔

		// �뿰寃�
		connect();

		StringBuffer SQL = new StringBuffer("select sum(sum) from time_check");

		try {

			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			total_sum = rs.getInt(1); // 紐⑤뱺 �늻�쟻�떆媛꾩쓽 �빀

			SQL = new StringBuffer("select count(*) from time_check");
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			team_count = rs.getInt(1); // �� 媛쒖닔

		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return total_sum / team_count / week; // �쟾泥� 二쇱감 �빀 / �� 媛��닔 / 二쇱감
	}

	// default 4踰�. �쟾泥� 3二�/�� 媛쒖닔 瑜� 理쒓렐 4二쇱튂
	public int[] default_getRecent4Week() {
		int[] arr = { 0, 0, 0, 0 };
		c.setLecture(lecture);
		c.setToday(c.get_today());

		int week = c.getW() - 1; // 二�
		int team_count = 0; // �� 媛쒖닔
		StringBuffer SQL;

		connect();

		try {
			SQL = new StringBuffer("select count(*) from time_check");
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			team_count = rs.getInt(1); // �� 媛쒖닔

			SQL = new StringBuffer("select sum(week" + week
					+ ") from time_check");
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			arr[3] = rs.getInt(1) / team_count; // �빐�떦 二쇱쓽 �쟾泥� �떆媛꾩쓽 �빀�쓣
												// 媛��졇�샂 / �� 媛쒖닔

			if ((week - 3) >= 3) {
				SQL = new StringBuffer("select sum(week" + (week - 3)
						+ ") from time_check");

				pstmt = con.prepareStatement(SQL.toString());
				rs = pstmt.executeQuery();
				rs.next();
				arr[0] = rs.getInt(1) / team_count;
			}

			if ((week - 2) >= 3) {
				SQL = new StringBuffer("select sum(week" + (week - 2)
						+ ") from time_check");

				pstmt = con.prepareStatement(SQL.toString());
				rs = pstmt.executeQuery();
				rs.next();
				arr[1] = rs.getInt(1) / team_count;
			}

			if ((week - 1) >= 3) {
				SQL = new StringBuffer("select sum(week" + (week - 1)
						+ ") from time_check ");

				pstmt = con.prepareStatement(SQL.toString());
				rs = pstmt.executeQuery();
				rs.next();
				arr[2] = rs.getInt(1) / team_count;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return arr;
	}

	// 1. �� �씠由꾩쓣 留ㅺ컻蹂��닔濡� 諛쏆븘 �빐�떦 ���쓽 吏��궃二� �븰�뒿�떆媛꾩쓣 諛섑솚
	public int getLastWeek(String team_name) {
		c.setLecture(lecture);
		c.setToday(c.get_today());

		String last_week = (c.getW() - 1) + ""; // 吏��궃二�
		int rst = 0;

		// �뿰寃�
		connect();

		StringBuffer SQL = new StringBuffer("select week");
		SQL.append(last_week);
		SQL.append(" from time_check where team_name='");
		SQL.append(team_name + "'");

		try {

			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			rst = rs.getInt(1); // 紐⑤뱺 二쇱감�쓽 �빀�쓣 媛��졇�삩�떎.

		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return rst;
	}

	// 2. �븳 ���쓽 �늻�쟻�떆媛�/二�
	public int getWeekAver(String team_name) {
		c.setLecture(lecture);
		c.setToday(c.get_today());

		int week = c.getW() - 2; // �뒪�꽣�뵒 吏꾪뻾 二�
		int rst = 0; // �빐�떦 ���쓽 �늻�쟻 �떆媛�

		// �뿰寃�
		connect();

		StringBuffer SQL = new StringBuffer("select sum from time_check ");
		SQL.append("where team_name='");
		SQL.append(team_name + "'");

		try {

			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			rst = rs.getInt(1); // �빐�떦 ���쓽 �늻�쟻 �떆媛꾩쓣 媛��졇�삩�떎

		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return rst / week; // �늻�쟻�떆媛�/�뒪�꽣�뵒湲곌컙(二�)
	}

	// 3. �빐�떦 ���쓽 �늻�쟻 �떆媛�
	public int getSum(String team_name) {
		int rst = 0; // �빐�떦 ���쓽 �늻�쟻 �떆媛�

		// �뿰寃�
		connect();

		StringBuffer SQL = new StringBuffer("select sum from time_check ");
		SQL.append("where team_name='");
		SQL.append(team_name + "'");

		try {
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			rst = rs.getInt(1); // �빐�떦 ���쓽 �늻�쟻 �떆媛꾩쓣 媛��졇�삩�떎

		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return rst;
	}

	// 4. �빐�떦 ���쓽 吏��궃 4二쇨컙 �뒪�꽣�뵒 �쁽�솴. 3, 4, 5二쇱쓽 寃쎌슦 �븵�쓣 0�쑝濡� 梨�
	public int[] getRecent4Week(String team_name) {
		int[] arr = { 0, 0, 0, 0 };
		c.setLecture(lecture);
		c.setToday(c.get_today());

		int week = c.getW() - 1; // 二�
		StringBuffer SQL;

		connect();

		SQL = new StringBuffer("select week" + week + " from time_check ");
		SQL.append("where team_name='");
		SQL.append(team_name + "'");
		System.err.println("SQL : " + SQL.toString());

		try {

			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			arr[3] = rs.getInt(1); // �빐�떦 ���쓽 �늻�쟻 �떆媛꾩쓣 媛��졇�삩�떎

			if ((week - 3) >= 3) {
				SQL = new StringBuffer("select week" + (week - 3)
						+ " from time_check ");
				SQL.append("where team_name='");
				SQL.append(team_name + "'");

				pstmt = con.prepareStatement(SQL.toString());
				rs = pstmt.executeQuery();
				rs.next();
				arr[0] = rs.getInt(1);
			}

			if ((week - 2) >= 3) {
				SQL = new StringBuffer("select week" + (week - 2)
						+ " from time_check ");
				SQL.append("where team_name='");
				SQL.append(team_name + "'");

				pstmt = con.prepareStatement(SQL.toString());
				rs = pstmt.executeQuery();
				rs.next();
				arr[1] = rs.getInt(1);
			}

			if ((week - 1) >= 3) {
				SQL = new StringBuffer("select week" + (week - 1)
						+ " from time_check ");
				SQL.append("where team_name='");
				SQL.append(team_name + "'");

				pstmt = con.prepareStatement(SQL.toString());
				rs = pstmt.executeQuery();
				rs.next();
				arr[2] = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return arr;
	}

	// 5. 爰얠��꽑洹몃옒�봽. �뒪�꽣�뵒 醫낅쪟蹂� 紐⑤뱺 4二쇱감 �룊洹�
	public int[] getRecent4WeekS(String study_type) {
		int[] arr = { 0, 0, 0, 0 };
		c.setLecture(lecture);
		c.setToday(c.get_today());

		int week = c.getW() - 1; // 二�
		int team_count = 0; // �� 媛쒖닔
		StringBuffer SQL;

		connect();

		try {

			SQL = new StringBuffer("select count(*) from time_check");
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			team_count = rs.getInt(1); // �� 媛쒖닔

			SQL = new StringBuffer("select sum(week" + week
					+ ") from time_check ");
			SQL.append("where study_type=(select study_type from time_check where team_name='");
			SQL.append(study_type + "')");

			System.err.println("SQL : " + SQL.toString());
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			arr[3] = rs.getInt(1) / team_count; // �빐�떦 二쇱감�쓽 紐⑤뱺 �빀(媛숈�
												// �뒪�꽣�뵒�쓽) / �� 媛쒖닔

			if ((week - 3) >= 3) {
				SQL = new StringBuffer("select sum(week" + (week - 3)
						+ ") from time_check ");
				SQL.append("where study_type=(select study_type from time_check where team_name='");
				SQL.append(study_type + "')");

				pstmt = con.prepareStatement(SQL.toString());
				rs = pstmt.executeQuery();
				rs.next();
				arr[0] = rs.getInt(1) / team_count; // 3二� �쟾�쓽 紐⑤뱺 �빀(媛숈�
													// �뒪�꽣�뵒�쓽) / �� 媛쒖닔
			}

			if ((week - 2) >= 3) {
				SQL = new StringBuffer("select sum(week" + (week - 2)
						+ ") from time_check ");
				SQL.append("where study_type=(select study_type from time_check where team_name='");
				SQL.append(study_type + "')");

				pstmt = con.prepareStatement(SQL.toString());
				rs = pstmt.executeQuery();
				rs.next();
				arr[1] = rs.getInt(1) / team_count; // 2二� �쟾�쓽 紐⑤뱺 �빀(媛숈�
													// �뒪�꽣�뵒�쓽) / �� 媛쒖닔
			}

			if ((week - 1) >= 3) {
				SQL = new StringBuffer("select sum(week" + (week - 1)
						+ ") from time_check ");
				SQL.append("where study_type=(select study_type from time_check where team_name='");
				SQL.append(study_type + "')");

				pstmt = con.prepareStatement(SQL.toString());
				rs = pstmt.executeQuery();
				rs.next();
				arr[2] = rs.getInt(1) / team_count; // 吏��궃二쇱쓽 紐⑤뱺 �빀(媛숈�
													// �뒪�꽣�뵒�쓽) / �� 媛쒖닔
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return arr;
	}
}