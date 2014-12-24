package org.egov.ptis.domain.dao.property;

import java.util.List;

import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;

public interface PropertyStatusValuesDAO extends org.egov.infstr.dao.GenericDAO
{
	public PropertyStatusValues getLatestPropertyStatusValuesByPropertyIdAndCode(String PropertyId,List Code);
	public List<PropertyStatusValues> getParentBasicPropsForChild(BasicProperty basicProperty);
}
