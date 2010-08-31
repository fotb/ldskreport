package com.report.dao;

import java.util.Map;

public interface IUnmodeledDataDAO {
	/**
	 * Branchs and Positions
	 * 
	 * @param metaObjAttrRelationsIdn
	 * @return
	 */
	Map findAllByMetaObjAttrRelationsIdn(String metaObjAttrRelationsIdn);
}
