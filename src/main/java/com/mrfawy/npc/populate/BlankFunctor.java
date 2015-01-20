package com.mrfawy.npc.populate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import com.mrfawy.npc.common.FunctorIF;

public class BlankFunctor implements FunctorIF {

	private static final String STRING_VALUE = "";
	private static final Integer INTEGER_VALUE = null;
	private static final int INT_VALUE = 0;
	private static final Date DATE_VALUE = null; // DateUtil.strToDate(STR_DATE_VALUE);
	private static final Double DOUBLE_VALUE = null;
	private static final double DBLE_VALUE = 0d;
	private static final Long LONG_VALUE = null;
	private static final long LNG_VALUE = 0;
	private static final Boolean BOOLEAN_VALUE = null;
	private static final boolean BLN_VALUE = false;
	private static final BigDecimal BIGDECIMAL_VALUE = null;
	private static final BigInteger BIGINTEGER_VALUE = null;
	private static final char CHAR_VALUE = ' ';
	protected Map<Class<?>, Object> classValueMap;

	public BlankFunctor() {
		init();
	}

	protected void init() {

		classValueMap = new HashMap<Class<?>, Object>();
		classValueMap.put(String.class, STRING_VALUE);
		classValueMap.put(Double.class, DOUBLE_VALUE);
		classValueMap.put(double.class, DBLE_VALUE);
		classValueMap.put(Long.class, LONG_VALUE);
		classValueMap.put(long.class, LNG_VALUE);
		classValueMap.put(Integer.class, INTEGER_VALUE);
		classValueMap.put(int.class, INT_VALUE);
		classValueMap.put(Date.class, DATE_VALUE);
		classValueMap.put(Boolean.class, BOOLEAN_VALUE);
		classValueMap.put(boolean.class, BLN_VALUE);
		classValueMap.put(char.class, CHAR_VALUE);
		classValueMap.put(Character.class, CHAR_VALUE);
		classValueMap.put(BigDecimal.class, BIGDECIMAL_VALUE);
		classValueMap.put(BigInteger.class, BIGINTEGER_VALUE);
		classValueMap.put(XMLGregorianCalendar.class, null);

	}

	
	public void visitPrimitive(Object o) {
		// TODO Auto-generated method stub

	}

	
	public void visitPrimitiveField(Object o, Field field) {
		try {

			Object o1 = classValueMap.get(field.getType());

			if (field.getType().isEnum()) {
				o1 = null;
			}

			if (field.getType() == javax.xml.bind.JAXBElement.class) {
				o1 = new JAXBElement<String>(
						new QName(
								"http://www.nationwide.com/schemas/billingandcollectionmgmt/billingXML/AccountAgreementManagement_2",
								"statementDate"), String.class, "");

			}

			o1 = getOverridenDefaultValue(o1, field);

			field.setAccessible(true);
			field.set(o, o1);

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Object getOverridenDefaultValue(Object defaultValue, Field field) {

		return defaultValue;

	}

	
	public void visitNonPrimitive(Object o) {
		// TODO Auto-generated method stub

	}

	
	public void visitNonPrimitiveField(Object o, Field field) {
		// TODO Auto-generated method stub

	}

	
	public void visitCollection(Object o) {
		if (o == null) {
			try {
				o = o.getClass().newInstance();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	
	public void visitCollectionField(Object o, Field field) {

	}

}
