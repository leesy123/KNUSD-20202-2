package practical.user;

public class user {
	public String name;
	int year;
	int month;
	int day;
	public static String secretNum;	//해싱된 비밀번호가 저장된다.

	public void input(String name, int year, int month, int day) {
		this.name = name;
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public void save() {

	}
}