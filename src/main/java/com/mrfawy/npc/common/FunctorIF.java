package com.mrfawy.npc.common;

import java.lang.reflect.Field;

public interface FunctorIF {
	
	void visitPrimitive(Object o);
	void visitPrimitiveField(Object o,Field field);
	
	void visitNonPrimitive(Object o);
	void visitNonPrimitiveField(Object o,Field field);
	
	void visitCollection(Object o);
	void visitCollectionField(Object o,Field field);

}
