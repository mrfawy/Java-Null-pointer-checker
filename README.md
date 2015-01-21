# Java-Null-pointer-checker
No more java null pointer exceptions as a result of an input complex object

## Definitions:
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
* providing statistics :
	* given a complex object , or as a result of a population operation , an object is returned which contains some  statistical info  about it, e.g. number of primitives , null and not null properties .. , at all levels
* Assumptions : Complex Objects to operate on are POJOs (default constructor , setters and getters)

## Examples
For more info please check class App

Suppose you have these objects ( complex by difinition)
We will use JSON to print object contents , setter/getters  are omitted 

``` Java
public class TestObjA {
	private String x;
	private TestObjD testObjD;
	private List<TestObjB> testObjBList;
	private List<TestObjD> testObjDList;
}	

public class TestObjB {
	private int z;
	private String k;
	private Date date = new Date();
}

public class TestObjC {
	
	private String data;
	
	private List<TestObjD>testObjD;
}

public class TestObjD {
	
	private String dValue;
	private BigDecimal bigDec;
}

```
### Populate Object
Now you want to populate TestObjA with default values ( to be used later in a test case ):

```Java
DefaultValuePopulator<TestObjA> rp = new DefaultValuePopulator<TestObjA>(TestObjA.class);
rp.initProcess();

TestObjA populatedObject=rp.getObject();

JSONUtil.printJson(populatedObject);

```

Here is JSON representation of populated Object 

```Json
{
  "x": "x",
  "testObjD": {
    "dValue": "dValue",
    "bigDec": 12
  },
  "testObjBList": [
    {
      "z": 10,
      "k": "k",
      "date": "Dec 31, 1969 6:00:00 PM",
      "testObjCList": [
        {
          "data": "data",
          "testObjD": [
            {
              "dValue": "dValue",
              "bigDec": 12
            }
          ]
        }
      ]
    }
  ],
  "testObjDList": [
    {
      "dValue": "dValue",
      "bigDec": 12
    }
  ]
}

```
As you can see , object is exploaded at all levels ( creating a new instance for any child object) , created a list and added an element in it , and populated all primitives , integer to 12 , a string propertry to its own name , dates , and so on , all these value can be customized by config , but the idea is clear 

### Null pointer exception testing

In This case we will generate all possible null properties at any level of the complex object

```Java
ComplexNullifyPopulator<TestObjA> cnp = new ComplexNullifyPopulator<TestObjA>(TestObjA.class);
List<TestObjA> perList = cnp.initGetNullPermutations();
//print
JSONUtil.printJson(perList);
```

we will get a list of all possible null combination at any level as follows :
```Json
==========AFTER COMPLEX NULL POPULATION ===========
[
  null,
  {
    "testObjBList": [
      {
        "z": 0,
        "testObjCList": [
          {
            "testObjD": [
              {}
            ]
          }
        ]
      }
    ],
    "testObjDList": [
      {}
    ]
  },
  {
    "testObjD": {},
    "testObjDList": [
      {}
    ]
  },
  {
    "testObjD": {},
    "testObjBList": [
      {
        "z": 0
      }
    ],
    "testObjDList": [
      {}
    ]
  }
]

```

to test your code if it could raise null pointer exception write a test case like this :

```Java
@Test
	public void testInputNull(){		
		ComplexNullifyPopulator<TestObjA>cnp=new ComplexNullifyPopulator<TestObjA>(TestObjA.class);
		List<TestObjA> perList=cnp.initGetNullPermutations();
		
		//For each possible null permutation 
		for(TestObjA myInput : perList){		
				myMethod(myInput);			
		}
		
	}
```

