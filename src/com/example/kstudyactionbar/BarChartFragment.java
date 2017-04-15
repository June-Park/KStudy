package com.example.kstudyactionbar;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BarChartFragment extends Fragment {

	static int default_graph[] = new int[7];
	static int graph[] = new int[11];

	TextView tv1;
	TextView tv2;
	TextView tv3;
	TextView lastweek;
	TextView weekaver;
	TextView total;

	int y_max = 0;

	@Override
	// 초기화
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	// 초기화 완료후 실행
	public void onStart() {
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = (ViewGroup) inflater.inflate(R.layout.barchart_fragment,
				null);

		// 텍스트뷰
		tv1 = (TextView) view.findViewById(R.id.tv_barchart_1);
		lastweek = (TextView) view
				.findViewById(R.id.tv_three_barchart_lastweek);
		tv2 = (TextView) view.findViewById(R.id.tv_barchart_2);
		weekaver = (TextView) view
				.findViewById(R.id.tv_three_barchart_week_aver);
		tv3 = (TextView) view.findViewById(R.id.tv_barchart_3);
		total = (TextView) view.findViewById(R.id.tv_three_barchart_total);

		// 폰트 설정
		Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),
				"happy_bold.TTF");
		tv1.setTypeface(typeface);
		lastweek.setTypeface(typeface);
		tv2.setTypeface(typeface);
		weekaver.setTypeface(typeface);
		tv3.setTypeface(typeface);
		total.setTypeface(typeface);

		// 그래프
		// 표시할 수치값
		List<double[]> values = new ArrayList<double[]>();

		// 분류에 대한 이름
		String[] titles = new String[] { "평균 학습 시간" };

		// 항목을 표시하는데 사용될 색상값
		int[] colors = new int[] { Color.BLUE };

		// 로그인 상태 - 내 학습 시간, 평균 학습 시간, 디폴트 (관리자 모드 포함) - 평균 학습 시간
		if (MainActivity.login_state
				&& !MainActivity.team_name.equals("kbuctl")) {
			// 내 학습 시간
			values.add(new double[] { graph[3], graph[4], graph[5], graph[6] });
			// 평균 학습 시간
			values.add(new double[] { graph[7], graph[8], graph[9], graph[10] });
			titles = new String[] { "내 학습 시간", "평균 학습 시간" };
			colors = new int[] { Color.BLUE, Color.rgb(20, 150, 20) };

			// 텍스트뷰 setText
			lastweek.setText(graph[0] + "분");
			weekaver.setText(graph[1] + "분");
			total.setText(graph[2] + "분");

			// y축 최댓값 설정
			for (int i = 3; i <= 10; i++) {
				if (y_max < graph[i])
					y_max = graph[i];
			}

		} else {
			// 평균 학습 시간
			values.add(new double[] { default_graph[3], default_graph[4],
					default_graph[5], default_graph[6] });

			// 텍스트뷰 setText
			lastweek.setText(default_graph[0] + "분");
			weekaver.setText(default_graph[1] + "분");
			total.setText(default_graph[2] + "분");

			// y축 최댓값 설정
			for (int i = 3; i <= 6; i++) {
				if (y_max < default_graph[i])
					y_max = default_graph[i];
			}
		}

		// 그래프 출력을 위한 그래픽 속성 지정객체
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		// 그래프 폰트
		renderer.setTextTypeface(typeface);

		// 상단 표시 제목과 글자 크기
		// renderer.setChartTitle("학습시간");
		// renderer.setChartTitleTextSize(40);

		// 분류명 글자 크기 및 각 색상 지정
		renderer.setLegendTextSize(30);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}

		// X, Y축 항목 이름과 글자 크기
		renderer.setXTitle("주차");
		renderer.setYTitle("학습시간");
		renderer.setAxisTitleTextSize(20);

		// 수치값 글자 크기 / X축 최소, 최댓값 / Y축 최소, 최댓값
		renderer.setLabelsTextSize(20);
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(y_max * 1.2);

		// x, y축 라인 색상
		renderer.setAxesColor(Color.DKGRAY);

		// 상단 제목, x, y축 제목, 수치값의 글자 색상
		renderer.setLabelsColor(Color.BLACK);

		// x축의 표시 간격
		renderer.setXLabels(5);

		// y축의 표시 간격
		renderer.setYLabels(10);

		// x, y축 정렬 방향
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);

		// x, y축 스크롤 여부 on/off
		renderer.setPanEnabled(false, false);

		// zoom 기능 on/off
		renderer.setZoomEnabled(false, false);

		// zoom 비율
		renderer.setZoomRate(1.0f);

		// 막대간 간격
		renderer.setBarSpacing(0.5f);

		// 그래프 레이아웃 테두리(X, Y축, 항목 등 표시 되는 곳) 색
		// renderer.setMarginsColor(Color.rgb(215, 250, 210));
		renderer.setMarginsColor(Color.WHITE);
		// margin과 경계선 색
		// renderer.setAxesColor(Color.RED);
		// X축 눈금 색
		// renderer.setXLabelsColor(Color.DKGRAY);

		// 설정 정보 설정
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		for (int i = 0; i < titles.length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			double[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}

		// 그래프 객체 생성
		GraphicalView gv = ChartFactory.getBarChartView(getActivity(), dataset,
				renderer, Type.DEFAULT);

		// 그래프를 LinearLayout에 추가
		LinearLayout lBody = (LinearLayout) view
				.findViewById(R.id.linear_graph_barchart);
		lBody.addView(gv);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}