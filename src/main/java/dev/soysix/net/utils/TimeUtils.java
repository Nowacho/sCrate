package dev.soysix.net.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TimeUtils {

	public static long parse(String input) {
		if (input == null || input.isEmpty()) {
			return -1L;
		}
		long result = 0L;
		StringBuilder number = new StringBuilder();
		for (int i = 0; i < input.length(); ++i) {
			char c = input.charAt(i);
			if (Character.isDigit(c)) {
				number.append(c);
			} else {
				String str;
				if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
					result += convert(Integer.parseInt(str), c);
					number = new StringBuilder();
				}
			}
		}
		return result;
	}

	private static long convert(int value, char unit) {
		switch (unit) {
			case 'y': {
				return value * TimeUnit.DAYS.toMillis(365L);
			}
			case 'M': {
				return value * TimeUnit.DAYS.toMillis(30L);
			}
			case 'd': {
				return value * TimeUnit.DAYS.toMillis(1L);
			}
			case 'h': {
				return value * TimeUnit.HOURS.toMillis(1L);
			}
			case 'm': {
				return value * TimeUnit.MINUTES.toMillis(1L);
			}
			case 's': {
				return value * TimeUnit.SECONDS.toMillis(1L);
			}
			default: {
				return -1L;
			}
		}
	}
}
