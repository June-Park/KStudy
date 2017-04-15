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
	 * SQLite ���� ����
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

		// �׼ǹ� ����
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
			// �ȵ���̵� ���� DB SQLite
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
			// �ȵ���̵� ���� DB SQLite
			sqLite = new DB_SQLite();
			sqLite.execute();
		}
	}

	// DB ����
	private void DB_createOpenDatabase() {
		// DB�� �����ϸ� ����. �������� ������ ����
		Log.e("tag", "DB_createOpenDatabase");
		database = openOrCreateDatabase(db_name, 0, null);
	}

	// ���̺� ����
	private void DB_createTable() {
		Log.e("tag", "DB_createTable");
		// String sql = "create table " + db_table + "(db_num, db_text)";
		// db_0�� �ٽ� ���� �ʱ� üũ ����, 1�� reset �� ������ ����
		String sql = "create table " + db_table
				+ "(db_num integer primary key autoincrement, db_help String)";
		try {
			database.execSQL(sql);// slq�� ����
		} catch (Exception e) {
		}

		sql = "insert into " + db_table + "(db_num, db_help) values(0,'F')";
		try {
			database.execSQL(sql);// slq�� ����
		} catch (Exception e) {
		}
	}

	// ������ ����
	private void DB_UpdateData(int num, String text) {
		Log.e("tag", "DB_insertData");
		// sql���� �����ϴ� ���������� Ʈ��������� �����ְڴٶ�� �ǹ̷� Ʈ����� ������ ��Ÿ���� �޼ҵ�
		database.beginTransaction();

		try {
			// String sql = "update " + db_table + " set db_help = '" + text +
			// "'";
			String sql = "update " + db_table + "(db_num, db_help) values('"
					+ num + "', '" + text + "')";
			database.execSQL(sql);

			// Ʈ��������� ������ ���������� SQL������ ��� ���������� ������ ����
			database.setTransactionSuccessful();

		} catch (Exception e) {
		} finally {
			// Ʈ������� ������ �޼ҵ�.
			database.endTransaction();
		}
	}

	class DB_SQLite extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			Log.e("tag", "doinbackground");
			// SQLite DB���� �� ���������� ������ ������ ����
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