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

package org.egov.infra.aadhaar.webservice.contract;

import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class AadhaarInfo implements Serializable {

    private static final long serialVersionUID = 1932252864573711817L;
    protected String name;
    protected String phone;
    protected String dob;
    protected String uid;
    protected String eid;
    protected String gender;
    protected String careof;
    protected boolean valid;

    public abstract String getName();

    public abstract String getPhone();

    public abstract String getDob();

    public abstract String getUid();

    public abstract String getGender();

    public abstract String getCareof();

    public abstract String getEid();

    public boolean getValid() {
        return StringUtils.isNotBlank(getUid()) || StringUtils.isNotBlank(getEid());
    }

    public String toJSON() {
        final Map<String, Object> aadhaarInfo = new HashMap<>();
        aadhaarInfo.put("name", getName());
        aadhaarInfo.put("phone", getPhone());
        aadhaarInfo.put("dob", getDob());
        aadhaarInfo.put("uid", getUid());
        aadhaarInfo.put("careof", getCareof());
        aadhaarInfo.put("eid", getEid());
        aadhaarInfo.put("gender", getGender());
        aadhaarInfo.put("valid", getValid());
        return new GsonBuilder().create().toJson(aadhaarInfo);
    }
}
