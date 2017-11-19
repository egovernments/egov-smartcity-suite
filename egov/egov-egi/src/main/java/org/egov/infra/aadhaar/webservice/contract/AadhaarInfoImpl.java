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

package org.egov.infra.aadhaar.webservice.contract;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getAadhaarInfoResponse", namespace = "http://srdhuidinfoservices/ecentric/com/xsd")
@XmlAccessorType(XmlAccessType.FIELD)
public class AadhaarInfoImpl extends AadhaarInfo {

    private static final long serialVersionUID = 6091570091083799419L;

    @XmlElement(name = "return")
    private Return returns;

    @Override
    public String getName() {
        return name = returns.name;
    }

    @Override
    public String getPhone() {
        return phone = returns.phoneNo.equals("101") ? "" : returns.phoneNo;
    }

    @Override
    public String getDob() {
        return dob = returns.dob;
    }

    @Override
    public String getGender() {
        return gender = returns.gender;
    }

    @Override
    public String getCareof() {
        return careof = returns.careof.equals("101") ? "" : returns.careof;
    }

    @Override
    public String getUid() {
        return uid = returns.uid.equals("101") ? "" : returns.uid;
    }

    @Override
    public String getEid() {
        return eid = returns.eid.equals("101") ? "" : returns.eid;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Return {
        @XmlElement
        private String buildingName;
        @XmlElement
        private String careof;
        @XmlElement
        private String district;
        @XmlElement
        private String district_name;
        @XmlElement
        private String dob;
        @XmlElement
        private String eid;
        @XmlElement
        private String gender;
        @XmlElement
        private String mandal;
        @XmlElement
        private String mandal_name;
        @XmlElement
        private String name;
        @XmlElement
        private String phoneNo;
        @XmlElement
        private String pincode;
        @XmlElement
        private String srdhwstxn;
        @XmlElement
        private String statecode;
        @XmlElement
        private String status;
        @XmlElement
        private String street;
        @XmlElement
        private String uid;
        @XmlElement
        private String village;
        @XmlElement
        private String village_name;
    }
}
