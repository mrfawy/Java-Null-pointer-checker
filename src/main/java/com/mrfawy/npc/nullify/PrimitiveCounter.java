package com.mrfawy.npc.nullify;

import java.util.List;

import com.mrfawy.npc.populator.DefaultValuePopulator;
import com.mrfawy.npc.traverse.ObjectInfo;
import com.mrfawy.npc.traverse.ReflectiveTraverser;

public class PrimitiveCounter<T> extends ReflectiveTraverser<T> {

	private PrimitiveCounterFunctor f;
	private ObjectInfo objInfo = new ObjectInfo();

	public PrimitiveCounter(T object) {
		super(object);

	}

	public PrimitiveCounter(Class<T> objClass) {
		super(objClass);

	}

	@Override
	public T process() {
		f = new PrimitiveCounterFunctor();
		traverse(f);
		objInfo.nonPopulatedFields = f.notPopulatedFieldList;
		objInfo.populatedFieldList = f.populatedFieldList;
		objInfo.nullFields = f.notPopulatedFieldList.size();
		objInfo.notNullFields = f.populatedFieldList.size();
		objInfo.totalFields = f.getTotalFields();
		return getObject();
	}

	/**
	 * Implicitly call defaultValue Population to build the object hirarchy
	 * 
	 * @return
	 */
	public T initProcess() {
		DefaultValuePopulator<T> dp = new DefaultValuePopulator<T>(getObject());
		setObject(dp.initProcess());
		return process();
	}

	public List<String> getPopulatedFieldList() {
		return f.getPopulatedFieldList();
	}

	public List<String> getNotPopulatedFieldList() {
		return f.getNotPopulatedFieldList();
	}

	public int getTotalFields() {
		return f.getTotalFields();
	}

	public ObjectInfo getObjInfo() {
		return objInfo;
	}

	public void setObjInfo(ObjectInfo objInfo) {
		this.objInfo = objInfo;
	}

}