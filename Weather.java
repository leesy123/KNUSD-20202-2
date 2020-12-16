package swproject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Weather {

	int pop;
	int sky;
	
	public Weather(int pop, int sky) {
		this.pop = pop;
		this.sky = sky;
	}
	
	public static Weather[] weather() throws IOException, ParseException {
		
		DateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		Date nowDate = new Date();
		String tempDate = sdFormat.format(nowDate);
		
		String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";
		String serviceKey = "C%2BP4bXTNTzO2ZPZh96nrcAERLuDsNQ46UJyhwF81T%2BG2E2yxBTlYFsByXHmhZEgyaVzTu5YvlcaMn0%2BHwFGgRQ%3D%3D";
		String nx = "89";
		String ny = "91";
		String baseDate = tempDate;
		String baseTime = "0500"; //���� 20�÷� ������ �ָ�, ������ 00�ú����� ������ ��.
		String type = "json";
		String numOfRows = "1000";
		
		StringBuilder urlBuilder = new StringBuilder(apiUrl);
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
		urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); // �浵
		urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); // ����
		urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "="+ URLEncoder.encode(baseDate, "UTF-8")); /* ��ȸ�ϰ���� ��¥ */
		urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "="+ URLEncoder.encode(baseTime, "UTF-8")); /* ��ȸ�ϰ���� �ð� AM 02�ú��� 3�ð� ���� */
		urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")); /* Ÿ�� */
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="+ URLEncoder.encode(numOfRows, "UTF-8")); /* �� ������ ��� �� */

		URL url = new URL(urlBuilder.toString());
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");

		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null)
			sb.append(line);
		rd.close();
		conn.disconnect();
		String result = sb.toString();
		
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(result);
		JSONObject parse_response = (JSONObject) obj.get("response");
		JSONObject parse_body = (JSONObject) parse_response.get("body");
		JSONObject parse_items = (JSONObject) parse_body.get("items");
		
		JSONArray parse_item = (JSONArray) parse_items.get("item");
		String category;
		JSONObject weather;
		
		String day = "";
		String time = "";
		
		String firstDate = baseDate;
		String secondDate = nextDay(firstDate);
		String thirdDate = nextDay(secondDate);
		
		Weather[] arr = new Weather[3];
		
		for (int i = 0; i < 3; i++)
			arr[i] = new Weather(0, 0);
		
		arr[0].pop = 0;
		arr[1].pop = 0;
		arr[2].pop = 0;
		
		float skysum = 0;
		float cnt = 0;
		
		for (int i = 0; i < parse_item.size(); i++) {
			weather = (JSONObject) parse_item.get(i);
			Object fcstValue = weather.get("fcstValue");
			Object fcstDate = weather.get("fcstDate");
			Object fcstTime = weather.get("fcstTime");
			
			category = (String)weather.get("category");
			
			if (!day.equals(fcstDate.toString())) {
				day = fcstDate.toString();
			}
			if (!time.equals(fcstTime.toString())) {
				time = fcstTime.toString();
			}
			
			if (day.equals(firstDate)) {
				if (category.equals("SKY")) {
					skysum += Float.parseFloat((String)fcstValue);
					cnt++;
				}
				else if (category.equals("POP")) {
					if (Integer.valueOf((String)fcstValue) >= 60)
						arr[0].pop = 1;
				}
				
				if (fcstTime.equals("2100")) {
					arr[0].sky = Math.round(skysum / cnt);
				}
			} else if (day.equals(secondDate)) {
				if (category.equals("SKY")) {
					if (fcstTime.equals("0000")) {
						skysum = 0;
						cnt = 0;
					}
					
					skysum += Float.parseFloat((String)fcstValue);
					cnt++;
				}
				else if (category.equals("POP")) {
					if (Integer.valueOf((String)fcstValue) >= 60)
						arr[1].pop = 1;
				}
				
				if (fcstTime.equals("2100")) {
					arr[1].sky = Math.round(skysum / cnt);
				}
			} else if (day.equals(thirdDate)) {	
				if (category.equals("SKY")) {
					if (fcstTime.equals("0000")) {
						skysum = 0;
						cnt = 0;
					}
					
					skysum += Float.parseFloat((String)fcstValue);
					cnt++;
				}
				else if (category.equals("POP")) {
					if (Integer.valueOf((String)fcstValue) >= 60)
						arr[2].pop = 1;
				}
				
				if (fcstTime.equals("2100") || i == parse_item.size() - 1) {
					arr[2].sky = Math.round(skysum / cnt);
				}
			}
		}
		
		return arr;
	}

	public static String nextDay(String today) { //���� ���� ���ڿ��� ��ȯ���ִ� �Լ�
		int year = Integer.parseInt(today.substring(0, 4));
		int month = Integer.parseInt(today.substring(4, 6));
		int day = Integer.parseInt(today.substring(6, 8));
		String newstr = "";
		
		if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) && (day == 31)) {
			if (month == 12) {
				year += 1;
				month = 1;
				day = 1;
			} else {
				month += 1;
				day = 1;
			}
		} else if ((month == 4 || month == 6 || month == 9 || month == 11)
				&& (day == 30)) {
			month += 1;
			day = 1;
		} else if ((month == 2) && (day == 29)) {
			month += 1;
			day = 1;
		} else {
			day += 1;
		}
		
		newstr += Integer.toString(year);
		
		if (month < 10)
			newstr += "0" + Integer.toString(month);
		else
			newstr += Integer.toString(month);
		
		if (day < 10)
			newstr += "0" + Integer.toString(day);
		else
			newstr += Integer.toString(day);
		
		return newstr;
	}

}
/*
 * �׸�         �׸��
 * POP      ����Ȯ��
 * PTY      ��������
 * REH      ����
 * SKY      �ϴû���
 * T3H      3�ð� ���
 * UUU      ǳ��(����)
 * VEC      ǳ��
 * VVV      ǳ��(����)
 * WSD      ǳ��
 */

/* ����
https://ming9mon.tistory.com/82
���������� ����: https://data.go.kr/index.do
*/