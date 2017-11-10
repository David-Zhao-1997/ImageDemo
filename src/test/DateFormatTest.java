package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatTest {
	public static void main(String args[]){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[EEE LLL dd HH:mm:ss yyyy]", Locale.US);
		Date date = new Date();
		System.out.println(simpleDateFormat.format(date));
	}
}
