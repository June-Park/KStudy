package GreenApp_Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import GreenApp_DB.Member_Data;
import GreenApp_DB.Member_DB;
import GreenApp_DB.Notice_Data;
import GreenApp_DB.Notice_DB;
import GreenApp_DB.Reservation_Schedule_Data;
import GreenApp_DB.Reservation_Schedule_DB;
import GreenApp_DB.Schedule_Data;
import GreenApp_DB.Schedule_DB;
import GreenApp_DB.Seating_Chart_DB;
import GreenApp_DB.Team_List_Data;
import GreenApp_DB.Team_List_DB;
import GreenApp_DB.Time_Check_Data;
import GreenApp_DB.Time_Check_DB;

public class Server_Thread implements Runnable {

	// Socket 통신 변수
	private DataInputStream data_In;
	private DataOutputStream data_Out;
	private Writer writer;
	private int n = 0;

	// Database Table 객체들
	Member_Data member_obj;
	Member_DB member;
	Notice_Data notice_obj;
	Notice_DB notice;
	Reservation_Schedule_Data reservation_schedule_obj;
	Reservation_Schedule_DB reservation_schedule;
	Schedule_Data schedule_obj;
	Schedule_DB schedule;
	Seating_Chart_DB seating_chart;
	Team_List_Data team_list_obj;
	Team_List_DB team_list;
	Time_Check_Data time_check_obj;
	Time_Check_DB time_check;

	// Intro
	// 1.배치도 : getState(). 스트링배열 그대로 반환.
	// 2.공지사항 : getNotice(). 오브젝트벡터를 스트링배열로 전환한 후 전송. 타이틀 컨텐츠 유알엘 날짜
	// 3.다음주 예약현황(1/15): count_res(). int[] 반환. 그대로 전송.
	// 4.디폴트 그래프 데이터 전송 : default_getLastWeek_Aver(),
	// default_getWeekAccumulate_Aver(), default_getWeekAccumulate_Aver(),
	// default_getRecent4Week를 한 배열로 만들어 반환. Int형 배열

	// Login
	// 5.비밀번호 확인 : check_pw(팀이름, 비밀번호). 반환형 String.
	// 6.팀 스터디 종류 : check_study_type(팀이름). 반환형 String.
	// 7.이번주 예약 : getSchedule(팀이름). 반환형 String[]. 그대로 전송.
	// 8.다음주 예약 : getNextSchedule(팀이름). 반환형 String[]. 그대로 전송!
	// 9.그래프 : getLastWeek(), getWeekAver(), getSum(), getRecent4Week(),
	// getRecent4WeekS()를 int형 배열로 합쳐 전송. 이미 있는 배열 합칠 때 조심할 것.
	// 10.팀원정보 : 팀명 넘겨주고 반환받은 오브젝트벡터를 스트링배열 벡터로 전환한 후 전송. 이름 폰번(i) 이메일 학번(i)

	// 11.시간표 신청 (이번주, 다음주) : mod(팀이름, 인덱스, 상태). 팀이름 방35개.
	// 12.시간표 취소 (이번주, 다음주) : 상동
	// 13.팀원 추가(스터디 신청) : insert(obj). 하나하나씩 받은 것 객체에 넣어서 db 넘겨줌
	// 14. 배치도 현황 변경 : state_change(방 번호)

	// Intro
	// 1.배치도 : getState(). 스트링배열 그대로 반환.
	// private String[] strarr_seating_chart;
	// 2.공지사항 : getNotice(). 오브젝트벡터를 스트링배열로 전환한 후 전송. 타이틀 컨텐츠 유알엘 날짜
	private Vector<String[]> vec_str_arr;
	// 3.다음주 예약현황(1/15): count_res(). int[] 반환. 그대로 전송.
	// private int[] intarr_count_res;
	// 4.디폴트 그래프 데이터 전송 : default_getLastWeek_Aver(),
	// default_getWeekAccumulate_Aver(), default_getWeekAccumulate_Aver(),
	// default_getRecent4Week를 한 배열로 만들어 반환. Int형 배열
	private int[] intarr_default_graph;

