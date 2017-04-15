package School_Calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class new_calendar {

	String date;
	int year;
	int month;
	int day;
	
	public new_calendar()
	{}
	
	public new_calendar(String date)
	{
		this.date = date;
		setYmd(date);
	}
	
	public void set_date(String date)
	{
		this.date = date;
		setYmd(date);
		
		//System.err.println(date + ", month : " + month + ", day : " + day);
	}
	
	public int get_week()
	{
		return get_yearweek(get_11day(date));
	}
	
	//오늘 날짜를 yyyy-mm-dd로 반환
	public String get_today()
	{
		Calendar c = Calendar.getInstance();
		DateFormat data = new SimpleDateFormat("YYYY-MM-dd");

		return data.format(c.getTime());
	}
	
	//yyyy-mm-dd 형식의 String을 입력하면
	//일:1, 월:2 ... 토:7의 숫자 요일값을 반환
	public int get_11day(String date)
	{
		int weekday = 0;

		int year = Integer.parseInt(date.substring(0, 4));
		int month = 1;
		int day = 1;

		if (month == 1 || month == 2)
			year--;
		month = (month + 9) % 12 + 1;
		int y = year % 100;
		int century = year / 100;
		int week = ((13 * month - 1) / 5 + day + y + y / 4 + century / 4 - 2 * century) % 7;
		if (week < 0)
			week = (week + 7) % 7;

		switch (week) {
		case 0:
			weekday = 1;
			break;
		case 1:
			weekday = 2;
			break;
		case 2:
			weekday = 3;
			break;
		case 3:
			weekday = 4;
			break;
		case 4:
			weekday = 5;
			break;
		case 5:
			weekday = 6;
			break;
		case 6:
			weekday = 7;
			break;

		}

		//System.err.println("올해의 1월 1일 : " + weekday);
		return weekday;
	}
	
	//yyyy-mm-dd 를 입력하면 요일을 숫자로 반환한다.
	public int get_day(String date)
	{
		int weekday = 0;

		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7));
		int day = Integer.parseInt(date.substring(8, 9));

		if (month == 1 || month == 2)
			year--;
		month = (month + 9) % 12 + 1;
		int y = year % 100;
		int century = year / 100;
		int week = ((13 * month - 1) / 5 + day + y + y / 4 + century / 4 - 2 * century) % 7;
		if (week < 0)
			week = (week + 7) % 7;

		switch (week) {
		case 0:
			weekday = 1;
			break;
		case 1:
			weekday = 2;
			break;
		case 2:
			weekday = 3;
			break;
		case 3:
			weekday = 4;
			break;
		case 4:
			weekday = 5;
			break;
		case 5:
			weekday = 6;
			break;
		case 6:
			weekday = 7;
			break;

		}

		//System.err.println("올해의 1월 1일 : " + weekday);
		return weekday;
	}
	
	public int get_yearweek(int year_weekday)
	{
		int yearweek = 0;
		int sum_weekday_data = year_weekday - 2; //1월1일 요일-2
		int sum_monthday_data = 0;
		
		switch(month)
		{
			case 1: sum_monthday_data = 0; break;
			case 2:	sum_monthday_data = 31; break;
			case 3: sum_monthday_data = 59; break;
			case 4: sum_monthday_data = 90; break;
			case 5: sum_monthday_data = 120; break;
			case 6: sum_monthday_data = 151; break;
			case 7: sum_monthday_data = 181; break;
			case 8: sum_monthday_data = 212; break;
			case 9: sum_monthday_data = 243; break;
			case 10: sum_monthday_data = 273; break;
			case 11: sum_monthday_data = 304; break;
			case 12: sum_monthday_data = 334; break;
				
		}
		
		yearweek =  (sum_monthday_data + day + sum_weekday_data)/7 + 1;
		//System.err.println(day + "일, 수요일이니까 " + sum_weekday_data + "달별 : " + sum_monthday_data);
		//System.err.println(date + "는 주주주 : " + yearweek);
		return yearweek;
	}
	
	public void setYmd(String date)
	{
		year = Integer.parseInt(date.substring(0,4));
		month = Integer.parseInt(date.substring(5,7));
		day = Integer.parseInt(date.substring(8,10));
	}
	
}
