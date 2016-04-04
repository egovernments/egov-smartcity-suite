/**
 * eGov suite of products aim to improve the internal efficiency,transparency, accountability and the service delivery of the
 * government organizations.
 *
 * Copyright (C) <2015> eGovernments Foundation
 *
 * The updated version of eGov suite of products as by eGovernments Foundation is available at http://www.egovernments.org
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/ or http://www.gnu.org/licenses/gpl.html .
 *
 * In addition to the terms of the GPL license to be adhered to in using this program, the following additional terms are to be
 * complied with:
 *
 * 1) All versions of this program, verbatim or modified must carry this Legal Notice.
 *
 * 2) Any misrepresentation of the origin of the material is prohibited. It is required that all modified versions of this
 * material be marked in reasonable ways as different from the original version.
 *
 * 3) This license does not grant any rights to any user of the program with regards to rights under trademark law for use of the
 * trade names or trademarks of eGovernments Foundation.
 *
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.commons.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgActiondetails;
import org.egov.commons.EgPartytype;
import org.egov.commons.EgSurrenderedCheques;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.commons.EgwTypeOfWork;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Installment;
import org.egov.commons.ObjectHistory;
import org.egov.commons.ObjectType;
import org.egov.commons.Relation;
import org.egov.commons.Scheme;
import org.egov.commons.Status;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.utils.FinancialYear;

public interface CommonsService {
    /**
     * Returns a installment object identified by its identifier.
     * 
     * @return Installment object if found or null refernce if not found in the
     *         system.
     * @see Installment.java
     * @throws ApplicationRuntimeException,
     *             if it finds a System exception.
     */
    public Installment getInstallmentByID(Integer id);

    /**
     * Persists the Installment passed in. Also rolls back the current
     * transaction, if it fails to create the installment.
     * 
     * @param installment
     * @throws ApplicationRuntimeException,
     *             if it fails to create.
     * @see Installment.java
     */
    public void createInstallment(Installment installment);

    /**
     * Deletes the Installment passed in. Also rolls back the current
     * transaction, if it fails to delete the installment.
     * 
     * @param installment
     * @throws ApplicationRuntimeException,
     *             if it fails to delete.
     * @see Installment.java
     */

    public void deleteInstallment(Installment installment);

    /**
     * Updates the Installment passed in. Also rolls back the current
     * transaction, if it fails to update the installment.
     * 
     * @param installment
     * @throws ApplicationRuntimeException,
     *             if it fails to update.
     * @see Installment.java
     */
    public void updateInstallment(Installment installment);

    /**
     * Returns all the Installments belonging to the passed in module.
     * 
     * @param module
     * @throws ApplicationRuntimeException,
     *             if it finds a System exception.
     * @see Installment.java
     * @see Module.java
     */

    public List<Installment> getInsatllmentByModule(Module module);

    /**
     * Returns all the Installments belonging to the passed in module and for a
     * particular year.
     * 
     * @param module,
     *            Date year
     * @throws ApplicationRuntimeException,
     *             if it finds a System exception.
     * @see Installment.java
     * @see Module.java
     */

    public List<Installment> getInsatllmentByModule(Module module, Date year);

    /**
     * Returns a Installment belonging to the passed in module and for a
     * particular year and having the given number.
     * 
     * @param module,
     *            Date year, installment number
     * @throws ApplicationRuntimeException,
     *             if it finds a System exception.
     * @see Installment.java
     * @see Module.java
     */

    public Installment getInsatllmentByModule(Module module, Date year, Integer installmentNumber);

    /**
     * Returns all the Installments in the system, for all modules and for all
     * years.
     * 
     * @throws ApplicationRuntimeException,
     *             if it finds a System exception.
     * @see Installment.java
     */

    public List<Installment> getAllInstallments();

    /**
     * Returns an appropriate Installment belonging to the passed in module and
     * for a particular period of that year. This is helpful when there are many
     * installments in a given year. So the installment returned will pertain
     * that period of the given date.
     * 
     * @param module
     * @param Date
     *            a particular installment date.
     * @throws ApplicationRuntimeException,
     *             if it finds a System exception.
     * @see Installment.java
     * @see Module.java
     */

    public Installment getInsatllmentByModuleForGivenDate(Module module, Date installmentDate);

    /**
     * Returns an appropriate Installment belonging to the passed in module and
     * for a current date of the current year. This is helpful when there are
     * many installments in a given year. So the installment returned will
     * pertain that period of the given date.
     * 
     * @param module
     * @throws ApplicationRuntimeException,
     *             if it finds a System exception.
     * @see Installment.java
     * @see Module.java
     */

    public Installment getInsatllmentByModuleForCurrDate(Module module);

    public String getCurrentInstallmentYear();

    // To get Width of Map
    public Map<Integer, Integer> getWidth(Integer bndryID);

    // To get Height of Map
    public Map<Integer, Integer> getHeight(Integer bndryID);

    /**
     * used in Inventory,Wardworks Returns the Fund if Id found ,otherwise
     * throws an exception
     * 
     * @param id
     * @return Fund
     */
    public Fund fundById(Integer id);

    /**
     * used in Inventory,Wardworks Returns the Fundsource if Id found ,otherwise
     * throws an exception
     * 
     * @param id
     * @return Fundsource
     */
    public Fundsource fundsourceById(Integer id);

    /**
     * used in Wardworks Returns the EgwStatus if Id found
     * 
     * @param statsuId
     * @return EgwStatus
     */
    public EgwStatus getEgwStatusById(Integer statsuId);

    /**
     * Returns the EgwStatus if Code found
     * 
     * @param code
     * @return EgwStatus
     */
    public EgwStatus getEgwStatusByCode(String code);

    /**
     * used in Wardworks Returns all the Active Funds
     * 
     * @return Fund
     */
    public List<Fund> getAllFunds();

    /**
     * used in Wardworks Returns all the Fundsource
     * 
     * @return Fundsource
     */
    public List<Fundsource> getAllFundSource();

    /**
     * used in Wardworks Returns all the EgActiondetails filtered by Module Id,
     * Action Type and Module Type
     * 
     * @param moduleId
     * @param actionType
     * @param moduleType);
     * @return EgActiondetails
     */
    public List<EgActiondetails> getEgActiondetailsFilterBy(String moduleId, ArrayList<String> actionType,
            String moduleType);

    /**
     * used in Wardworks Returns the EgActiondetails object filtered by Module
     * Id, Action Type and Module Type
     * 
     * @param moduleId
     * @param actionType
     * @param moduleType);
     * @return EgActiondetails
     */
    public EgActiondetails getEgActiondetailsByWorksdetailId(String moduleId, String actionType, String moduleType);

    /**
     * used in Wardworks Creates the EgActiondetails
     * 
     * @param egActiondetails
     */
    public void createEgActiondetails(EgActiondetails egActiondetails);

    /**
     * used in Wardworks Updates the EgActiondetails
     * 
     * @param egActiondetails
     */
    public void updateEgActiondetails(EgActiondetails egActiondetails);

    /**
     * used in Wardworks Creates the EgwSatuschange
     * 
     * @param egwSatuschange
     */
    public void createEgwSatuschange(EgwSatuschange egwSatuschange);

    /**
     * used in Wardworks Returns the Fundsource object if Id found
     * 
     * @param fundSourceId
     * @return Fundsource
     */
    public Fundsource getFundSourceById(Integer fundSourceId);

    /**
     * used in Wardworks Returns the Fund object if Id found
     * 
     * @param fundId
     * @return Fund
     */
    public Fund getFundById(Integer fundId);

    /**
     * used in Wardworks Returns all the EgUom
     * 
     * @return EgUom
     */
    /* public List<EgUom> findAllUom(); */

    /**
     * used in Wardworks Returns the Relation object if Id found
     * 
     * @param relationId
     * @return Relation
     */
    public Relation getRelationById(Integer relationId);

    /**
     * used in Wardworks Returns the EgUom object if Id found
     * 
     * @param uomId
     * @return EgUom
     */
    /* public EgUom getUomById(Integer uomId); */

    /**
     * used in Wardworks Returns all the EgwStatus for the list of Ids found
     * 
     * @param statusId
     * @return EgwStatus
     */
    public List<EgwStatus> getEgwStatusFilterByStatus(ArrayList<Integer> statusId);

    /**
     * @param moduleType
     *            Module type
     * @param statusCode
     *            Status code
     * @return EgwStatus object for given module type and status code
     */
    public EgwStatus getStatusByModuleAndCode(String moduleType, String statusCode);

    public List<EgActiondetails> getEgActiondetailsFilterBy(ArrayList<String> actionType, String moduleType);

    /**
     * used in Wardworks Returns all the Status if moduleType found
     * 
     * @param moduleType
     */
    public List<Status> getStatusByModuleType(String moduleType);

    /**
     * used in Wardworks Creates Accountdetailkey
     * 
     * @param accountdetailkey
     * @return Accountdetailkey
     */
    public void createAccountdetailkey(Accountdetailkey accountdetailkey);

    /**
     * used in Wardworks Returns Status object if id found
     * 
     * @param id
     * @return Status
     */
    public Status findEgInvStatusById(Integer id);

    /**
     * used in Wardworks Returns EgwStatus object if id found
     * 
     * @param id
     * @return EgwStatus
     */
    public EgwStatus findEgwStatusById(Integer id);

    /**
     * used in Wardworks Returns all the Financial Years
     */
    public List<CFinancialYear> getAllFinancialYearList();

    /**
     * used in Wardworks Returns CFinancialYear object if id found
     * 
     * @param id
     * @return CFinancialYear
     */
    public CFinancialYear findFinancialYearById(Long id);

    /**
     * used in Wardworks Returns CFunction object if id found
     * 
     * @param functionId
     * @return CFunction
     */
    public CFunction getCFunctionById(Long functionId);

    /**
     * used in Wardworks Returns CChartOfAccounts object if id found
     * 
     * @param majorCode
     * @return CChartOfAccounts
     */
    public CChartOfAccounts findGlCodeById(String majorCode);

    /**
     * used in Wardworks Returns List of Function objects
     */
    public List<CFunction> getAllFunction();

    /**
     * used in Wardworks Returns CChartOfAccounts object if id found
     * 
     * @param chartOfAccountsId
     * @return CChartOfAccounts
     */
    public CChartOfAccounts getCChartOfAccountsById(Long chartOfAccountsId);

    /**
     * used in Wardworks Returns List of CGeneralLedger objects
     * 
     * @param voucherHeaderId
     */
    public List<CGeneralLedger> getGeneralLedgerList(Long voucherHeaderId) throws Exception;

    /**
     * Returns all the CChartOfAccounts of glcode.
     */
    public CChartOfAccounts getCChartOfAccountsByGlCode(String glCode);

    /**
     * Returns Function List
     */
    public Collection<CFunction> getFunctionList() throws Exception;

    /**
     * Used in Budget Returns CVoucherHeader object
     */
    public CVoucherHeader findVoucherHeaderById(Long id) throws Exception;

    /**
     * Returns CFunction object
     */
    public CFunction findFunctionById(Long id) throws Exception;

    /**
     * Returns All FinancialYear List
     */
    public Collection<CFinancialYear> getFinancialYearList() throws Exception;

    /**
     * This API will return the transaction no for any type of txn. Input
     * :Type,transaction date and connection Output :Transaction number in the
     * format txnType+number+/+month+/+year
     */
    public String getTxnNumber(String txnType, String vDate, Connection con) throws Exception;

    /**
     * @param connection
     * @return currentDate in String format
     * @throws Exception
     */
    public String getCurrentDate(Connection connection) throws Exception;

    /**
     * @param finYearRange
     * @return CFinancialYear
     */
    public CFinancialYear getFinancialYearByFinYearRange(String finYearRange);

    /**
     * @return List of Active FinancialYears Objects
     */
    public List<CFinancialYear> getAllActiveFinancialYearList();

    /**
     * @return active for posting financials year
     */
    public List<CFinancialYear> getAllActivePostingFinancialYear();

    /**
     * used in Wardworks Returns List of EgwTypeOfWork objects where parent is
     * null
     */
    public List<EgwTypeOfWork> getAllParentOrderByCode();

    /**
     * used in Wardworks Returns List of EgwTypeOfWork objects
     */
    public List<EgwTypeOfWork> getAllTypeOfWork();

    /**
     * used in Wardworks Returns EgwTypeOfWork object
     * 
     * @param id
     */
    public EgwTypeOfWork getTypeOfWorkById(Long id);

    /**
     * used in Wardworks Returns EgwTypeOfWork object
     * 
     * @param code
     */
    public EgwTypeOfWork findByCode(String code);

    /**
     * used in Wardworks Creates EgwTypeOfWork
     * 
     * @param egwTypeOfWork
     */
    public void createEgwTypeOfWork(EgwTypeOfWork egwTypeOfWork);

    /**
     * used in Wardworks Updates EgwTypeOfWork
     * 
     * @param egwTypeOfWork
     */
    public void updateEgwTypeOfWork(EgwTypeOfWork egwTypeOfWork);

    /**
     * @param code
     * @param parentCode
     * @param description
     * @return list of EgwTypeOfWork filtered by optional conditions
     */
    public List<EgwTypeOfWork> getTypeOfWorkDetailFilterBy(String code, String parentCode, String description);

    /**
     * @param code
     * @param parentCode
     * @param description
     * @param partyTypeCode
     * @return list of EgwTypeOfWork filtered by optional conditions
     */
    public List<EgwTypeOfWork> getTypeOfWorkDetailFilterByParty(String code, String parentCode, String description,
            String partyTypeCode);

    public List<EgPartytype> getPartyTypeDetailFilterBy(String code, String parentCode, String description);

    /**
     * @return current financial year id
     */
    public String getCurrYearFiscalId();

    /**
     * @return current financial year starting date
     */
    public String getCurrYearStartDate();

    /**
     * @return previous financial year id
     */
    public String getPrevYearFiscalId();

    /**
     * used in birth and Death project Returns all the EgwStatus object filtered
     * by ModuleType
     * 
     * @param moduleType
     * @return List
     */
    public List<EgwStatus> getStatusByModule(String moduleType);

    /**
     * used in Deductions Returns EgPartytype object
     * 
     * @param id
     */
    public EgPartytype getPartytypeById(Integer id);

    /**
     * used in Deductions Creates EgPartytype
     * 
     * @param egPartytype
     */
    public void createEgPartytype(EgPartytype egPartytype);

    /**
     * used in Deductions Updates EgPartytype
     * 
     * @param egPartytype
     */
    public void updateEgPartytype(EgPartytype egPartytype);

    public List<EgPartytype> findAllPartyTypeChild();

    public List<EgwTypeOfWork> findAllParentPartyType();

    public List<EgwTypeOfWork> findAllChildPartyType();

    /**
     * @param status
     * @return Listof CVoucherHeaders where status=0
     * @throws Exception
     */

    public List<CVoucherHeader> getVoucherHeadersByStatus(Integer status) throws Exception;

    /**
     * @param status
     * @param type
     * @return
     * @throws Exception
     *             if glcode is not a control code it will return 0
     */
    public List<CVoucherHeader> getVoucherHeadersByStatusAndType(Integer status, String type) throws Exception;

    /**
     * @param uom
     * @return
     * @throws Exception
     */
    /* public EgUom getUomByUom(String uom) throws Exception; */

    /**
     * @param id
     * @return Bankaccount object
     */
    public Bankaccount getBankaccountById(Integer id);

    public Bankbranch getBankbranchById(Integer id);

    /**
     * Creates EgSurrenderedCheques
     * 
     * @param egSurrenderedCheques
     */
    public void createEgSurrenderedCheques(EgSurrenderedCheques egSurrenderedCheques);

    /**
     * Updates EgSurrenderedCheques
     * 
     * @param egSurrenderedCheques
     */
    public void updateEgSurrenderedCheques(EgSurrenderedCheques egSurrenderedCheques);

    /**
     * @param code
     * @return EgPartytype
     */
    public EgPartytype getPartytypeByCode(String code);

    public String getAccountdetailtypeAttributename(Connection connection, String name) throws Exception;

    public List<Bankaccount> getAllBankAccounts();

    public List<Bankbranch> getAllBankBranchs();

    /**
     * @param id
     * @return Bank object
     */
    public Bank getBankById(Integer id);

    public ObjectType getObjectTypeByType(String type);

    public ObjectType getObjectTypeById(Integer type);

    public ObjectHistory createObjectHistory(ObjectHistory objhistory);

    /**
     * @param voucherHeaderId
     * @return Deduction Amount
     */
    public String getCBillDeductionAmtByVhId(Long voucherHeaderId);

    public Vouchermis getVouchermisByVhId(Integer vhId);

    /**
     * @param cgn
     * @return CVoucherHeader object
     */
    public CVoucherHeader getVoucherHeadersByCGN(String cgn);

    /**
     * @param moduleType
     *            Module type
     * @param codeList
     *            List of status codes
     * @return List of all EgwStatus objects filtered by given module type and
     *         list of status codes
     */
    public List<EgwStatus> getStatusListByModuleAndCodeList(String moduleType, List codeList);

    /**
     * @param functionCode
     * @return
     */
    public CFunction getFunctionByCode(String functionCode);

    public List<Fund> getAllActiveIsLeafFunds();

    /**
     * This method returns all active functionary records.
     * 
     * @return
     */
    public List<Functionary> getActiveFunctionaries();

    /**
     * This method returns the active and is active for posting Account records
     * having classification as '4' , for a given type.
     * 
     * @param type
     * @return
     */
    public List<CChartOfAccounts> getActiveAccountsForType(char type) throws ApplicationException;

    /**
     * to get the list of chartofaccounts based on the purposeId
     * 
     * @param purposeId
     * @return list of COA object(s)
     */
    public List<CChartOfAccounts> getAccountCodeByPurpose(Integer purposeId) throws ApplicationException;

    /**
     * This menthos will list the accoyntdetailtype for the account code and
     * detail type name
     * 
     * @param glCode
     * @param name
     * @return
     * @throws Exception
     */
    public Accountdetailtype getAccountDetailTypeIdByName(String glCode, String name) throws Exception;

    /**
     * This method will list the detailed chartofaccounts object that are active
     * for posting
     * 
     * @return
     * @throws ApplicationException
     */
    public List<CChartOfAccounts> getDetailedAccountCodeList() throws ApplicationException;

    /**
     * This method returns the active and is leaf fund sources.
     * 
     * @return a list of <code>Fundsource</code> objects
     * @throws ApplicationException
     */
    public List<Fundsource> getAllActiveIsLeafFundSources() throws ApplicationException;

    /**
     * to get the function object
     * 
     * @param id
     * @return
     */
    public Functionary getFunctionaryById(Integer id) throws ApplicationException;

    /**
     * To get the scheme object
     * 
     * @param id
     * @return
     * @throws ApplicationException
     */
    public Scheme getSchemeById(Integer id) throws ApplicationException;

    /**
     * To get the scheme object
     * 
     * @param id
     * @return
     * @throws ApplicationException
     */
    public SubScheme getSubSchemeById(Integer id) throws ApplicationException;

    /**
     * to get the financial year
     * 
     * @param id
     * @return
     */
    public CFinancialYear getFinancialYearById(Long id);

    public CFunction getFunctionById(Long Id);

    /**
     * This will return the accountdetailkeyid if the object passed is mapped as
     * a subledger type
     * 
     * @param master
     * @return accountdetailtypeid in case of subledger, else returns null
     * @throws ApplicationException
     */
    public Integer getDetailtypeforObject(Object master) throws ApplicationException;

    /**
     * @description - this API returns the detailtypeobject for an account code
     * @param glCode
     * @return List<Accountdetailtype>
     * @throws ApplicationException
     */
    public List<Accountdetailtype> getDetailTypeListByGlCode(String glCode) throws ApplicationException;

    /**
     * @param fundCode
     * @return
     */
    public Fund fundByCode(String fundCode);

    /**
     * @param code
     * @return
     */
    public Scheme schemeByCode(String code);

    /**
     * @param code
     * @return
     */
    public Fundsource getFundSourceByCode(String code);

    /**
     * @param code
     * @return
     */
    public SubScheme getSubSchemeByCode(String code);

    /**
     * @param bankCode
     * @return
     */
    public Bank getBankByCode(String bankCode);

    /**
     * @param bankAccNum
     * @param bankBranchCode
     * @param bankCode
     * @return
     */
    public Bankaccount getBankAccountByAccBranchBank(String bankAccNum, String bankBranchCode, String bankCode);

    public Functionary getFunctionaryByCode(BigDecimal code);

    public Functionary getFunctionaryByName(String name);

    public List<Accountdetailtype> getAccountdetailtypeListByGLCode(final String glCode) throws ApplicationException;

    /**
     * @param type
     * @return
     * @throws ValidationException
     */
    public List<CChartOfAccounts> getActiveAccountsForTypes(char[] type) throws ValidationException;

    /**
     * @param purposeId
     * @return
     * @throws ValidationException
     */
    public List<CChartOfAccounts> getAccountCodeByListOfPurposeId(Integer[] purposeId) throws ValidationException;

    /**
     * @param asOndate
     * @return
     * @throws ValidationException
     */
    public List<CChartOfAccounts> getListOfDetailCode(String glCode) throws ValidationException;

    /*
     * public List<EgUom> getAllUomsWithinCategoryByUom(Integer uomId) throws
     * ValidationException; public BigDecimal getConversionFactorByUom(Integer
     * uomId) throws ValidationException; public BigDecimal
     * getConversionFactorByFromUomToUom(Integer fromuomId, Integer touomId)
     * throws ValidationException;
     */

    /**
     * used in Financials Returns Financial Year based on date
     * 
     * @param estDate
     */
    public CFinancialYear getFinancialYearByDate(Date date);

    public List<EgPartytype> getSubPartyTypes(String code);

    /**
     * @description - This API Returns Ward Boundary based on Latitutde and
     *              Longitude Provided the Shape file with approprate boundary
     *              (Ward) is available in classpath.
     * @param latitude
     * @param longitude
     * @return Wardid if available else null
     */

    public Long getBndryIdFromShapefile(Double latitude, Double longitude);

    /**
     * Returns Financial Year based on date irrespective of active status
     * 
     * @param date
     */
    public CFinancialYear getFinYearByDate(Date date);

    public Accountdetailtype getAccountDetailTypeByName(String name);

}