	// Login
	// 5.비밀번호 확인 : check_pw(팀이름, 비밀번호). 반환형 String.
	// private String str_pw_check;
	// 6.팀 스터디 종류 : check_study_type(팀이름). 반환형 String.
	// private String str_study_type;
	// 7.이번주 예약 : getSchedule(팀이름). 반환형 String[]. 그대로 전송.
	// private String[] strarr_thisSchedule;
	// 8.다음주 예약 : getNextSchedule(팀이름). 반환형 String[]. 그대로 전송!
	// private String[] strarr_nextSchedule;
	// 9.그래프 : getLastWeek(), getWeekAver(), getSum(), getRecent4Week(),
	// getRecent4WeekS()를 int형 배열로 합쳐 전송. 이미 있는 배열 합칠 때 조심할 것.
	private int[] intarr_graph;
	// 10.팀원정보 : 팀명 넘겨주고 반환받은 오브젝트벡터를 스트링배열 벡터로 전환한 후 전송. 이름 폰번(i) 이메일 학번(i)
	private Vector<String[]> vec_member;

	// ///////////////////////생성자 시작
	public Server_Thread(DataInputStream data_In, DataOutputStream data_Out) {

		this.data_In = data_In;
		this.data_Out = data_Out;

		writer = new Writer(data_Out, data_In);

		// /////////////////변수 생성
		// Intro
		// 1.배치도 : getState(). 스트링배열 그대로 반환.
		// strarr_seating_chart = new String[35];
		// 2.공지사항 : getNotice(). 오브젝트벡터를 스트링배열로 전환한 후 전송. 타이틀 컨텐츠 유알엘 날짜
		// 3.다음주 예약현황(1/15): count_res(). int[] 반환. 그대로 전송.
		// intarr_count_res = new int[35];
		// 4.디폴트 그래프 데이터 전송 : default_getLastWeek_Aver(),
		// default_getWeekAccumulate_Aver(), default_getWeekAccumulate_Aver(),
		// default_getRecent4Week를 한 배열로 만들어 반환. Int형 배열
		intarr_default_graph = new int[7];

		// Login
		// 5.비밀번호 확인 : check_pw(팀이름, 비밀번호). 반환형 String.
		// str_pw_check = "";
		// 6.팀 스터디 종류 : check_study_type(팀이름). 반환형 String.
		// str_study_type = "";
		// 7.이번주 예약 : getSchedule(팀이름). 반환형 String[]. 그대로 전송.
		// strarr_thisSchedule = new String[35];
		// 8.다음주 예약 : getNextSchedule(팀이름). 반환형 String[]. 그대로 전송!
		// strarr_nextSchedule = new String[35];
		// 9.그래프 : getLastWeek(), getWeekAver(), getSum(), getRecent4Week(),
		// getRecent4WeekS()를 int형 배열로 합쳐 전송. 이미 있는 배열 합칠 때 조심할 것.
		intarr_graph = new int[11];
		// 10.팀원정보 : 팀명 넘겨주고 반환받은 오브젝트벡터를 스트링배열 벡터로 전환한 후 전송. 이름 폰번(i) 이메일 학번(i)
		vec_member = new Vector<String[]>();

		// /////////////////DB 객체화
		member_obj = new Member_Data();
		member = new Member_DB();
		notice_obj = new Notice_Data();
		notice = new Notice_DB();
		reservation_schedule_obj = new Reservation_Schedule_Data();
		reservation_schedule = new Reservation_Schedule_DB();
		schedule_obj = new Schedule_Data();
		schedule = new Schedule_DB();
		seating_chart = new Seating_Chart_DB();
		team_list_obj = new Team_List_Data();
		team_list = new Team_List_DB();
		time_check_obj = new Time_Check_Data();
		time_check = new Time_Check_DB();
	}

