package com.example.kstudyactionbar;

import java.util.Calendar;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	/*
	 * SQLite 관련 변수
	 */
	SQLiteDatabase database;
	String db_name = "cs3.db";
	String db_table = "help";
	DB_SQLite sqLite;

	public static Activity mActivity;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	static Context context;

	ActionBar actionBar;

	SocketThread socketThread;
	Thread thread;

	LoginBtnSync btnSync;
	ResetSync resetSync;

	ProgressDialog progressDialog;

	// drawer 메뉴
	DrawerLayout drawer;
	LinearLayout drawer_linear;

	Button btn_help; // 도움말
	Button btn_member; // 멤버 보기
	Button btn_notice; // 문의하기

	static String team_name = "null"; // 내 팀명
	static String team_type = "0"; // 스터디 종류

	static String help_state = "1"; // 메인 도움말 다시 보지 않기 체크 상태
	static String help_main_state; // 메인 도움말 다시 보지 않기 체크 상태
	static Boolean login_state = false; // 로그인 상태

	// 화면 비율 크기
	DisplayMetrics metrics;
	static int dialog_width;
	static int dialog_height;

	Dialog_button dialog; // 경고 팝업

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_linear);

		// 안드로이드 내장 DB SQLite
		sqLite = new DB_SQLite();
		sqLite.execute();

		mActivity = MainActivity.this;
		context = this;

		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		dialog_width = metrics.widthPixels * 5 / 6;
		dialog_height = metrics.heightPixels / 3;

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// actionbar title 폰트 변경
		Typeface typeface = Typeface
				.createFromAsset(getAssets(), "BAUHS93.TTF");

		try {
			Integer titleId = Resources.getSystem().getIdentifier(
					"action_bar_title", "id", "android");
			TextView title = (TextView) getWindow().findViewById(titleId);
			title.setTypeface(typeface);
		} catch (Exception e) {
			Log.e("ccc", "Failed to obtain action bar title reference");
		}

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		// drawer 메뉴
		drawer = (DrawerLayout) findViewById(R.id.layout_main_drawer);

		// drawer view
		drawer_linear = (LinearLayout) findViewById(R.id.drawer_linear);
		drawer_linear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (drawer.isDrawerOpen(drawer_linear))
					drawer.closeDrawer(drawer_linear);
			}
		});

		// 도움말 버튼
		btn_help = (Button) findViewById(R.id.btn_help_linear);
		btn_help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						HelpActivity.class);
				startActivity(intent);
			}
		});

		// member 보기 버튼
		btn_member = (Button) findViewById(R.id.btn_member_linear);
		btn_member.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// 로그인 상태일 때만 확인 가능
				if (login_state) {
					Intent intent = new Intent(MainActivity.this,
							MemberActivity.class);
					startActivity(intent);
				} else {
					// 경고 팝업
					dialog = new Dialog_button(context, "경고",
							"로그인 후 이용 가능 합니다.");
					dialog.show();
				}
			}
		});

		// 공지사항 or 문의하기 버튼
		btn_notice = (Button) findViewById(R.id.btn_question_linear);
		btn_notice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						NoticeActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem itemLogin = menu.findItem(R.id.action_login);
		if (login_state)
			itemLogin.setIcon(R.drawable.logout);
		else
			itemLogin.setIcon(R.drawable.login);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_login:
			if (login_state) {
				// 로그아웃
				login_state = false;

				resetSync = new ResetSync();
				resetSync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				// 로그인 화면으로 전환
				btnSync = new LoginBtnSync();
				btnSync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
			break;
		case R.id.action_reset:
			// 메인 reset
			resetSync = new ResetSync();
			resetSync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			break;
		default:
			break;
		}
		return true;
	}

	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			// Menu Key 클릭시 메뉴 drawer 열리고 닫힘
			case KeyEvent.KEYCODE_MENU:
				if (!drawer.isDrawerOpen(drawer_linear))
					drawer.openDrawer(drawer_linear);
				else
					drawer.closeDrawer(drawer_linear);
				return true;
			}
		}
		return super.onKeyDown(KeyCode, event);
	}

	/* Back key 두번눌러 종료 코드 시작 */
	private static final int MSG_TIMER_EXPIRED = 1;
	private static final int BACKEY_TIMEOUT = 2000;
	private boolean mIsBackKeyPressed = false;
	private long mCurrentTimeInMillis = 0;

	@Override
	public void onBackPressed() {
		if (mIsBackKeyPressed == false) {
			mIsBackKeyPressed = true;
			mCurrentTimeInMillis = Calendar.getInstance().getTimeInMillis();
			Toast.makeText(this, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.",
					Toast.LENGTH_SHORT).show();
			mTimerHander.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED,
					BACKEY_TIMEOUT);
		} else {
			mIsBackKeyPressed = false;
			if (Calendar.getInstance().getTimeInMillis() <= (mCurrentTimeInMillis + (BACKEY_TIMEOUT))) {
				// // 안드로이드 내장 DB SQLite
				// help_sqLite = new help_DB_SQLite();
				// help_sqLite.execute();
				finish();
			}
		}
	}

	private Handler mTimerHander = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIMER_EXPIRED:
				mIsBackKeyPressed = false;
				break;
			}
		}
	}; /* Back key 두번눌러 종료 코드 끝 */

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		if (drawer != null)
			if (drawer.isDrawerOpen(drawer_linear))
				drawer.closeDrawer(drawer_linear);

		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;

			if (position % 3 == 0)
				fragment = new TimetableFragment();
			else if (position % 3 == 1)
				fragment = new SeatingChartFragment();
			else if (position % 3 == 2)
				fragment = new BarChartFragment();

			return fragment; // PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public static class PlaceholderFragment extends Fragment {
		private static final String ARG_SECTION_NUMBER = "section_number";

		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
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
		String sql = "create table " + db_table
				+ "(db_num integer primary key autoincrement, db_help String)";
		try {
			database.execSQL(sql);// slq문 실행
		} catch (Exception e) {
		}

		sql = "insert into " + db_table + "(db_num, db_help) values(1,'T')";
		try {
			database.execSQL(sql);// slq문 실행
		} catch (Exception e) {
		}
	}

	// 데이터 조회
	private String DB_getData() {
		// String sql = "select db_help from " + db_table + " where db_num = 0";
		String sql = "select db_help from " + db_table;
		Cursor cursor = database.rawQuery(sql, null);
		cursor.moveToFirst();
		help_state = cursor.getString(0);
		cursor.close();
		return help_state;
	}

	// 데이터넣기
	private void DB_insertData(int num, String text) {
		Log.e("tag", "DB_insertData");
		// sql문을 실행하는 일정구간을 트랜잭션으로 묶어주겠다라는 의미로 트랜잭션 시작을 나타내는 메소드
		database.beginTransaction();

		try {
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

		help_main_state = text;
	}

	class DB_SQLite extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// SQLite DB연결 후 버전정보를 가져와 서버에 전송
			DB_createOpenDatabase();
			DB_createTable();
			if (help_main_state == null)
				DB_insertData(1, "T");
			// help_state = DB_getData();
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (help_main_state.equals("T")) {
				Intent intent = new Intent(MainActivity.this,
						MainHelpActivity.class);
				startActivity(intent);
				help_main_state = "F";
			}
		}
	}

	class LoginBtnSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(context, "",
					"로딩중. 잠시만 기다려주세요...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 8; // SocketThread CIS 팀목록 연결
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "CIS 팀목록 thread 8 실행 중");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
			progressDialog.dismiss();
		}
	}

	class ResetSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(context, "",
					"로딩중. 잠시만 기다려주세요...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 9; // SocketThread 실시간 확인 연결
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "실시간 확인 thread 9 실행 중");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mActivity.recreate();
			progressDialog.dismiss();
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
}