package com.example.kstudyactionbar;

import android.app.ActionBar;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;

public class MainHelpActivity extends Activity {

	/*
	 * SQLite 관련 변수
	 */
	SQLiteDatabase database;
	String db_name = "cs3.db";
	String db_table = "help";

	DB_SQLite sqLite;

	ImageView iv_help;
	CheckBox cb_help_view;
	ImageView iv_help_close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_help);

		// 액션바 숨김
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		iv_help = (ImageView) findViewById(R.id.iv_main_help);
		iv_help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		cb_help_view = (CheckBox) findViewById(R.id.checkBox_help);
		if (cb_help_view.isChecked()) {
			// 안드로이드 내장 DB SQLite
			sqLite = new DB_SQLite();
			sqLite.execute();
		}

		iv_help_close = (ImageView) findViewById(R.id.iv_main_help_close);
		iv_help_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (cb_help_view.isChecked()) {
			// 안드로이드 내장 DB SQLite
			sqLite = new DB_SQLite();
			sqLite.execute();
		}
	}

	// DB 생성
	private void DB_createOpenDatabase() {
		// DB가 존재하면 오픈. 존재하지 않으면 생성
		Log.e("tag", "DB_createOpenDatabase");
		database = openOrCreateDatabase(db_name, 0, null);
	}

	// 테이블 생성
	private void DB_createTable() {
		Log.e("tag", "DB_createTable");
		// String sql = "create table " + db_table + "(db_num, db_text)";
		// db_0은 다시 보지 않기 체크 여부, 1은 reset 시 보여짐 설정
		String sql = "create table " + db_table
				+ "(db_num integer primary key autoincrement, db_help String)";
		try {
			database.execSQL(sql);// slq문 실행
		} catch (Exception e) {
		}

		sql = "insert into " + db_table + "(db_num, db_help) values(0,'F')";
		try {
			database.execSQL(sql);// slq문 실행
		} catch (Exception e) {
		}
	}

	// 데이터 수정
	private void DB_UpdateData(int num, String text) {
		Log.e("tag", "DB_insertData");
		// sql문을 실행하는 일정구간을 트랜잭션으로 묶어주겠다라는 의미로 트랜잭션 시작을 나타내는 메소드
		database.beginTransaction();

		try {
			// String sql = "update " + db_table + " set db_help = '" + text +
			// "'";
			String sql = "update " + db_table + "(db_num, db_help) values('"
					+ num + "', '" + text + "')";
			database.execSQL(sql);

			// 트랜잭션으로 묶어준 일정영역의 SQL문들이 모두 성공적으로 끝났을 지정
			database.setTransactionSuccessful();

		} catch (Exception e) {
		} finally {
			// 트랜잭션을 끝내는 메소드.
			database.endTransaction();
		}
	}

	class DB_SQLite extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			Log.e("tag", "doinbackground");
			// SQLite DB연결 후 버전정보를 가져와 서버에 전송
			DB_createOpenDatabase();
			DB_createTable();
			if (MainActivity.help_state == null)
				DB_UpdateData(0, "F");
			MainActivity.help_state = "F";
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			Log.e("tag", "onprogressupdate");
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.e("ccc", "DB_getD9ata().equals(f)");
		}
	}
}