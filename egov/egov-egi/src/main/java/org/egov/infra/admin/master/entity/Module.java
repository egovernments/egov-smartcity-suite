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
package org.egov.infra.admin.master.entity;

import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.lib.rrbac.model.Action;

@Entity
@Table(name="eg_module")
public class Module extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -632195454827894969L;
    private String name;
    private boolean enabled;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parentModule")
    private Module parentModule;
    private String displayName;
    private Integer orderNumber;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "eg_action", joinColumns = @JoinColumn(name = "id") , inverseJoinColumns = @JoinColumn(name = "module_id") )
    private Set<Action> actions = Collections.emptySet();
    private String contextRoot;

    public String getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(final String contextRoot) {
        this.contextRoot = contextRoot;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Module getParentModule() {
        return parentModule;
    }

    public void setParentModule(final Module parentModule) {
        this.parentModule = parentModule;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final Integer displayOrder) {
        this.orderNumber = displayOrder;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(final Set<Action> actions) {
        this.actions = actions;
    }
}
