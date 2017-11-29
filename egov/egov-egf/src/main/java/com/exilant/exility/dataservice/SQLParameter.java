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
package com.exilant.exility.dataservice;

public class SQLParameter {
    // name of the variable in the DataColelction that cintains value for this parameter
    public String dataSource;
    // is this parameter a must?
    // if true: Exility generates an error if the value is not found. You should not put any braces
    // around this parameter. (as there is no question of deleting anything.
    // if false: this field is optional. You must provide braces ({ }) around this parameter.
    // Exility removes these braces after substituting the values if found.
    // If value is not found, the string within braces, including the braces is removed
    public boolean isRequired;
    // if the field is not present in DC, do you want Exility to take a default value? This woudl be helpful
    // in scenarios where the field is optional, but removing text is not possible. You can have a default value.
    public String defaultValue;
    // is the parameter a list (and not a single value) example is for in operator.
    // WHERE customerCode in ['a', 'b'.....]
    // if this is 'true', exility looks for value in ValueList. If it is not found, it checks in Values.
    // In any case, one or more values found are made into a 'list'. List is a,b,c or just a.
    // exampe custoomerCode in [@1] will translate to customerCode in ['a','b','c'] or simply ['a'].
    public boolean isList = false;
    // this is applicable only for list. while making teh list, exility uss this flag to put single quote
    // around the values.
    public boolean listRequiresQuotes = false;

    // Default Constructor
    public SQLParameter() {
        super();
    }

}
