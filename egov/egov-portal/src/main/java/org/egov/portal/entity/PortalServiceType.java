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
package org.egov.portal.entity;

import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static org.egov.portal.entity.PortalServiceType.SEQ_EGP_PORTALSERVICE;

@Entity
@Table(name = "EGP_PORTALSERVICE")
@SequenceGenerator(name = SEQ_EGP_PORTALSERVICE, sequenceName = SEQ_EGP_PORTALSERVICE, allocationSize = 1)
public class PortalServiceType extends AbstractAuditable {

    private static final long serialVersionUID = -2699479432753522613L;

    public static final String SEQ_EGP_PORTALSERVICE = "seq_egp_portalservice";

    @Id
    @GeneratedValue(generator = SEQ_EGP_PORTALSERVICE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODULE")
    private Module module;

    @NotNull
    @Column(name = "CODE")
    private String code;

    @NotNull
    @Length(max = 250)
    @Column(name = "NAME")
    private String name;

    @Column(name = "SLA")
    private Integer sla;

    @NotNull
    @Column(name = "URL")
    private String url;

    @Column(name = "ISACTIVE")
    private Boolean isActive;

    @Column(name = "USERSERVICE")
    private Boolean userService;

    @Column(name = "BUSINESSUSERSERVICE")
    private Boolean businessUserService;

    @Column(name = "HELPDOCLINK")
    private String helpDocLink;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSla() {
        return sla;
    }

    public void setSla(Integer sla) {
        this.sla = sla;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getUserService() {
        return userService;
    }

    public void setUserService(Boolean userService) {
        this.userService = userService;
    }

    public Boolean getBusinessUserService() {
        return businessUserService;
    }

    public void setBusinessUserService(Boolean businessUserService) {
        this.businessUserService = businessUserService;
    }

    public String getHelpDocLink() {
        return helpDocLink;
    }

    public void setHelpDocLink(String helpDocLink) {
        this.helpDocLink = helpDocLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
