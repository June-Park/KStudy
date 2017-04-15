package School_Calendar;

public class SchoolCalendar extends new_calendar{
	
	int lectureWeek; //개강주
	int todayWeek; //이번주
	int schoolWeek; //개강 후 몇째주
	
	//사용법 : new SchoolCalendar(개강일, 오늘날짜)
	//c.getW();
	
	public SchoolCalendar()
	{}
	
	public SchoolCalendar(String lecture, String today)
	{
		setLecture(lecture);
		setToday(today);
	}
	
	public int getW()
	{
		//System.err.println(todayWeek + "," +lectureWeek);
		schoolWeek = todayWeek - lectureWeek + 1;
		return schoolWeek;
	}
	
	//개강주
	public void setLecture(String date)
	{
		super.set_date(date);
		lectureWeek = super.get_week();
	}
	
	//이번주
	public void setToday(String date)
	{
		super.set_date(date);
		todayWeek = super.get_week();
	}
	
	
	
/*	public int getSchoolWeek(String lecture, String date)
	{
		c.set_date(lecture);
		lectureWeek = c.get_week();
		
		System.err.println(lectureWeek);
		
		c.set_date(date);
		schoolWeek = c.get_week()-lectureWeek+1;
		System.err.println(lectureWeek);
		
		return schoolWeek;
	}*/
}
