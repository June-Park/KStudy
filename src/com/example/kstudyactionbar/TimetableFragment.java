package com.example.kstudyactionbar;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimetableFragment extends Fragment {

	/*
	 * SQLite ���� ����
	 */
	SQLiteDatabase database;
	String db_name = "cs3.db";
	String db_table = "timetable";
	int db_num[] = new int[45];
	static String db_text[] = new String[45];

	DB_SQLite sqLite;

	nextScheduleSync n_scheduleSync;
	ScheduleSync scheduleSync;
	ProgressDialog progressDialog;
	int scheduleNum;

	SocketThread socketThread;
	Thread thread;

	static Button btn_next_timetable[]; // ������ ���� �ð�ǥ
	static Button btn_this_timetable[]; // �̹��� �ð�ǥ

	// �� ���࿡ �ش� t/f
	static String next_timetable[] = new String[35];
	static String this_timetable[] = new String[35];
	// ������ �ð�ǥ �� ���� ��Ȳ �� (n/10)
	static int next_timetable_count[] = new int[35];
	// ���� ��Ȳ ���� ����
	static String isTrue = "t";

	// �� ���� �ð� ��
	static int nextCount = 0; // ������ ���� �ð� ��
	static int thisCount = 0; // �̹��� ���� �ð� ��
	int ableCount = 0; // ���� ���� �ð�

	Button btn_change; // �ð�ǥ ���� ��ư
	Button btn_ok; // �ð�ǥ ���� ��ư �Ϸ�

	static Dialog_button dialog; // ��� �˾�
	Dialog_two_button two_dialog; // ���� �˾�
	Dialog_edit edit_dialog; // �ð�ǥ ���� �˾�

	@Override
	// �ʱ�ȭ
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	// �ʱ�ȭ �Ϸ��� ����
	public void onStart() {
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.timetable_fragment, container,
				false);

		// �ȵ���̵� ���� DB SQLite
		sqLite = new DB_SQLite();
		sqLite.execute();

		thisCount = 0;
		nextCount = 0;

		// �α��� ������ ��
		if (MainActivity.login_state) {
			// �� ����� �ð� �� üũ
			for (int i = 0; i < 35; i++) {
				if (this_timetable[i].equals("t"))
					thisCount++;
				if (next_timetable[i].equals("t"))
					nextCount++;
			}

			// ���͵� �������� ���� ���� �ð�
			if (MainActivity.team_type.equals("1")
					|| MainActivity.team_type.equals("4"))
				ableCount = 1;
			else if (MainActivity.team_type.equals("2"))
				ableCount = 3;
			else
				ableCount = 5;
		}

		// ���� & ��� �˾�
		two_dialog = new Dialog_two_button(getActivity(), "Ȯ��", "�����Ͻðڽ��ϱ�?",
				true, 0);
		// ��� �˾�
		dialog = new Dialog_button(getActivity(), "���", "�ִ� ���� ���� �ð��� "
				+ ableCount + "�ð� �Դϴ�.");
		// �ð�ǥ ���� �˾�
		edit_dialog = new Dialog_edit(getActivity(), "�ð�ǥ ����",
				"������ �ð�ǥ�� �Է��ϼ���.", 0);

		// ������ �ð�ǥ ��ư
		int btn_id = R.id.btn_timetable1;
		btn_next_timetable = new Button[35];
		for (int i = 0; i < btn_next_timetable.length; i++) {
			btn_next_timetable[i] = (Button) view.findViewById(btn_id + i);
			btn_next_timetable[i].setTag(i); // ��ư ��ȣ (ID)
			btn_next_timetable[i].setText(next_timetable_count[i] + "/10");
			btn_next_timetable[i].setSelected(false);
		}
		if (MainActivity.login_state) {
			// �α��� ������ �� ������ ���� ���� ������
			for (int i = 0; i < next_timetable.length; i++)
				// ����� ������ �� isSelected�� true
				if (next_timetable[i].equals("t")) {
					btn_next_timetable[i].setSelected(true);
					btn_next_timetable[i]
							.setBackgroundResource(R.drawable.next_timetable_on);
				}
		}

		for (int i = 0; i < 35; i++) {
			btn_next_timetable[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// �α��� ������ ���� ����/��� ����
					if (MainActivity.login_state) {
						// ������� ���� �κ��̸� ����, ����� �κ��̸� ���
						if (v.isSelected()) {
							two_dialog.setContentstring("������ ����Ͻðڽ��ϱ�?");
							two_dialog.setNum((Integer) v.getTag());
							two_dialog.setThisweek(true);
							two_dialog.show();
						} else {
							// ���� ���� �ð��� ���� ���� �ð����� ���� ��� ���� �ȳ� �˾�
							if (nextCount < ableCount) {
								two_dialog.setContentstring("�����Ͻðڽ��ϱ�?");
								two_dialog.setNum((Integer) v.getTag());
								two_dialog.setThisweek(true);
								two_dialog.show();
							} else {
								// ��û�� �� �ִ� �� �ʰ�
								dialog.setContentstring("�ִ� ���� ���� �ð��� "
										+ ableCount + "�ð� �Դϴ�.");
								dialog.show();
							}
						}
					} else {
						// �α��� �� �̿��� �ּ��� �˾�
						dialog.setContentstring("�α��� �� �̿� �����մϴ�.");
						dialog.show();
					}
				}
			});
		}

		Log.e("tag", "this timetable start");
		// �̹��� �ð�ǥ ��ư
		int iv_id = R.id.iv_timetable1;
		btn_this_timetable = new Button[45];

		String str;
		for (int i = 0; i < btn_this_timetable.length; i++) {
			btn_this_timetable[i] = (Button) view.findViewById(iv_id + i);
			btn_this_timetable[i].setTag(i);
			btn_this_timetable[i].setText(db_text[i]);
			btn_this_timetable[i].setSelected(false);
		}
		if (MainActivity.login_state) {
			// �α��� ������ �� �̹��� ���� ���� ������
			Log.e("ccc", "this_timetable");
			for (int i = 0; i < this_timetable.length; i++)
				// ����� ������ �� isSelected�� true
				if (this_timetable[i].equals("t")) {
					btn_this_timetable[i + 5].setSelected(true);
					btn_this_timetable[i + 5]
							.setBackgroundResource(R.drawable.timetable_orange);
				}
		}

		for (int i = 0; i < 45; i++) {
			btn_this_timetable[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// �ð�ǥ ���� Ȱ��ȭ ����
					if (btn_change.isSelected()) {
						edit_dialog.setNum((Integer) v.getTag());
						edit_dialog.editText.setText(null);
						edit_dialog.show();
					}
					// �ð�ǥ ���� ��Ȱ��ȭ ����
					else {
						// �α��� ������ ���� ����/��� ����
						if (MainActivity.login_state) {
							int intTag = (Integer) v.getTag();
							if (intTag >= 5 && intTag <= 40) {
								// ������� ���� �κ��̸� ����, ����� �κ��̸� ���
								if (v.isSelected()) {
									two_dialog
											.setContentstring("������ ����Ͻðڽ��ϱ�?");
									two_dialog.setNum(intTag);
									two_dialog.setThisweek(false);
									two_dialog.show();
								} else {
									if (thisCount < ableCount) {
										two_dialog
												.setContentstring("�����Ͻðڽ��ϱ�?");
										two_dialog.setNum(intTag);
										two_dialog.setThisweek(false);
										two_dialog.show();
									} else {
										// ��û�� �� �ִ� �� �ʰ�
										dialog.setContentstring("�ִ� ���� ���� �ð��� "
												+ ableCount + "�ð� �Դϴ�.");
										dialog.show();
									}
								}
							} else {
								// ���� �Ұ��� �ð�!!
								dialog.setContentstring("���� �Ұ����� �ð��Դϴ�.");
								dialog.show();
							}
						} else {
							// �α��� �� �̿��� �ּ��� �˾�
							dialog.setContentstring("�α��� �� �̿� �����մϴ�.");
							dialog.show();
						}
					}
				}
			});
		}

		// �̹��� �� ���� Ȯ�� & �ð�ǥ
		// �� �ð�ǥ ����
		btn_change = (Button) view.findViewById(R.id.btn_timetable_change);
		btn_ok = (Button) view.findViewById(R.id.btn_timetable_finish);
		btn_change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �ð�ǥ ���� ��ư Ŭ��
				// btn_chagne ����, isSelected�� false
				// btn_ok ������, isSelected�� true
				btn_change.setVisibility(View.GONE);
				btn_change.setSelected(true);
				btn_ok.setVisibility(View.VISIBLE);
				btn_ok.setSelected(false);
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �ð�ǥ ���� �Ϸ� ��ư Ŭ��
				// btn_change ������, isSelected�� true
				// btn_ok ����, isSelected�� false
				btn_change.setVisibility(View.VISIBLE);
				btn_change.setSelected(false);
				btn_ok.setVisibility(View.GONE);
				btn_ok.setSelected(true);

				// SQLite timetable DB ����
				DB_deleteData();
				for (int i = 0; i < 45; i++)
					DB_insertData(i, btn_this_timetable[i].getText().toString());
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public class Dialog_two_button extends Dialog implements OnClickListener {
		LayoutInflater li;
		LinearLayout edit;
		TextView tv_yes;
		TextView tv_no;
		TextView titletext;
		TextView contenttext;
		String titlestring;
		String contentstring;
		boolean thisweek;
		int num;
		LinearLayout layout;
		Context cont;

		public Dialog_two_button(Context context, String title, String content,
				Boolean week, int num) {
			super(context);

			li = getLayoutInflater();
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			View v = (View) li.inflate(R.layout.dialog, null);
			layout = (LinearLayout) v.findViewById(R.id.linearlayout_dialog);

			v.setLayoutParams(new LinearLayout.LayoutParams(
					MainActivity.dialog_width, MainActivity.dialog_height));
			layout.setLayoutParams(new LinearLayout.LayoutParams(
					MainActivity.dialog_width, MainActivity.dialog_height));

			edit = (LinearLayout) v.findViewById(R.id.layout_dialog_edit);
			edit.setVisibility(View.GONE);

			titletext = (TextView) v.findViewById(R.id.tv_dialog_title);
			contenttext = (TextView) v.findViewById(R.id.tv_dialog_content);
			tv_yes = (TextView) v.findViewById(R.id.tv_dialog_yes);
			tv_no = (TextView) v.findViewById(R.id.tv_dialog_no);
			tv_yes.setOnClickListener(this);
			tv_no.setOnClickListener(this);
			setContentView(v);

			Typeface typeface = Typeface.createFromAsset(getContext()
					.getAssets(), "happy_light.TTF");
			titletext.setTypeface(typeface);
			contenttext.setTypeface(typeface);

			typeface = Typeface.createFromAsset(getContext().getAssets(),
					"happy_bold.TTF");
			tv_yes.setTypeface(typeface);
			tv_no.setTypeface(typeface);

			setCont(context);
			setTitlestring(title);
			setContentstring(content);

		}

		public void setCont(Context cont) {
			this.cont = cont;
		}

		public void setTitlestring(String titlestring) {
			titletext.setText(titlestring);
			this.titlestring = titlestring;
		}

		public void setContentstring(String contentstring) {
			contenttext.setText(contentstring);
			this.contentstring = contentstring;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public void setThisweek(boolean thisweek) {
			this.thisweek = thisweek;
		}

		@Override
		public void onClick(View v) {

			// yes ��ư
			if (v.getId() == R.id.tv_dialog_yes) {
				thread = new Thread(socketThread = new SocketThread());

				// ������ �ð�ǥ
				if (thisweek) {
					SocketThread.socket_num = num + 1;
					SocketThread.num = 5; // SocketThread ������ ���� ����
					if (btn_next_timetable[num].isSelected()) {
						// ����� ���¿����� ���
						btn_next_timetable[num]
								.setBackgroundResource(R.drawable.next_timetable_off);
						btn_next_timetable[num].setSelected(false);
						btn_next_timetable[num]
								.setText((--next_timetable_count[num]) + "/10");
						next_timetable[num] = "f";
						SocketThread.socket_str = "f";
						nextCount--;

						thread.start();
					} else {
						// ������� ���� ���¿����� ����
						// 10ĭ �̸����� Ȯ���ϰ� ����
						scheduleNum = num;
						n_scheduleSync = new nextScheduleSync();
						n_scheduleSync
								.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
				}

				// �̹��� �ð�ǥ
				else {
					SocketThread.socket_num = num + 1;
					SocketThread.num = 4; // SocketThread �̹��� ���� ����

					if (btn_this_timetable[num].isSelected()) {
						// ����� ���¿����� ���
						btn_this_timetable[num]
								.setBackgroundResource(R.drawable.timetable_gray);
						btn_this_timetable[num].setSelected(false);
						this_timetable[num - 5] = "f";
						SocketThread.socket_str = "f";
						thisCount--;

						thread.start();
					} else {
						// ������� ���� ���¿����� ����
						scheduleNum = num;
						scheduleSync = new ScheduleSync();
						scheduleSync
								.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
				}

				this.dismiss();
			}

			// no ��ư
			else if (v.getId() == R.id.tv_dialog_no) {
				this.dismiss(); // �˾�â �ݱ�
			}
		}
	}

	public static class Dialog_button extends Dialog implements OnClickListener {
		LayoutInflater li;
		LinearLayout edit;
		TextView tv_yes;
		LinearLayout nobtn;
		TextView titletext;
		TextView contenttext;
		String titlestring;
		String contentstring;
		LinearLayout layout;
		Context cont;

		public Dialog_button(Context context, String title, String content) {
			super(context);
			li = getLayoutInflater();
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			View v = (View) li.inflate(R.layout.dialog, null);
			layout = (LinearLayout) v.findViewById(R.id.linearlayout_dialog);

			v.setLayoutParams(new LinearLayout.LayoutParams(
					MainActivity.dialog_width, MainActivity.dialog_height));
			layout.setLayoutParams(new LinearLayout.LayoutParams(
					MainActivity.dialog_width, MainActivity.dialog_height));

			edit = (LinearLayout) v.findViewById(R.id.layout_dialog_edit);
			edit.setVisibility(View.GONE);

			titletext = (TextView) v.findViewById(R.id.tv_dialog_title);
			contenttext = (TextView) v.findViewById(R.id.tv_dialog_content);
			tv_yes = (TextView) v.findViewById(R.id.tv_dialog_yes);
			tv_yes.setText("Ȯ��");
			tv_yes.setOnClickListener(this);
			nobtn = (LinearLayout) v.findViewById(R.id.layout_no_btn);
			nobtn.setVisibility(View.GONE);
			setContentView(v);

			Typeface typeface = Typeface.createFromAsset(getContext()
					.getAssets(), "happy_light.TTF");
			titletext.setTypeface(typeface);
			contenttext.setTypeface(typeface);

			typeface = Typeface.createFromAsset(getContext().getAssets(),
					"happy_bold.TTF");
			tv_yes.setTypeface(typeface);

			setCont(context);
			setTitlestring(title);
			setContentstring(content);
		}

		public void setCont(Context cont) {
			this.cont = cont;
		}

		public void setTitlestring(String titlestring) {
			titletext.setText(titlestring);
			this.titlestring = titlestring;
		}

		public void setContentstring(String contentstring) {
			contenttext.setText(contentstring);
			this.contentstring = contentstring;
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.tv_dialog_yes) {
				this.dismiss(); // �˾�â �ݱ�
			}
		}
	}

	public static class Dialog_edit extends Dialog implements OnClickListener {
		LayoutInflater li;
		LinearLayout edit;
		EditText editText;
		TextView tv_yes;
		TextView tv_no;
		TextView titletext;
		TextView contenttext;
		String titlestring;
		String contentstring;
		int num;
		LinearLayout layout;
		Context cont;

		public Dialog_edit(Context context, String title, String content,
				int num) {
			super(context);
			li = getLayoutInflater();
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			View v = (View) li.inflate(R.layout.dialog, null);
			layout = (LinearLayout) v.findViewById(R.id.linearlayout_dialog);

			v.setLayoutParams(new LinearLayout.LayoutParams(
					MainActivity.dialog_width, MainActivity.dialog_height));
			layout.setLayoutParams(new LinearLayout.LayoutParams(
					MainActivity.dialog_width, MainActivity.dialog_height));

			edit = (LinearLayout) v.findViewById(R.id.layout_dialog_edit);
			editText = (EditText) v.findViewById(R.id.edit_dialog);

			titletext = (TextView) v.findViewById(R.id.tv_dialog_title);
			contenttext = (TextView) v.findViewById(R.id.tv_dialog_content);
			tv_yes = (TextView) v.findViewById(R.id.tv_dialog_yes);
			tv_no = (TextView) v.findViewById(R.id.tv_dialog_no);
			tv_yes.setOnClickListener(this);
			tv_no.setOnClickListener(this);
			setContentView(v);

			Typeface typeface = Typeface.createFromAsset(getContext()
					.getAssets(), "happy_light.TTF");
			titletext.setTypeface(typeface);
			contenttext.setTypeface(typeface);

			typeface = Typeface.createFromAsset(getContext().getAssets(),
					"happy_bold.TTF");
			tv_yes.setTypeface(typeface);
			tv_no.setTypeface(typeface);

			setCont(context);
			setTitlestring(title);
			setContentstring(content);
		}

		public void setCont(Context cont) {
			this.cont = cont;
		}

		public void setTitlestring(String titlestring) {
			titletext.setText(titlestring);
			this.titlestring = titlestring;
		}

		public void setContentstring(String contentstring) {
			contenttext.setText(contentstring);
			this.contentstring = contentstring;
		}

		public void setNum(int num) {
			this.num = num;
		}

		@Override
		public void onClick(View v) {
			// yes ��ư
			if (v.getId() == R.id.tv_dialog_yes) {
				btn_this_timetable[num].setText(editText.getText());
				db_text[num] = editText.getText().toString();
				this.dismiss();
			}

			// no ��ư
			else if (v.getId() == R.id.tv_dialog_no) {
				this.dismiss(); // �˾�â �ݱ�
			}
		}
	}

	// ������ ���� ���� ���� Ȯ�� ��û
	class nextScheduleSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(MainActivity.context, "",
					"�ε���. ��ø� ��ٷ��ּ���...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 12; // SocketThread ������ ���� ���� ���� ����
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "������ ���� ���� ���� ������");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			// 10ĭ ���� ����� ���� ����
			if (isTrue.equals("t")) {
				btn_next_timetable[scheduleNum]
						.setBackgroundResource(R.drawable.next_timetable_on);
				btn_next_timetable[scheduleNum].setSelected(true);
				btn_next_timetable[scheduleNum]
						.setText((++next_timetable_count[scheduleNum]) + "/10");
				next_timetable[scheduleNum] = "t";
				SocketThread.socket_str = "t";
				nextCount++;

				thread = new Thread(socketThread = new SocketThread());
				SocketThread.num = 5; // SocketThread ������ ���� ����
				thread.start();
			} else {
				// �̹� ���� �� á��
				dialog.setContentstring("�ִ� ���� ������ ���� ��� �Ϸ�Ǿ����ϴ�.");
				dialog.show();
			}
		}
	}

	// �̹��� ���� ���� ���� Ȯ�� ��û
	class ScheduleSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(MainActivity.context, "",
					"�ε���. ��ø� ��ٷ��ּ���...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 11; // SocketThread �̹��� ���� ���� ���� ����
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "�̹��� ���� ���� ���� ������");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			// 14ĭ ���� ����� ���� ����
			if (isTrue.equals("t")) {
				btn_this_timetable[scheduleNum]
						.setBackgroundResource(R.drawable.timetable_orange);
				btn_this_timetable[scheduleNum].setSelected(true);
				this_timetable[scheduleNum - 5] = "t";
				SocketThread.socket_str = "t";
				thisCount++;

				thread = new Thread(socketThread = new SocketThread());
				SocketThread.num = 4; // SocketThread �̹��� ���� ����
				thread.start();
			} else {
				// �̹� ���� �� á��
				dialog.setContentstring("�ִ� ���� ������ ���� ��� �Ϸ�Ǿ����ϴ�.");
				dialog.show();
			}
		}
	}

	// DB ����
	private void DB_createOpenDatabase() {
		// DB�� �����ϸ� ����. �������� ������ ����
		database = getActivity().openOrCreateDatabase(db_name, 0, null);
	}

	// ���̺� ����
	private void DB_createTable() {
		// String sql = "create table " + db_table + "(db_num, db_text)";
		String sql = "create table " + db_table
				+ "(db_num integer primary key autoincrement, db_text String)";
		try {
			database.execSQL(sql);// slq�� ����
		} catch (Exception e) {
		}
	}

	// ������ ��ȸ
	private void DB_getData() {
		String sql = "select db_num, db_text from " + db_table;
		Cursor cursor = database.rawQuery(sql, null);

		cursor.moveToFirst();

		int i = 0;
		while (!cursor.isAfterLast() && i < 45) {
			db_num[i] = cursor.getInt(0);
			db_text[i] = cursor.getString(1);
			// ���� �ʿ��� �۾� ó��
			cursor.moveToNext();
		}
		cursor.close();
	}

	// �����ͳֱ�
	private void DB_insertData(int num, String text) {
		Log.e("tag", "DB_insertData");
		// sql���� �����ϴ� ���������� Ʈ��������� �����ְڴٶ�� �ǹ̷� Ʈ����� ������ ��Ÿ���� �޼ҵ�
		database.beginTransaction();

		try {
			String sql = "insert into " + db_table
					+ "(db_num, db_text) values('" + num + "', '" + text + "')";
			database.execSQL(sql);

			// Ʈ��������� ������ ���������� SQL������ ��� ���������� ������ ����
			database.setTransactionSuccessful();

		} catch (Exception e) {
		} finally {
			// Ʈ������� ������ �޼ҵ�.
			database.endTransaction();
		}
	}

	// ������ ����
	private void DB_deleteData() {
		String sql = "delete from " + db_table;
		try {
			database.execSQL(sql);// slq�� ����
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class DB_SQLite extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// SQLite DB���� �� ���������� ������ ������ ����
			DB_createOpenDatabase();
			DB_createTable();
			DB_getData();
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
}