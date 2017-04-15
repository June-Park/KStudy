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
	String ip = "210.123.254.47";// 서버 ip
	Vector<String[]> file_vec;

	DataOutputStream send_File; // 서버와 소켓연결 - 보내기용
	DataInputStream get_File; // 서버와 소켓연결 - 받기용
	FileOutputStream fos; // 파일 스트림
	File file; // 서버에서 읽어올 파일

	String read;

	static int num; // 소켓 연결할 번호

	// 다른 activity에서 사용할 임시 변수
	static int socket_num;
	static String socket_str;
	static String socket_str_arr[] = new String[7];

	@Override
	public void run() {
		// 소켓 연결
		Log.e("ccc", "런");

		try {
			socket = new Socket(ip, 10001);
			send_File = new DataOutputStream(socket.getOutputStream());
			get_File = new DataInputStream(socket.getInputStream());

			Log.e("ccc", "스트림");

			// 소캣 thread 실행할 번호 전송
			send_File.writeInt(num);
			send_File.flush();

			Log.e("ccc", "switch 시작");

			switch (num) {
			// 인트로
			case 1:
				Log.e("ccc", "case 1");
				// 배치도 실시간 현황
				// String[]로 가져옴
				read = get_File.readUTF();
				Log.e("ccc", "case 1 " + read);

				// TwoFragment String 배열에 t/f 저장
				SeatingChartFragment.map = strarr(read);

				Log.e("ccc", "case 3");
				// 다음주 예약 현황 (count)
				// int[]로 가져옴
				read = get_File.readUTF();
				Log.e("ccc", "case 3 2 " + read);

				TimetableFragment.next_timetable_count = intarr(read);

				Log.e("ccc", "case 4");
				// default 그래프 데이터
				// int[]로 가져옴
				read = get_File.readUTF();
				Log.e("ccc", "case 4 " + read);

				BarChartFragment.default_graph = intarr(read);
				break;
			// 비밀번호 확인
			case 2:
				Log.e("ccc", "case 5");
				// 비밀번호 확인
				// 팀이름, 비밀번호 전송, 비밀번호 확인 String(t/f) 가져옴
				send_File.writeUTF(LoginActivity.team_name);
				send_File.writeUTF(LoginActivity.team_pw);
				read = get_File.readUTF();
				Log.e("ccc", "비밀번호 확인 : " + read);

				if (read.equals("Y")) {
					MainActivity.login_state = true;
				}
				break;
			// 로그인 이후
			case 3:
				Log.e("ccc", "case 6");
				// 스터디 종류
				// 팀 이름 전송, 팀 종류 String 가져옴
				send_File.writeUTF(MainActivity.team_name);
				read = get_File.readUTF();
				Log.e("ccc", "team_type read : " + read);
				MainActivity.team_type = read;

				Log.e("ccc", "case 7");
				// 이번주 예약
				// 팀 이름 전송, 스터디 예약 시간 String[]로 가져옴
				send_File.writeUTF(MainActivity.team_name);
				read = get_File.readUTF();

				TimetableFragment.this_timetable = strarr(read);

				Log.e("ccc", "case 8");
				// 다음주 예약
				// 팀 이름 전송, 스터디 예약 시간 String[]로 가져옴
				send_File.writeUTF(MainActivity.team_name);
				read = get_File.readUTF();

				TimetableFragment.next_timetable = strarr(read);

				Log.e("ccc", "case 9");
				// 그래프
				// 팀 이름 전송, 지난주 시간, 지난주 평균, 합, 최근 4주, 최근 4주 평균 int[]로 가져옴
				send_File.writeUTF(MainActivity.team_name);
				read = get_File.readUTF();

				BarChartFragment.graph = intarr(read);

				Log.e("ccc", "case 10");
				// 팀원 정보
				// 팀 이름 전송, 팀원 정보 vector<String[]>로 가져옴
				send_File.writeUTF(MainActivity.team_name);
				read = get_File.readUTF();

				MemberActivity.team_member = vec_strarr(read);

				Log.e("ccc", "case 20");
				// 관리자용 - team 전체 list vector<String[]>로 가져옴
				if (MainActivity.team_name.equals("kbuctl")) {
					read = get_File.readUTF();
					MemberActivity.team_list = vec_teamlist_arr(read);
				}
				break;
			// 실시간 통신
			case 4:
				Log.e("ccc", "case 11");
				// 이번주 시간표 신청/취소
				// 팀 이름, 인덱스, 상태 전송
				send_File.writeUTF(MainActivity.team_name);
				send_File.writeInt(socket_num);
				send_File.writeUTF(socket_str);
				break;
			case 5:
				Log.e("ccc", "case 12");
				// 다음주 시간표 신청/취소
				// 팀 이름, 인덱스, 상태 전송
				send_File.writeUTF(MainActivity.team_name);
				send_File.writeInt(socket_num);
				send_File.writeUTF(socket_str);
				break;
			case 6:
				Log.e("ccc", "case 13");
				// 스터디 신청(팀원 추가)
				// 팀이름, 비밀번호, 스터디 종류, 과목명, 기부 동의여부, OT 참석 인원 전송, (학번, 전공, 이름,
				// 전화번호, 메일)
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
				// 배치도 현황 변경
				// 상태 바꿀 방 번호 전송
				send_File.writeUTF(SeatingChartFragment.map_num + "");
				send_File.flush();
				break;
			case 8:
				Log.e("ccc", "case 15");
				// CIS 팀목록
				read = get_File.readUTF();
				LoginActivity.CIS_team_list = strarr(read);
				break;
			case 9:
				Log.e("ccc", "case 16");
				// 실시간 새로고침

				// 배치도 실시간 현황
				// String[]로 가져옴
				read = get_File.readUTF();
				Log.e("ccc", "case 1 " + read);

				// TwoFragment String 배열에 t/f 저장
				SeatingChartFragment.map = strarr(read);

				Log.e("ccc", "case 3");
				// 다음주 예약 현황 (count)
				// int[]로 가져옴
				read = get_File.readUTF();
				Log.e("ccc", "case 3 2 " + read);

				TimetableFragment.next_timetable_count = intarr(read);

				break;
			case 10:
				Log.e("ccc", "case 17");
				// 신청하기 사용가능한 팀명 확인
				// 사용하려는 팀명 전송
				// 사용가능하면 Y, 불가능하면 N
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
				// 이번주 예약 현황카운터
				send_File.writeInt(socket_num);
				read = get_File.readUTF();
				TimetableFragment.isTrue = read;
				break;
			case 12:
				Log.e("ccc", "case 19");
				// 다음주 예약 가능 여부
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