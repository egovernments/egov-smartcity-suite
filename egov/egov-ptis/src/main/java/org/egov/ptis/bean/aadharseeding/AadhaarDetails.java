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
package org.egov.ptis.bean.aadharseeding;

public class AadhaarDetails {

    public String getReson() {
        return Reson;
    }

    public void setReson(String Reson) {
        this.Reson = Reson;
    }

    protected String Reson;

    public String getCare_of() {
        return care_of;
    }

    public void setCare_of(String care_of) {
        this.care_of = care_of;
    }

    public String getSNO() {
        return SNO;
    }

    public void setSNO(String SNO) {
        this.SNO = SNO;
    }

    protected String UID_NUM;

    public String getUID_NUM() {
        return UID_NUM;
    }

    public void setUID_NUM(String UID_NUM) {
        this.UID_NUM = UID_NUM;
    }

    protected String SNO;
    protected String care_of;
    protected String CITIZEN_NAME;
    protected String DOB_DT;
    protected String DATE_OF_DEATH;
    protected String GENDER;
    protected String CASTE;
    protected String CASTE_CATEGORY;
    protected String CASTE_GROUP;
    protected String CASTE_SYNONYM;
    protected String VOTER_ID;

    public String getAuth_reason() {
        return Auth_reason;
    }

    public void setAuth_reason(String Auth_reason) {
        this.Auth_reason = Auth_reason;
    }

    protected String Auth_reason;
    protected String RATION_ID;
    protected String KISSAN_CARD;
    protected String HOUSEHOLD_ID;
    protected String WARD_NO;
    protected String FLAT_NO;
    protected String FLOOR_NO;
    protected String BUILDING_NAME;
    protected String STREET;
    protected String DISTRICT_CODE;
    protected String DISTRICT_NAME;
    protected String TEHSIL_CODE;
    protected String TEHSIL_NAME;
    protected String VT_CODE;

    protected String VT_NAME;
    protected String PINCODE;
    protected String RURAL_URBAN_FLAG;
    protected String ADDRESS_TYPE;
    protected String MARITAL_STATUS;
    protected String RELATIONSHIP_HOH;
    protected String MOBILE_NUMBER;
    protected String EMAIL_ID;
    protected String EDU_QUALIFICATION;
    protected String SCHOOL_NAME;
    protected String SCHOOL_SECTION;
    protected String EDU_QUAL_STATUS;
    protected String EMPLOYMENT_STATUS;
    protected String OCCUPATION;
    protected String TRAD_FAM_OCCUPATION;
    protected String OCCUPATION_TYPE;
    protected String OCCUPATION_CATEGORY;
    protected String SECONDARY_OCCUP;
    protected String ANNUAL_INCOME;
    protected String RELIGION;
    protected String MOTHER_TONGUE;
    protected String COOKING_FUEL;
    protected String NPCI_LINK_STATUS;
    protected String NPCI_BANK_NAME;
    protected String CH_BEEMA_OPTED;
    protected String CH_BEEMA_BANK_ACC_STATUS;
    protected String CH_BEEMA_BANK_ACC_NUM;
    protected String CH_BEEMA_BANK_NAME;
    protected String CH_BEEMA_BANK_IFSC;
    protected String CH_BEEMA_NOMINEE_AADHAAR;
    protected String PUBLIC_REP_POSITION;
    protected String EKYC_STATUS;
    protected String AVAIL_OF_ISL;
    protected String SOAK_PIT;
    protected String HLTH_HYGIENE_DWELL_AREA;
    protected String HOUSE_CONST_TYPE;
    protected String HOUSE_DRINK_WATER_SRC;
    protected String HOUSE_OWNERSHIP;
    protected String HEALTH_INSURANCE;
    protected String MOVABLE_PROPERTIES;
    protected String UPDATED_ON;
    protected String BUILDING_TSH_NUM;
    protected String DWELLING_TSH_NUM;
    protected String COOKING_FUEL_1;
    protected String ELEC_DEVICES;
    protected String TYPE_DISABILITY;
    protected String RESIDENT_STATUS;
    protected String MEESEVA_ID;
    protected String PENSION_ID;
    protected String NREGS_ID;
    protected String SHG_ID;
    protected String POST_SCHOLORSHIP_ID;
    protected String BPCL_ID;
    protected String IOCL_ID;
    protected String CDMA_PPT_ID;
    protected String EPDCL_ID;
    protected String WEB_LAND_ID;
    protected String PRE_SCHOLORSHIP_ID;
    protected String GOVT_EMP_ID;
    protected String LABOUR_ID;
    protected String RTA_OWNERS_ID;
    protected String RTA_DRIVERS_ID;
    protected String SPDCL_ID;
    protected String HOUSING_ID;
    protected String HPCL_ID;
    protected String SSA_ID;
    protected String RESCO_ID;
    protected String GEO_LAT;
    protected String GEO_LONG;

