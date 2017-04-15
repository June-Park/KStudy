package com.example.kstudyactionbar;

import java.util.Vector;

import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MemberActivity extends ActionBarActivity {

	static Vector<String[]> team_member = new Vector<String[]>();
	static Vector<String[]> team_list = new Vector<String[]>();

	LayoutInflater inflater;
	static Vector<View> in_view;

	ViewGroup root; // layout root view
	LinearLayout rootLinear; // join_inview°¡ ºÙÀ» LinearLayout

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"happy_bold.TTF");

		ActionBar actionBar = getActionBar();
		if (MainActivity.team_name.equals("kbuctl"))
			actionBar.setTitle("ÆÀ list º¸±â");
		else
			actionBar.setTitle(MainActivity.team_name);

		try {
			Integer titleId = Resources.getSystem().getIdentifier(
					"action_bar_title", "id", "android");
			TextView title = (TextView) getWindow().findViewById(titleId);
			title.setTypeface(typeface);
		} catch (Exception e) {
		}

		// root View
		inflater = getLayoutInflater();
		root = (ViewGroup) inflater.inflate(R.layout.activity_member, null);
		rootLinear = (LinearLayout) root.findViewById(R.id.layout_member);

		// child View
		in_view = new Vector<View>();

		// °ü¸®ÀÚ - team list / ÆÀ - member list
		if (MainActivity.team_name.equals("kbuctl")) {
			for (int i = 0; i < team_list.size(); i++) {
				in_view.add((View) inflater.inflate(R.layout.teamlist_inview,
						null));

				in_view.get(i).setLayoutParams(
						new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								MainActivity.dialog_height / 3));

				in_view.get(i).setTag(i);

				ImageView iv_teamtype = (ImageView) in_view.get(i)
						.findViewById(R.id.iv_teamlist_type);
				TextView tv_teamname = (TextView) in_view.get(i).findViewById(
						R.id.tv_teamlist_name);

				tv_teamname.setText(team_list.get(i)[0]);
				tv_teamname.setTypeface(typeface);

				if (Integer.parseInt(team_list.get(i)[1]) == 1)
					iv_teamtype
							.setBackgroundResource(R.drawable.study_application_1);
				else if (Integer.parseInt(team_list.get(i)[1]) == 2)
					iv_teamtype
							.setBackgroundResource(R.drawable.study_application_3);
				else if (Integer.parseInt(team_list.get(i)[1]) == 3)
					iv_teamtype
							.setBackgroundResource(R.drawable.study_application_ex);
				else
					iv_teamtype
							.setBackgroundResource(R.drawable.study_application_cis);

				rootLinear.addView(in_view.get(i));
			}
		} else {
			for (int i = 0; i < team_member.size(); i++) {
				in_view.add((View) inflater.inflate(R.layout.member_inview,
						null));

				in_view.get(i).setLayoutParams(
						new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								MainActivity.dialog_height * 2 / 3));

				in_view.get(i).setTag(i);

				TextView sidText = (TextView) in_view.get(i).findViewById(
						R.id.tv_member_sid);
				TextView nameText = (TextView) in_view.get(i).findViewById(
						R.id.tv_member_name);
				TextView phoneText = (TextView) in_view.get(i).findViewById(
						R.id.tv_member_phone);
				TextView mailText = (TextView) in_view.get(i).findViewById(
						R.id.tv_member_mail);

				sidText.setText(team_member.get(i)[0]);
				nameText.setText(team_member.get(i)[1]);
				phoneText.setText(team_member.get(i)[2]);
				Linkify.addLinks(phoneText, Linkify.PHONE_NUMBERS);
				mailText.setText(team_member.get(i)[3]);
				Linkify.addLinks(mailText, Linkify.EMAIL_ADDRESSES);

				sidText.setTypeface(typeface);
				nameText.setTypeface(typeface);
				phoneText.setTypeface(typeface);
				mailText.setTypeface(typeface);

				rootLinear.addView(in_view.get(i));
			}
		}

		setContentView(root);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_member_close:
			finish();
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.member, menu);
		return true;
	}
}