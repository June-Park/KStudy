package com.example.kstudyactionbar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class JoinSelectActivity extends ActionBarActivity {

	static Activity joinSelectActivity;

	TextView tv_1;
	TextView tv_2;
	TextView tv_3;

	ImageButton btn_1;
	ImageButton btn_3;
	ImageButton btn_ex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_select);

		joinSelectActivity = JoinSelectActivity.this;

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

		tv_1 = (TextView) findViewById(R.id.tv_1_study);
		tv_1.setTypeface(typeface);
		tv_2 = (TextView) findViewById(R.id.tv_3_study);
		tv_2.setTypeface(typeface);
		tv_3 = (TextView) findViewById(R.id.tv_ex_study);
		tv_3.setTypeface(typeface);

		btn_1 = (ImageButton) findViewById(R.id.btn_1study);
		btn_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JoinSelectActivity.this,
						JoinActivity.class);
				intent.putExtra("studytype", "1");
				startActivity(intent);
			}
		});

		btn_3 = (ImageButton) findViewById(R.id.btn_3study);
		btn_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JoinSelectActivity.this,
						JoinActivity.class);
				intent.putExtra("studytype", "2");
				startActivity(intent);
			}
		});

		btn_ex = (ImageButton) findViewById(R.id.btn_ex);
		btn_ex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JoinSelectActivity.this,
						JoinActivity.class);
				intent.putExtra("studytype", "3");
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_joinselect_help:
			Intent intent = new Intent(JoinSelectActivity.this,
					JoinHelpActivity.class);
			startActivity(intent);
			break;
		case R.id.action_joinselect_close:
			finish();
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.joinselect, menu);
		return true;
	}
}