	// /////////////////////Thread Run!
	public void run() {
		try {
			n = data_In.readInt(); // 수행할 작업을 숫자로 받음!
			// System.out.println(n);
			int temp_index;
			String temp_name, temp_pw, temp_state;
			int[] temp_int_arr = new int[4];
			String[] temp_str_arr = new String[4];

			switch (n) {

			// intro
			case 1:
				// 1.배치도 : getState(). 스트링배열 그대로 반환.
				System.err.println("case 1");
				writer.send(seating_chart.getState());

				// 2.공지사항 : getNotice(). 오브젝트벡터를 스트링배열로 전환한 후 전송. 타이틀 컨텐츠 유알엘 날짜
				// System.err.println("case 2 start");
				// Vector<notice_data> temp_vec = notice.getNotice();
				// String[] temp_str = new String[4];
				//
				// for (int a = 0; a < temp_vec.size(); a++) {
				// temp_str[0] = temp_vec.get(a).getTitle();
				// temp_str[1] = temp_vec.get(a).getContent();
				// temp_str[2] = temp_vec.get(a).getUrl();
				// temp_str[3] = temp_vec.get(a).getDate();
				// vec_notice.add(temp_str);
				// }
				// writer.send(vec_notice);

				// 3.다음주 예약현황(1/15): count_res(). int[] 반환. 그대로 전송.
				System.err.println("case 3 start");
				writer.send(reservation_schedule.count_res());

				// 4.디폴트 그래프 데이터 전송 : default_getLastWeek_Aver(1),
				// default_getWeekAccumulate_Aver(1),
				// default_getAccumulate_Aver(1), default_getRecent4Week(4)를 한
				// 배열로 만들어 반환. Int형 배열
				System.err.println("case 4 start");
				temp_int_arr = time_check.default_getRecent4Week();
				intarr_default_graph[0] = time_check.default_getLastWeek_Aver();
				intarr_default_graph[1] = time_check
						.default_getWeekAccumulate_Aver();
				intarr_default_graph[2] = time_check
						.default_getAccumulate_Aver();
				intarr_default_graph[3] = temp_int_arr[0];
				intarr_default_graph[4] = temp_int_arr[1];
				intarr_default_graph[5] = temp_int_arr[2];
				intarr_default_graph[6] = temp_int_arr[3];

				writer.send(intarr_default_graph);
				break;

			// login
			// 5.비밀번호 확인 : check_pw(팀이름, 비밀번호). 반환형 String.
			case 2:
				System.err.println("case 5 start");
				temp_name = data_In.readUTF();
				temp_pw = data_In.readUTF();

				writer.send(team_list.check_pw(temp_name, temp_pw));
				break;

			// 6.팀 스터디 종류 : check_study_type(팀이름). 반환형 String.
			case 3:
				System.err.println("case 6 start");
				temp_name = data_In.readUTF();

				writer.send(team_list.check_study_type(temp_name));

				// 7.이번주 예약 : getSchedule(팀이름). 반환형 String[]. 그대로 전송.
				System.err.println("case 7 start");
				temp_name = data_In.readUTF();

				writer.send(schedule.getSchedule(temp_name));
				System.err.println("case 7 finish");

				// 8.다음주 예약 : getNextSchedule(팀이름). 반환형 String[]. 그대로 전송!
				System.err.println("case 8 start");
				temp_name = data_In.readUTF();

				writer.send(reservation_schedule.getNextSchedule(temp_name));

				// 9.그래프 : getLastWeek(), getWeekAver(), getSum(),
				// getRecent4Week(), getRecent4WeekS()를 int형 배열로 합쳐 전송. 이미 있는 배열
				// 합칠 때 조심할 것.
				System.err.println("case 9 start");
				temp_name = data_In.readUTF();

				intarr_graph[0] = time_check.getLastWeek(temp_name);
				intarr_graph[1] = time_check.getWeekAver(temp_name);
				intarr_graph[2] = time_check.getSum(temp_name);
				System.err.println(intarr_graph[2]);
				temp_int_arr = time_check.getRecent4Week(temp_name);
				intarr_graph[3] = temp_int_arr[0];
				intarr_graph[4] = temp_int_arr[1];
				intarr_graph[5] = temp_int_arr[2];
				intarr_graph[6] = temp_int_arr[3];

				temp_int_arr = time_check.getRecent4WeekS(temp_name);
				intarr_graph[7] = temp_int_arr[0];
				intarr_graph[8] = temp_int_arr[1];
				intarr_graph[9] = temp_int_arr[2];
				intarr_graph[10] = temp_int_arr[3];

				writer.send(intarr_graph);

				// 10.팀원정보 : getMember(팀명). 팀명 넘겨주고 반환받은 오브젝트벡터를 스트링배열 벡터로 전환한 후
				// 전송. 이름 폰번(i) 이메일 학번(i)
				System.err.println("case 10 start");
				temp_name = data_In.readUTF();

				Vector<Member_Data> temp_vec_member = member
						.getMember(temp_name);
				System.err.println("case 10 vector");
				for (int i = 0; i < temp_vec_member.size(); i++) {
					temp_str_arr = new String[4];
					temp_str_arr[0] = temp_vec_member.get(i)
							.getStudent_number() + "";
					temp_str_arr[1] = temp_vec_member.get(i).getName();
					temp_str_arr[2] = temp_vec_member.get(i).getPhone_number();
					temp_str_arr[3] = temp_vec_member.get(i).getEmail_address();

					vec_member.add(temp_str_arr);
				}
				writer.send(vec_member);
				System.err.println("case 10 finish");

				// 모든 팀 리스트를 가져옴. 반환형은 벡터, 제네릭 스트링 배열. [0]은 팀명, [1]은 스터디 타입
				if (temp_name.equals("kbuctl")) {
					vec_str_arr = new Vector<String[]>();
					vec_str_arr = team_list.get_TeamList();
					writer.send(vec_str_arr);
				}
				break;

			// real-time
			// 11.시간표 신청/취소 (이번주) : mod(팀이름, 인덱스, 상태). 팀이름 방35개.
			case 4:
				System.err.println("case 11 start");
				temp_name = data_In.readUTF();
				temp_index = data_In.readInt();
				temp_state = data_In.readUTF();

				schedule.mod(temp_name, temp_index, temp_state);
				break;

			// 12.시간표 신청/취소 (다음주) : 상동
			case 5:
				System.err.println("case 12 start");
				temp_name = data_In.readUTF();
				temp_index = data_In.readInt();
				temp_state = data_In.readUTF();

				reservation_schedule.mod(temp_name, temp_index, temp_state);
				break;

			// 13.팀원 추가(스터디 신청) : insert(obj). 하나하나씩 받은 것 객체에 넣어서 db 넘겨줌
			case 6:
				System.err.println("case 13 start");
				// team_list에 추가
				team_list_obj.setTeam_name(data_In.readUTF());
				team_list_obj.setTeam_pw(data_In.readUTF());
				team_list_obj.setStudy_type(data_In.readUTF());
				team_list_obj.setSubject(data_In.readUTF());
				team_list_obj.setDonation_consent(data_In.readUTF());
				team_list_obj.setOrientation(data_In.readInt());
				team_list.insert(team_list_obj);

				// member에 추가
				vec_member = vec_strarr(data_In.readUTF());
				for (int i = 0; i < vec_member.size(); i++) {
					member_obj.setStudent_number(Integer.parseInt(vec_member
							.get(i)[0]));
					member_obj.setMajor(vec_member.get(i)[1]);
					member_obj.setName(vec_member.get(i)[2]);
					member_obj.setPhone_number(vec_member.get(i)[3]);
					member_obj.setEmail_address(vec_member.get(i)[4]);
					member_obj
							.setCredits(Integer.parseInt(vec_member.get(i)[5]));
					member_obj.setTeam_name(team_list_obj.getTeam_name());

					member.insert(member_obj);
				}

				// schedule, reservation_schedule에 추가
				schedule.insert(schedule_obj);
				reservation_schedule.insert(reservation_schedule_obj);

				// time_check에 추가
				time_check_obj.setTeam_name(team_list_obj.getTeam_name());
				time_check_obj.setStudy_type(Integer.parseInt(team_list_obj
						.getStudy_type()));
				time_check.insert(time_check_obj);

				break;

			// 14. 배치도 현황 변경 : state_change(방 번호)
			case 7:
				System.err.println("case 14 start");
				temp_name = data_In.readUTF();
				seating_chart.state_change(temp_name);
				break;

			// 15. CIS 팀 목록
			case 8:
				System.err.println("case 15 start");
				writer.send(team_list.get_CIS_List("str"));
				break;

			// 16. 실시간 새로고침
			case 9:
				// 1.배치도 : getState(). 스트링배열 그대로 반환.
				System.err.println("case 1");
				writer.send(seating_chart.getState());

				// 3.다음주 예약현황(1/15): count_res(). int[] 반환. 그대로 전송.
				System.err.println("case 3 start");
				writer.send(reservation_schedule.count_res());
				break;

			// 17. ID 중복체크. 사용 가능한 아이디면 "Y", 아니면 "N"
			case 10:
				temp_name = data_In.readUTF();
				writer.send(team_list.check_id(temp_name));
				break;

			// 이번주 예약의 방이 갯수 반환.
			case 11:
				temp_index = data_In.readInt();
				writer.send(schedule.count_room(temp_index));
				break;

			// 다음주 예약의 방이 10개 미만인지 판단. 방 index를 받아 예약 가능한 방이면 "t", 아니면 "f"를 반환.
			case 12:
				temp_index = data_In.readInt();
				writer.send(reservation_schedule.count_room(temp_index));
				break;

			default:
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Vector<String[]> vec_strarr(String str) {
		StringTokenizer tokenizer = new StringTokenizer(str, "////");
		// StringTokenizer temp_t;
		String[] temp;
		Vector<String[]> temp_vec = new Vector<String[]>();

		while (tokenizer.hasMoreElements()) {
			temp = new String[6];
			// temp_t = new StringTokenizer(tokenizer.nextToken(), "//");
			for (int a = 0; a < temp.length; a++) {
				temp[a] = tokenizer.nextToken();
			}
			temp_vec.add(temp);
		}
		return temp_vec;
	}
}