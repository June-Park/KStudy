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

		// ��Ʈ ����
		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"happy_bold.TTF");

		// �׼ǹ� ����
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		// �α��� tab ����
		tabhost = (TabHost) findViewById(R.id.tabhost);
		tabhost.setup();

		TabSpec tab1 = tabhost.newTabSpec("tab1");
		tab1.setIndicator("�н����");
		tab1.setContent(R.id.tab1);
		tabhost.addTab(tab1);

		TabSpec tab2 = tabhost.newTabSpec("tab2");
		tab2.setIndicator("CIS");
		tab2.setContent(R.id.tab2);
		tabhost.addTab(tab2);

		tabhost.setCurrentTab(0);

		// ���̸�, ��й�ȣ �Է� EditText
		edit_id = (EditText) findViewById(R.id.edit_id);
		edit_pw = (EditText) findViewById(R.id.edit_pw);

		CIS_id = (Spinner) findViewById(R.id.spinner_CIS_id);
		CIS_pw = (EditText) findViewById(R.id.edit_CIS_pw);

		// spinner
		CIS_id.setPrompt("CIS ���̸� ����");
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, CIS_team_list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		CIS_id.setAdapter(adapter);

		// ��ư
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setTypeface(typeface);
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ����, ��й�ȣ ������ �� ���� �ް� �α���
				team_name = edit_id.getText().toString();
				team_pw = edit_pw.getText().toString();

				if (team_name == null || team_pw == null) {
					Toast.makeText(LoginActivity.this, "���̵� �Ǵ� ��й�ȣ�� Ȯ�����ּ���.",
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
				// ���͵� ���� Activity�� �̵�
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
				// ����, ��й�ȣ ������ �� ���� �ް� �α���
				team_name = CIS_id.getSelectedItem().toString();
				team_pw = CIS_pw.getText().toString();

				if (team_name == null || team_pw == null) {
					Toast.makeText(LoginActivity.this, "���̵� �Ǵ� ��й�ȣ�� Ȯ�����ּ���.",
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
				// CIS ��û�ϱ� Activity�� �̵�
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
					"�ε���. ��ø� ��ٷ��ּ���...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 2; // SocketThread �α��� ��й�ȣ Ȯ�� ����
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "�α��� ��й�ȣ Ȯ�� thread ���� ��");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// �α��� ������ MainActivity�� ���� ����, Login ���� ���� �޾ƿ�
			if (MainActivity.login_state) {
				MainActivity.team_name = team_name;
				sync = new LoginSync();
				sync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				// �α��� ���� �˾�
				progressDialog.dismiss();
				Toast.makeText(LoginActivity.this, "���̵� �Ǵ� ��й�ȣ�� Ȯ�����ּ���.",
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
			SocketThread.num = 3; // SocketThread �α��� �Ϸ� ���� ����
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "�α��� �Ϸ� thread 6 ���� ��");
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

	// ���ǳ� adapter - ��Ʈ typeface ���� ����
	public class SpinnerAdapter extends ArrayAdapter<String> {
		Context context;
		String[] items = new String[] {};

		public SpinnerAdapter(final Context context,
				final int textViewResourceId, final String[] objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
			this.context = context;
		}

		// ���ǳ� Ŭ���� �������� View�� ����
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

		// �⺻ ���ǳ� View ����
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