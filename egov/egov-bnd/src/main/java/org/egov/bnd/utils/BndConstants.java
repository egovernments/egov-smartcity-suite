/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface BndConstants {

    public static final String NOTSTATED = "Not Stated";
    public static final String HOSPTIAL = "Hospital";
    public static final String MALE = "Male";
    public static final String FEMALE = "FeMale";
    public static final String KNOWN = "known";
    public static final String UNKNOWN = "Unknown";
    public static final String NOTAVAILABLE = "NA";
    public static final String PERMANENTADDRESS = "PERMANENTADDRESS";
    public static final String EVENTADDRESS = "EVENTADDRESS";
    public static final String PRESENTADDRESS = "PRESENTADDRESS";
    public static final String REQUIRED = "Required";
    public static final String INVALID = "Invalid";

    public static final String BIRTHREGISTRATION = "BIRTHREGISTRATION";
    public static final String STILLBIRTHREGISTRATION = "STILLBIRTHREGISTRATION";
    public static final String APPROVED = "Approved";
    public static final String CREATED = "Created";
    public static final String CANCELLED = "Cancelled";
    public static final String CHILDADOPTION = "ADOPTION";
    public static final String USUALADDRESS = "USUALADDRESS";
    public static final String BNDMODULE = "BND";
    public static final String FATHER = "Father";
    public static final String MOTHER = "Mother";
    public static final String OTHER = "Other";
    public static final String FREECERTIFICATE = "Free Certificate";
    public static final String PAIDCERTIFICATE = "Paid Certificate";
    public static final String BIRTH = "BIRTH";
    public static final String STILLBIRTHNUM = "STILLBIRTH";
    public static final String ADOPTIONSUFFIX = "ADPT";
    public static final String STILLBIRTH = "StillBirth";
    public static final String HOSPITALBIRTH = "HOSPITALBIRTH";
    public static final String HOSPITALSTILLBIRTH = "HOSPITALSTILLBIRTH";
    public static final String HOSPITALBIRTHSUFFIX = "HB";
    public static final String HOSPITALSTILLBIRTHSUFFIX = "HS";

    public static final String CORRESPONDINGADDRESS = "CORRESPONDINGADDRESS";
    public static final String REGUNITKEYFORHOSPITAL = "REGUNITKEYFORHOSPITAL";
    public static final String GRACEPERIODKEY = "GRACEPERIOD";
    public static final String NUMBERGENKEY = "NUMBERGENKEY";
    public static final String REGISTRATIONDATE = "REGISTRATIONDATE";
    public static final String UPDATENAME = "UPDATE_NAME";
    public static final String NASTATECONST = "OS";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String PLACETYPEHOSPITAL = "Hospital";
    public static final String PLACETYPEHOUSE = "House";
    public static final String PLACETYPEOTHERS = "Other";
    public static final String PLACETYPENOTSTATED = "Not Stated";
    public static final String DEATHREGISTRATION = "DEATHREGISTRATION";
    public static final String DECEASEDADDRESS = "DECEASEDADDRESS";
    public static final String DEATH = "DEATH";

    public static final String HOSPITALDEATH = "HOSPITALDEATH";
    public static final String HOSPITALDEATHSUFFIX = "HD";
    public static final String HUSBAND = "Husband";

    // for search screen
    public static final String SEARCHBIRTH = "Birth";
    public static final String SEARCHDEATH = "Death";
    public static final String SEARCHNONAVAILABILITY = "NonAvailability";
    public static final String SEARCHSTILLBIRTH = "Still Birth";
    // Actions to be performed after search
    public static final String VIEW = "View";
    public static final String UPDATE = "Update";
    public static final String ADOPTION = "Child Adoption";
    public static final String SIDELETTER = "Generate Side Letter";
    public static final String CERTIFICATEGENERATIONFORBIRTH = "Collect Fee for Name Inclusion/Certificate Generation";
    public static final String CERTIFICATEGENERATION = "Generate Certificate";
    public static final String LOCKRECORD = "Lock Record";
    public static final String UNLOCKRECORD = "Unlock Record";
    public static final String NAMEINCLUSION = "Name Inclusion";
    public static final String LOCK = "Locked";
    public static final String MODEUNLOCK = "unlock";
    public static final String NAMEINCLUSIONCOMPLETED = "Completed";
    public static final String NAMEINCLUSIONPENDING = "Pending";
    public static final String KEYFORCERTIFICATECOST = "CERTIFICATEFEE";

    public static final String STATENAME = "STATE_NAME";
    public static final String BND_ROLE = "BND_ROLE";
    public static final String QUERY_GETUSERS = "getAllUsersWhoBelongToBnD";
    public static final String QUERY_GETROLES = "getOnlyBnDRolesOfUser";

    // for workflow
    public static final String SCRIPT_SAVE = "save";

    public static final String BIRTHREGISTRATIONMODULE = "Birth";
    public static final String STILLBIRTHREGISTRATIONMODULE = "StilBirth";
    public static final String DEATHREGISTRATIONMODULE = "Death";

    public static final String BIRTHREGISTRATION_TEMPLATE = "BirthCertificate";
    public static final String NONAVLREGN_TEMPLATE = "NACertificate";
    public static final String DEATHREGISTRATION_TEMPLATE = "DeathCertificate";

    public static final String CITYIMAGENAME = "chennai.jpg";
    public static final String HOSPITALUSER = "HOSPITALUSER";
    public static final String BIRTHVIEWURL = "URL_BIRTH_REPORT";
    public static final String DEATHVIEWURL = "URL_DEATH_REPORT";
    public static final String DEFAULTLOGONAME = "india.png";

    public static final List<String> CHILDRENLIST = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", NOTSTATED);
    public static final List<String> PREGNANTDURATIONLIST = Arrays.asList("28", "29", "30", "31", "32", "33", "34",
            "35", "36", "37", "38", "39", "40", "41", "42", NOTSTATED);
    public static final List<String> MOTHERAGELIST = Arrays.asList("11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36",
            "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
            "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72",
            "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90",
            "91", "92", "93", "94", "95", "96", "97", "98", "99", NOTSTATED);

    public final Map<Integer, String> CHILDRENMAP = new TreeMap<Integer, String>() {
        /**
         *
         */
        private static final long serialVersionUID = -6453897449406782463L;

        {
            put(1, "1");
            put(2, "2");
            put(3, "3");
            put(4, "4");
            put(5, "5");
            put(6, "6");
            put(7, "7");
            put(8, "8");
            put(9, "9");
            put(10, "10");
            put(11, "11");
            put(12, "12");
            put(13, "13");
            put(0, NOTSTATED);

        }
    };

    public static final Map<Integer, String> PREGNANTDURATIONMAP = new TreeMap<Integer, String>() {
        /**
         *
         */
        private static final long serialVersionUID = 3089221108441256673L;

        {
            for (final String key : PREGNANTDURATIONLIST)
                if (key.equals(NOTSTATED))
                    put(0, key);
                else
                    put(Integer.valueOf(key), key);
        }
    };

    public static final Map<Integer, String> MOTHERAGEMAP = new TreeMap<Integer, String>() {
        /**
         *
         */
        private static final long serialVersionUID = 767772492765320730L;

        {
            for (final String key : MOTHERAGELIST)
                if (key.equals(NOTSTATED))
                    put(0, key);
                else
                    put(Integer.valueOf(key), key);
        }
    };

    public static final Map<Integer, String> OPTIONMAP = new HashMap<Integer, String>() {
        /**
         *
         */
        private static final long serialVersionUID = -3932469622626148596L;

        {
            put(0, UNKNOWN);
            put(1, KNOWN);
        }
    };

    public static final List<String> INFORMANTRELATIONLIST = Arrays.asList(FATHER, MOTHER, OTHER);

    public static final Map<Integer, String> DECISIONNMAP = new HashMap<Integer, String>() {
        /**
         *
         */
        private static final long serialVersionUID = -3716954529822054615L;

        {
            put(0, YES);
            put(1, NO);
            put(2, NOTSTATED);
        }
    };

    // for search screen
    public static final List<String> REGISTRATIONTYPELIST = Arrays.asList(SEARCHBIRTH, SEARCHDEATH, SEARCHSTILLBIRTH);
    public static final List<String> REGISTRATIONTYPELISTFORRECEIPT = Arrays.asList(SEARCHBIRTH, SEARCHDEATH,
            SEARCHNONAVAILABILITY);

    public static final List<String> SEARCHBIRTHDROPDOWNLIST = Arrays.asList(VIEW, UPDATE, ADOPTION, SIDELETTER,
            CERTIFICATEGENERATIONFORBIRTH, LOCKRECORD, NAMEINCLUSION);
    public static final List<String> SEARCHDEATHDROPDOWNLIST = Arrays.asList(VIEW, UPDATE, CERTIFICATEGENERATION,
            LOCKRECORD);
    public static final List<String> SEARCHSTILLBIRTHDROPDOWNLIST = Arrays.asList(VIEW, UPDATE, LOCKRECORD);

    public static final String FEECOLLECTION = "Fee Collection";
    public static final String BNDMODULENAME = "Bnd";
    public static final String FUNCTIONARY_CODE = "BND_FUNCTIONARY_CODE";
    public static final String FUND_SOURCE_CODE = "BND_FUND_SOURCE_CODE";
    public static final String DEPARTMENT_CODE = "BND_DEPARTMENT_CODE";
    public static final String FUND_CODE = "BND_FUND_CODE";
    public static final String FUNCTION_CODE = "BND_FUNCTION_CODE";
    public static final String SERVICE_CODE = "BND";
    public static final Map<Integer, String> FormMap = new HashMap<Integer, String>() {
        /**
         *
         */
        private static final long serialVersionUID = -2086698634555517521L;

        {
            put(0, BIRTH);
            put(1, DEATH);
        }
    };
    public static final String REGISTRATIONNUMBER = " Registration Number: ";
    public static final String registrationDate = " Registration Date: ";
    public static final String nameOfTheDeceased = " Name of the Deceased: ";
    public static final String BNDFEECOLLECTIONCREATEDSTATUS = "Created";
    public static final String BNDFEECOLLECTIONCOLLECTEDSTATUS = "Collected";
    public static final String BNDFEECOLLECTIONCANCELLEDSTATUS = "Cancelled";
    public static final String BNDFEECOLLECTIONSTATUS = "BNDFEECOLLECTIONSTATUS";
    public static final String CERTIFICATEFEE = "Certificate Fee";
    public static final String SEARCHFEE = "Search Fee";

    public static final String NAMEINCLUSIONFEE = "NAMEINCLUSIONFEE";
    public static final List<String> TRANSACTIONTYPES = Arrays.asList(FREECERTIFICATE, PAIDCERTIFICATE);
    public static final String ACKNOWLEDGEMENTNUMBERFORMAT = "00000000";
    public static final String NAFORMFEE = "NAFORMFEE";

    /*
     * public final Map<Integer,String> STATUSMAP = new
     * TreeMap<Integer,String>(){ { put(1,"Approved"); put(2,"Cancelled");
     * put(3,"Invalid"); put(4,"Locked"); put(5,"NotApproved"); } };
     */
    public static final List<String> REGISTRATIONREPORTLIST = Arrays.asList(SEARCHBIRTH, SEARCHDEATH);

    public static final Map<String, String> REPORTFORMATMAP = new HashMap<String, String>() {
        /**
         *
         */
        private static final long serialVersionUID = 3398606180308406359L;

        {

            put("BOTH", "Combined Report");
            put("CURRENT", "Within one year of occurence ");
            put("DELAYED", "After one year of occurence");
        }
    };
   
    public static final String BNDCOMMONSERVICE = "bndCommonService";
    
    public final Map<Integer, String> MONTHMAP = new TreeMap<Integer, String>() {
        private static final long serialVersionUID = 4941229249625704832L;

        {
            put(1, "January");
            put(2, "February");
            put(3, "March");
            put(4, "April");
            put(5, "May");
            put(6, "June");
            put(7, "July");
            put(8, "August");
            put(9, "September");
            put(10, "October");
            put(11, "November");
            put(12, "December");
        }
    };
}
