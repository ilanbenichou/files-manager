package com.ilanbenichou.filesmanager.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DateHelper {

	private DateHelper() {
	}

	public static Date nowDate() {
		return new Date();
	}

	public static long nowTime() {
		return DateHelper.nowDate().getTime();
	}

	public static String formatYyyyMmDdHhMmSsSss(final Date date) {
		return new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss.SSS").format(date);
	}

	public static String formatYyyyMmDdHhMmSs(final Date date) {
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
	}

	public static String timeToString(final long time) {

		final TimeUnit millisecondsTu = TimeUnit.MILLISECONDS;
		final TimeUnit secondsTu = TimeUnit.SECONDS;
		final TimeUnit minutesTu = TimeUnit.MINUTES;
		final TimeUnit hoursTu = TimeUnit.HOURS;

		final long hours = millisecondsTu.toHours(time);
		final long minutes = millisecondsTu.toMinutes(time - hoursTu.toMillis(hours));
		final long secondes = millisecondsTu.toSeconds(time - hoursTu.toMillis(hours) - minutesTu.toMillis(minutes));
		final long milliseconds = millisecondsTu.toMillis(time - hoursTu.toMillis(hours) - minutesTu.toMillis(minutes) - secondsTu.toMillis(secondes));

		return String.format("%02d:%02d:%02d.%03d", hours, minutes, secondes, milliseconds);

	}

}