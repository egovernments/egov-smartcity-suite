package org.egov.ptis.domain.dao.property;

import java.util.ArrayList;
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.hibernate.Query;
import org.hibernate.Session;

public class PropertyStatusValuesHibernateDAO extends GenericHibernateDAO implements PropertyStatusValuesDAO {
	public PropertyStatusValuesHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}

	public PropertyStatusValues getLatestPropertyStatusValuesByPropertyIdAndCode(String PropertyId, List Code) {
		Query qry = getSession().createQuery(
				"from PropertyStatusValues PSV " + "left join fetch PSV.basicProperty BP "
						+ "left join fetch PSV.propertyStatus PS "
						+ "where PSV.isActive ='Y' and BP.upicNo =:PropertyId and PS.statusCode in (:Code) "
						+ "order by PSV.createdDate desc").setMaxResults(1);
		qry.setString("PropertyId", PropertyId);
		qry.setParameterList("Code", Code);
		return (PropertyStatusValues) qry.uniqueResult();
	}

	public List<PropertyStatusValues> getParentBasicPropsForChild(BasicProperty basicProperty) {
		List<PropertyStatusValues> propStatValueList = new ArrayList<PropertyStatusValues>();
		if (basicProperty != null) {
			Query qry = getSession()
					.createQuery(
							"from PropertyStatusValues PSV left join fetch PSV.propertyStatus PS where PSV.basicProperty =:BasicPropertyId and PS.statusCode = 'CREATE' and PSV.isActive='Y' ");
			qry.setString("BasicPropertyId", basicProperty.getId().toString());
			propStatValueList = qry.list();
		}
		return propStatValueList;
	}

}
