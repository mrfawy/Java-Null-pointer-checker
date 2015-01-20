package com.mrfawy.npc.populate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.mrfawy.npc.common.FunctorIF;
import com.mrfawy.npc.traverse.ClassUtil;

public class DefaultValueFunctor implements FunctorIF {

	// String will be the fieldName;
	private static final String STRING_VALUE = "value";

	private static final Integer INTEGER_VALUE = new Integer(10);
	private static final int INT_VALUE = 10;
	private static final Date DATE_VALUE = new Date(12); // DateUtil.strToDate(STR_DATE_VALUE);
	private static final Double DOUBLE_VALUE = new Double(1.02);
	private static final double DBLE_VALUE = 1.02d;
	private static final Long LONG_VALUE = new Long(2);
	private static final Long LNG_VALUE = 2l;
	private static final Boolean BOOLEAN_VALUE = new Boolean(false);
	private static final boolean BLN_VALUE = false;
	private static final BigDecimal BIGDECIMAL_VALUE = new BigDecimal(12);
	private static final BigInteger BIGINTEGER_VALUE = new BigInteger("100");
	private static final char CHAR_VALUE = 'k';

	protected Map<Class<?>, Object> classValueMap;

	private int cnt;

	public DefaultValueFunctor() {
		init();
	}

	protected void init() {

		Date fixedDate = getDate();

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

	}

	private Date getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dateInString = "07/07/2013";

		try {

			Date date = formatter.parse(dateInString);
			return date;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void visitPrimitiveField(Object o, Field field) {

		try {

			Object o1 = classValueMap.get(field.getType());

			if (field.getType().isEnum()) {
				o1 = getEunmValue(field, o1);
			}

			if (field.getType() == javax.xml.bind.JAXBElement.class) {
				o1 = new JAXBElement<String>(
						new QName(
								"http://www.nationwide.com/schemas/billingandcollectionmgmt/billingXML/AccountAgreementManagement_2",
								"statementDate"), String.class, field.getName());

			}

			o1 = getOverridenDefaultValue(o1, field);

			field.setAccessible(true);
			field.set(o, o1);
			// System.out.println("COUNTER :"+cnt++);

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Object getEunmValue(Field field, Object o1) {
		// Override how to get enum value
		if (field.getType().isEnum()) {
			List enumConstantsList = Arrays.asList(field.getType()
					.getEnumConstants());
			o1 = enumConstantsList.get(0);

			if (field.getType().toString().contains("SeverityType")) {
				for (Object s : enumConstantsList) {
					if (s.toString().contains("SUCCESS")) {
						o1 = s;
					}
				}
			}
		}
		return o1;
	}

	private Object getOverridenDefaultValue(Object defaultValue, Field field) {
		if (field.getType().toString().contains("String")) {
			return field.getName();
		} else {
			return defaultValue;
		}
	}

	public void visitPrimitive(Object o) {

	}

	public void visitNonPrimitive(Object o) {
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

	public void visitNonPrimitiveField(Object o, Field field) {
		CreateFieldInstance(o, field, field.getType());

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
		if (o != null) {

			/*
			 * were handling only List as of now in Collections. Also we only
			 * check for one of the class as we have cheked for class equality
			 * in previous method. SO if code reaches this point both objects
			 * has to be of same class
			 */
			if (List.class.isAssignableFrom(field.getType())) {
				processFieldList(o, field);
			}

		}

	}

	private void processFieldList(Object o, Field field) {
		Object fieldObject = ClassUtil.runGetter(o, field);
		// if List is null create a new empty one
		if (fieldObject == null) {
			CreateFieldInstance(o, field, ArrayList.class);
		}
		fieldObject = ClassUtil.runGetter(o, field);
		if (((List) fieldObject).isEmpty()) {
			try {
				// if List is empty , add a dummy element
				ParameterizedType genericListType = (ParameterizedType) field
						.getGenericType();
				// JAXB is a special element contains another inner
				// parameterized type of String
				if (genericListType.toString().contains("JAXBElement")) {
					ParameterizedType subParameType = (ParameterizedType) genericListType
							.getActualTypeArguments()[0];
					// if the field is intendted to hold date , try to infere
					// and create string representation for date
					String value = field.getName();
					if (field.getName().contains("date")) {
						value = "01-01-2014";
					}
					JAXBElement<String> obj = new JAXBElement<String>(
							new QName(
									"http://www.nationwide.com/schemas/billingandcollectionmgmt/billingXML/AccountAgreementManagement_2",
									"statementDate"), String.class, value);
					((List) fieldObject).add(obj);
				} else if (genericListType.toString().contains(
						"org.w3c.dom.Element")) {
					// TODO: skip this field
					// System.out.println("WARNING : NOT POPULATED: "+field);
				} else {
					Class<?> GenereicListClass = (Class<?>) genericListType
							.getActualTypeArguments()[0];

					// if INTEGER , no default constructor , so get it from or
					// values
					if (ClassUtil.isWrapper(GenereicListClass)) {
						((List) fieldObject).add(classValueMap
								.get(GenereicListClass));
					}
					// if Generic Complex Object , must have default Constructor
					else {

						((List) fieldObject).add(GenereicListClass
								.newInstance());
					}
				}

			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void CreateFieldInstance(Object o, Field field, Class c) {
		Object fieldObject = ClassUtil.runGetter(o, field);
		if (fieldObject == null) {
			field.setAccessible(true);
			try {
				if (!Modifier.isFinal(field.getModifiers())) {
					field.set(o, c.newInstance());
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected Map<Class<?>, Object> getClassValueMap() {
		return classValueMap;
	}

	protected void setClassValueMap(Map<Class<?>, Object> classValueMap) {
		this.classValueMap = classValueMap;
	}

	protected static String getStringValue() {
		return STRING_VALUE;
	}

	protected static Integer getIntegerValue() {
		return INTEGER_VALUE;
	}

	protected static Integer getIntValue() {
		return INT_VALUE;
	}

	protected static Date getDateValue() {
		return DATE_VALUE;
	}

	protected static Double getDoubleValue() {
		return DOUBLE_VALUE;
	}

	protected static double getDbleValue() {
		return DBLE_VALUE;
	}

	protected static Long getLongValue() {
		return LONG_VALUE;
	}

	protected static Long getLngValue() {
		return LNG_VALUE;
	}

	protected static Boolean getBooleanValue() {
		return BOOLEAN_VALUE;
	}

	protected static boolean isBlnValue() {
		return BLN_VALUE;
	}

	protected static BigDecimal getBigdecimalValue() {
		return BIGDECIMAL_VALUE;
	}

	protected static BigInteger getBigintegerValue() {
		return BIGINTEGER_VALUE;
	}

	protected static char getCharValue() {
		return CHAR_VALUE;
	}

}
