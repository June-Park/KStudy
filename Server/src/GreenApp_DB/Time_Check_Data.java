package GreenApp_DB;

public class Time_Check_Data {

	private String team_name;
	private int study_type;
	private int assigned_time;
	private int week;
	private int week_time;
	private int total_time;
	private int left_time;

	public Time_Check_Data() {
		team_name = "";
		study_type = 0;
		assigned_time = 0;
		week = 0;
		week_time = 0;
		total_time = 0;
		left_time = 0;
	}

	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public int getStudy_type() {
		return study_type;
	}

	public void setStudy_type(int study_type) {
		this.study_type = study_type;
	}

	public int getAssigned_time() {
		return assigned_time;
	}

	public void setAssigned_time(int assigned_time) {
		this.assigned_time = assigned_time;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getWeek_time() {
		return week_time;
	}

	public void setWeek_time(int week_time) {
		this.week_time = week_time;
	}

	public int getTotal_time() {
		return total_time;
	}

	public void setTotal_time(int total_time) {
		this.total_time = total_time;
	}

	public int getLeft_time() {
		return left_time;
	}

	public void setLeft_time(int left_time) {
		this.left_time = assigned_time - total_time;
	}
}