# Java-Null-pointer-checker
No more java null pointer exceptions as a result of an input complex object

## Defintions
* primitive Object : A simple object that contains no other object , in java it could be String,Integer,BigDecimal,Date,Double,Boolean,...
* Complex Object : An object which contain any number of properties which could be primitive objects or or a collection ( List for now), and/or another Complex Object ( recursively)

## What's available ?
This utility handles 2 main scenarios
* You have a Complex object and you want to populate with sample values e.g.to be used in you test cases
	* it's quite daunting to go at each level and populate values 
	* we provide options to populate all properties , or only non primitives
* Null pointer exception check
	* Suppose in your code , your input is a complex object , and you start to read some properties in your code from it ( at any level), it's not an easy task to write a test case to check for all different possible combination that this object has a null of some property at any level
	* We provide a class that will generate these combinations for you , to test your code , just call this method to retrieve all combinations , and voila your code is tested 
* prividng statistics :
	* given a complex object , or as a result of a population operation , an object is returned which contains some  statistical info  about it, e.g. number of primitives , null and not null properties .. , at all levels
* Asumption : Complex Objects to operate on are POJOs (default constructor , setters and getters)

## Examples
For more info please check class App

Suppose you have these objects ( complex by difinition)
We will use JSON to print object contents , setter/getters  are omitted 

`
TestObjA {

	private String x;
	private TestObjD testObjD;
	private List<TestObjB> testObjBList;
	private List<TestObjD> testObjDList;
}	
`

`public class TestObjB {
	private int z;
	private String k;
	private Date date = new Date();
}
`

`
public class TestObjC {
	
	private String data;
	
	private List<TestObjD>testObjD;
}
`

`
public class TestObjD {
	
	private String dValue;
	private BigDecimal bigDec;
}
`


