package GreenApp_DB;

public class Reservation_Schedule_Data {

	private String team_name;
	private String[] t = new String[40];
		
	public Reservation_Schedule_Data() //?ƒ?„±?
	{
		team_name = "";
		for(int a=0 ; a<40 ; a++)
		{
			t[a] = "f";
		}
	}
	
	
	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public String getT(int index) {
		return t[index];
	}

	public void setT(int index) {
		t[index] = "t";
	}

}
