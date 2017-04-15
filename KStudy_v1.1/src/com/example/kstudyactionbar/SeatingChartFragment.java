package com.example.kstudyactionbar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SeatingChartFragment extends Fragment {

	SocketThread socketThread;
	Thread thread;

	static int map_num;

	static String map[] = new String[14];
	ImageView btn_map[];

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.seatingchart_fragment, container,
				false);

		btn_map = new ImageView[14];
		btn_map[0] = (ImageView) v.findViewById(R.id.btn_map_s1);
		btn_map[1] = (ImageView) v.findViewById(R.id.btn_map_s2);
		btn_map[2] = (ImageView) v.findViewById(R.id.btn_map_s3);
		btn_map[3] = (ImageView) v.findViewById(R.id.btn_map_1);
		btn_map[4] = (ImageView) v.findViewById(R.id.btn_map_2);
		btn_map[5] = (ImageView) v.findViewById(R.id.btn_map_3);
		btn_map[6] = (ImageView) v.findViewById(R.id.btn_map_4);
		btn_map[7] = (ImageView) v.findViewById(R.id.btn_map_5);
		btn_map[8] = (ImageView) v.findViewById(R.id.btn_map_6);
		btn_map[9] = (ImageView) v.findViewById(R.id.btn_map_7);
		btn_map[10] = (ImageView) v.findViewById(R.id.btn_map_8);
		btn_map[11] = (ImageView) v.findViewById(R.id.btn_map_9);
		btn_map[12] = (ImageView) v.findViewById(R.id.btn_map_10);
		btn_map[13] = (ImageView) v.findViewById(R.id.btn_map_11);

		for (int i = 0; i < btn_map.length; i++) {
			btn_map[i].setTag(i);

			if (map[i].equals("t")) {
				btn_map[i]
						.setBackgroundResource(R.drawable.seating_chart_on_s_light);
				btn_map[i].setSelected(true);

			} else {
				btn_map[i]
						.setBackgroundResource(R.drawable.seating_chart_off_s);
				btn_map[i].setSelected(false);
			}
		}
		for (int i = 0; i < btn_map.length; i++) {
			btn_map[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 관리자만 변경 가능
					if (MainActivity.team_name.equals("kbuctl")) {
						if (btn_map[(Integer) v.getTag()].isSelected()) {
							// on 상태 - off 전환
							btn_map[(Integer) v.getTag()]
									.setBackgroundResource(R.drawable.seating_chart_off_s);
							btn_map[(Integer) v.getTag()].setSelected(false);
						} else {
							// off 상태 - on 전환
							btn_map[(Integer) v.getTag()]
									.setBackgroundResource(R.drawable.seating_chart_on_s_light);
							btn_map[(Integer) v.getTag()].setSelected(true);
						}
						map_num = (Integer) (v.getTag());
						thread = new Thread(socketThread = new SocketThread());
						SocketThread.num = 7; // SocketThread 배치도 현황 변경 연결
						thread.start();
					}
				}
			});
		}
		return v;
	}
}