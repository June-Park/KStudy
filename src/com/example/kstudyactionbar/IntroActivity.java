package com.example.kstudyactionbar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class IntroActivity extends Activity {

	SocketThread socketThread;
	Thread thread;
	IntroSync sync;

	ImageView iv_intro;

	boolean pro_bool = true; // ���α׷��� ���� �Ǵ� ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		// �׼ǹ� ����
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		iv_intro = (ImageView) findViewById(R.id.iv_intro_logo);

		sync = new IntroSync();
		sync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	class IntroSync extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			thread = new Thread(socketThread = new SocketThread());
			SocketThread.num = 1; // SocketThread ��Ʈ�� ����
			thread.start();
			while (thread.isAlive())
				Log.e("thread", "intro thread ���� ��");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Intent intent = new Intent(IntroActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}