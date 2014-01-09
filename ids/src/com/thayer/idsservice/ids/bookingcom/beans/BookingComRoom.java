package com.thayer.idsservice.ids.bookingcom.beans;

public class BookingComRoom {
	private String order;
	private String guest_name;

	private String room_type;

	private String number_of_persons;

	private String arrival;

	private String departure;

	private String number_of_nights;

	private String total_costs;

	private String costs_per_night;

	private String status;

	private String smoking_preference;

	private String guest_remarks;

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public String getCosts_per_night() {
		return costs_per_night;
	}

	public void setCosts_per_night(String costs_per_night) {
		this.costs_per_night = costs_per_night;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getGuest_name() {
		return guest_name;
	}

	public void setGuest_name(String guest_name) {
		this.guest_name = guest_name;
	}

	public String getGuest_remarks() {
		return guest_remarks;
	}

	public void setGuest_remarks(String guest_remarks) {
		this.guest_remarks = guest_remarks;
	}

	public String getNumber_of_nights() {
		return number_of_nights;
	}

	public void setNumber_of_nights(String number_of_nights) {
		this.number_of_nights = number_of_nights;
	}

	public String getNumber_of_persons() {
		return number_of_persons;
	}

	public void setNumber_of_persons(String number_of_persons) {
		this.number_of_persons = number_of_persons;
	}

	public String getRoom_type() {
		return room_type;
	}

	public void setRoom_type(String room_type) {
		this.room_type = room_type;
	}

	public String getSmoking_preference() {
		return smoking_preference;
	}

	public void setSmoking_preference(String smoking_preference) {
		this.smoking_preference = smoking_preference;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotal_costs() {
		return total_costs;
	}

	public void setTotal_costs(String total_costs) {
		this.total_costs = total_costs;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
