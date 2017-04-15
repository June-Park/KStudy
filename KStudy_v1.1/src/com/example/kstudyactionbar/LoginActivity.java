package com.example.kstudyactionbar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class LoginActivity extends Activity {

	public static Activity loginActivity;
	Context context;

	SocketThread socketThread;
	Thread thread;

	LoginCheckSync sync_check;
	LoginSync sync;

	ProgressDialog progressDialog;

	TabHost tabhost;

	EditText edit_id;
	EditText edit_pw;
	Spinner CIS_id;
	EditText CIS_pw;

	Button btn_login;
	Button btn_join;
	Button btn_CIS_login;
	Button btn_CIS_join;

	static String CIS_team_list[];
	ArrayAdapter<String> adapter;

	static String team_name;
	static String team_pw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginActivity = LoginActivity.this;
		context = this;

		// 폰트 설정
		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"happy_bold.TTF");

		// 액션바 숨김
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		// 로그인 tab 설정
		tabhost = (TabHost) findViewById(R.id.tabhost);
		tabhost.setup();

		TabSpec tab1 = tabhost.newTabSpec("tab1");
		tab1.setIndicator("학습향상");
		tab1.setContent(R.id.tab1);
		tabhost.addTab(tab1);

		TabSpec tab2 = tabhost.newTabSpec("tab2");
		tab2.setIndicator("CIS");
		tab2.setContent(R.id.tab2);
		tabhost.addTab(tab2);

		tabhost.setCurrentTab(0);

		// 팀이름, 비밀번호 입력 EditText
		edit_id = (EditText) findViewById(R.id.edit_id);
		edit_pw = (EditText) findViewById(R.id.edit_pw);

		CIS_id = (Spinner) findViewById(R.id.spinner_CIS_id);
		CIS_pw = (EditText) findViewById(R.id.edit_CIS_pw);

		// spinner
		CIS_id.setPrompt("CIS 팀이름 선택");
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, CIS_team_list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		CIS_id.setAdapter(adapter);

		// 버튼
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setTypeface(typeface);
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 팀명, 비밀번호 같으면 팀 정보 받고 로그인
				team_name = edit_id.getText().toString();
				team_pw = edit_pw.getText().toString();

				if (team_name == null || team_pw == null) {
					Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 확인해주세요.",
							Toast.LENGTH_SHORT).show();
				} else {
					sync_check = new LoginCheckSync();
					sync_check
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
			}
		});

		btn_join = (Button) findViewById(R.id.btn_join);
		btn_join.setTypeface(typeface);
		btn_join.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 스터디 선택 Activity로 이동
				Intent intent = new Intent(LoginActivity.this,
						JoinSelectActivity.class);
				startActivity(intent);
			}
		});

		btn_CIS_login = (Button) findViewById(R.id.btn_login2);
		btn_CIS_login.setTypeface(typeface);
		btn_CIS_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 팀명, 비밀번호 같으면 팀 정보 받고 로그인
				team_name = CIS_id.getSelectedItem().toString();
				team_pw = CIS_pw.getText().toString();

				if (team_name == null || team_pw == null) {
					Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 확인해주세요.",
							Toast.LENGTH_SHORT).show();
				} else {
					sync_check = new LoginCheckSync();
					sync_check
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
			}
		});

		btn_CIS_join = (Button) findViewById(R.id.btn_join2);
		btn_CIS_join.setTypeface(typeface);
		btn_CIS_join.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// CIS 신청하기 Activity로 이동
				Intent intent = new Intent(LoginActivity.this,
						CISActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	class LoginCheckSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(context, "",
					"로딩중. 잠시만 기다려주세요...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 2; // SocketThread 로그인 비밀번호 확인 연결
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "로그인 비밀번호 확인 thread 실행 중");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// 로그인 성공시 MainActivity에 팀명 저장, Login 이후 정보 받아옴
			if (MainActivity.login_state) {
				MainActivity.team_name = team_name;
				sync = new LoginSync();
				sync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				// 로그인 실패 팝업
				progressDialog.dismiss();
				Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 확인해주세요.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class LoginSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 3; // SocketThread 로그인 완료 이후 연결
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "로그인 완료 thread 6 실행 중");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			MainActivity.mActivity.finish();
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	// 스피너 adapter - 폰트 typeface 변경 설정
	public class SpinnerAdapter extends ArrayAdapter<String> {
		Context context;
		String[] items = new String[] {};

		public SpinnerAdapter(final Context context,
				final int textViewResourceId, final String[] objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
			this.context = context;
		}

		// 스피너 클릭시 보여지는 View의 정의
		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(
						android.R.layout.simple_spinner_dropdown_item, parent,
						false);
			}

			TextView tv = (TextView) convertView
					.findViewById(android.R.id.text1);
			tv.setText(items[position]);
			// tv.setTypeface(typeface);
			return convertView;
		}

		// 기본 스피너 View 정의
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(
						android.R.layout.simple_spinner_item, parent, false);
			}

			TextView tv = (TextView) convertView
					.findViewById(android.R.id.text1);
			tv.setText(items[position]);
			// tv.setTypeface(typeface);

			return convertView;
		}
	}
}