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

	EditText edit_teamname; // ����
	EditText edit_pw; // �� ��й�ȣ
	Button btn_name_check; // ���� Ȯ�� ��ư
	String studytype; // ���͵� ����

	TextView tv_subject; // �����
	ArrayAdapter<CharSequence> subject_tv_adapter; // subject list

	ImageView join_plus; // �ο� �߰� ��ư

	Spinner spinner_OT; // OT ���� �ο� spinner
	ArrayAdapter<CharSequence> spinner_adapter; // spinner OT list
	Spinner spinner_subject; // �����ڵ� spinner
	ArrayAdapter<CharSequence> subject_spinner_adapter; // spinner subject list
	ArrayAdapter<CharSequence> major_spinner_adapter; // spinner major list

	CheckBox checkBox; // ���� ���� üũ�ڽ�

	static boolean name_check = false; // ���� Ȯ�� ����

	Button btn_back; // ��� ��ư
	Button btn_join; // ��û�ϱ� ��ư

	LinearLayout layout_subjectcode;

	// ////
	LayoutInflater inflater;
	static Vector<View> in_view;
	Vector<String[]> join_member; // �� ��� vector - �й�, ����, �̸�, �ڵ��� ��ȣ, ����
	int vector_size = 0;

	// inview �׸�
	EditText edit_sid;
	Spinner spinner_major;
	EditText edit_name;
	EditText edit_phone;
	EditText edit_mail;

	ViewGroup root; // layout root view
	LinearLayout rootLinear; // join_inview�� ���� LinearLayout

	Context context;

	Dialog_two_button dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		join_activity = JoinActivity.this;

		context = this;
		studytype = getIntent().getStringExtra("studytype");

		// ��Ʈ ����
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

		// 1���͵� �̿ܿ��� �����ڵ� ����
		if (!studytype.equals("1")) {
			layout_subjectcode = (LinearLayout) root
					.findViewById(R.id.layout_join_subjectcode);
			layout_subjectcode.setVisibility(View.GONE);

		}

		// ����, ��й�ȣ
		edit_teamname = (EditText) root.findViewById(R.id.edit_join_teamname);
		edit_pw = (EditText) root.findViewById(R.id.edit_join_teampw);

		// ��밡���� ���� Ȯ��
		btn_name_check = (Button) root
				.findViewById(R.id.btn_join_teamname_check);
		btn_name_check.setTypeface(typeface);
		btn_name_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edit_teamname.getText().toString().equals("")) {
					Toast.makeText(JoinActivity.this, "����� ������ �Է����ּ���.",
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
			// ����� ����
			tv_subject = (TextView) root.findViewById(R.id.tv_join_subject);
			subject_tv_adapter = ArrayAdapter.createFromResource(context,
					R.array.subjectname, 0);

			// ���� �ڵ� ���� spinner
			spinner_subject = (Spinner) root
					.findViewById(R.id.spinner_join_subjectcode);
			spinner_subject.setPrompt("���� �ڵ�");

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
							// �����ڵ� spinner ���ý� ����� textview ����
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

		// ���� ���� spinner
		major_spinner_adapter = ArrayAdapter.createFromResource(context,
				R.array.major, android.R.layout.simple_spinner_item);
		major_spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// ��� �߰�
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
					Toast.makeText(JoinActivity.this, "���͵�� �ִ� 6����� ��û �����մϴ�.",
							Toast.LENGTH_SHORT).show();
			}
		});

		// ��� ���� ���� checkbox
		checkBox = (CheckBox) root.findViewById(R.id.checkBox_join);

		// OT ���� �ο� ���� spinner
		spinner_OT = (Spinner) root.findViewById(R.id.spinner_join_num);
		spinner_OT.setPrompt("OT ���� �ο�");

		spinner_adapter = ArrayAdapter.createFromResource(context,
				R.array.OT_number, android.R.layout.simple_spinner_item);
		spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_OT.setAdapter(spinner_adapter);

		// ��� ��ư
		btn_back = (Button) root.findViewById(R.id.btn_join_back);
		btn_back.setTypeface(typeface);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// ��û�ϱ� ��ư
		btn_join = (Button) root.findViewById(R.id.btn_join_finish);
		btn_join.setTypeface(typeface);
		btn_join.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Boolean join = true; // ��ĭ Ȯ�� ����

				// ���� Ȯ�� �Ϸ��
				if (name_check) {
					// ��� ����
					join_member = new Vector<String[]>();

					// ����, pw, ���͵� ����, �����, ��ο���, OT ���� �ο�
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
					// �� �ο� �� ��ŭ ��� ���Ϳ� ����
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
					Log.e("ccc", "�� ��� ���� ���� : " + str);
					SocketThread.socket_str_arr[6] = str;

					// ��� ĭ�� �ԷµǸ� ��û �ȳ� �˾�
					if (join) {
						// ��û�ο��� �ּ� 1�� �̻�
						if (vector_size < 2)
							Toast.makeText(JoinActivity.this,
									"���͵�� �ּ� 2�� �̻� ��û �����մϴ�.",
									Toast.LENGTH_SHORT).show();
						else {
							dialog = new Dialog_two_button(context, "�ȳ�",
									"��û�Ͻðڽ��ϱ�?");
							dialog.show();
						}
					} else
						Toast.makeText(JoinActivity.this, "��ĭ�� ��� �Է����ּ���.",
								Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(JoinActivity.this, "������ Ȯ�����ּ���.",
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
			SocketThread.num = 10; // SocketThread ���� Ȯ�� ����
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "���� Ȯ�� thread 10 ���� ��");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// ��밡���� �����̸� Ȯ�� �Ϸ�, �Ұ����ϸ� �̹� ��� �� Toast
			if (name_check) {
				Toast.makeText(JoinActivity.this, "��� ������ �����Դϴ�.",
						Toast.LENGTH_SHORT).show();
				btn_name_check.setText("Ȯ�� �Ϸ�");
			} else {
				Toast.makeText(JoinActivity.this, "�̹� ������� �����Դϴ�.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class JoinSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(context, "",
					"�ε���. ��ø� ��ٷ��ּ���...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 6; // SocketThread ���͵� ��û ����
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "���͵� ��û thread 6 ���� ��");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Toast.makeText(JoinActivity.this, "��û�� �Ϸ�Ǿ����ϴ�.", Toast.LENGTH_LONG)
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
			// yes ��ư
			if (v.getId() == R.id.tv_dialog_yes) {
				sync = new JoinSync();
				sync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				this.dismiss();
			}

			// no ��ư
			else if (v.getId() == R.id.tv_dialog_no) {
				this.dismiss(); // �˾�â �ݱ�
			}
		}
	}
}