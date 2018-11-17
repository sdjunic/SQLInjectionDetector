package main;

import student.*;

public class Main {

	public static void main(String[] args) {
		java.util.Scanner sc = new java.util.Scanner();
		Database database = new Database(db.DB.connection, "Student");
		User u = database.getUser(sc.nextLine());
		database.checkIfUserExists(u);
	}

}
