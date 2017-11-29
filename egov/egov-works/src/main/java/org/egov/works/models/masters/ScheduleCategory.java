/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
package org.egov.works.models.masters;

import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Unique(fields = "code", id = "id", tableName = "EGW_SCHEDULECATEGORY", columnName = "CODE", message = "scheduleCategory.code.isunique")
public class ScheduleCategory extends BaseModel {

    private static final long serialVersionUID = -9168726999209110086L;
    @Length(max = 150, message = "ScheCategory.description.length")
    private String description;
    @Length(max = 15, message = "ScheCategory.code.length")
    @Required(message = "contractor.code.null")
    private String code;
    private ScheduleCategory parent;

    @PersistenceContext
    private EntityManager entityManager;

    public ScheduleCategory() {
    }

    public ScheduleCategory getParent() {
        return parent;
    }

    public void setParent(final ScheduleCategory parent) {
        this.parent = parent;
    }

    @StringLengthFieldValidator(fieldName = "description", message = "ScheCategory.description.length", key = "i18n.key", shortCircuit = true, trim = true, minLength = "1", maxLength = "150")
    @NotEmpty(message = "scheduleCategory.description.not.empty")
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @NotEmpty(message = "scheduleCategory.code.not.empty")
    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public ScheduleCategory(final String code, final String description) {
        super();
        this.code = code;
        this.description = description;
    }

    public String getSearchableData() {
        return getCode() + " " + getDescription() + " " + (parent == null ? "" : parent.getSearchableData());
    }

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();

        if (code == null && description == null)
            return Arrays.asList(new ValidationError("code", "scheduleCategory.code.not.empty"));
        if (description == null)
            return Arrays.asList(new ValidationError("description", "scheduleCategory.description.not.empty"));

        if (validationErrors.isEmpty())
            return null;
        else
            return validationErrors;
    }
}

/*
 * public boolean validateId(final String code) { final Query query = entityManager.createQuery(
 * "(from ScheduleCategory  where code  =  :code)"); query.setParameter("code", code); final List retList = query.getResultList();
 * if (retList != null && !retList.isEmpty()) return false; else return true; } }
 */
