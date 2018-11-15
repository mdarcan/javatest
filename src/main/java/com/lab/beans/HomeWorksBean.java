package com.lab.beans;

import java.io.Serializable;
import java.util.Date;

public class HomeWorksBean implements Serializable {
	
	private Date hWDt;
	private String hW;
	private String cA;	
	private boolean flgHomeWSend;
	private boolean flgClassASend;

	public HomeWorksBean(Date dt, String homeW, String classA) {
       hWDt = dt;
       hW = homeW;
       cA = classA;
       setFlgHomeWSend(false);
       setFlgClassASend(false);
	}
	
	public void setData(Date dt) {
		hWDt = dt;
	}
	
	public void setHWork(String homeW) {
		hW = homeW;
	}

	public String getHWork() {
		return hW;
	}

	public void setClassActivity(String classA) {
		cA = classA;
	}
	
	public String getClassActivity() {
		return cA;
	}
	
	public Date getData() {
		return hWDt;
	}	
		
	@Override
	public String toString() {
		return new StringBuffer(" Data : ")
				.append(this.hWDt).append(" || Argomento : ")
				.append(this.cA).append(" || Compito : ")
				.append(this.hW).toString();
	}

	public boolean getFlgHomeWSend() {
		return flgHomeWSend;
	}

	public void setFlgHomeWSend(boolean flgHomeWSent) {
		this.flgHomeWSend = flgHomeWSent;
	}

	public boolean getFlgClassASend() {
		return flgClassASend;
	}

	public void setFlgClassASend(boolean flgClassASent) {
		this.flgClassASend = flgClassASent;
	}	

}
