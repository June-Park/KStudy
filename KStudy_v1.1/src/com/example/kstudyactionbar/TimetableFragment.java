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
	 * SQLite 관련 변수
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

	static Button btn_next_timetable[]; // 다음주 예약 시간표
	static Button btn_this_timetable[]; // 이번주 시간표

	// 내 예약에 해당 t/f
	static String next_timetable[] = new String[35];
	static String this_timetable[] = new String[35];
	// 다음주 시간표 총 예약 현황 수 (n/10)
	static int next_timetable_count[] = new int[35];
	// 예약 현황 가능 여부
	static String isTrue = "t";

	// 내 예약 시간 수
	static int nextCount = 0; // 다음주 예약 시간 수
	static int thisCount = 0; // 이번주 예약 시간 수
	int ableCount = 0; // 예약 가능 시간

	Button btn_change; // 시간표 변경 버튼
	Button btn_ok; // 시간표 변경 버튼 완료

	static Dialog_button dialog; // 경고 팝업
	Dialog_two_button two_dialog; // 예약 팝업
	Dialog_edit edit_dialog; // 시간표 편집 팝업

	@Override
	// 초기화
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	// 초기화 완료후 실행
	public void onStart() {
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.timetable_fragment, container,
				false);

		// 안드로이드 내장 DB SQLite
		sqLite = new DB_SQLite();
		sqLite.execute();

		thisCount = 0;
		nextCount = 0;

		// 로그인 상태일 때
		if (MainActivity.login_state) {
			// 내 예약된 시간 수 체크
			for (int i = 0; i < 35; i++) {
				if (this_timetable[i].equals("t"))
					thisCount++;
				if (next_timetable[i].equals("t"))
					nextCount++;
			}

			// 스터디 종류별로 예약 가능 시간
			if (MainActivity.team_type.equals("1")
					|| MainActivity.team_type.equals("4"))
				ableCount = 1;
			else if (MainActivity.team_type.equals("2"))
				ableCount = 3;
			else
				ableCount = 5;
		}

		// 예약 & 취소 팝업
		two_dialog = new Dialog_two_button(getActivity(), "확인", "예약하시겠습니까?",
				true, 0);
		// 경고 팝업
		dialog = new Dialog_button(getActivity(), "경고", "최대 예약 가능 시간은 "
				+ ableCount + "시간 입니다.");
		// 시간표 변경 팝업
		edit_dialog = new Dialog_edit(getActivity(), "시간표 변경",
				"변경할 시간표를 입력하세요.", 0);

		// 다음주 시간표 버튼
		int btn_id = R.id.btn_timetable1;
		btn_next_timetable = new Button[35];
		for (int i = 0; i < btn_next_timetable.length; i++) {
			btn_next_timetable[i] = (Button) view.findViewById(btn_id + i);
			btn_next_timetable[i].setTag(i); // 버튼 번호 (ID)
			btn_next_timetable[i].setText(next_timetable_count[i] + "/10");
			btn_next_timetable[i].setSelected(false);
		}
		if (MainActivity.login_state) {
			// 로그인 상태일 때 다음주 예약 정보 가져옴
			for (int i = 0; i < next_timetable.length; i++)
				// 예약된 상태일 때 isSelected는 true
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
					// 로그인 상태일 때만 예약/취소 가능
					if (MainActivity.login_state) {
						// 예약되지 않은 부분이면 예약, 예약된 부분이면 취소
						if (v.isSelected()) {
							two_dialog.setContentstring("예약을 취소하시겠습니까?");
							two_dialog.setNum((Integer) v.getTag());
							two_dialog.setThisweek(true);
							two_dialog.show();
						} else {
							// 현재 예약 시간이 예약 가능 시간보다 작을 경우 예약 안내 팝업
							if (nextCount < ableCount) {
								two_dialog.setContentstring("예약하시겠습니까?");
								two_dialog.setNum((Integer) v.getTag());
								two_dialog.setThisweek(true);
								two_dialog.show();
							} else {
								// 신청할 수 있는 수 초과
								dialog.setContentstring("최대 예약 가능 시간은 "
										+ ableCount + "시간 입니다.");
								dialog.show();
							}
						}
					} else {
						// 로그인 후 이용해 주세요 팝업
						dialog.setContentstring("로그인 후 이용 가능합니다.");
						dialog.show();
					}
				}
			});
		}

		Log.e("tag", "this timetable start");
		// 이번주 시간표 버튼
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
			// 로그인 상태일 때 이번주 예약 정보 가져옴
			Log.e("ccc", "this_timetable");
			for (int i = 0; i < this_timetable.length; i++)
				// 예약된 상태일 때 isSelected는 true
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
					// 시간표 변경 활성화 상태
					if (btn_change.isSelected()) {
						edit_dialog.setNum((Integer) v.getTag());
						edit_dialog.editText.setText(null);
						edit_dialog.show();
					}
					// 시간표 변경 비활성화 상태
					else {
						// 로그인 상태일 때만 예약/취소 가능
						if (MainActivity.login_state) {
							int intTag = (Integer) v.getTag();
							if (intTag >= 5 && intTag <= 40) {
								// 예약되지 않은 부분이면 예약, 예약된 부분이면 취소
								if (v.isSelected()) {
									two_dialog
											.setContentstring("예약을 취소하시겠습니까?");
									two_dialog.setNum(intTag);
									two_dialog.setThisweek(false);
									two_dialog.show();
								} else {
									if (thisCount < ableCount) {
										two_dialog
												.setContentstring("예약하시겠습니까?");
										two_dialog.setNum(intTag);
										two_dialog.setThisweek(false);
										two_dialog.show();
									} else {
										// 신청할 수 있는 수 초과
										dialog.setContentstring("최대 예약 가능 시간은 "
												+ ableCount + "시간 입니다.");
										dialog.show();
									}
								}
							} else {
								// 예약 불가능 시간!!
								dialog.setContentstring("예약 불가능한 시간입니다.");
								dialog.show();
							}
						} else {
							// 로그인 후 이용해 주세요 팝업
							dialog.setContentstring("로그인 후 이용 가능합니다.");
							dialog.show();
						}
					}
				}
			});
		}

		// 이번주 내 예약 확인 & 시간표
		// 내 시간표 변경
		btn_change = (Button) view.findViewById(R.id.btn_timetable_change);
		btn_ok = (Button) view.findViewById(R.id.btn_timetable_finish);
		btn_change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 시간표 변경 버튼 클릭
				// btn_chagne 숨김, isSelected는 false
				// btn_ok 보여줌, isSelected는 true
				btn_change.setVisibility(View.GONE);
				btn_change.setSelected(true);
				btn_ok.setVisibility(View.VISIBLE);
				btn_ok.setSelected(false);
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 시간표 변경 완료 버튼 클릭
				// btn_change 보여줌, isSelected는 true
				// btn_ok 숨김, isSelected는 false
				btn_change.setVisibility(View.VISIBLE);
				btn_change.setSelected(false);
				btn_ok.setVisibility(View.GONE);
				btn_ok.setSelected(true);

				// SQLite timetable DB 수정
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

			// yes 버튼
			if (v.getId() == R.id.tv_dialog_yes) {
				thread = new Thread(socketThread = new SocketThread());

				// 다음주 시간표
				if (thisweek) {
					SocketThread.socket_num = num + 1;
					SocketThread.num = 5; // SocketThread 다음주 예약 연결
					if (btn_next_timetable[num].isSelected()) {
						// 예약된 상태였으면 취소
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
						// 예약되지 않은 상태였으면 예약
						// 10칸 미만인지 확인하고 예약
						scheduleNum = num;
						n_scheduleSync = new nextScheduleSync();
						n_scheduleSync
								.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
				}

				// 이번주 시간표
				else {
					SocketThread.socket_num = num + 1;
					SocketThread.num = 4; // SocketThread 이번주 예약 연결

					if (btn_this_timetable[num].isSelected()) {
						// 예약된 상태였으면 취소
						btn_this_timetable[num]
								.setBackgroundResource(R.drawable.timetable_gray);
						btn_this_timetable[num].setSelected(false);
						this_timetable[num - 5] = "f";
						SocketThread.socket_str = "f";
						thisCount--;

						thread.start();
					} else {
						// 예약되지 않은 상태였으면 예약
						scheduleNum = num;
						scheduleSync = new ScheduleSync();
						scheduleSync
								.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
				}

				this.dismiss();
			}

			// no 버튼
			else if (v.getId() == R.id.tv_dialog_no) {
				this.dismiss(); // 팝업창 닫기
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
			tv_yes.setText("확인");
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
				this.dismiss(); // 팝업창 닫기
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
			// yes 버튼
			if (v.getId() == R.id.tv_dialog_yes) {
				btn_this_timetable[num].setText(editText.getText());
				db_text[num] = editText.getText().toString();
				this.dismiss();
			}

			// no 버튼
			else if (v.getId() == R.id.tv_dialog_no) {
				this.dismiss(); // 팝업창 닫기
			}
		}
	}

	// 다음주 예약 가능 여부 확인 신청
	class nextScheduleSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(MainActivity.context, "",
					"로딩중. 잠시만 기다려주세요...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 12; // SocketThread 다음주 예약 가능 여부 연결
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "다음주 예약 가능 여부 실행중");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			// 10칸 이하 예약시 예약 가능
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
				SocketThread.num = 5; // SocketThread 다음주 예약 연결
				thread.start();
			} else {
				// 이미 예약 꽉 찼다
				dialog.setContentstring("최대 예약 가능한 방이 모두 완료되었습니다.");
				dialog.show();
			}
		}
	}

	// 이번주 예약 가능 여부 확인 신청
	class ScheduleSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(MainActivity.context, "",
					"로딩중. 잠시만 기다려주세요...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 11; // SocketThread 이번주 예약 가능 여부 연결
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "이번주 예약 가능 여부 실행중");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			// 14칸 이하 예약시 예약 가능
			if (isTrue.equals("t")) {
				btn_this_timetable[scheduleNum]
						.setBackgroundResource(R.drawable.timetable_orange);
				btn_this_timetable[scheduleNum].setSelected(true);
				this_timetable[scheduleNum - 5] = "t";
				SocketThread.socket_str = "t";
				thisCount++;

				thread = new Thread(socketThread = new SocketThread());
				SocketThread.num = 4; // SocketThread 이번주 예약 연결
				thread.start();
			} else {
				// 이미 예약 꽉 찼다
				dialog.setContentstring("최대 예약 가능한 방이 모두 완료되었습니다.");
				dialog.show();
			}
		}
	}

	// DB 생성
	private void DB_createOpenDatabase() {
		// DB가 존재하면 오픈. 존재하지 않으면 생성
		database = getActivity().openOrCreateDatabase(db_name, 0, null);
	}

	// 테이블 생성
	private void DB_createTable() {
		// String sql = "create table " + db_table + "(db_num, db_text)";
		String sql = "create table " + db_table
				+ "(db_num integer primary key autoincrement, db_text String)";
		try {
			database.execSQL(sql);// slq문 실행
		} catch (Exception e) {
		}
	}

	// 데이터 조회
	private void DB_getData() {
		String sql = "select db_num, db_text from " + db_table;
		Cursor cursor = database.rawQuery(sql, null);

		cursor.moveToFirst();

		int i = 0;
		while (!cursor.isAfterLast() && i < 45) {
			db_num[i] = cursor.getInt(0);
			db_text[i] = cursor.getString(1);
			// 실제 필요한 작업 처리
			cursor.moveToNext();
		}
		cursor.close();
	}

	// 데이터넣기
	private void DB_insertData(int num, String text) {
		Log.e("tag", "DB_insertData");
		// sql문을 실행하는 일정구간을 트랜잭션으로 묶어주겠다라는 의미로 트랜잭션 시작을 나타내는 메소드
		database.beginTransaction();

		try {
			String sql = "insert into " + db_table
					+ "(db_num, db_text) values('" + num + "', '" + text + "')";
			database.execSQL(sql);

			// 트랜잭션으로 묶어준 일정영역의 SQL문들이 모두 성공적으로 끝났을 지정
			database.setTransactionSuccessful();

		} catch (Exception e) {
		} finally {
			// 트랜잭션을 끝내는 메소드.
			database.endTransaction();
		}
	}

	// 데이터 삭제
	private void DB_deleteData() {
		String sql = "delete from " + db_table;
		try {
			database.execSQL(sql);// slq문 실행
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class DB_SQLite extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// SQLite DB연결 후 버전정보를 가져와 서버에 전송
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