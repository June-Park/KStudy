package GreenApp_DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Team_List_DB {

	private Connection con = null;
	private PreparedStatement pstmt = null;
	private String driverName = "com.mysql.jdbc.Driver";
	private String dbURL = "jdbc:mysql://localhost/green_app";
	private String db_name = "root";
	private String db_pw = "0123";
	private ResultSet rs = null;
	private Vector<Team_List_Data> team_vec;
	private Team_List_Data team_obj;
	private String temp_pw;

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

	// �� 異붽�
	public void insert(Team_List_Data data) {
		connect();

		StringBuffer SQL = new StringBuffer("insert into team_list ");
		SQL.append("values (?, ?, ?, ?, ?, ?)");
		System.err.println("SQL : " + SQL.toString());

		try {

			pstmt = con.prepareStatement(SQL.toString());

			pstmt.setString(1, data.getTeam_name());
			pstmt.setString(2, data.getTeam_pw());
			pstmt.setString(3, data.getStudy_type());
			pstmt.setString(4, data.getSubject());
			pstmt.setString(5, data.getDonation_consent());
			pstmt.setInt(6, data.getOrientation());

			pstmt.execute();
			System.err.println("pstmt : " + pstmt.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
	}

	// �븞�뱶濡쒖씠�뱶�뿉�꽌 ��紐낆쓣 諛쏆븘 鍮꾨�踰덊샇瑜� �솗�씤. 留욌뒗 鍮꾨�踰덊샇硫� Y, �븘�땲硫� N.
	public String check_pw(String read, String pw) {

		connect();

		StringBuffer SQL = new StringBuffer(
				"select team_pw from team_list where team_name = '" + read
						+ "'");
		try {
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			temp_pw = rs.getString(1);
		} catch (Exception e) {
			System.err.println(e.toString());
			return "N";
		}

		disconnect();
		System.err.println("team_pw : " + temp_pw);

		return (temp_pw.equals(pw) ? "Y" : "N");
	}

	// �븞�뱶濡쒖씠�뱶�뿉�꽌 ��紐낆쓣 諛쏆븘 �뒪�꽣�뵒 ���엯�쓣 int�삎�쑝濡� �룎�젮以��떎.
	// 1-1�뒪�꽣�뵒, 2-3�뒪�꽣�뵒, 3-�씡�뒪�듃由�, 4-CIS
	public String check_study_type(String read) {

		String temp_type = "";

		connect();

		StringBuffer SQL = new StringBuffer(
				"select study_type from team_list where team_name = '" + read
						+ "'");

		try {
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();

			rs.next();
			temp_type = rs.getString(1);
		} catch (Exception e) {

		}

		disconnect();

		return temp_type;
	}

	// CIS 팀 목록 전송
	public String get_CIS_List(String str) {
		String temp_str = "";

		connect();

		StringBuffer SQL = new StringBuffer(
				"select team_name from team_list where study_type='4'");

		try {
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				temp_str += (rs.getString(1) + "//");
			}

		} catch (Exception e) {

		}

		disconnect();

		System.err.println("cis team list : " + temp_str);
		return temp_str;
	}

	public Vector<String[]> get_TeamList() {

		Vector<String[]> temp_vec = new Vector<String[]>();
		String[] temp_arr;

		connect();

		StringBuffer SQL = new StringBuffer(
				"select team_name, study_type from team_list");

		System.err.println("SQL : " + SQL.toString());

		try {
			pstmt = con.prepareStatement(SQL.toString());
			System.err.println("pstmt : " + pstmt.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				temp_arr = new String[2];
				temp_arr[0] = rs.getString(1);
				temp_arr[1] = rs.getString(2);
				temp_vec.add(temp_arr);
			}

		} catch (Exception e) {

		}

		disconnect();

		return temp_vec;
	}

	// ID 중복검사
	public String check_id(String team_name) {

		connect();

		StringBuffer SQL = new StringBuffer(
				"select team_name from team_list where team_name = '"
						+ team_name + "'");
		try {
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();
			rs.next();
			if (rs.getString(1) == null)
				return "Y";
		} catch (Exception e) {
			System.err.println(e.toString());
			return "Y";
		}

		disconnect();

		return "N";
	}

}