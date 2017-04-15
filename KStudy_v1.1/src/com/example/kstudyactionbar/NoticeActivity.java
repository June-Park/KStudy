package com.example.kstudyactionbar;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NoticeActivity extends ActionBarActivity {

	TextView tv_1;
	TextView tv_2;
	TextView tv_3;
	TextView tv_homepage;
	TextView tv_eportpolio;
	TextView tv_tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);

		// 폰트 설정
		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"happy_bold.TTF");

		try {
			Integer titleId = Resources.getSystem().getIdentifier(
					"action_bar_title", "id", "android");
			TextView title = (TextView) getWindow().findViewById(titleId);
			title.setTypeface(typeface);
		} catch (Exception e) {
		}

		tv_1 = (TextView) findViewById(R.id.tv_notice_1);
		tv_1.setTypeface(typeface);
		tv_2 = (TextView) findViewById(R.id.tv_notice_2);
		tv_2.setTypeface(typeface);
		tv_3 = (TextView) findViewById(R.id.tv_notice_3);
		tv_3.setTypeface(typeface);

		tv_homepage = (TextView) findViewById(R.id.tv_notice_homepage);
		tv_homepage.setTypeface(typeface);
		Linkify.addLinks(tv_homepage, Linkify.WEB_URLS);

		tv_eportpolio = (TextView) findViewById(R.id.tv_notice_eportfolio);
		tv_eportpolio.setTypeface(typeface);
		Linkify.addLinks(tv_eportpolio, Linkify.WEB_URLS);

		tv_tel = (TextView) findViewById(R.id.tv_notice_tel);
		tv_tel.setTypeface(typeface);
		Linkify.addLinks(tv_tel, Linkify.PHONE_NUMBERS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_notice_close:
			finish();
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.notice, menu);
		return true;
	}

}