package com.mallex2000;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EbesucherService {
	static Logger LOG = LoggerFactory.getLogger(EbesucherService.class);
	static String user = "";
	static String token = "";

	public double readRewardsLastHour(String myUrl) {
		int hour = calculateLastHour();
		String content = callGet(myUrl);
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(content);
			String s = obj.get("" + hour).toString();
			return Double.parseDouble(s);
		} catch (ParseException e) {
			throw new RuntimeException("can not find hour " + hour + " in json", e);
		}
	}

	private int calculateLastHour() {
		Calendar rightNow = Calendar.getInstance();
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
		hour = hour--;
		if (hour < 1) {
			hour = 24;
		}
		return hour;
	}

	public String readJson(String myUrl) {
		String content = callGet(myUrl);
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(content);
			return obj.get("1").toString();
		} catch (ParseException e) {
			throw new RuntimeException("can not find token in json", e);
		}
	}

	private String callGet(String myUrl) {
		URI uri = URI.create(myUrl);
		try {
			URL url = uri.toURL();
			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setRequestProperty("Accept", "application/json");
			urlCon.setRequestProperty("Content-type", "application/json");
			String auth = user + ":" + token;
			byte[] encodedAuth = java.util.Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
			String authHeaderValue = "Basic " + new String(encodedAuth);
			urlCon.setRequestProperty("Authorization", authHeaderValue);
			try {
				InputStream in = new BufferedInputStream(urlCon.getInputStream());
				return isToString(in);
			} finally {
				urlCon.disconnect();
			}
		} catch (IOException e) {
			throw new RuntimeException("can not call url=" + uri, e);
		}
	}

	private String isToString(InputStream inputStream) {
		String text = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
				.collect(Collectors.joining("\n"));
		return text;
	}
}
