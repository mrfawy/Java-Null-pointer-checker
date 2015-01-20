package com.mrfawy.npc.nullify;

import java.lang.reflect.Field;

import com.mrfawy.npc.common.FunctorIF;

public class ComplexCountFunctor implements FunctorIF{
	
	private int complexCount=0;
	
	

	public int getComplexCount() {
		return complexCount;
	}

	public void setComplexCount(int complexCount) {
		this.complexCount = complexCount;
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
		complexCount++;
		
	}

	@Override
	public void visitCollection(Object o) {
		
		
	}

	@Override
	public void visitCollectionField(Object o, Field field) {
		complexCount++;
		
	}

	

	

	

}
