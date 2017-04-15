package GreenApp_DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Member_DB {

	private Connection con = null; // DB 占쏙옙占쏙옙
	private PreparedStatement pstmt = null;
	private String driverName = "com.mysql.jdbc.Driver";
	private String dbURL = "jdbc:mysql://localhost/green_app";
	private String db_name = "root";
	private String db_pw = "0123";
	private ResultSet rs = null;
	private Vector<Member_Data> member_vec;
	private Member_Data member_obj;
	// /
	Reservation_Schedule_Data res_data = new Reservation_Schedule_Data();
	Reservation_Schedule_DB res_db = new Reservation_Schedule_DB();

	Schedule_Data sch_data = new Schedule_Data();
	Schedule_DB sch_db = new Schedule_DB();

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

	// 硫ㅻ쾭 異붽�
	public void insert(Member_Data data) {
		connect();

		StringBuffer SQL = new StringBuffer("insert into member ");
		SQL.append("values (?, ?, ?, ?, ?, ?, ?, ?)");

		try {
			pstmt = con.prepareStatement(SQL.toString());

			pstmt.setInt(1, 0); // AI �옄�룞 �븷�떦
			pstmt.setString(2, data.getTeam_name());
			pstmt.setString(3, data.getName());
			pstmt.setString(4, data.getPhone_number());
			pstmt.setString(5, data.getEmail_address());
			pstmt.setString(6, data.getMajor());
			pstmt.setInt(7, data.getStudent_number());
			pstmt.setInt(8, data.getCredits());

			System.err.println("pstmt : " + pstmt.toString());
			pstmt.execute();

			res_data.setTeam_name(data.getTeam_name());
			res_db.insert(res_data);

			sch_data.setTeam_name(data.getTeam_name());
			sch_db.insert(sch_data);

			// �쉶�썝媛��엯�쓣 �븯�뒗 �닚媛� �삁�빟 �뀒�씠釉붿뿉 湲곕낯 �뒠�뵆�씠 �깮�꽦�맂�떎.
			// �깉 �삁�빟�쓣 �븷 �떆�뿉�뒗 insert媛� �븘�땲�씪 湲곗〈 �뒠�뵆�쓽 update媛� �씠猷⑥뼱吏꾨떎.
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
	}

	// �븞�뱶濡쒖씠�뱶�뿉�꽌 ��紐낆쓣 諛쏆븘 ���썝�쓣 寃��깋�빐 ���썝�젙蹂대�� �꽆寃⑥��떎.
	// 諛섑솚�삎�� 踰≫꽣, 踰≫꽣 �궗�씠利덈뒗 蹂대궡吏� �븡�쓬.
	public Vector<Member_Data> getMember(String read) {

		System.err.println("getMember start");
		member_vec = new Vector<Member_Data>();

		connect();

		StringBuffer SQL = new StringBuffer(
				"select * from member where team_name = '" + read + "'");
		System.err.println("SQL : " + SQL.toString());

		try {
			pstmt = con.prepareStatement(SQL.toString());
			System.err.println("pstmt : " + pstmt.toString());
			rs = pstmt.executeQuery();
			System.err.println("rs : " + rs.toString());

			while (rs.next()) {

				member_obj = new Member_Data(); // 硫ㅻ쾭 媛앹껜 珥덇린�솕

				System.err.println(rs.getString(3));
				member_obj.setName(rs.getString(3)); // �씠由�
				System.err.println("member_obj.getName() : "
						+ member_obj.getName());
				System.err.println(rs.getString(4));
				member_obj.setPhone_number(rs.getString(4)); // �룿踰덊샇
				System.err.println("member_obj.getPhone_number() : "
						+ member_obj.getPhone_number());
				System.err.println(rs.getString(5));
				member_obj.setEmail_address(rs.getString(5)); // �씠硫붿씪
				System.err.println("member_obj.getEmail_address() : "
						+ member_obj.getEmail_address());
				System.err.println(rs.getInt(7));
				member_obj.setStudent_number(rs.getInt(7)); // �븰踰�
				System.err.println("member_obj.getStudent_number() : "
						+ member_obj.getStudent_number());

				// ////////// �씠嫄� 梨꾪깮�떆 荑쇰━臾몄쓽 select *, rs.getString(n) 遺�遺꾩쓣
				// �닔�젙�븷 寃�.

				/*
				 * member_obj.setNo(rs.getInt(1));
				 * member_obj.setTeam_name(rs.getString(2));
				 * member_obj.setName(rs.getString(3));
				 * member_obj.setPhone_number(rs.getInt(4));
				 * member_obj.setEmail_address(rs.getString(5));
				 * member_obj.setMajor(rs.getString(6));
				 * member_obj.setStudent_number(rs.getInt(7));
				 * member_obj.setMajor_code(rs.getString(8));
				 * member_obj.setCredits(rs.getInt(9));
				 */

				member_vec.add(member_obj);
			}
		} catch (Exception e) {
			System.err.println("catch");
		}

		disconnect();
		System.err.println("getMember finish");

		return member_vec;
	}
}