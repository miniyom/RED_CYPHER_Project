package com.cyphers.game.RecordSearch.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApiDate {
	//매칭기록 조회시 시간 설정
	private static LocalDateTime now = LocalDateTime.now();
	private static LocalDateTime oneWeeksAgo = now.minusWeeks(1);
	private static LocalDateTime ninetyDaysAgo = now.minusDays(90);
	private static LocalDateTime halfYearsAgo = ninetyDaysAgo.minusDays(90);
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	public final static String NOW = now.format(formatter); 
	public final static String ONE_WEEKS_AGO = oneWeeksAgo.format(formatter); 
	public final static String NINETY_DAYS_AGO = ninetyDaysAgo.format(formatter); 
	public final static String HALF_YEARS_AGO = halfYearsAgo.format(formatter); 
}
