package com.example.kstudyactionbar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;
import android.util.Log;

public class SocketThread implements Runnable {
	Socket socket;
	String ip = "210.123.254.47";// ���� ip
	Vector<String[]> file_vec;

	DataOutputStream send_File; // ������ ���Ͽ��� - �������
	DataInputStream get_File; // ������ ���Ͽ��� - �ޱ��
	FileOutputStream fos; // ���� ��Ʈ��
	File file; // �������� �о�� ����

	String read;

	static int num; // ���� ������ ��ȣ

	// �ٸ� activity���� ����� �ӽ� ����
	static int socket_num;
	static String socket_str;
	static String socket_str_arr[] = new String[7];

	@Override
	public void run() {
		// ���� ����
		Log.e("ccc", "��");

		try {
			socket = new Socket(ip, 10001);
			send_File = new DataOutputStream(socket.getOutputStream());
			get_File = new DataInputStream(socket.getInputStream());

			Log.e("ccc", "��Ʈ��");

			// ��Ĺ thread ������ ��ȣ ����
			send_File.writeInt(num);
			send_File.flush();

			Log.e("ccc", "switch ����");

			switch (num) {
			// ��Ʈ��
			case 1:
				Log.e("ccc", "case 1");
				// ��ġ�� �ǽð� ��Ȳ
				// String[]�� ������
				read = get_File.readUTF();
				Log.e("ccc", "case 1 " + read);

				// TwoFragment String �迭�� t/f ����
				SeatingChartFragment.map = strarr(read);

				Log.e("ccc", "case 3");
				// ������ ���� ��Ȳ (count)
				// int[]�� ������
				read = get_File.readUTF();
				Log.e("ccc", "case 3 2 " + read);

				TimetableFragment.next_timetable_count = intarr(read);

				Log.e("ccc", "case 4");
				// default �׷��� ������
				// int[]�� ������
				read = get_File.readUTF();
				Log.e("ccc", "case 4 " + read);

				BarChartFragment.default_graph = intarr(read);
				break;
			// ��й�ȣ Ȯ��
			case 2:
				Log.e("ccc", "case 5");
				// ��й�ȣ Ȯ��
				// ���̸�, ��й�ȣ ����, ��й�ȣ Ȯ�� String(t/f) ������
				send_File.writeUTF(LoginActivity.team_name);
				send_File.writeUTF(LoginActivity.team_pw);
				read = get_File.readUTF();
				Log.e("ccc", "��й�ȣ Ȯ�� : " + read);

				if (read.equals("Y")) {
					MainActivity.login_state = true;
				}
				break;
			// �α��� ����
			case 3:
				Log.e("ccc", "case 6");
				// ���͵� ����
				// �� �̸� ����, �� ���� String ������
				send_File.writeUTF(MainActivity.team_name);
				read = get_File.readUTF();
				Log.e("ccc", "team_type read : " + read);
				MainActivity.team_type = read;

				Log.e("ccc", "case 7");
				// �̹��� ����
				// �� �̸� ����, ���͵� ���� �ð� String[]�� ������
				send_File.writeUTF(MainActivity.team_name);
				read = get_File.readUTF();

				TimetableFragment.this_timetable = strarr(read);

				Log.e("ccc", "case 8");
				// ������ ����
				// �� �̸� ����, ���͵� ���� �ð� String[]�� ������
				send_File.writeUTF(MainActivity.team_name);
				read = get_File.readUTF();

				TimetableFragment.next_timetable = strarr(read);

				Log.e("ccc", "case 9");
				// �׷���
				// �� �̸� ����, ������ �ð�, ������ ���, ��, �ֱ� 4��, �ֱ� 4�� ��� int[]�� ������
				send_File.writeUTF(MainActivity.team_name);
				read = get_File.readUTF();

				BarChartFragment.graph = intarr(read);

				Log.e("ccc", "case 10");
				// ���� ����
				// �� �̸� ����, ���� ���� vector<String[]>�� ������
				send_File.writeUTF(MainActivity.team_name);
				read = get_File.readUTF();

				MemberActivity.team_member = vec_strarr(read);

				Log.e("ccc", "case 20");
				// �����ڿ� - team ��ü list vector<String[]>�� ������
				if (MainActivity.team_name.equals("kbuctl")) {
					read = get_File.readUTF();
					MemberActivity.team_list = vec_teamlist_arr(read);
				}
				break;
			// �ǽð� ���
			case 4:
				Log.e("ccc", "case 11");
				// �̹��� �ð�ǥ ��û/���
				// �� �̸�, �ε���, ���� ����
				send_File.writeUTF(MainActivity.team_name);
				send_File.writeInt(socket_num);
				send_File.writeUTF(socket_str);
				break;
			case 5:
				Log.e("ccc", "case 12");
				// ������ �ð�ǥ ��û/���
				// �� �̸�, �ε���, ���� ����
				send_File.writeUTF(MainActivity.team_name);
				send_File.writeInt(socket_num);
				send_File.writeUTF(socket_str);
				break;
			case 6:
				Log.e("ccc", "case 13");
				// ���͵� ��û(���� �߰�)
				// ���̸�, ��й�ȣ, ���͵� ����, �����, ��� ���ǿ���, OT ���� �ο� ����, (�й�, ����, �̸�,
				// ��ȭ��ȣ, ����)
				send_File.writeUTF(socket_str_arr[0]);
				send_File.writeUTF(socket_str_arr[1]);
				send_File.writeUTF(socket_str_arr[2]);
				send_File.writeUTF(socket_str_arr[3]);
				send_File.writeUTF(socket_str_arr[4]);
				send_File.writeInt(Integer.parseInt(socket_str_arr[5]));
				send_File.writeUTF(socket_str_arr[6]);
				break;
			case 7:
				Log.e("ccc", "case 14");
				// ��ġ�� ��Ȳ ����
				// ���� �ٲ� �� ��ȣ ����
				send_File.writeUTF(SeatingChartFragment.map_num + "");
				send_File.flush();
				break;
			case 8:
				Log.e("ccc", "case 15");
				// CIS �����
				read = get_File.readUTF();
				LoginActivity.CIS_team_list = strarr(read);
				break;
			case 9:
				Log.e("ccc", "case 16");
				// �ǽð� ���ΰ�ħ

				// ��ġ�� �ǽð� ��Ȳ
				// String[]�� ������
				read = get_File.readUTF();
				Log.e("ccc", "case 1 " + read);

				// TwoFragment String �迭�� t/f ����
				SeatingChartFragment.map = strarr(read);

				Log.e("ccc", "case 3");
				// ������ ���� ��Ȳ (count)
				// int[]�� ������
				read = get_File.readUTF();
				Log.e("ccc", "case 3 2 " + read);

				TimetableFragment.next_timetable_count = intarr(read);

				break;
			case 10:
				Log.e("ccc", "case 17");
				// ��û�ϱ� ��밡���� ���� Ȯ��
				// ����Ϸ��� ���� ����
				// ��밡���ϸ� Y, �Ұ����ϸ� N
				send_File.writeUTF(socket_str);
				send_File.flush();
				read = get_File.readUTF();
				Log.e("ccc", "case 10 " + read);

				if (read.equals("Y"))
					JoinActivity.name_check = true;
				else
					JoinActivity.name_check = false;
				break;
			case 11:
				Log.e("ccc", "case 18");
				// �̹��� ���� ��Ȳī����
				send_File.writeInt(socket_num);
				read = get_File.readUTF();
				TimetableFragment.isTrue = read;
				break;
			case 12:
				Log.e("ccc", "case 19");
				// ������ ���� ���� ����
				send_File.writeInt(socket_num);
				read = get_File.readUTF();
				TimetableFragment.isTrue = read;
				break;
			default:
				break;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

	// ////////////////////// tokenizer////////////////////////////////
	StringTokenizer tokenizer;

	public String[] strarr(String str) {
		tokenizer = new StringTokenizer(str, "//");
		String[] temp = new String[tokenizer.countTokens()];

		for (int a = 0; tokenizer.hasMoreElements(); a++) {
			temp[a] = tokenizer.nextToken();
		}
		return temp;
	}

	public int[] intarr(String str) {
		tokenizer = new StringTokenizer(str, "//");
		int[] temp = new int[tokenizer.countTokens()];

		for (int a = 0; tokenizer.hasMoreElements(); a++) {
			temp[a] = Integer.parseInt(tokenizer.nextToken());
		}
		return temp;
	}

	public Vector<String[]> vec_strarr(String str) {
		tokenizer = new StringTokenizer(str, "////");
		// StringTokenizer temp_t;
		String[] temp;
		Vector<String[]> temp_vec = new Vector<String[]>();

		while (tokenizer.hasMoreElements()) {
			temp = new String[4];
			// temp_t = new StringTokenizer(tokenizer.nextToken(), "//");
			for (int a = 0; a < temp.length; a++) {
				temp[a] = tokenizer.nextToken();
			}
			temp_vec.add(temp);
		}
		return temp_vec;
	}

	public Vector<String[]> vec_teamlist_arr(String str) {
		tokenizer = new StringTokenizer(str, "////");
		// StringTokenizer temp_t;
		String[] temp;
		Vector<String[]> temp_vec = new Vector<String[]>();

		while (tokenizer.hasMoreElements()) {
			temp = new String[2];
			// temp_t = new StringTokenizer(tokenizer.nextToken(), "//");
			for (int a = 0; a < temp.length; a++) {
				temp[a] = tokenizer.nextToken();
			}
			temp_vec.add(temp);
		}
		return temp_vec;
	}
}