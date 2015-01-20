package com.mrfawy.npc.nullify;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import com.mrfawy.npc.common.FunctorIF;

public class PrimitiveNullifyFunctor implements FunctorIF {
	
	private static final String STRING_VALUE = null;
	private static final Integer INTEGER_VALUE = null;
	private static final int INT_VALUE = 0;
	private static final Date DATE_VALUE = null; // DateUtil.strToDate(STR_DATE_VALUE);
	private static final Double DOUBLE_VALUE = 0d;
	private static final Long LONG_VALUE = 0l;
	private static final Boolean BOOLEAN_VALUE = false;
	private static final BigDecimal BIGDECIMAL_VALUE = null ;
	private static final BigInteger BIGINTEGER_VALUE=null;
	private static final char CHAR_VALUE ='\0' ;
	private static final XMLGregorianCalendar XML_GREGORIAN_CAL=null;
	
	private static final Map<Class<?>,Object> classValueMap ;
	
	static{		
		
		classValueMap = new HashMap<Class<?>, Object>() ;
		classValueMap.put(String.class, STRING_VALUE) ;
		classValueMap.put(Double.class, DOUBLE_VALUE) ;
		classValueMap.put(double.class, DOUBLE_VALUE) ;
		classValueMap.put(Long.class, LONG_VALUE) ;
		classValueMap.put(long.class, LONG_VALUE) ;  
		classValueMap.put(Integer.class,INTEGER_VALUE) ;
		classValueMap.put(int.class, INT_VALUE) ;
		classValueMap.put(Date.class, DATE_VALUE) ;
		classValueMap.put(Boolean.class, BOOLEAN_VALUE) ;
		classValueMap.put(boolean.class, BOOLEAN_VALUE) ;
		classValueMap.put(char.class, CHAR_VALUE) ;
		classValueMap.put(Character.class, CHAR_VALUE) ;
		classValueMap.put(BigDecimal.class, BIGDECIMAL_VALUE) ;
		classValueMap.put(BigInteger.class, BIGINTEGER_VALUE) ;
		classValueMap.put(XMLGregorianCalendar.class, XML_GREGORIAN_CAL) ;
	}

	@Override
	public void visitPrimitiveField(Object o, Field field) {
		
		try {
			field.setAccessible(true);
			field.set(o, classValueMap.get(field.getType()));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	@Override
	public void visitNonPrimitive(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitNonPrimitiveField(Object o, Field field) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitCollectionField(Object o, Field field) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitPrimitive(Object o) {
		o=classValueMap.get(o.getClass());
		
	}

	@Override
	public void visitCollection(Object o) {		
		
	}

	

	

}
