package com.example.kstudyactionbar;

import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CISActivity extends Activity {

	static Activity CIS_activity;

	SocketThread socketThread;
	Thread thread;

	JoinSync sync;

	TextView tv_name; // ����
	EditText edit_pw; // �� ��й�ȣ
	TextView tv_subject; // �����
	ArrayAdapter<CharSequence> subject_tv_adapter; // subject list

	ImageView join_plus; // �ο� �߰� ��ư

	// spinner
	Spinner spinner; // �����ڵ� spinner
	ArrayAdapter<CharSequence> adapter; // spinner subject list
	ArrayAdapter<CharSequence> major_spinner_adapter; // spinner major list

	static boolean name_check = false; // ���� Ȯ�� ����

	Button btn_back; // ��� ��ư
	Button btn_join; // ��û�ϱ� ��ư

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

		CIS_activity = CISActivity.this;

		context = this;

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
		root = (ViewGroup) inflater.inflate(R.layout.activity_cis, null);
		rootLinear = (LinearLayout) root.findViewById(R.id.layout_CIS_inview);

		// ����, ��й�ȣ
		tv_name = (TextView) root.findViewById(R.id.tv_CIS_teamname);
		edit_pw = (EditText) root.findViewById(R.id.edit_CIS_teampw);

		// ����� ����
		tv_subject = (TextView) root.findViewById(R.id.tv_CIS_teamname);
		subject_tv_adapter = ArrayAdapter.createFromResource(context,
				R.array.subjectname, 0);

		// ���� �ڵ� ���� spinner
		spinner = (Spinner) root.findViewById(R.id.spinner_CIS_subjectcode);
		spinner.setPrompt("���� �ڵ�");

		adapter = ArrayAdapter.createFromResource(context, R.array.subjectcode,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// �����ڵ� spinner ���ý� ����� textview ����
				tv_subject.setText(subject_tv_adapter.getItem(spinner
						.getSelectedItemPosition()));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// child View
		in_view = new Vector<View>();

		// ���� ���� spinner
		major_spinner_adapter = ArrayAdapter.createFromResource(context,
				R.array.major, android.R.layout.simple_spinner_item);
		major_spinner_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// ��� �߰�
		join_plus = (ImageView) root.findViewById(R.id.iv_CIS_plus);
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
					Toast.makeText(CISActivity.this, "���͵�� �ִ� 6����� ��û �����մϴ�.",
							Toast.LENGTH_SHORT).show();
			}
		});

		// ��� ��ư
		btn_back = (Button) root.findViewById(R.id.btn_CIS_back);
		btn_back.setTypeface(typeface);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// ��û�ϱ� ��ư
		btn_join = (Button) root.findViewById(R.id.btn_CIS_finish);
		btn_join.setTypeface(typeface);
		btn_join.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Boolean join = true; // ��ĭ Ȯ�� ����

				// ��� ����
				join_member = new Vector<String[]>();

				// ����, pw, ���͵� ����, �����, ��ο���, OT ���� �ο�
				if (vector_size != 0)
					SocketThread.socket_str_arr[0] = tv_name.getText()
							.toString()
							+ "_"
							+ ((EditText) rootLinear.getChildAt(0)
									.findViewById(R.id.edit_name)).getText()
									.toString();
				SocketThread.socket_str_arr[1] = edit_pw.getText().toString();
				if (edit_pw.getText().toString().equals(""))
					join = false;
				SocketThread.socket_str_arr[2] = "4";
				SocketThread.socket_str_arr[3] = tv_subject.getText()
						.toString();
				SocketThread.socket_str_arr[4] = "f";
				SocketThread.socket_str_arr[5] = "0";

				for (int i = 0; i < 6; i++)
					Log.e("ccc", "Socket_str_arr : "
							+ SocketThread.socket_str_arr[i]);

				String str = "";
				// �� �ο� �� ��ŭ ��� ���Ϳ� ����
				for (int i = 0; i < vector_size; i++) {
					join_member.add(new String[] { "", "", "", "", "", "0" });
					join_member.get(i)[0] = ((EditText) rootLinear
							.getChildAt(i).findViewById(R.id.edit_sid))
							.getText().toString();
					join_member.get(i)[1] = ((Spinner) rootLinear.getChildAt(i)
							.findViewById(R.id.spinner_major))
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
						Toast.makeText(CISActivity.this,
								"���͵�� �ּ� 2�� �̻� ��û �����մϴ�.", Toast.LENGTH_SHORT)
								.show();
					else {
						dialog = new Dialog_two_button(context, "�ȳ�",
								"��û�Ͻðڽ��ϱ�?");
						dialog.show();
					}
				} else
					Toast.makeText(CISActivity.this, "��ĭ�� ��� �Է����ּ���.",
							Toast.LENGTH_SHORT).show();
			}
		});

		setContentView(root);
	}

	class JoinSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
			// ��û�Ϸ� toast
			Toast.makeText(CISActivity.this, "��û�� �Ϸ�Ǿ����ϴ�.", Toast.LENGTH_LONG)
					.show();
			CIS_activity.finish();
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