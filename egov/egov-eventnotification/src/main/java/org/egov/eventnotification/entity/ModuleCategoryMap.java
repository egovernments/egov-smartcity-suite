/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.eventnotification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractPersistable;

@Entity
@Table(name = "egevntnotification_module_category_map")
@SequenceGenerator(name = ModuleCategoryMap.SEQ_EGEN_MODULE_CATEGORY, sequenceName = ModuleCategoryMap.SEQ_EGEN_MODULE_CATEGORY, allocationSize = 1)
public class ModuleCategoryMap extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 177193060109912140L;

    public static final String SEQ_EGEN_MODULE_CATEGORY = "seq_egevntnotification_module_category_map";

    @Id
    @GeneratedValue(generator = SEQ_EGEN_MODULE_CATEGORY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Valid
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "moduleid")
    @NotNull
    private TemplateModule module;

    @Valid
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryid")
    @NotNull
    private ModuleCategory category;

    @Column(name = "attributes_available")
    private boolean attributesAvailable;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public TemplateModule getModule() {
        return module;
    }

    public void setModule(TemplateModule module) {
        this.module = module;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public void setCategory(ModuleCategory category) {
        this.category = category;
    }

    public boolean isAttributesAvailable() {
        return attributesAvailable;
    }

    public void setAttributesAvailable(boolean attributesAvailable) {
        this.attributesAvailable = attributesAvailable;
    }

}
