package com.example.kstudyactionbar;

import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class JoinActivity extends ActionBarActivity {

	static Activity join_activity;
	SocketThread socketThread;
	Thread thread;

	TeamnameCheckSync checkSync;
	JoinSync sync;

	ProgressDialog progressDialog;

	EditText edit_teamname; // 팀명
	EditText edit_pw; // 팀 비밀번호
	Button btn_name_check; // 팀명 확인 버튼
	String studytype; // 스터디 종류

	TextView tv_subject; // 과목명
	ArrayAdapter<CharSequence> subject_tv_adapter; // subject list

	ImageView join_plus; // 인원 추가 버튼

	Spinner spinner_OT; // OT 참석 인원 spinner
	ArrayAdapter<CharSequence> spinner_adapter; // spinner OT list
	Spinner spinner_subject; // 과목코드 spinner
	ArrayAdapter<CharSequence> subject_spinner_adapter; // spinner subject list
	ArrayAdapter<CharSequence> major_spinner_adapter; // spinner major list

	CheckBox checkBox; // 동의 여부 체크박스

	static boolean name_check = false; // 팀명 확인 여부

	Button btn_back; // 취소 버튼
	Button btn_join; // 신청하기 버튼

	LinearLayout layout_subjectcode;

	// ////
	LayoutInflater inflater;
	static Vector<View> in_view;
	Vector<String[]> join_member; // 팀 멤버 vector - 학번, 전공, 이름, 핸드폰 번호, 메일
	int vector_size = 0;

	// inview 항목
	EditText edit_sid;
	Spinner spinner_major;
	EditText edit_name;
	EditText edit_phone;
	EditText edit_mail;

	ViewGroup root; // layout root view
	LinearLayout rootLinear; // join_inview가 붙을 LinearLayout

	Context context;

	Dialog_two_button dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		join_activity = JoinActivity.this;

		context = this;
		studytype = getIntent().getStringExtra("studytype");

		// 폰트 설정
		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"happy_light.TTF");

		try {
			Integer titleId = Resources.getSystem().getIdentifier(
					"action_bar_title", "id", "android");
			TextView title = (TextView) getWindow().findViewById(titleId);
			title.setTypeface(typeface);
		} catch (Exception e) {
		}

		// root View
		inflater = getLayoutInflater();
		root = (ViewGroup) inflater.inflate(R.layout.activity_join, null);
		rootLinear = (LinearLayout) root.findViewById(R.id.layout_join_inview);

		// 1스터디 이외에는 과목코드 숨김
		if (!studytype.equals("1")) {
			layout_subjectcode = (LinearLayout) root
					.findViewById(R.id.layout_join_subjectcode);
			layout_subjectcode.setVisibility(View.GONE);

		}

		// 팀명, 비밀번호
		edit_teamname = (EditText) root.findViewById(R.id.edit_join_teamname);
		edit_pw = (EditText) root.findViewById(R.id.edit_join_teampw);

		// 사용가능한 팀명 확인
		btn_name_check = (Button) root
				.findViewById(R.id.btn_join_teamname_check);
		btn_name_check.setTypeface(typeface);
		btn_name_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edit_teamname.getText().toString().equals("")) {
					Toast.makeText(JoinActivity.this, "사용할 팀명을 입력해주세요.",
							Toast.LENGTH_SHORT).show();
				} else {
					SocketThread.socket_str = edit_teamname.getText()
							.toString();
					checkSync = new TeamnameCheckSync();
					checkSync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
			}
		});

		if (studytype.equals("1")) {
			// 과목명 셋팅
			tv_subject = (TextView) root.findViewById(R.id.tv_join_subject);
			subject_tv_adapter = ArrayAdapter.createFromResource(context,
					R.array.subjectname, 0);

			// 과목 코드 선택 spinner
			spinner_subject = (Spinner) root
					.findViewById(R.id.spinner_join_subjectcode);
			spinner_subject.setPrompt("과목 코드");

			subject_spinner_adapter = ArrayAdapter.createFromResource(context,
					R.array.subjectcode, android.R.layout.simple_spinner_item);
			subject_spinner_adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_subject.setAdapter(subject_spinner_adapter);

			spinner_subject
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View arg1, int position, long arg3) {
							// 과목코드 spinner 선택시 과목명 textview 변경
							tv_subject.setText(subject_tv_adapter
									.getItem(spinner_subject
											.getSelectedItemPosition()));
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
		}

		// child View
		in_view = new Vector<View>();

		// 전공 선택 spinner
		major_spinner_adapter = ArrayAdapter.createFromResource(context,
				R.array.major, android.R.layout.simple_spinner_item);
		major_spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 멤버 추가
		join_plus = (ImageView) root.findViewById(R.id.iv_join_plus);
		join_plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (vector_size < 6) {
					in_view.add((View) inflater.inflate(R.layout.join_inview,
							null));
					in_view.get(vector_size).setLayoutParams(
							new LinearLayout.LayoutParams(
									ViewGroup.LayoutParams.MATCH_PARENT,
									ViewGroup.LayoutParams.MATCH_PARENT));

					in_view.get(vector_size).setTag(vector_size);

					edit_sid = (EditText) in_view.get(vector_size)
							.findViewById(R.id.edit_sid);
					spinner_major = (Spinner) in_view.get(vector_size)
							.findViewById(R.id.spinner_major);
					spinner_major.setAdapter(major_spinner_adapter);

					edit_name = (EditText) in_view.get(vector_size)
							.findViewById(R.id.edit_name);
					edit_phone = (EditText) in_view.get(vector_size)
							.findViewById(R.id.edit_phone);
					edit_mail = (EditText) in_view.get(vector_size)
							.findViewById(R.id.edit_mail);

					rootLinear.addView(in_view.get(vector_size++));
				} else
					Toast.makeText(JoinActivity.this, "스터디는 최대 6명까지 신청 가능합니다.",
							Toast.LENGTH_SHORT).show();
			}
		});

		// 기부 동의 여부 checkbox
		checkBox = (CheckBox) root.findViewById(R.id.checkBox_join);

		// OT 참석 인원 선택 spinner
		spinner_OT = (Spinner) root.findViewById(R.id.spinner_join_num);
		spinner_OT.setPrompt("OT 참석 인원");

		spinner_adapter = ArrayAdapter.createFromResource(context,
				R.array.OT_number, android.R.layout.simple_spinner_item);
		spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_OT.setAdapter(spinner_adapter);

		// 취소 버튼
		btn_back = (Button) root.findViewById(R.id.btn_join_back);
		btn_back.setTypeface(typeface);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// 신청하기 버튼
		btn_join = (Button) root.findViewById(R.id.btn_join_finish);
		btn_join.setTypeface(typeface);
		btn_join.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Boolean join = true; // 빈칸 확인 변수

				// 팀명 확인 완료시
				if (name_check) {
					// 멤버 벡터
					join_member = new Vector<String[]>();

					// 팀명, pw, 스터디 종류, 과목명, 기부여부, OT 참석 인원
					SocketThread.socket_str_arr[0] = edit_teamname.getText()
							.toString();
					SocketThread.socket_str_arr[1] = edit_pw.getText()
							.toString();
					if (edit_pw.getText().toString().equals(""))
						join = false;
					SocketThread.socket_str_arr[2] = studytype;
					if (studytype.equals("1"))
						SocketThread.socket_str_arr[3] = tv_subject.getText()
								.toString();
					else
						SocketThread.socket_str_arr[3] = "null";
					if (checkBox.isChecked())
						SocketThread.socket_str_arr[4] = "t";
					else
						SocketThread.socket_str_arr[4] = "f";
					SocketThread.socket_str_arr[5] = spinner_OT
							.getSelectedItem().toString();

					String str = "";
					// 팀 인원 수 만큼 멤버 벡터에 저장
					for (int i = 0; i < vector_size; i++) {
						join_member
								.add(new String[] { "", "", "", "", "", "0" });
						join_member.get(i)[0] = ((EditText) rootLinear
								.getChildAt(i).findViewById(R.id.edit_sid))
								.getText().toString();
						join_member.get(i)[1] = ((Spinner) rootLinear
								.getChildAt(i).findViewById(R.id.spinner_major))
								.getSelectedItem().toString();
						join_member.get(i)[2] = ((EditText) rootLinear
								.getChildAt(i).findViewById(R.id.edit_name))
								.getText().toString();
						join_member.get(i)[3] = ((EditText) rootLinear
								.getChildAt(i).findViewById(R.id.edit_phone))
								.getText().toString();
						join_member.get(i)[4] = ((EditText) rootLinear
								.getChildAt(i).findViewById(R.id.edit_mail))
								.getText().toString();

						for (int a = 0; a < 6; a++) {
							if (join_member.get(i)[a].toString().equals(""))
								join = false;
							str += join_member.get(i)[a] + "//";
						}
						str += "////";
					}
					Log.e("ccc", "팀 멤버 전송 내역 : " + str);
					SocketThread.socket_str_arr[6] = str;

					// 모든 칸이 입력되면 신청 안내 팝업
					if (join) {
						// 신청인원은 최소 1명 이상
						if (vector_size < 2)
							Toast.makeText(JoinActivity.this,
									"스터디는 최소 2인 이상 신청 가능합니다.",
									Toast.LENGTH_SHORT).show();
						else {
							dialog = new Dialog_two_button(context, "안내",
									"신청하시겠습니까?");
							dialog.show();
						}
					} else
						Toast.makeText(JoinActivity.this, "빈칸을 모두 입력해주세요.",
								Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(JoinActivity.this, "팀명을 확인해주세요.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		setContentView(root);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_join_close:
			finish();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.join, menu);
		return true;
	}

	class TeamnameCheckSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 10; // SocketThread 팀명 확인 연결
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "팀명 확인 thread 10 실행 중");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// 사용가능한 팀명이면 확인 완료, 불가능하면 이미 사용 중 Toast
			if (name_check) {
				Toast.makeText(JoinActivity.this, "사용 가능한 팀명입니다.",
						Toast.LENGTH_SHORT).show();
				btn_name_check.setText("확인 완료");
			} else {
				Toast.makeText(JoinActivity.this, "이미 사용중인 팀명입니다.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class JoinSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(context, "",
					"로딩중. 잠시만 기다려주세요...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 6; // SocketThread 스터디 신청 연결
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "스터디 신청 thread 6 실행 중");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Toast.makeText(JoinActivity.this, "신청이 완료되었습니다.", Toast.LENGTH_LONG)
					.show();
			join_activity.finish();
			JoinSelectActivity.joinSelectActivity.finish();
			LoginActivity.loginActivity.finish();
		}
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
		LinearLayout layout;
		Context cont;

		public Dialog_two_button(Context context, String title, String content) {
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

		@Override
		public void onClick(View v) {
			// yes 버튼
			if (v.getId() == R.id.tv_dialog_yes) {
				sync = new JoinSync();
				sync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				this.dismiss();
			}

			// no 버튼
			else if (v.getId() == R.id.tv_dialog_no) {
				this.dismiss(); // 팝업창 닫기
			}
		}
	}
}