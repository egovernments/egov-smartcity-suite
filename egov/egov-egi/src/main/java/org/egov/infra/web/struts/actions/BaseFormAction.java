/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.web.struts.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.interceptor.ParameterNameAware;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ParentPackage("egov")
public abstract class BaseFormAction extends ActionSupport
        implements ModelDriven<Object>, ParameterAware, SessionAware, Preparable, RequestAware, ParameterNameAware {
    public static final String INDEX = "index";
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String VIEW = "view";
    protected static final Logger LOG = LoggerFactory.getLogger(BaseFormAction.class);
    private static final long serialVersionUID = 1L;
    @Autowired
    @Qualifier("persistenceService")
    protected transient PersistenceService persistenceService;
    protected transient Map<String, Object> request;
    protected transient Map<String, List> dropdownData = new HashMap<>();
    protected transient Map<String, Class> relations = new HashMap<>();
    protected transient Map<String, String> ordering = new HashMap<>();
    protected transient Map<String, String[]> parameters;
    private transient Map<String, Object> session;

    protected Map<String, Object> session() {
        return session;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(final Map<String, Object> session) {
        this.session = session;
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(final PersistenceService service) {
        persistenceService = service;
    }

    @Override
    public void prepare() {
        final Map<String, Class> relationships = getRelationships();
        for (final Entry<String, Class> rel : relationships.entrySet())
            try {
                setRelationship(rel.getKey(), rel.getValue());
            } catch (IntrospectionException e) {
                throw new ApplicationRuntimeException(String.format("Could not set relationship for key %s of class %s",
                        rel.getKey(), rel.getValue().getSimpleName()), e);
            }
    }

    private void setRelationship(final String relationshipName, final Class class1) throws IntrospectionException {
        final String[] ids = parameters.get(relationshipName);
        if (ids != null && ids.length > 0) {
            final String id = ids[0];
            if (isNotBlank(id) && Long.valueOf(id) > 0) {
                final PropertyDescriptor propDiscriptor = new PropertyDescriptor("id", class1);
                if (class1 != null && "Fund".equals(class1.getSimpleName()))
                    setValue(relationshipName, getPersistenceService().load(Integer.valueOf(id), class1));
                else if (propDiscriptor.getPropertyType().isAssignableFrom(Long.class))
                    setValue(relationshipName, getPersistenceService().getSession().get(class1, Long.valueOf(id)));
                else
                    setValue(relationshipName, getPersistenceService().load(Integer.valueOf(id), class1));
            }

        }
    }

    protected void setValue(final String relationshipName, final Object relation) {
        ActionContext.getContext().getValueStack().setValue("model." + relationshipName, relation);
    }

    public Map<String, Class> getRelationships() {
        return relations;
    }

    public Map<String, List> getDropdownData() {
        return dropdownData;
    }

    @Override
    public void setRequest(final Map<String, Object> request) {
        this.request = request;
    }

    protected void setupDropdownDataExcluding(final String... excluded) {
        final List<String> excludedRelations = new ArrayList<>();
        if (excluded != null)
            for (final String e : excluded)
                excludedRelations.add(e);
        for (final Entry<String, Class> rel : relations.entrySet())
            if (!excludedRelations.contains(rel.getKey()))
                if (!ordering.containsKey(rel.getKey()))
                    dropdownData.put(rel.getKey() + "List",
                            getPersistenceService().findAllBy("from " + relations.get(rel.getKey()).getSimpleName()));
                else
                    dropdownData.put(rel.getKey() + "List", getPersistenceService().findAllBy(
                            "from " + relations.get(rel.getKey()).getSimpleName() + " order by " + ordering.get(rel.getKey())));
    }

    protected void addRelatedEntity(final String name, final Class type) {
        relations.put(name, type);
    }

    protected void addRelatedEntity(final String name, final Class type, final String order) {
        relations.put(name, type);
        ordering.put(name, order);
    }

    protected void addDropdownData(final String name, final List values) {
        dropdownData.put(name, values);
    }

    @Override
    public boolean acceptableParameterName(final String paramName) {
        return !relations.containsKey(paramName);
    }

    @Override
    public void setParameters(Map<String, String[]> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getOrdering() {
        return ordering;
    }

    public String tokenName() {
        return this.getClass().getSimpleName() + UUID.randomUUID();
    }
}
