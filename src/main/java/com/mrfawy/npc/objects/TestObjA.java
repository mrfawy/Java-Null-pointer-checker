package com.mrfawy.npc.objects;

import java.util.List;

public class TestObjA {

	private String x;
	private TestObjD testObjD;
	private List<TestObjB> testObjBList;
	private List<TestObjD> testObjDList;

	public TestObjA() {
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public TestObjD getTestObjD() {
		return testObjD;
	}

	public void setTestObjD(TestObjD testObjD) {
		this.testObjD = testObjD;
	}

	public List<TestObjB> getTestObjBList() {
		return testObjBList;
	}

	public void setTestObjBList(List<TestObjB> testObjBList) {
		this.testObjBList = testObjBList;
	}

	public List<TestObjD> getTestObjDList() {
		return testObjDList;
	}

	public void setTestObjDList(List<TestObjD> testObjDList) {
		this.testObjDList = testObjDList;
	}

}
