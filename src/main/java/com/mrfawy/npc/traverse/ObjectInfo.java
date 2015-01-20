package com.mrfawy.npc.traverse;

import java.util.List;

public class ObjectInfo {
	
	public Integer totalFields ;
	public Integer notNullFields ;
	public Integer nullFields ;
	public List<String> populatedFieldList;
	public List<String> nonPopulatedFields;

	public ObjectInfo(){
		totalFields = 0 ;
		notNullFields = 0;
		nullFields = 0;
	}
	
	public ObjectInfo(Integer totalFields, Integer notNullFields,Integer nullFields) {
		this.totalFields = totalFields;
		this.notNullFields = notNullFields;
		this.nullFields = nullFields;
	}

	@Override
	public String toString() {
		return "ObjectInfo [totalFields=" + totalFields + ", notNullFields="
				+ notNullFields + ", nullFields=" + nullFields + "]";
	}

}
