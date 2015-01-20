package com.mrfawy.npc.objects;

import java.util.Date;
import java.util.List;

public class TestObjB {
	private int z;
	private String k;
	private Date date = new Date();

	private List<TestObjC> testObjCList;

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<TestObjC> getTestObjCList() {
		return testObjCList;
	}

	public void setTestObjCList(List<TestObjC> testObjCList) {
		this.testObjCList = testObjCList;
	}

}