    public String getVT_NAME() {
        return VT_NAME;
    }

    public void setVT_NAME(String VT_NAME) {
        this.VT_NAME = VT_NAME;
    }

    public String getGEO_LAT() {
        return GEO_LAT;
    }

    public void setGEO_LAT(String GEO_LAT) {
        this.GEO_LAT = GEO_LAT;
    }

    public String getGEO_LONG() {
        return GEO_LONG;
    }

    public void setGEO_LONG(String GEO_LONG) {
        this.GEO_LONG = GEO_LONG;
    }

    protected String base64file;

    public String getBase64file() {
        return base64file;
    }

    public void setBase64file(String base64file) {
        this.base64file = base64file;
    }

    protected String Srdhwstxn;
    protected String status;

    public String getSrdhwstxn() {
        return Srdhwstxn;
    }

    public void setSrdhwstxn(String Srdhwstxn) {
        this.Srdhwstxn = Srdhwstxn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCITIZEN_NAME() {
        return CITIZEN_NAME;
    }

    public void setCITIZEN_NAME(String CITIZEN_NAME) {
        this.CITIZEN_NAME = CITIZEN_NAME;
    }

    public String getDOB_DT() {
        return DOB_DT;
    }

    public void setDOB_DT(String DOB_DT) {
        this.DOB_DT = DOB_DT;
    }

    public String getDATE_OF_DEATH() {
        return DATE_OF_DEATH;
    }

    public void setDATE_OF_DEATH(String DATE_OF_DEATH) {
        this.DATE_OF_DEATH = DATE_OF_DEATH;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getCASTE() {
        return CASTE;
    }

    public void setCASTE(String CASTE) {
        this.CASTE = CASTE;
    }

    public String getCASTE_CATEGORY() {
        return CASTE_CATEGORY;
    }

    public void setCASTE_CATEGORY(String CASTE_CATEGORY) {
        this.CASTE_CATEGORY = CASTE_CATEGORY;
    }

    public String getCASTE_GROUP() {
        return CASTE_GROUP;
    }

    public void setCASTE_GROUP(String CASTE_GROUP) {
        this.CASTE_GROUP = CASTE_GROUP;
    }

    public String getCASTE_SYNONYM() {
        return CASTE_SYNONYM;
    }

    public void setCASTE_SYNONYM(String CASTE_SYNONYM) {
        this.CASTE_SYNONYM = CASTE_SYNONYM;
    }

    public String getVOTER_ID() {
        return VOTER_ID;
    }

    public void setVOTER_ID(String VOTER_ID) {
        this.VOTER_ID = VOTER_ID;
    }

    public String getRATION_ID() {
        return RATION_ID;
    }

    public void setRATION_ID(String RATION_ID) {
        this.RATION_ID = RATION_ID;
    }

    public String getKISSAN_CARD() {
        return KISSAN_CARD;
    }

    public void setKISSAN_CARD(String KISSAN_CARD) {
        this.KISSAN_CARD = KISSAN_CARD;
    }

    public String getHOUSEHOLD_ID() {
        return HOUSEHOLD_ID;
    }

    public void setHOUSEHOLD_ID(String HOUSEHOLD_ID) {
        this.HOUSEHOLD_ID = HOUSEHOLD_ID;
    }

    public String getWARD_NO() {
        return WARD_NO;
    }

    public void setWARD_NO(String WARD_NO) {
        this.WARD_NO = WARD_NO;
    }

    public String getFLAT_NO() {
        return FLAT_NO;
    }

    public void setFLAT_NO(String FLAT_NO) {
        this.FLAT_NO = FLAT_NO;
    }

    public String getFLOOR_NO() {
        return FLOOR_NO;
    }

    public void setFLOOR_NO(String FLOOR_NO) {
        this.FLOOR_NO = FLOOR_NO;
    }

    public String getBUILDING_NAME() {
        return BUILDING_NAME;
    }

    public void setBUILDING_NAME(String BUILDING_NAME) {
        this.BUILDING_NAME = BUILDING_NAME;
    }

    public String getSTREET() {
        return STREET;
    }

    public void setSTREET(String STREET) {
        this.STREET = STREET;
    }

    public String getDISTRICT_CODE() {
        return DISTRICT_CODE;
    }

    public void setDISTRICT_CODE(String DISTRICT_CODE) {
        this.DISTRICT_CODE = DISTRICT_CODE;
    }

    public String getDISTRICT_NAME() {
        return DISTRICT_NAME;
    }

    public void setDISTRICT_NAME(String DISTRICT_NAME) {
        this.DISTRICT_NAME = DISTRICT_NAME;
    }

    public String getTEHSIL_CODE() {
        return TEHSIL_CODE;
    }

    public void setTEHSIL_CODE(String TEHSIL_CODE) {
        this.TEHSIL_CODE = TEHSIL_CODE;
    }

    public String getTEHSIL_NAME() {
        return TEHSIL_NAME;
    }

    public void setTEHSIL_NAME(String TEHSIL_NAME) {
        this.TEHSIL_NAME = TEHSIL_NAME;
    }

    public String getVT_CODE() {
        return VT_CODE;
    }

    public void setVT_CODE(String VT_CODE) {
        this.VT_CODE = VT_CODE;
    }

    public String getPINCODE() {
        return PINCODE;
    }

    public void setPINCODE(String PINCODE) {
        this.PINCODE = PINCODE;
    }

    public String getRURAL_URBAN_FLAG() {
        return RURAL_URBAN_FLAG;
    }

    public void setRURAL_URBAN_FLAG(String RURAL_URBAN_FLAG) {
        this.RURAL_URBAN_FLAG = RURAL_URBAN_FLAG;
    }

    public String getADDRESS_TYPE() {
        return ADDRESS_TYPE;
    }

    public void setADDRESS_TYPE(String ADDRESS_TYPE) {
        this.ADDRESS_TYPE = ADDRESS_TYPE;
    }

    public String getMARITAL_STATUS() {
        return MARITAL_STATUS;
    }

    public void setMARITAL_STATUS(String MARITAL_STATUS) {
        this.MARITAL_STATUS = MARITAL_STATUS;
    }

    public String getRELATIONSHIP_HOH() {
        return RELATIONSHIP_HOH;
    }

    public void setRELATIONSHIP_HOH(String RELATIONSHIP_HOH) {
        this.RELATIONSHIP_HOH = RELATIONSHIP_HOH;
    }

    public String getMOBILE_NUMBER() {
        return MOBILE_NUMBER;
    }

    public void setMOBILE_NUMBER(String MOBILE_NUMBER) {
        this.MOBILE_NUMBER = MOBILE_NUMBER;
    }

    public String getEMAIL_ID() {
        return EMAIL_ID;
    }

    public void setEMAIL_ID(String EMAIL_ID) {
        this.EMAIL_ID = EMAIL_ID;
    }

    public String getEDU_QUALIFICATION() {
        return EDU_QUALIFICATION;
    }

    public void setEDU_QUALIFICATION(String EDU_QUALIFICATION) {
        this.EDU_QUALIFICATION = EDU_QUALIFICATION;
    }

    public String getSCHOOL_NAME() {
        return SCHOOL_NAME;
    }

    public void setSCHOOL_NAME(String SCHOOL_NAME) {
        this.SCHOOL_NAME = SCHOOL_NAME;
    }

    public String getSCHOOL_SECTION() {
        return SCHOOL_SECTION;
    }

    public void setSCHOOL_SECTION(String SCHOOL_SECTION) {
        this.SCHOOL_SECTION = SCHOOL_SECTION;
    }

    public String getEDU_QUAL_STATUS() {
        return EDU_QUAL_STATUS;
    }

    public void setEDU_QUAL_STATUS(String EDU_QUAL_STATUS) {
        this.EDU_QUAL_STATUS = EDU_QUAL_STATUS;
    }

    public String getEMPLOYMENT_STATUS() {
        return EMPLOYMENT_STATUS;
    }

    public void setEMPLOYMENT_STATUS(String EMPLOYMENT_STATUS) {
        this.EMPLOYMENT_STATUS = EMPLOYMENT_STATUS;
    }

    public String getOCCUPATION() {
        return OCCUPATION;
    }

    public void setOCCUPATION(String OCCUPATION) {
        this.OCCUPATION = OCCUPATION;
    }

    public String getTRAD_FAM_OCCUPATION() {
        return TRAD_FAM_OCCUPATION;
    }

    public void setTRAD_FAM_OCCUPATION(String TRAD_FAM_OCCUPATION) {
        this.TRAD_FAM_OCCUPATION = TRAD_FAM_OCCUPATION;
    }

    public String getOCCUPATION_TYPE() {
        return OCCUPATION_TYPE;
    }

    public void setOCCUPATION_TYPE(String OCCUPATION_TYPE) {
        this.OCCUPATION_TYPE = OCCUPATION_TYPE;
    }

    public String getOCCUPATION_CATEGORY() {
        return OCCUPATION_CATEGORY;
    }

    public void setOCCUPATION_CATEGORY(String OCCUPATION_CATEGORY) {
        this.OCCUPATION_CATEGORY = OCCUPATION_CATEGORY;
    }

    public String getSECONDARY_OCCUP() {
        return SECONDARY_OCCUP;
    }

    public void setSECONDARY_OCCUP(String SECONDARY_OCCUP) {
        this.SECONDARY_OCCUP = SECONDARY_OCCUP;
    }

    public String getANNUAL_INCOME() {
        return ANNUAL_INCOME;
    }

    public void setANNUAL_INCOME(String ANNUAL_INCOME) {
        this.ANNUAL_INCOME = ANNUAL_INCOME;
    }

    public String getRELIGION() {
        return RELIGION;
    }

    public void setRELIGION(String RELIGION) {
        this.RELIGION = RELIGION;
    }

    public String getMOTHER_TONGUE() {
        return MOTHER_TONGUE;
    }

    public void setMOTHER_TONGUE(String MOTHER_TONGUE) {
        this.MOTHER_TONGUE = MOTHER_TONGUE;
    }

    public String getCOOKING_FUEL() {
        return COOKING_FUEL;
    }

    public void setCOOKING_FUEL(String COOKING_FUEL) {
        this.COOKING_FUEL = COOKING_FUEL;
    }

    public String getNPCI_LINK_STATUS() {
        return NPCI_LINK_STATUS;
    }

    public void setNPCI_LINK_STATUS(String NPCI_LINK_STATUS) {
        this.NPCI_LINK_STATUS = NPCI_LINK_STATUS;
    }

    public String getNPCI_BANK_NAME() {
        return NPCI_BANK_NAME;
    }

    public void setNPCI_BANK_NAME(String NPCI_BANK_NAME) {
        this.NPCI_BANK_NAME = NPCI_BANK_NAME;
    }

    public String getCH_BEEMA_OPTED() {
        return CH_BEEMA_OPTED;
    }

    public void setCH_BEEMA_OPTED(String CH_BEEMA_OPTED) {
        this.CH_BEEMA_OPTED = CH_BEEMA_OPTED;
    }

    public String getCH_BEEMA_BANK_ACC_STATUS() {
        return CH_BEEMA_BANK_ACC_STATUS;
    }

    public void setCH_BEEMA_BANK_ACC_STATUS(String CH_BEEMA_BANK_ACC_STATUS) {
        this.CH_BEEMA_BANK_ACC_STATUS = CH_BEEMA_BANK_ACC_STATUS;
    }

    public String getCH_BEEMA_BANK_ACC_NUM() {
        return CH_BEEMA_BANK_ACC_NUM;
    }

    public void setCH_BEEMA_BANK_ACC_NUM(String CH_BEEMA_BANK_ACC_NUM) {
        this.CH_BEEMA_BANK_ACC_NUM = CH_BEEMA_BANK_ACC_NUM;
    }

    public String getCH_BEEMA_BANK_NAME() {
        return CH_BEEMA_BANK_NAME;
    }

    public void setCH_BEEMA_BANK_NAME(String CH_BEEMA_BANK_NAME) {
        this.CH_BEEMA_BANK_NAME = CH_BEEMA_BANK_NAME;
    }

    public String getCH_BEEMA_BANK_IFSC() {
        return CH_BEEMA_BANK_IFSC;
    }

    public void setCH_BEEMA_BANK_IFSC(String CH_BEEMA_BANK_IFSC) {
        this.CH_BEEMA_BANK_IFSC = CH_BEEMA_BANK_IFSC;
    }

    public String getCH_BEEMA_NOMINEE_AADHAAR() {
        return CH_BEEMA_NOMINEE_AADHAAR;
    }

    public void setCH_BEEMA_NOMINEE_AADHAAR(String CH_BEEMA_NOMINEE_AADHAAR) {
        this.CH_BEEMA_NOMINEE_AADHAAR = CH_BEEMA_NOMINEE_AADHAAR;
    }

    public String getPUBLIC_REP_POSITION() {
        return PUBLIC_REP_POSITION;
    }

    public void setPUBLIC_REP_POSITION(String PUBLIC_REP_POSITION) {
        this.PUBLIC_REP_POSITION = PUBLIC_REP_POSITION;
    }

    public String getEKYC_STATUS() {
        return EKYC_STATUS;
    }

    public void setEKYC_STATUS(String EKYC_STATUS) {
        this.EKYC_STATUS = EKYC_STATUS;
    }

    public String getAVAIL_OF_ISL() {
        return AVAIL_OF_ISL;
    }

    public void setAVAIL_OF_ISL(String AVAIL_OF_ISL) {
        this.AVAIL_OF_ISL = AVAIL_OF_ISL;
    }

    public String getSOAK_PIT() {
        return SOAK_PIT;
    }

    public void setSOAK_PIT(String SOAK_PIT) {
        this.SOAK_PIT = SOAK_PIT;
    }

    public String getHLTH_HYGIENE_DWELL_AREA() {
        return HLTH_HYGIENE_DWELL_AREA;
    }

    public void setHLTH_HYGIENE_DWELL_AREA(String HLTH_HYGIENE_DWELL_AREA) {
        this.HLTH_HYGIENE_DWELL_AREA = HLTH_HYGIENE_DWELL_AREA;
    }

    public String getHOUSE_CONST_TYPE() {
        return HOUSE_CONST_TYPE;
    }

    public void setHOUSE_CONST_TYPE(String HOUSE_CONST_TYPE) {
        this.HOUSE_CONST_TYPE = HOUSE_CONST_TYPE;
    }

    public String getHOUSE_DRINK_WATER_SRC() {
        return HOUSE_DRINK_WATER_SRC;
    }

    public void setHOUSE_DRINK_WATER_SRC(String HOUSE_DRINK_WATER_SRC) {
        this.HOUSE_DRINK_WATER_SRC = HOUSE_DRINK_WATER_SRC;
    }

    public String getHOUSE_OWNERSHIP() {
        return HOUSE_OWNERSHIP;
    }

    public void setHOUSE_OWNERSHIP(String HOUSE_OWNERSHIP) {
        this.HOUSE_OWNERSHIP = HOUSE_OWNERSHIP;
    }

    public String getHEALTH_INSURANCE() {
        return HEALTH_INSURANCE;
    }

    public void setHEALTH_INSURANCE(String HEALTH_INSURANCE) {
        this.HEALTH_INSURANCE = HEALTH_INSURANCE;
    }

    public String getMOVABLE_PROPERTIES() {
        return MOVABLE_PROPERTIES;
    }

    public void setMOVABLE_PROPERTIES(String MOVABLE_PROPERTIES) {
        this.MOVABLE_PROPERTIES = MOVABLE_PROPERTIES;
    }

    public String getUPDATED_ON() {
        return UPDATED_ON;
    }

    public void setUPDATED_ON(String UPDATED_ON) {
        this.UPDATED_ON = UPDATED_ON;
    }

    public String getBUILDING_TSH_NUM() {
        return BUILDING_TSH_NUM;
    }

    public void setBUILDING_TSH_NUM(String BUILDING_TSH_NUM) {
        this.BUILDING_TSH_NUM = BUILDING_TSH_NUM;
    }

    public String getDWELLING_TSH_NUM() {
        return DWELLING_TSH_NUM;
    }

    public void setDWELLING_TSH_NUM(String DWELLING_TSH_NUM) {
        this.DWELLING_TSH_NUM = DWELLING_TSH_NUM;
    }

    public String getCOOKING_FUEL_1() {
        return COOKING_FUEL_1;
    }

    public void setCOOKING_FUEL_1(String COOKING_FUEL_1) {
        this.COOKING_FUEL_1 = COOKING_FUEL_1;
    }

    public String getELEC_DEVICES() {
        return ELEC_DEVICES;
    }

    public void setELEC_DEVICES(String ELEC_DEVICES) {
        this.ELEC_DEVICES = ELEC_DEVICES;
    }

    public String getTYPE_DISABILITY() {
        return TYPE_DISABILITY;
    }

    public void setTYPE_DISABILITY(String TYPE_DISABILITY) {
        this.TYPE_DISABILITY = TYPE_DISABILITY;
    }

    public String getRESIDENT_STATUS() {
        return RESIDENT_STATUS;
    }

    public void setRESIDENT_STATUS(String RESIDENT_STATUS) {
        this.RESIDENT_STATUS = RESIDENT_STATUS;
    }

    public String getMEESEVA_ID() {
        return MEESEVA_ID;
    }

    public void setMEESEVA_ID(String MEESEVA_ID) {
        this.MEESEVA_ID = MEESEVA_ID;
    }

    public String getPENSION_ID() {
        return PENSION_ID;
    }

    public void setPENSION_ID(String PENSION_ID) {
        this.PENSION_ID = PENSION_ID;
    }

    public String getNREGS_ID() {
        return NREGS_ID;
    }

    public void setNREGS_ID(String NREGS_ID) {
        this.NREGS_ID = NREGS_ID;
    }

    public String getSHG_ID() {
        return SHG_ID;
    }

    public void setSHG_ID(String SHG_ID) {
        this.SHG_ID = SHG_ID;
    }

    public String getPOST_SCHOLORSHIP_ID() {
        return POST_SCHOLORSHIP_ID;
    }

    public void setPOST_SCHOLORSHIP_ID(String POST_SCHOLORSHIP_ID) {
        this.POST_SCHOLORSHIP_ID = POST_SCHOLORSHIP_ID;
    }

    public String getBPCL_ID() {
        return BPCL_ID;
    }

    public void setBPCL_ID(String BPCL_ID) {
        this.BPCL_ID = BPCL_ID;
    }

    public String getIOCL_ID() {
        return IOCL_ID;
    }

    public void setIOCL_ID(String IOCL_ID) {
        this.IOCL_ID = IOCL_ID;
    }

    public String getCDMA_PPT_ID() {
        return CDMA_PPT_ID;
    }

    public void setCDMA_PPT_ID(String CDMA_PPT_ID) {
        this.CDMA_PPT_ID = CDMA_PPT_ID;
    }

    public String getEPDCL_ID() {
        return EPDCL_ID;
    }

    public void setEPDCL_ID(String EPDCL_ID) {
        this.EPDCL_ID = EPDCL_ID;
    }

    public String getWEB_LAND_ID() {
        return WEB_LAND_ID;
    }

    public void setWEB_LAND_ID(String WEB_LAND_ID) {
        this.WEB_LAND_ID = WEB_LAND_ID;
    }

    public String getPRE_SCHOLORSHIP_ID() {
        return PRE_SCHOLORSHIP_ID;
    }

    public void setPRE_SCHOLORSHIP_ID(String PRE_SCHOLORSHIP_ID) {
        this.PRE_SCHOLORSHIP_ID = PRE_SCHOLORSHIP_ID;
    }

    public String getGOVT_EMP_ID() {
        return GOVT_EMP_ID;
    }

    public void setGOVT_EMP_ID(String GOVT_EMP_ID) {
        this.GOVT_EMP_ID = GOVT_EMP_ID;
    }

    public String getLABOUR_ID() {
        return LABOUR_ID;
    }

    public void setLABOUR_ID(String LABOUR_ID) {
        this.LABOUR_ID = LABOUR_ID;
    }

    public String getRTA_OWNERS_ID() {
        return RTA_OWNERS_ID;
    }

    public void setRTA_OWNERS_ID(String RTA_OWNERS_ID) {
        this.RTA_OWNERS_ID = RTA_OWNERS_ID;
    }

    public String getRTA_DRIVERS_ID() {
        return RTA_DRIVERS_ID;
    }

    public void setRTA_DRIVERS_ID(String RTA_DRIVERS_ID) {
        this.RTA_DRIVERS_ID = RTA_DRIVERS_ID;
    }

    public String getSPDCL_ID() {
        return SPDCL_ID;
    }

    public void setSPDCL_ID(String SPDCL_ID) {
        this.SPDCL_ID = SPDCL_ID;
    }

    public String getHOUSING_ID() {
        return HOUSING_ID;
    }

    public void setHOUSING_ID(String HOUSING_ID) {
        this.HOUSING_ID = HOUSING_ID;
    }

    public String getHPCL_ID() {
        return HPCL_ID;
    }

    public void setHPCL_ID(String HPCL_ID) {
        this.HPCL_ID = HPCL_ID;
    }

    public String getSSA_ID() {
        return SSA_ID;
    }

    public void setSSA_ID(String SSA_ID) {
        this.SSA_ID = SSA_ID;
    }

    public String getRESCO_ID() {
        return RESCO_ID;
    }

    public void setRESCO_ID(String RESCO_ID) {
        this.RESCO_ID = RESCO_ID;
    }

}
