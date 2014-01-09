package com.thayer.idsservice.ids.bookingcom.beans;

import java.util.Collection;

public class BookingComResv {
	private String title = "";
	private String reservation_number="";

	private String booking_date="";

	private String hotelnumber_name="";

	private String booker_name="";

	private String e_mail="";
	
	private String cc_type="";
	
	private String cc_number="";
	
	private String cc_name="";
	
	private String cc_cvc="";
	
	private String cc_expiration_date="";
	
	private String dc_issue_number="";
	
	private String dc_start_date="";
	

	private String street="";

	private String city="";

	private String country="";

	private String telephone="";

	private String language="";
	
	private String arriveDate = "";

	private String total_number_of_rooms="";

	private String total_number_of_guests="";

	private String total_costs_all_rooms="";

	private String total_commission="";

	private String guest_remarks="";

	private Collection resvRooms;

	public String getBooker_name() {
		return booker_name;
	}

	public void setBooker_name(String booker_name) {
		this.booker_name = booker_name;
	}

	public String getBooking_date() {
		return booking_date;
	}

	public void setBooking_date(String booking_date) {
		this.booking_date = booking_date;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getE_mail() {
		return e_mail;
	}

	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}

	public String getGuest_remarks() {
		return guest_remarks;
	}

	public void setGuest_remarks(String guest_remarks) {
		this.guest_remarks = guest_remarks;
	}

	public String getHotelnumber_name() {
		return hotelnumber_name;
	}

	public void setHotelnumber_name(String hotelnumber_name) {
		this.hotelnumber_name = hotelnumber_name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getReservation_number() {
		return reservation_number;
	}

	public void setReservation_number(String reservation_number) {
		this.reservation_number = reservation_number;
	}

	public Collection getResvRooms() {
		return resvRooms;
	}

	public void setResvRooms(Collection resvRooms) {
		this.resvRooms = resvRooms;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTotal_commission() {
		return total_commission;
	}

	public void setTotal_commission(String total_commission) {
		this.total_commission = total_commission;
	}

	public String getTotal_costs_all_rooms() {
		return total_costs_all_rooms;
	}

	public void setTotal_costs_all_rooms(String total_costs_all_rooms) {
		this.total_costs_all_rooms = total_costs_all_rooms;
	}

	public String getTotal_number_of_guests() {
		return total_number_of_guests;
	}

	public void setTotal_number_of_guests(String total_number_of_guests) {
		this.total_number_of_guests = total_number_of_guests;
	}

	public String getTotal_number_of_rooms() {
		return total_number_of_rooms;
	}

	public void setTotal_number_of_rooms(String total_number_of_rooms) {
		this.total_number_of_rooms = total_number_of_rooms;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCc_cvc() {
		return cc_cvc;
	}

	public void setCc_cvc(String cc_cvc) {
		this.cc_cvc = cc_cvc;
	}

	public String getCc_expiration_date() {
		return cc_expiration_date;
	}

	public void setCc_expiration_date(String cc_expiration_date) {
		this.cc_expiration_date = cc_expiration_date;
	}

	public String getCc_name() {
		return cc_name;
	}

	public void setCc_name(String cc_name) {
		this.cc_name = cc_name;
	}

	public String getCc_number() {
		return cc_number;
	}

	public void setCc_number(String cc_number) {
		this.cc_number = cc_number;
	}

	public String getCc_type() {
		return cc_type;
	}

	public void setCc_type(String cc_type) {
		this.cc_type = cc_type;
	}

	public String getDc_issue_number() {
		return dc_issue_number;
	}

	public void setDc_issue_number(String dc_issue_number) {
		this.dc_issue_number = dc_issue_number;
	}

	public String getDc_start_date() {
		return dc_start_date;
	}

	public void setDc_start_date(String dc_start_date) {
		this.dc_start_date = dc_start_date;
	}

	public String getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}
}
