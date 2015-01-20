package com.mrfawy.npc.objects;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

public class TestObjA {

	private String x ;	
	private TestObjD testObjD;
	private List<TestObjB> testObjBList;
	private List<TestObjD> testObjDList;

	public TestObjA() {
		init();
	}

	private void init() {
		/*
		 * TestObjB o1=new TestObjB();
		 * 
		 * TestObjB o2=new TestObjB();
		 * 
		 * testObjs.add(o1); testObjs.add(o2);
		 * 
		 * 
		 * TestObjC c1=new TestObjC();
		 * 
		 * testObjsC.add(c1);
		 */

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
