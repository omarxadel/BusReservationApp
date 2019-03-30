package application;
import java.util.*;

public class Manager extends Users {
	public Manager(String fname, String lname, String uname, String pw, int ID, String country, String city) {
		Manager.super.firstname = fname;
		Manager.super.lastname = lname;
		Manager.super.username = uname;
		Manager.super.setPassword(pw);
		Manager.super.ID = ID;
		Manager.super.city = city;
		Manager.super.country = country;
	}
	public Trip addTrip(String s, String f, float t, String v, Date d) {
		Trip trip = new Trip();
		trip.start = s;
		trip.destination = f;
		trip.vehicle = v;
		trip.ticket = t;
		trip.date = d;
		return trip;
	}
}