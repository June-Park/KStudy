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
	// �ʱ�ȭ
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	// �ʱ�ȭ �Ϸ��� ����
	public void onStart() {
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = (ViewGroup) inflater.inflate(R.layout.barchart_fragment,
				null);

		// �ؽ�Ʈ��
		tv1 = (TextView) view.findViewById(R.id.tv_barchart_1);
		lastweek = (TextView) view
				.findViewById(R.id.tv_three_barchart_lastweek);
		tv2 = (TextView) view.findViewById(R.id.tv_barchart_2);
		weekaver = (TextView) view
				.findViewById(R.id.tv_three_barchart_week_aver);
		tv3 = (TextView) view.findViewById(R.id.tv_barchart_3);
		total = (TextView) view.findViewById(R.id.tv_three_barchart_total);

		// ��Ʈ ����
		Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),
				"happy_bold.TTF");
		tv1.setTypeface(typeface);
		lastweek.setTypeface(typeface);
		tv2.setTypeface(typeface);
		weekaver.setTypeface(typeface);
		tv3.setTypeface(typeface);
		total.setTypeface(typeface);

		// �׷���
		// ǥ���� ��ġ��
		List<double[]> values = new ArrayList<double[]>();

		// �з��� ���� �̸�
		String[] titles = new String[] { "��� �н� �ð�" };

		// �׸��� ǥ���ϴµ� ���� ����
		int[] colors = new int[] { Color.BLUE };

		// �α��� ���� - �� �н� �ð�, ��� �н� �ð�, ����Ʈ (������ ��� ����) - ��� �н� �ð�
		if (MainActivity.login_state
				&& !MainActivity.team_name.equals("kbuctl")) {
			// �� �н� �ð�
			values.add(new double[] { graph[3], graph[4], graph[5], graph[6] });
			// ��� �н� �ð�
			values.add(new double[] { graph[7], graph[8], graph[9], graph[10] });
			titles = new String[] { "�� �н� �ð�", "��� �н� �ð�" };
			colors = new int[] { Color.BLUE, Color.rgb(20, 150, 20) };

			// �ؽ�Ʈ�� setText
			lastweek.setText(graph[0] + "��");
			weekaver.setText(graph[1] + "��");
			total.setText(graph[2] + "��");

			// y�� �ִ� ����
			for (int i = 3; i <= 10; i++) {
				if (y_max < graph[i])
					y_max = graph[i];
			}

		} else {
			// ��� �н� �ð�
			values.add(new double[] { default_graph[3], default_graph[4],
					default_graph[5], default_graph[6] });

			// �ؽ�Ʈ�� setText
			lastweek.setText(default_graph[0] + "��");
			weekaver.setText(default_graph[1] + "��");
			total.setText(default_graph[2] + "��");

			// y�� �ִ� ����
			for (int i = 3; i <= 6; i++) {
				if (y_max < default_graph[i])
					y_max = default_graph[i];
			}
		}

		// �׷��� ����� ���� �׷��� �Ӽ� ������ü
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		// �׷��� ��Ʈ
		renderer.setTextTypeface(typeface);

		// ��� ǥ�� ����� ���� ũ��
		// renderer.setChartTitle("�н��ð�");
		// renderer.setChartTitleTextSize(40);

		// �з��� ���� ũ�� �� �� ���� ����
		renderer.setLegendTextSize(30);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}

		// X, Y�� �׸� �̸��� ���� ũ��
		renderer.setXTitle("����");
		renderer.setYTitle("�н��ð�");
		renderer.setAxisTitleTextSize(20);

		// ��ġ�� ���� ũ�� / X�� �ּ�, �ִ� / Y�� �ּ�, �ִ�
		renderer.setLabelsTextSize(20);
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(y_max * 1.2);

		// x, y�� ���� ����
		renderer.setAxesColor(Color.DKGRAY);

		// ��� ����, x, y�� ����, ��ġ���� ���� ����
		renderer.setLabelsColor(Color.BLACK);

		// x���� ǥ�� ����
		renderer.setXLabels(5);

		// y���� ǥ�� ����
		renderer.setYLabels(10);

		// x, y�� ���� ����
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);

		// x, y�� ��ũ�� ���� on/off
		renderer.setPanEnabled(false, false);

		// zoom ��� on/off
		renderer.setZoomEnabled(false, false);

		// zoom ����
		renderer.setZoomRate(1.0f);

		// ���밣 ����
		renderer.setBarSpacing(0.5f);

		// �׷��� ���̾ƿ� �׵θ�(X, Y��, �׸� �� ǥ�� �Ǵ� ��) ��
		// renderer.setMarginsColor(Color.rgb(215, 250, 210));
		renderer.setMarginsColor(Color.WHITE);
		// margin�� ��輱 ��
		// renderer.setAxesColor(Color.RED);
		// X�� ���� ��
		// renderer.setXLabelsColor(Color.DKGRAY);

		// ���� ���� ����
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

		// �׷��� ��ü ����
		GraphicalView gv = ChartFactory.getBarChartView(getActivity(), dataset,
				renderer, Type.DEFAULT);

		// �׷����� LinearLayout�� �߰�
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