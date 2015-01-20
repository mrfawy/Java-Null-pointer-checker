package com.mrfawy.npc.nullify;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.mrfawy.npc.common.FunctorIF;

public class PrimitiveCounterFunctor implements FunctorIF {

	public List<String> populatedFieldList = new ArrayList<String>();
	public List<String> notPopulatedFieldList = new ArrayList<String>();
	private int totalFields = 0;

	@Override
	public void visitPrimitive(Object o) {

	}

	@Override
	public void visitPrimitiveField(Object o, Field field) {
		totalFields++;
		field.setAccessible(true);
		Object fieldObject;
		try {
			fieldObject = field.get(o);
			if (fieldObject != null || field.getType().equals(boolean.class)) {
				populatedFieldList.add(field.getName());

			} else {
				notPopulatedFieldList.add(field.getName());

			}
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
	public void visitCollection(Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitCollectionField(Object o, Field field) {
		// TODO Auto-generated method stub

	}

	public List<String> getPopulatedFieldList() {
		return populatedFieldList;
	}

	public List<String> getNotPopulatedFieldList() {
		return notPopulatedFieldList;
	}

	public int getTotalFields() {
		return totalFields;
	}

}
