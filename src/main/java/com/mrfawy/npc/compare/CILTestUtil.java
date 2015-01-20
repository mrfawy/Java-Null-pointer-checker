package com.mrfawy.npc.compare;

import java.util.ArrayList;
import java.util.List;

public class CILTestUtil {
	
	public static List<String> compareTwoList(List<String> targetList, List<String> testedList)
	
	{
		List<String> deepCopyTargetList = new ArrayList<String>();
		for(String p : targetList)
			deepCopyTargetList.add((String)p);
		
		deepCopyTargetList.removeAll(testedList);
		return  deepCopyTargetList;
	}
	
	public static void main (String args[])
	{
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		list1.add("1");
		list1.add("2");
		list1.add("3");
		list2.add("1");
		list2.add("3");
		list2.add("4");
		System.out.println("Before list1:"+ list1);
		System.out.println("list2:"+ list2);
		System.out.println(compareTwoList(list1,list2));
		System.out.println("After list1:"+ list1);
	}

}
