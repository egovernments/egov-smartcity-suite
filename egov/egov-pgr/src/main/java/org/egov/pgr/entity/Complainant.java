/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.pgr.entity;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.validation.regex.Constants;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import static org.egov.pgr.entity.Complainant.SEQ_COMPLAINANT;

@Entity
@Table(name = "egpgr_complainant")
@SequenceGenerator(name = SEQ_COMPLAINANT, sequenceName = SEQ_COMPLAINANT, allocationSize = 1)
public class Complainant extends AbstractPersistable<Long> {

    public static final String SEQ_COMPLAINANT = "SEQ_EGPGR_COMPLAINANT";
    private static final long serialVersionUID = 5691022600220045218L;
    @Id
    @GeneratedValue(generator = SEQ_COMPLAINANT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Length(max = 150)
    @SafeHtml
    private String name;

    @Length(max = 20)
    @SafeHtml
    @Pattern(regexp = Constants.MOBILE_NUM)
    private String mobile;

    @Length(max = 100)
    @SafeHtml
    @Email(regexp = Constants.EMAIL)
    private String email;

    @ManyToOne
    @JoinColumn(name = "userDetail", nullable = true)
    private User userDetail;

    @Length(max = 256)
    @SafeHtml
    private String address;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(final User userDetail) {
        this.userDetail = userDetail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

}
