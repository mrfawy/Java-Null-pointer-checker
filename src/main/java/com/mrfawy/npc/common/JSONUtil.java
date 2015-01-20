package com.mrfawy.npc.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Use this Utility to print a formatted JSon String representation of an object
 * 
 * @author abdelm2
 * 
 */

public class JSONUtil {

	private static Gson gsonFromatted = null;
	private static Gson gsonNonFormatted = null;
	

	private static Gson getGson(boolean formatted) {
		if (formatted) {
			if (gsonFromatted == null) {
				gsonFromatted = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			}
			return gsonFromatted;
		} else {
			if (gsonNonFormatted == null) {
				gsonNonFormatted = new GsonBuilder().disableHtmlEscaping().create();
			}
			return gsonNonFormatted;
		}

	}

	/**
	 * print the JSON string to console . use this for debugging purposes
	 * 
	 * @param obj
	 */

	public static void printJson(Object obj) {

		System.out.println(JSONUtil.getJsonStringFormatted(obj));
	}

	/**
	 * get the JSON representation for an object
	 * 
	 * @param obj
	 * @param formatted
	 *            if false , then only a wrapped one line will be returned
	 * @return
	 */
	public static String getJsonString(Object obj, boolean formatted) {
		
		return getGson(formatted).toJson(obj);
	}

	public static String getJsonStringFormatted(Object obj) {
		return getJsonString(obj, true);
	}

	public static String getJsonStringNonFormatted(Object obj) {
		return getJsonString(obj, false);
	}

	/**
	 * create an object from JSON string <br>
	 * it uses reflection and and naming conventions no annotations , hence the string should have the same field names<br> 
	 * @param jsonStr the JSON representation of the object to be created 
	 * @param objectClass the class of the object to be returned 
	 * @return
	 */
	public static Object getObjectFromJson(String jsonStr, Class objectClass) {
		try{
			 return getGson(false).fromJson(jsonStr, objectClass);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return null;
	}

}
