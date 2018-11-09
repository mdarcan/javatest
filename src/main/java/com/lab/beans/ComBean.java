package com.lab.beans;

import java.io.Serializable;
import java.util.Date;

public class ComBean implements Serializable {
	
	private Date data;
	private String comu;

	public ComBean(Date dt, String com) {
       data = dt;
       comu = com;
	}
	
	public void setData(Date dt) {
		data = dt;
	}
	
	public void setCom(String com) {
		comu = com;
	}
	
	public String getCom() {
		return comu;
	}
	
	public Date getData() {
		return data;
	}	
		
	@Override
	public String toString() {
		return new StringBuffer(" Data : ")
				.append(this.data).append(" || Comunicazione : ")
				.append(this.comu).toString();
	}	

}
