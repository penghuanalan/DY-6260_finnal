package com.dayuan.dy_6260chartscanner.entity;

public class Server {

	private int id;
	private String address;
	private String user;
	private String password;
	
	
	public Server() {
		super();
	}


	public Server(int id, String address, String user, String password) {
		super();
		this.id = id;
		this.address = address;
		this.user = user;
		this.password = password;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
