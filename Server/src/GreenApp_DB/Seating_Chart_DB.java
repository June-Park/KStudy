package GreenApp_DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Seating_Chart_DB {
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private String driverName = "com.mysql.jdbc.Driver";
	private String dbURL = "jdbc:mysql://localhost/green_app";
	private String db_name = "root";
	private String db_pw = "0123";
	private ResultSet rs = null;
	private Vector<Time_Check_Data> time_vec;
	private Time_Check_Data time_obj;

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

	// 媛� 諛⑹쓽 on/off 蹂��솕
	// 諛� 踰덊샇瑜� 諛쏆븘 �긽�깭媛� F->T, T->F濡� 蹂��솚�븳�떎.
	public void state_change(String room_number) {
		connect();

		// room 인덱스에 따라 속성명으로 바꿔주기
		for (int i = 0; i < 3; i++)
			if (room_number.equals(i + ""))
				room_number = "s" + (i + 1);
		for (int i = 3; i < 14; i++)
			if (room_number.equals(i + ""))
				room_number = "m" + (i - 2);

		String temp_roomState = ""; // �씠�쟾�쓽 諛� �긽�깭瑜� ���옣�븷 �엫�떆 蹂��닔
		String next_roomState = "";

		StringBuffer SQL = new StringBuffer("select " + room_number
				+ " from seating_chart");

		try {
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			temp_roomState = rs.getString(1);
		} catch (Exception e) {
		}

		next_roomState = (temp_roomState.equals("t") ? "f" : "t");
		// 猷� �긽�깭媛� T ���쑝硫� F濡�, F���쑝硫� T濡� �떎�쓬 �긽�깭瑜� 吏��젙�빐以��떎.

		// SQL = new StringBuffer("SET SQL_SAFE_UPDATES=0"); // update safe
		// 紐⑤뱶�씪 寃쎌슦 �빐�젣
		SQL = new StringBuffer("");
		SQL.append("update seating_chart set " + room_number + "='"
				+ next_roomState + "'");
		// insert媛� �븘�땶 update �떆�궓�떎.c
		// �빐�떦 �뀒�씠釉붿쓽 �뒠�뵆�� �빆�긽 �븯�굹.

		try {
			pstmt = con.prepareStatement(SQL.toString());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
	}

	// 諛� �쟾泥댁쓽 t/f瑜� �뒪�듃留� 諛곗뿴濡� 諛섑솚
	public String[] getState() {
		// String[] arr = new String[14];
		String[] arr = { "t", "e", "s", "t", "", "", "", "", "", "", "", "",
				"", "", };

		connect();

		StringBuffer SQL = new StringBuffer("SELECT * FROM seating_chart");

		try {
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				for (int a = 0; a < 14; a++) {
					arr[a] = rs.getString(a + 2);
					System.err.println(arr[a] + "/" + a);
				}
			}
		} catch (Exception e) {
		}

		disconnect();

		return arr;
	}
}
/*
 * package GreenApp_DB;
 * 
 * //�� 異붽� public void insert(team_list_data data) { connect();
 * 
 * StringBuffer SQL = new StringBuffer("insert into team_list ");
 * SQL.append("values (?, ?, ?, ?, ?)");
 * 
 * try {
 * 
 * pstmt = con.prepareStatement(SQL.toString());
 * 
 * pstmt.setString(1, data.getTeam_name()); pstmt.setString(2,
 * data.getTeam_pw()); pstmt.setString(3, data.getStudy_type());
 * pstmt.setString(4, data.getDonation_consent()); pstmt.setInt(5,
 * data.getOrientation());
 * 
 * pstmt.execute(); } catch (SQLException e) { e.printStackTrace(); }
 * 
 * disconnect(); }
 * 
 * }
 */