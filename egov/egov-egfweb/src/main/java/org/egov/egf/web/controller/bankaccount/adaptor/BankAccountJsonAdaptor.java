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

package org.egov.egf.web.controller.bankaccount.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.commons.Bankaccount;

import java.lang.reflect.Type;

public class BankAccountJsonAdaptor implements JsonSerializer<Bankaccount> {
    @Override
    public JsonElement serialize(final Bankaccount bankaccount, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (bankaccount != null) {
            if (bankaccount.getAccountnumber() != null)
                jsonObject.addProperty("accountnumber", bankaccount.getAccountnumber());
            else
                jsonObject.addProperty("accountnumber", "");
            if (bankaccount.getFund() != null && bankaccount.getFund().getName() != null)
                jsonObject.addProperty("fund", bankaccount.getFund().getName());
            else
                jsonObject.addProperty("fund", "");
            if (bankaccount.getBankbranch() != null && bankaccount.getBankbranch().getBank() != null)
                jsonObject.addProperty("bank", bankaccount.getBankbranch().getBank().getName());
            else
                jsonObject.addProperty("bank", "");
            if (bankaccount.getBankbranch() != null && bankaccount.getBankbranch().getBranchname() != null)
                jsonObject.addProperty("bankbranch", bankaccount.getBankbranch().getBranchname());
            else
                jsonObject.addProperty("bankbranch", "");
            if (bankaccount.getChartofaccounts() != null && bankaccount.getChartofaccounts().getGlcode() != null)
                jsonObject.addProperty("glcode", bankaccount.getChartofaccounts().getGlcode());
            else
                jsonObject.addProperty("glcode", "");
            if (bankaccount.getAccounttype() != null)
                jsonObject.addProperty("accounttype", bankaccount.getAccounttype());
            else
                jsonObject.addProperty("accounttype", "");
            if (bankaccount.getPayTo() != null)
                jsonObject.addProperty("payto", bankaccount.getPayTo());
            else
                jsonObject.addProperty("payto", "");
            if (bankaccount.getType() != null)
                jsonObject.addProperty("usagetype", bankaccount.getType().toString());
            else
                jsonObject.addProperty("usagetype", "");
            if (bankaccount.getNarration() != null)
                jsonObject.addProperty("narration", bankaccount.getNarration());
            else
                jsonObject.addProperty("narration", "");
            if (bankaccount.getIsactive() != null)
            {
                if(bankaccount.getIsactive())
                    jsonObject.addProperty("isactive", "Y");
                else
                    jsonObject.addProperty("isactive", "N");
            }
            else
                jsonObject.addProperty("isactive", "");
            jsonObject.addProperty("id", bankaccount.getId());
        }
        return jsonObject;
    }
}