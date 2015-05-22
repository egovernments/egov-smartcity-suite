/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.web.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.interceptor.ParameterNameAware;

public abstract class BaseFormAction extends ActionSupport implements ModelDriven<Object>, ParameterAware, SessionAware, Preparable, RequestAware, ParameterNameAware {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	public static final String INDEX = "index";
	public static final String NEW = "new";
	public static final String EDIT = "edit";
	public static final String TRANSACTIONSUCCESS = "transactionsuccess";
	protected PersistenceService persistenceService;
	protected Map<String, Object> request;
	protected Map<String, List> dropdownData = new HashMap<String, List>();
	protected Map<String, Class> relations = new HashMap<String, Class>();
	protected Map<String, String> ordering = new HashMap<String, String>();
	protected Map<String, String[]> parameters;

	protected Map<String, Object> session() {
		return this.session;
	}

	@Override
	public void setSession(final Map<String, Object> session) {
		this.session = session;
	}

	public Map<String, Object> getSession() {
		return this.session;
	}

	public PersistenceService getPersistenceService() {
		return this.persistenceService;
	}

	@Override
	public void prepare() {
		final Map<String, Class> relationships = getRelationships();
		for (final Entry<String, Class> rel : relationships.entrySet()) {
			setRelationship(rel.getKey(), rel.getValue());
		}
	}

	private void setRelationship(final String relationshipName, final Class class1) {
		final String[] ids = this.parameters.get(relationshipName);
		Object relation = null;

		if (ids != null && ids.length > 0) {
			final String id = ids[0];
			if (id != null && id.length() > 0) {
				try {
					relation = getPersistenceService().find("from " + class1.getName() + " where id="+id);
					/*
					if (class1.getMethod("getId").getReturnType().getSimpleName().equals("Long")) {
						relation = getPersistenceService().find("from " + class1.getName() + " where id=?", Long.parseLong(id));
					} else {
						relation = getPersistenceService().find("from " + class1.getName() + " where id=?", Integer.parseInt(id));
					}*/

					setValue(relationshipName, relation);

				} catch (final Exception iae) {
					throw new EGOVRuntimeException("Model class does not have getId method", iae);
				}
			}
		}
	}

	protected void setValue(final String relationshipName, final Object relation) {
		ActionContext.getContext().getValueStack().setValue("model." + relationshipName, relation);
	}

	public Map<String, Class> getRelationships() {
		return this.relations;
	}

	public Map<String, List> getDropdownData() {
		return this.dropdownData;
	}

	@Override
	public void setRequest(final Map<String, Object> request) {
		this.request = request;
	}

	public void setPersistenceService(final PersistenceService service) {
		this.persistenceService = service;
	}

	protected void setupDropdownDataExcluding(final String... excluded) {
		final List<String> excludedRelations = new ArrayList<String>();
		if (excluded != null) {
			for (final String e : excluded) {
				excludedRelations.add(e);
			}
		}
		for (final Entry<String, Class> rel : this.relations.entrySet()) {
			if (!excludedRelations.contains(rel.getKey())) {
				if (!this.ordering.containsKey(rel.getKey())) {
					this.dropdownData.put(rel.getKey() + "List", getPersistenceService().findAllBy("from " + this.relations.get(rel.getKey()).getSimpleName()));
				} else {
					this.dropdownData.put(rel.getKey() + "List", getPersistenceService().findAllBy("from " + this.relations.get(rel.getKey()).getSimpleName() + " order by " + this.ordering.get(rel.getKey())));
				}
			}
		}
	}

	protected void addRelatedEntity(final String name, final Class type) {
		this.relations.put(name, type);
	}

	protected void addRelatedEntity(final String name, final Class type, final String order) {
		this.relations.put(name, type);
		this.ordering.put(name, order);
	}

	protected void addDropdownData(final String name, final List values) {
		this.dropdownData.put(name, values);
	}

	@Override
	public boolean acceptableParameterName(final String paramName) {
		return !this.relations.containsKey(paramName);
	}

	@Override
	public void setParameters(final Map<String, String[]> parameters) {
		this.parameters = parameters;
	}

	public Map<String, String> getOrdering() {
		return this.ordering;
	}

	public String tokenName() {
		return this.getClass().getSimpleName() + UUID.randomUUID();
	}
}
