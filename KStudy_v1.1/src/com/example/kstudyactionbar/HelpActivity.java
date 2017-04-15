package com.example.kstudyactionbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HelpActivity extends ActionBarActivity {

	ViewPager pager;

	RelativeLayout left;
	RelativeLayout right;

	ImageView[] help;
	ImageView help_1;
	ImageView help_2;
	ImageView help_3;
	ImageView help_4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"happy_bold.TTF");

		try {
			Integer titleId = Resources.getSystem().getIdentifier(
					"action_bar_title", "id", "android");
			TextView title = (TextView) getWindow().findViewById(titleId);
			title.setTypeface(typeface);
		} catch (Exception e) {
		}

		pager = (ViewPager) findViewById(R.id.viewpager_help);
		ImageAdapter adapter = new ImageAdapter();
		pager.setAdapter(adapter);

		// 버튼 클릭
		left = (RelativeLayout) findViewById(R.id.layout_left);
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pager.getCurrentItem() != 0)
					pager.setCurrentItem(pager.getCurrentItem() - 1);
			}
		});

		right = (RelativeLayout) findViewById(R.id.layout_right);
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pager.getCurrentItem() != pager.getChildCount())
					pager.setCurrentItem(pager.getCurrentItem() + 1);
			}
		});

		help = new ImageView[4];

		help[0] = (ImageView) findViewById(R.id.iv_help_1st);
		help[0].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(0);
			}
		});

		help[1] = (ImageView) findViewById(R.id.iv_help_2nd);
		help[1].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(1);
			}
		});
		help[2] = (ImageView) findViewById(R.id.iv_help_3rd);
		help[2].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(2);
			}
		});
		help[3] = (ImageView) findViewById(R.id.iv_help_4th);
		help[3].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(3);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_help_close:
			finish();
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	class ImageAdapter extends PagerAdapter {
		Context context;

		public ImageAdapter() {
			super();
		}

		private int[] GalImages = { R.drawable.help_intro,
				R.drawable.help1page, R.drawable.help2page,
				R.drawable.help3page };

		@Override
		public int getCount() {
			return GalImages.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public void finishUpdate(View container) {
			int position = pager.getCurrentItem();
			if (position == 0) {
				help[0].setBackgroundResource(R.drawable.help_selected);
				help[1].setBackgroundResource(R.drawable.help_unselected);
				help[2].setBackgroundResource(R.drawable.help_unselected);
				help[3].setBackgroundResource(R.drawable.help_unselected);
			} else if (position == 1) {
				help[0].setBackgroundResource(R.drawable.help_unselected);
				help[1].setBackgroundResource(R.drawable.help_selected);
				help[2].setBackgroundResource(R.drawable.help_unselected);
				help[3].setBackgroundResource(R.drawable.help_unselected);
			} else if (position == 2) {
				help[0].setBackgroundResource(R.drawable.help_unselected);
				help[1].setBackgroundResource(R.drawable.help_unselected);
				help[2].setBackgroundResource(R.drawable.help_selected);
				help[3].setBackgroundResource(R.drawable.help_unselected);
			} else if (position == 3) {
				help[0].setBackgroundResource(R.drawable.help_unselected);
				help[1].setBackgroundResource(R.drawable.help_unselected);
				help[2].setBackgroundResource(R.drawable.help_unselected);
				help[3].setBackgroundResource(R.drawable.help_selected);
			}
		}

		@Override
		public void startUpdate(View container) {
			int position = pager.getCurrentItem();
			if (position == 0) {
				help[0].setBackgroundResource(R.drawable.help_selected);
				help[1].setBackgroundResource(R.drawable.help_unselected);
				help[2].setBackgroundResource(R.drawable.help_unselected);
				help[3].setBackgroundResource(R.drawable.help_unselected);
			} else if (position == 1) {
				help[0].setBackgroundResource(R.drawable.help_unselected);
				help[1].setBackgroundResource(R.drawable.help_selected);
				help[2].setBackgroundResource(R.drawable.help_unselected);
				help[3].setBackgroundResource(R.drawable.help_unselected);
			} else if (position == 2) {
				help[0].setBackgroundResource(R.drawable.help_unselected);
				help[1].setBackgroundResource(R.drawable.help_unselected);
				help[2].setBackgroundResource(R.drawable.help_selected);
				help[3].setBackgroundResource(R.drawable.help_unselected);
			} else if (position == 3) {
				help[0].setBackgroundResource(R.drawable.help_unselected);
				help[1].setBackgroundResource(R.drawable.help_unselected);
				help[2].setBackgroundResource(R.drawable.help_unselected);
				help[3].setBackgroundResource(R.drawable.help_selected);
			}
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ImageView imageView = new ImageView(getApplicationContext());
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setImageResource(GalImages[position]);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}
}