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

package org.egov.infra.admin.common.entity;

import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Locale;
import java.util.Objects;

import static org.egov.infra.admin.common.entity.MessageTemplate.SEQ_MESSAGETEMPLATE;
import static org.egov.infra.validation.constants.ValidationErrorCode.INVALID_ALPHABETS_UNDERSCORE_HYPHEN_SPACE;
import static org.egov.infra.validation.constants.ValidationRegex.ALPHABETS_UNDERSCORE_HYPHEN_SPACE;

@Entity
@Table(name = "eg_messagetemplate")
@Unique(fields = "templateName", enableDfltMsg = true)
@SequenceGenerator(name = SEQ_MESSAGETEMPLATE, sequenceName = SEQ_MESSAGETEMPLATE, allocationSize = 1)
@Cacheable
public class MessageTemplate extends AbstractPersistable<Long> {
    protected static final String SEQ_MESSAGETEMPLATE = "SEQ_EG_MESSAGETEMPLATE";
    private static final long serialVersionUID = 3650406178933970435L;

    @Id
    @GeneratedValue(generator = SEQ_MESSAGETEMPLATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Length(max = 100)
    @SafeHtml
    @Column(updatable = false)
    @Pattern(regexp = ALPHABETS_UNDERSCORE_HYPHEN_SPACE, message = INVALID_ALPHABETS_UNDERSCORE_HYPHEN_SPACE)
    private String templateName;

    @NotBlank
    @SafeHtml
    private String template;

    @NotBlank
    @Length(max = 10)
    @SafeHtml
    private String locale;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(final String template) {
        this.template = template;
    }

    public Locale getLocale() {
        return new Locale(locale);
    }

    public void setLocale(final String locale) {
        this.locale = locale;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof MessageTemplate))
            return false;
        MessageTemplate that = (MessageTemplate) other;
        return Objects.equals(getTemplateName(), that.getTemplateName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTemplateName());
    }
}
