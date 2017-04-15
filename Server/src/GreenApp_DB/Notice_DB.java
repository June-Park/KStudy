package GreenApp_DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class Notice_DB {

	private Connection con = null; // DB 占쏙옙占쏙옙
	private PreparedStatement pstmt = null;
	private String driverName = "com.mysql.jdbc.Driver";
	private String dbURL = "jdbc:mysql://localhost/green_app";
	private String db_name = "root";
	private String db_pw = "0123";
	private ResultSet rs = null;
	private Vector<Notice_Data> notice_vec;
	private Notice_Data notice_obj;

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

	// 怨듭� 異붽�
	public void insert(Notice_Data data) {
		connect();

		StringBuffer SQL = new StringBuffer("insert into notice ");
		SQL.append("values (?, ?, ?, ?, ?)");

		try {
			pstmt = con.prepareStatement(SQL.toString());

			pstmt.setInt(1, 0); // AI �옄�룞 �븷�떦
			pstmt.setString(2, data.getTitle());
			pstmt.setString(3, data.getContent());
			pstmt.setString(4, data.getUrl());
			pstmt.setString(5, getTime());
			// pstmt.setString(5, data.getDate()); ///////////////////////////

			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();
	}

	// 紐⑤뱺 怨듭� 媛��졇�� 踰≫꽣濡� 諛섑솚
	public Vector<Notice_Data> getNotice() {
		connect();

		StringBuffer SQL = new StringBuffer("SELECT * FROM notice");

		try {
			pstmt = con.prepareStatement(SQL.toString());
			rs = pstmt.executeQuery();

			notice_obj = new Notice_Data();
			notice_vec = new Vector<Notice_Data>();

			while (rs.next()) {
				notice_obj.setTitle(rs.getString(2));
				notice_obj.setContent(rs.getString(3));
				notice_obj.setUrl(rs.getString(4));
				notice_obj.setDate(rs.getString(5));

				notice_vec.add(notice_obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		disconnect();

		return notice_vec;
	}

	// 怨듭��쓽 �옉�꽦 �떆媛꾩쓣 諛쏆븘 �빐�떦 怨듭�瑜� �궘�젣�븳�떎.
	// date format : 140917051013 (12�옄由�)
	public void delete(String notice_date) {
		connect();

		StringBuffer SQL = new StringBuffer("delete from notice ");
		SQL.append("where date = '" + notice_date + "'");

		try {
			pstmt = con.prepareStatement(SQL.toString());
			pstmt.execute();
		} catch (Exception e) {

		}

		disconnect();
	}

	// �쁽�옱 �궇吏쒕�� yyMMddHHmmss �삎�떇�쑝濡� 諛섑솚
	public String getTime() {
		long now = System.currentTimeMillis();
		Date date = new Date(now);
		SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyMMddHHmmss");
		String time = CurDateFormat.format(date);

		return time;
	}
}
