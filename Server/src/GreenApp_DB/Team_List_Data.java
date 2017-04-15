package GreenApp_DB;

public class Team_List_Data {

	private String team_name;
	private String team_pw;
	private String study_type;
	private String subject;
	private String donation_consent;
	private int orientation;

	public Team_List_Data() {
		team_name = "";
		team_pw = "";
		study_type = "";
		subject = "";
		donation_consent = "";
		orientation = 0;
	}

	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public String getTeam_pw() {
		return team_pw;
	}

	public void setTeam_pw(String team_pw) {
		this.team_pw = team_pw;
	}

	public String getStudy_type() {
		return study_type;
	}

	public String getSubject() {
		return subject;
	}

	public void setStudy_type(String study_type) {
		this.study_type = study_type;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDonation_consent() {
		return donation_consent;
	}

	public void setDonation_consent(String donation_consent) {
		this.donation_consent = donation_consent;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
}