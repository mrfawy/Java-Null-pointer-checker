package com.mrfawy.npc.nullify;

import java.lang.reflect.Field;

import com.mrfawy.npc.common.FunctorIF;

public class NullifyObjectAtIndexFunctor implements FunctorIF{
	private int targetIndex;
	private int indexCounter=0;

	

	public NullifyObjectAtIndexFunctor(int targetIndex) {
		super();
		this.targetIndex = targetIndex;
	}

	@Override
	public void visitPrimitive(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitPrimitiveField(Object o, Field field) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitNonPrimitive(Object o) {
			
	}

	@Override
	public void visitNonPrimitiveField(Object o, Field field) {
		
		if(indexCounter==targetIndex){
			nullifyField(o,field);			
		}
		indexCounter++;	
		
	}

	@Override
	public void visitCollection(Object o) {
		
		
		
	}

	@Override
	public void visitCollectionField(Object o, Field field) {
		
		if(indexCounter==targetIndex){
			nullifyField(o,field);			
		}
		indexCounter++;	
	}
	
	private void nullifyField(Object o, Field field) {
		try {
			field.setAccessible(true);
			field.set(o, null);				
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getTargetIndex() {
		return targetIndex;
	}

	public void setTargetIndex(int targetIndex) {
		this.targetIndex = targetIndex;
	}

	
}
