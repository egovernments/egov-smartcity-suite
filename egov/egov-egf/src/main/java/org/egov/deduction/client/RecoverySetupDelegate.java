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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * RecoverySetupDelegate.java Created on Oct 5, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.deduction.client;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.Bank;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.EgPartytype;
import org.egov.commons.dao.BankDAO;
import org.egov.commons.dao.ChartOfAccountsDAO;
import org.egov.commons.dao.EgPartytypeHibernateDAO;
import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.dao.recoveries.EgDeductionDetailsHibernateDAO;
import org.egov.dao.recoveries.RecoveryDAOFactory;
import org.egov.dao.recoveries.TdsHibernateDAO;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.recoveries.EgDeductionDetails;
import org.egov.model.recoveries.Recovery;
import org.egov.services.recoveries.RecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO Brief Description of the purpose of the class/interface
 *
 * @author Iliyaraja S mani
 * @version 1.00
 */
@Transactional(readOnly = true)
public class RecoverySetupDelegate {
    public static final Logger LOGGER = Logger.getLogger(RecoverySetupDelegate.class);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    Date dt;

    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
    Date dt2;

    private RecoveryService recoveryService;
    private final EgDeductionDetailsHibernateDAO egDeductionDetHibernateDao;
    private final TdsHibernateDAO tdsHibernateDAO;

    @Autowired
    private ChartOfAccountsDAO coaDAO;
    @Autowired
    private EgPartytypeHibernateDAO partyTypeDAO;
    @Autowired
    private BankDAO bankDAO;
    @Autowired
    private EgwTypeOfWorkHibernateDAO typeOfWrkDAO;

    // ChartOfAccountsHibernateDAO coaDAO;
    // EgPartytypeHibernateDAO partyDAO;
    // EgwTypeOfWorkHibernateDAO workDAO;

    public RecoverySetupDelegate() {

        egDeductionDetHibernateDao = RecoveryDAOFactory.getDAOFactory().getEgDeductionDetailsDAO();
        tdsHibernateDAO = RecoveryDAOFactory.getDAOFactory().getTdsDAO();

        // coaDAO = (ChartOfAccountsHibernateDAO) CommonsHibernateDaoFactory.getDAOFactory().getChartOfAccountsDAO();
        // partyDAO = CommonsHibernateDaoFactory.getDAOFactory().getEgPartytypeDAO();
        // workDAO = CommonsHibernateDaoFactory.getDAOFactory().getEgwTypeOfWorkDAO();

    }

    @Transactional
    public void createRecovery(final RecoverySetupForm rsf, final Integer userId) throws Exception {

        Recovery tds1 = null;
        EgDeductionDetails dedDetail = null;
        final Timestamp curDate = new Timestamp(System.currentTimeMillis());

        tds1 = new Recovery();
        // For Tds table insert
        if (rsf.getRecovCode() != null && rsf.getRecovCode().length() > 0)
            tds1.setType(rsf.getRecovCode());

        if (rsf.getRecovName() != null && rsf.getRecovName().length() > 0)
            tds1.setRecoveryName(rsf.getRecovName());

        if (rsf.getRecovIFSCCode() != null && rsf.getRecovIFSCCode().length() > 0)
            tds1.setIfscCode(rsf.getRecovIFSCCode());

        if (rsf.getRecovBankAccount() != null && rsf.getRecovBankAccount().length() > 0)
            tds1.setAccountNumber(rsf.getRecovBankAccount());

        // this is for employee
        if (rsf.getEmprecovAccCodeId() != null && rsf.getEmprecovAccCodeId().length() > 0)
            if (!rsf.getEmprecovAccCodeId().equalsIgnoreCase("0"))
                rsf.setRecovAccCodeId(rsf.getEmprecovAccCodeId());

        if (rsf.getRecovAccCodeId() != null && rsf.getRecovAccCodeId().length() > 0)
            tds1.setChartofaccounts((CChartOfAccounts) coaDAO.findById(Long.parseLong(rsf.getRecovAccCodeId()), false));
        if (rsf.getRecovRemitTo() != null && rsf.getRecovRemitTo().length() > 0)
            tds1.setRemitted(rsf.getRecovRemitTo());

       /* if (rsf.getRecovBSRCode() != null && rsf.getRecovBSRCode().length() > 0)
            tds1.setBsrcode(rsf.getRecovBSRCode());
*/
        if (rsf.getCapLimit() != null && rsf.getCapLimit().length() > 0)
            tds1.setCaplimit(new BigDecimal(rsf.getCapLimit()));
        // only for employees
       /* if (rsf.getRecovRemitTo() != null && rsf.getRecovRemitTo().length() > 0)
            if (rsf.getIsEarning() != null && rsf.getIsEarning().length() > 0)
                tds1.setIsEarning(rsf.getIsEarning());
            else
                tds1.setIsEarning(null);*/
        if (rsf.getRecMode() != null && rsf.getRecMode().length() > 0)
            tds1.setRecoveryMode(rsf.getRecMode().toUpperCase().charAt(0));

        /*
         * if(rsf.getRecMode().equalsIgnoreCase("automatic") || (rsf.getRecMode().equalsIgnoreCase("manual") &&
         * rsf.getBankLoan().equalsIgnoreCase("off"))) { if(rsf.getRecovRemitTo()!=null && rsf.getRecovRemitTo().length()>0)
         * tds1.setRemitted(rsf.getRecovRemitTo()); if(rsf.getRecovBSRCode()!=null && rsf.getRecovBSRCode().length()>0)
         * tds1.setBsrcode(rsf.getRecovBSRCode()); } else
         */
        if (rsf.getRecMode().equalsIgnoreCase("manual") && rsf.getBankLoan().equalsIgnoreCase("on"))
            if (!rsf.getBank().equals("0") && rsf.getBank().length() > 0) {
                final Bank bank = bankDAO.getBankById(Integer.parseInt(rsf.getBank()));
                tds1.setBank(bank);
                // tds1.setRemitted(bank.getName());
            }

        if (rsf.getRecovAppliedTo() != null && rsf.getRecovAppliedTo().length() > 0)
            tds1.setEgPartytype((EgPartytype) partyTypeDAO.findById(Integer.parseInt(rsf.getRecovAppliedTo()), false));

        tds1.setCalculationType(rsf.getCalculationType());
        tds1.setDescription(rsf.getDescription());
       // tds1.setSection(rsf.getSection());

        tds1.setIsactive(true);

        if (LOGGER.isInfoEnabled())
            LOGGER.info(" BEFORE INSERT INTO TDS TABLE");
        recoveryService.createTds(tds1);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("AFTER INSERT INTO TDS TABLE");

        // For eg_deduction_details table insert
        final Long tdsid = tds1.getId();
        if (rsf.getRecMode().equalsIgnoreCase("automatic"))
            for (int i = 0; i < rsf.getLowAmount().length; i++) {

                dedDetail = new EgDeductionDetails();

                if (tdsid != null)
                    dedDetail.setRecovery(recoveryService.getTdsById(tdsid));

                if (rsf.getPartyType() != null)
                    if (rsf.getPartyType()[i] != null && !rsf.getPartyType()[i].equals("0") && rsf.getPartyType()[i].length() > 0)
                        dedDetail.setEgpartytype((EgPartytype) partyTypeDAO.findById(Integer.parseInt(rsf.getPartyType()[i]),
                                false));

                if (rsf.getDocType() != null)
                    if (rsf.getDocType()[i] != null && !rsf.getDocType()[i].equals("0") && rsf.getDocType()[i].length() > 0)
                        dedDetail
                        .setWorkDocType(typeOfWrkDAO.getTypeOfWorkById(Long.parseLong(rsf.getDocType()[i])));

                if (rsf.getLowAmount()[i] != null && rsf.getLowAmount()[i].length() > 0)
                    dedDetail.setLowlimit(new BigDecimal(rsf.getLowAmount()[i]));

                if (rsf.getHighAmount()[i] != null && rsf.getHighAmount()[i].length() > 0)
                    dedDetail.setHighlimit(new BigDecimal(rsf.getHighAmount()[i]));

                if (rsf.getITPercentage()[i] != null && rsf.getITPercentage()[i].length() > 0)
                    dedDetail.setIncometax(new BigDecimal(rsf.getITPercentage()[i]));

                if (rsf.getSurPercentage()[i] != null && rsf.getSurPercentage()[i].length() > 0)
                    dedDetail.setSurcharge(new BigDecimal(rsf.getSurPercentage()[i]));

                if (rsf.getEduCessPercentage()[i] != null && rsf.getEduCessPercentage()[i].length() > 0)
                    dedDetail.setEducation(new BigDecimal(rsf.getEduCessPercentage()[i]));

                if (rsf.getFlatAmount()[i] != null && rsf.getFlatAmount()[i].length() > 0)
                    dedDetail.setFlatAmount(new BigDecimal(rsf.getFlatAmount()[i]));

                if (rsf.getCumulativeAmountHigh()[i] != null && rsf.getCumulativeAmountHigh()[i].length() > 0)
                    dedDetail.setCumulativeHighLimit(new BigDecimal(rsf.getCumulativeAmountHigh()[i]));

                if (rsf.getCumulativeAmountLow()[i] != null && rsf.getCumulativeAmountLow()[i].length() > 0)
                    dedDetail.setCumulativeLowLimit(new BigDecimal(rsf.getCumulativeAmountLow()[i]));

                if (rsf.getRecovDateFrom()[i] != null && rsf.getRecovDateFrom()[i].length() > 0) {
                    dt = new Date();
                    dt = sdf.parse(rsf.getRecovDateFrom()[i]);
                    final String fromDate = formatter.format(dt);
                    dedDetail.setDatefrom(new Date(fromDate));
                }
                if (rsf.getRecovDateTo()[i] != null && rsf.getRecovDateTo()[i].length() > 0) {
                    dt = new Date();
                    dt = sdf.parse(rsf.getRecovDateTo()[i]);
                    final String toDate = formatter.format(dt);
                    dedDetail.setDateto(new Date(toDate));
                }

                dedDetail.setLastmodifieddate(curDate);

                if (LOGGER.isInfoEnabled())
                    LOGGER.info(" BEFORE INSERT INTO eg_deduction_details table");
                recoveryService.createEgDeductionDetails(dedDetail);
                if (LOGGER.isInfoEnabled())
                    LOGGER.info(" AFTER INSERT INTO eg_deduction_details table");
            }

        EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeMaster");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeAllChild");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-typeOfWorkParent");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodesForLiability");

        EgovMasterDataCaching.getInstance().removeFromCache("egi-tds");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-tdsType");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-recovery");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-egwTypeOfWork");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-egwSubTypeOfWork");

    }

    @Transactional
    public void modifyRecovery(final RecoverySetupForm rsf, final Integer userId) throws Exception {

        final EgDeductionDetails dedDetail = null;
        final Timestamp curDate = new Timestamp(System.currentTimeMillis());

        // For Tds table UPDATE
        final Recovery tds1 = recoveryService.findById(Long.parseLong(rsf.getTdsTypeId()));

        if (rsf.getRecovCode() != null && rsf.getRecovCode().length() > 0)
            tds1.setType(rsf.getRecovCode());

        if (rsf.getRecovName() != null && rsf.getRecovName().length() > 0)
            tds1.setRecoveryName(rsf.getRecovName());

        if (rsf.getRecovIFSCCode() != null)
            tds1.setIfscCode(rsf.getRecovIFSCCode());

        if (rsf.getRecovBankAccount() != null)
            tds1.setAccountNumber(rsf.getRecovBankAccount());
        // this is for employee
        if (rsf.getEmprecovAccCodeId() != null && rsf.getEmprecovAccCodeId().length() > 0)
            if (!rsf.getEmprecovAccCodeId().equalsIgnoreCase("0"))
                rsf.setRecovAccCodeId(rsf.getEmprecovAccCodeId());
        if (rsf.getRecovAccCodeId() != null && rsf.getRecovAccCodeId().length() > 0)
            tds1.setChartofaccounts((CChartOfAccounts) coaDAO.findById(Long.parseLong(rsf.getRecovAccCodeId()), false));
        if (rsf.getRecovRemitTo() != null && rsf.getRecovRemitTo().length() > 0)
            tds1.setRemitted(rsf.getRecovRemitTo());

        /*if (rsf.getRecovBSRCode() != null && rsf.getRecovBSRCode().length() > 0)
            tds1.setBsrcode(rsf.getRecovBSRCode());*/

        if (rsf.getCapLimit() != null && rsf.getCapLimit().length() > 0)
            tds1.setCaplimit(new BigDecimal(rsf.getCapLimit()));
        else
            tds1.setCaplimit(null);

       /* if (rsf.getIsEarning() != null && rsf.getIsEarning().length() > 0)
            tds1.setIsEarning(rsf.getIsEarning());
        else
            tds1.setIsEarning(null);*/

        if (rsf.getRecMode().equalsIgnoreCase("automatic")
                || rsf.getRecMode().equalsIgnoreCase("manual") && rsf.getBankLoan().equalsIgnoreCase("off"))
            tds1.setBank(null);
        else if (rsf.getRecMode().equalsIgnoreCase("manual") && rsf.getBankLoan().equalsIgnoreCase("on"))
            if (!rsf.getBank().equals("0") && rsf.getBank().length() > 0) {
                final Bank bank = bankDAO.getBankById(Integer.parseInt(rsf.getBank()));
                tds1.setBank(bank);
                // tds1.setRemitted(bank.getName());
                // tds1.setBsrcode(null);
            } else
                tds1.setBank(null);

        if (rsf.getRecovAppliedTo() != null && rsf.getRecovAppliedTo().length() > 0)
            tds1.setEgPartytype((EgPartytype) partyTypeDAO.findById(Integer.parseInt(rsf.getRecovAppliedTo()), false));

        tds1.setCalculationType(rsf.getCalculationType());
        tds1.setDescription(rsf.getDescription());
       // tds1.setSection(rsf.getSection());
        tds1.setRecoveryMode(rsf.getRecMode().toUpperCase().charAt(0));
        tds1.setIsactive(true);

        if (LOGGER.isInfoEnabled())
            LOGGER.info(" BEFORE UPDATE INTO TDS TABLE");
        recoveryService.updateTds(tds1);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("AFTER UPDATE INTO TDS TABLE");

        // For eg_deduction_details table update
        if (rsf.getRecMode().equalsIgnoreCase("automatic")) {
            List<EgDeductionDetails> recoveryDetailList = new ArrayList<EgDeductionDetails>();
            recoveryDetailList = recoveryService.findByTds(tds1);

            /* BOTH SIZE EQUAL BUT ID IS DIFFERENT MEANS UPDATE,DELETE,INSERT START */
            if (recoveryDetailList.size() == rsf.getId().length) {

                int i = 0, j = 0, k = 0;
                final Integer[] id = new Integer[recoveryDetailList.size()];
                for (; i < recoveryDetailList.size(); i++) {

                    final EgDeductionDetails ded = recoveryDetailList.get(i);
                    id[i] = ded.getId().intValue();
                    if (id[i] == Integer.parseInt(rsf.getId()[i])) {

                        final EgDeductionDetails dedDetail1 = recoveryService.getEgDeductionDetailsById(Integer
                                .parseInt(rsf.getId()[i]));
                        dedDetail1.setRecovery(tds1);

                        if (rsf.getPartyType() != null) {
                            if (rsf.getPartyType()[i] != null && !rsf.getPartyType()[i].equals("0")
                                    && rsf.getPartyType()[i].length() > 0)
                                dedDetail1.setEgpartytype((EgPartytype) partyTypeDAO.findById(
                                        Integer.parseInt(rsf.getPartyType()[i]), false));
                            else
                                dedDetail1.setEgpartytype(null);
                        } else
                            dedDetail1.setEgpartytype(null);
                        if (rsf.getDocType() != null) {
                            if (rsf.getDocType()[i] != null && !rsf.getDocType()[i].equals("0")
                                    && rsf.getDocType()[i].length() > 0)
                                dedDetail1.setWorkDocType(typeOfWrkDAO.getTypeOfWorkById(Long.parseLong(rsf
                                        .getDocType()[i])));
                            else
                                dedDetail1.setWorkDocType(null);
                        } else
                            dedDetail1.setWorkDocType(null);

                        /*
                         * if (rsf.getDocType() != null) { if (rsf.getDocType()[i] != null && !rsf.getDocType()[i].equals("0") &&
                         * rsf.getDocType()[i].length() > 0) { if (rsf.getSubType()[i] != null && !rsf.getSubType()[i].equals("0")
                         * && rsf.getSubType()[i].length() > 0) dedDetail1.setWorkDocSubType((EgwTypeOfWork)
                         * cmnMngr.getTypeOfWorkById(Long.parseLong(rsf.getSubType()[i]))); else {
                         * dedDetail1.setWorkDocSubType(null); } } else { dedDetail1.setWorkDocSubType(null); } } else {
                         * dedDetail1.setWorkDocSubType(null); }
                         */

                        if (rsf.getLowAmount()[i] != null && rsf.getLowAmount()[i].length() > 0)
                            dedDetail1.setLowlimit(new BigDecimal(rsf.getLowAmount()[i]));

                        if (rsf.getHighAmount()[i] != null && rsf.getHighAmount()[i].length() > 0)
                            dedDetail1.setHighlimit(new BigDecimal(rsf.getHighAmount()[i]));
                        else
                            dedDetail1.setHighlimit(null);

                        if (rsf.getCumulativeAmountLow()[i] != null && rsf.getCumulativeAmountLow()[i].length() > 0)
                            dedDetail1.setCumulativeLowLimit(new BigDecimal(rsf.getCumulativeAmountLow()[i]));

                        if (rsf.getCumulativeAmountHigh()[i] != null && rsf.getCumulativeAmountHigh()[i].length() > 0)
                            dedDetail1.setCumulativeHighLimit(new BigDecimal(rsf.getCumulativeAmountHigh()[i]));

                        if (rsf.getITPercentage()[i] != null && rsf.getITPercentage()[i].length() > 0)
                            dedDetail1.setIncometax(new BigDecimal(rsf.getITPercentage()[i]));
                        else
                            dedDetail1.setIncometax(null);

                        if (rsf.getSurPercentage()[i] != null && rsf.getSurPercentage()[i].length() > 0)
                            dedDetail1.setSurcharge(new BigDecimal(rsf.getSurPercentage()[i]));
                        else
                            dedDetail1.setSurcharge(null);

                        if (rsf.getEduCessPercentage()[i] != null && rsf.getEduCessPercentage()[i].length() > 0)
                            dedDetail1.setEducation(new BigDecimal(rsf.getEduCessPercentage()[i]));
                        else
                            dedDetail1.setEducation(null);

                        if (rsf.getFlatAmount()[i] != null && rsf.getFlatAmount()[i].length() > 0)
                            dedDetail1.setFlatAmount(new BigDecimal(rsf.getFlatAmount()[i]));
                        else
                            dedDetail1.setFlatAmount(null);

                        if (rsf.getRecovDateFrom()[i] != null && rsf.getRecovDateFrom()[i].length() > 0) {

                            dt = new Date();
                            dt = sdf.parse(rsf.getRecovDateFrom()[i]);
                            final String fromDate = formatter.format(dt);

                            dedDetail1.setDatefrom(new Date(fromDate));
                        }
                        if (rsf.getRecovDateTo()[i] != null && rsf.getRecovDateTo()[i].length() > 0) {

                            dt = new Date();
                            dt = sdf.parse(rsf.getRecovDateTo()[i]);
                            final String toDate = formatter.format(dt);

                            dedDetail1.setDateto(new Date(toDate));
                        } else
                            dedDetail1.setDateto(null);

                        dedDetail1.setLastmodifieddate(curDate);

                        recoveryService.updateEgDeductionDetails(dedDetail1);

                        j = i + 1;
                        k = i + 1;

                    } // if
                    else {
                        for (; j < recoveryDetailList.size(); j++) {

                            final EgDeductionDetails dedDetail2 = recoveryDetailList.get(j);
                            recoveryService.deleteEgDeductionDetails(dedDetail2);
                        }
                        for (; k < recoveryDetailList.size(); k++) {

                            EgDeductionDetails dedDetail3 = null;
                            dedDetail3 = new EgDeductionDetails();

                            dedDetail3.setRecovery(tds1);

                            if (rsf.getPartyType() != null) {
                                if (rsf.getPartyType()[k] != null && !rsf.getPartyType()[k].equals("0")
                                        && rsf.getPartyType()[k].length() > 0)
                                    dedDetail3.setEgpartytype((EgPartytype) partyTypeDAO.findById(
                                            Integer.parseInt(rsf.getPartyType()[k]), false));
                                else
                                    dedDetail3.setEgpartytype(null);
                            } else
                                dedDetail3.setEgpartytype(null);

                            if (rsf.getDocType() != null) {
                                if (rsf.getDocType()[k] != null && !rsf.getDocType()[k].equals("0")
                                        && rsf.getDocType()[k].length() > 0)
                                    dedDetail3.setWorkDocType(typeOfWrkDAO.getTypeOfWorkById(Long.parseLong(rsf
                                            .getDocType()[k])));
                                else
                                    dedDetail3.setWorkDocType(null);
                            } else
                                dedDetail3.setWorkDocType(null);

                            /*
                             * if (rsf.getDocType() != null) { if (rsf.getDocType()[k] != null && !rsf.getDocType()[k].equals("0")
                             * && rsf.getDocType()[k].length() > 0) { if (rsf.getSubType()[k] != null &&
                             * !rsf.getSubType()[k].equals("0") && rsf.getSubType()[k].length() > 0)
                             * dedDetail3.setWorkDocSubType((EgwTypeOfWork)
                             * cmnMngr.getTypeOfWorkById(Long.parseLong(rsf.getSubType()[k]))); else
                             * dedDetail3.setWorkDocSubType(null); } else dedDetail3.setWorkDocSubType(null); } else
                             * dedDetail3.setWorkDocSubType(null);
                             */

                            if (rsf.getLowAmount()[k] != null && rsf.getLowAmount()[k].length() > 0)
                                dedDetail3.setLowlimit(new BigDecimal(rsf.getLowAmount()[k]));

                            if (rsf.getHighAmount()[k] != null && rsf.getHighAmount()[k].length() > 0)
                                dedDetail3.setHighlimit(new BigDecimal(rsf.getHighAmount()[k]));
                            else
                                dedDetail3.setHighlimit(null);

                            if (rsf.getCumulativeAmountLow()[k] != null && rsf.getCumulativeAmountLow()[k].length() > 0)
                                dedDetail3.setCumulativeLowLimit(new BigDecimal(rsf.getCumulativeAmountLow()[k]));

                            if (rsf.getCumulativeAmountHigh()[k] != null && rsf.getCumulativeAmountHigh()[k].length() > 0)
                                dedDetail3.setCumulativeHighLimit(new BigDecimal(rsf.getCumulativeAmountHigh()[k]));

                            if (rsf.getITPercentage()[k] != null && rsf.getITPercentage()[k].length() > 0)
                                dedDetail3.setIncometax(new BigDecimal(rsf.getITPercentage()[k]));
                            else
                                dedDetail3.setIncometax(null);

                            if (rsf.getSurPercentage()[k] != null && rsf.getSurPercentage()[k].length() > 0)
                                dedDetail3.setSurcharge(new BigDecimal(rsf.getSurPercentage()[k]));
                            else
                                dedDetail3.setSurcharge(null);

                            if (rsf.getEduCessPercentage()[k] != null && rsf.getEduCessPercentage()[k].length() > 0)
                                dedDetail3.setEducation(new BigDecimal(rsf.getEduCessPercentage()[k]));
                            else
                                dedDetail3.setEducation(null);

                            if (rsf.getFlatAmount()[k] != null && rsf.getFlatAmount()[k].length() > 0)
                                dedDetail3.setFlatAmount(new BigDecimal(rsf.getFlatAmount()[k]));
                            else
                                dedDetail3.setFlatAmount(null);

                            if (rsf.getRecovDateFrom()[k] != null && rsf.getRecovDateFrom()[k].length() > 0) {
                                dt = new Date();
                                dt = sdf.parse(rsf.getRecovDateFrom()[k]);
                                final String fromDate = formatter.format(dt);

                                dedDetail3.setDatefrom(new Date(fromDate));
                            }
                            if (rsf.getRecovDateTo()[k] != null && rsf.getRecovDateTo()[k].length() > 0) {
                                dt = new Date();
                                dt = sdf.parse(rsf.getRecovDateTo()[k]);
                                final String toDate = formatter.format(dt);

                                dedDetail3.setDateto(new Date(toDate));
                            } else
                                dedDetail3.setDateto(null);
                            dedDetail3.setLastmodifieddate(curDate);
                            recoveryService.createEgDeductionDetails(dedDetail3);
                        }
                    } // else
                } // main for
            } // main if
            /* BOTH SIZE EQUAL BUT ID IS DIFFERENT MEANS UPDATE,DELETE,INSERT END */

            /* NOT EQUAL (MODEL SIZE > FORM BEAN SIZE)--- UPDATE,DELETE,INSERT START */

            else if (recoveryDetailList.size() > rsf.getId().length) {
                int i = 0, j = 0, k = 0;
                final Integer[] id = new Integer[recoveryDetailList.size()];

                for (; i < rsf.getId().length; i++) {
                    final EgDeductionDetails ded = recoveryDetailList.get(i);
                    id[i] = ded.getId().intValue();

                    if (id[i] == Integer.parseInt(rsf.getId()[i])) {
                        final EgDeductionDetails dedDetail4 = recoveryService.getEgDeductionDetailsById(Integer
                                .parseInt(rsf.getId()[i]));
                        dedDetail4.setRecovery(tds1);

                        if (rsf.getPartyType() != null) {
                            if (rsf.getPartyType()[i] != null && !rsf.getPartyType()[i].equals("0")
                                    && rsf.getPartyType()[i].length() > 0)
                                dedDetail4.setEgpartytype((EgPartytype) partyTypeDAO.findById(
                                        Integer.parseInt(rsf.getPartyType()[i]), false));
                            else
                                dedDetail4.setEgpartytype(null);
                        } else
                            dedDetail4.setEgpartytype(null);

                        if (rsf.getDocType() != null) {
                            if (rsf.getDocType()[i] != null && !rsf.getDocType()[i].equals("0")
                                    && rsf.getDocType()[i].length() > 0)
                                dedDetail4.setWorkDocType(typeOfWrkDAO.getTypeOfWorkById(Long.parseLong(rsf
                                        .getDocType()[i])));
                            else
                                dedDetail4.setWorkDocType(null);
                        } else
                            dedDetail4.setWorkDocType(null);

                        /*
                         * if (rsf.getDocType() != null) { if (rsf.getDocType()[i] != null && !rsf.getDocType()[i].equals("0") &&
                         * rsf.getDocType()[i].length() > 0) { if (rsf.getSubType()[i] != null && !rsf.getSubType()[i].equals("0")
                         * && rsf.getSubType()[i].length() > 0) dedDetail4.setWorkDocSubType((EgwTypeOfWork)
                         * cmnMngr.getTypeOfWorkById(Long.parseLong(rsf.getSubType()[i]))); else
                         * dedDetail4.setWorkDocSubType(null); } else dedDetail4.setWorkDocSubType(null); } else
                         * dedDetail4.setWorkDocSubType(null);
                         */

                        if (rsf.getLowAmount()[i] != null && rsf.getLowAmount()[i].length() > 0)
                            dedDetail4.setLowlimit(new BigDecimal(rsf.getLowAmount()[i]));

                        if (rsf.getHighAmount()[i] != null && rsf.getHighAmount()[i].length() > 0)
                            dedDetail4.setHighlimit(new BigDecimal(rsf.getHighAmount()[i]));
                        else
                            dedDetail4.setHighlimit(null);

                        if (rsf.getCumulativeAmountLow()[i] != null && rsf.getCumulativeAmountLow()[i].length() > 0)
                            dedDetail4.setCumulativeLowLimit(new BigDecimal(rsf.getCumulativeAmountLow()[i]));

                        if (rsf.getCumulativeAmountHigh()[i] != null && rsf.getCumulativeAmountHigh()[i].length() > 0)
                            dedDetail4.setCumulativeHighLimit(new BigDecimal(rsf.getCumulativeAmountHigh()[i]));

                        if (rsf.getITPercentage()[i] != null && rsf.getITPercentage()[i].length() > 0)
                            dedDetail4.setIncometax(new BigDecimal(rsf.getITPercentage()[i]));
                        else
                            dedDetail4.setIncometax(null);

                        if (rsf.getSurPercentage()[i] != null && rsf.getSurPercentage()[i].length() > 0)
                            dedDetail4.setSurcharge(new BigDecimal(rsf.getSurPercentage()[i]));
                        else
                            dedDetail4.setSurcharge(null);

                        if (rsf.getEduCessPercentage()[i] != null && rsf.getEduCessPercentage()[i].length() > 0)
                            dedDetail4.setEducation(new BigDecimal(rsf.getEduCessPercentage()[i]));
                        else
                            dedDetail4.setEducation(null);

                        if (rsf.getFlatAmount()[i] != null && rsf.getFlatAmount()[i].length() > 0)
                            dedDetail4.setFlatAmount(new BigDecimal(rsf.getFlatAmount()[i]));
                        else
                            dedDetail4.setFlatAmount(null);

                        if (rsf.getRecovDateFrom()[i] != null && rsf.getRecovDateFrom()[i].length() > 0) {
                            dt = new Date();
                            dt = sdf.parse(rsf.getRecovDateFrom()[i]);
                            final String fromDate = formatter.format(dt);

                            dedDetail4.setDatefrom(new Date(fromDate));
                        }
                        if (rsf.getRecovDateTo()[i] != null && rsf.getRecovDateTo()[i].length() > 0) {
                            dt = new Date();
                            dt = sdf.parse(rsf.getRecovDateTo()[i]);
                            final String toDate = formatter.format(dt);

                            dedDetail4.setDateto(new Date(toDate));
                        } else
                            dedDetail4.setDateto(null);

                        dedDetail4.setLastmodifieddate(curDate);

                        recoveryService.updateEgDeductionDetails(dedDetail4);
                        j = i + 1;
                        k = i + 1;

                    } // if

                } // for

                for (; j < recoveryDetailList.size(); j++) {
                    final EgDeductionDetails dedDetail2 = recoveryDetailList.get(j);
                    // detailDAO.delete(dedDetail2);
                    recoveryService.deleteEgDeductionDetails(dedDetail2);
                }
                for (; k < rsf.getId().length; k++) {
                    EgDeductionDetails dedDetail3 = null;
                    dedDetail3 = new EgDeductionDetails();

                    dedDetail3.setRecovery(tds1);

                    if (rsf.getPartyType() != null) {
                        if (rsf.getPartyType()[k] != null && !rsf.getPartyType()[k].equals("0")
                                && rsf.getPartyType()[k].length() > 0)
                            dedDetail3.setEgpartytype((EgPartytype) partyTypeDAO.findById(
                                    Integer.parseInt(rsf.getPartyType()[k]), false));
                        else
                            dedDetail3.setEgpartytype(null);
                    } else
                        dedDetail3.setEgpartytype(null);

                    if (rsf.getDocType() != null) {
                        if (rsf.getDocType()[k] != null && !rsf.getDocType()[k].equals("0") && rsf.getDocType()[k].length() > 0)
                            dedDetail.setWorkDocType(typeOfWrkDAO.getTypeOfWorkById(Long.parseLong(rsf
                                    .getDocType()[k])));
                        else
                            dedDetail.setWorkDocType(null);
                    } else
                        dedDetail.setWorkDocType(null);

                    /*
                     * if (rsf.getDocType() != null) { if (rsf.getDocType()[k] != null && !rsf.getDocType()[k].equals("0") &&
                     * rsf.getDocType()[k].length() > 0) { if (rsf.getSubType()[k] != null && !rsf.getSubType()[k].equals("0") &&
                     * rsf.getSubType()[k].length() > 0) dedDetail3.setWorkDocSubType((EgwTypeOfWork)
                     * cmnMngr.getTypeOfWorkById(Long.parseLong(rsf.getSubType()[k]))); else dedDetail3.setWorkDocSubType(null); }
                     * else dedDetail3.setWorkDocSubType(null); } else dedDetail3.setWorkDocSubType(null);
                     */

                    if (rsf.getLowAmount()[k] != null && rsf.getLowAmount()[k].length() > 0)
                        dedDetail3.setLowlimit(new BigDecimal(rsf.getLowAmount()[k]));

                    if (rsf.getHighAmount()[k] != null && rsf.getHighAmount()[k].length() > 0)
                        dedDetail3.setHighlimit(new BigDecimal(rsf.getHighAmount()[k]));
                    else
                        dedDetail3.setHighlimit(null);

                    if (rsf.getCumulativeAmountLow()[k] != null && rsf.getCumulativeAmountLow()[k].length() > 0)
                        dedDetail3.setCumulativeLowLimit(new BigDecimal(rsf.getCumulativeAmountLow()[k]));

                    if (rsf.getCumulativeAmountHigh()[k] != null && rsf.getCumulativeAmountHigh()[k].length() > 0)
                        dedDetail3.setCumulativeHighLimit(new BigDecimal(rsf.getCumulativeAmountHigh()[k]));

                    if (rsf.getITPercentage()[k] != null && rsf.getITPercentage()[k].length() > 0)
                        dedDetail3.setIncometax(new BigDecimal(rsf.getITPercentage()[k]));
                    else
                        dedDetail3.setIncometax(null);

                    if (rsf.getSurPercentage()[k] != null && rsf.getSurPercentage()[k].length() > 0)
                        dedDetail3.setSurcharge(new BigDecimal(rsf.getSurPercentage()[k]));
                    else
                        dedDetail3.setSurcharge(null);

                    if (rsf.getEduCessPercentage()[k] != null && rsf.getEduCessPercentage()[k].length() > 0)
                        dedDetail3.setEducation(new BigDecimal(rsf.getEduCessPercentage()[k]));
                    else
                        dedDetail3.setEducation(null);

                    if (rsf.getFlatAmount()[k] != null && rsf.getFlatAmount()[k].length() > 0)
                        dedDetail3.setFlatAmount(new BigDecimal(rsf.getFlatAmount()[k]));
                    else
                        dedDetail3.setFlatAmount(null);

                    if (rsf.getRecovDateFrom()[k] != null && rsf.getRecovDateFrom()[k].length() > 0) {
                        dt = new Date();
                        dt = sdf.parse(rsf.getRecovDateFrom()[k]);
                        final String fromDate = formatter.format(dt);

                        dedDetail3.setDatefrom(new Date(fromDate));
                    }
                    if (rsf.getRecovDateTo()[k] != null && rsf.getRecovDateTo()[k].length() > 0) {
                        dt = new Date();
                        dt = sdf.parse(rsf.getRecovDateTo()[k]);
                        final String toDate = formatter.format(dt);

                        dedDetail3.setDateto(new Date(toDate));
                    } else
                        dedDetail3.setDateto(null);

                    dedDetail3.setLastmodifieddate(curDate);

                    recoveryService.createEgDeductionDetails(dedDetail3);
                }

            }
            else /* NOT EQUAL (MODEL SIZE < FORM BEAN SIZE)--- UPDATE,DELETE,INSERT START */
                if (recoveryDetailList.size() < rsf.getId().length) {
                    int i = 0, j = 0, k = 0;
                    final Integer[] id = new Integer[rsf.getId().length];

                    for (; i < recoveryDetailList.size(); i++) {

                        final EgDeductionDetails ded = recoveryDetailList.get(i);
                        id[i] = ded.getId().intValue();

                        if (id[i] == Integer.parseInt(rsf.getId()[i])) {

                            final EgDeductionDetails dedDetail5 = recoveryService
                                    .getEgDeductionDetailsById(Integer.parseInt(rsf.getId()[i]));
                            dedDetail5.setRecovery(tds1);

                            if (rsf.getPartyType() != null) {
                                if (rsf.getPartyType()[i] != null && !rsf.getPartyType()[i].equals("0")
                                        && rsf.getPartyType()[i].length() > 0)
                                    dedDetail5.setEgpartytype((EgPartytype) partyTypeDAO.findById(
                                            Integer.parseInt(rsf.getPartyType()[i]), false));
                                else
                                    dedDetail5.setEgpartytype(null);
                            } else
                                dedDetail5.setEgpartytype(null);

                            if (rsf.getDocType() != null) {
                                if (rsf.getDocType()[i] != null && !rsf.getDocType()[i].equals("0")
                                        && rsf.getDocType()[i].length() > 0)
                                    dedDetail5.setWorkDocType(typeOfWrkDAO.getTypeOfWorkById(Long.parseLong(rsf
                                            .getDocType()[i])));
                                else
                                    dedDetail5.setWorkDocType(null);
                            } else
                                dedDetail5.setWorkDocType(null);

                            /*
                         * if (rsf.getDocType() != null) { if (rsf.getDocType()[i] != null && !rsf.getDocType()[i].equals("0") &&
                         * rsf.getDocType()[i].length() > 0) { if (rsf.getSubType()[i] != null && !rsf.getSubType()[i].equals("0")
                         * && rsf.getSubType()[i].length() > 0) dedDetail5.setWorkDocSubType((EgwTypeOfWork)
                         * cmnMngr.getTypeOfWorkById(Long.parseLong(rsf.getSubType()[i]))); else
                         * dedDetail5.setWorkDocSubType(null); } else dedDetail5.setWorkDocSubType(null); } else
                         * dedDetail5.setWorkDocSubType(null);
                         */

                            if (rsf.getLowAmount()[i] != null && rsf.getLowAmount()[i].length() > 0)
                                dedDetail5.setLowlimit(new BigDecimal(rsf.getLowAmount()[i]));

                            if (rsf.getHighAmount()[i] != null && rsf.getHighAmount()[i].length() > 0)
                                dedDetail5.setHighlimit(new BigDecimal(rsf.getHighAmount()[i]));
                            else
                                dedDetail5.setHighlimit(null);

                            if (rsf.getCumulativeAmountLow()[i] != null && rsf.getCumulativeAmountLow()[i].length() > 0)
                                dedDetail5.setCumulativeLowLimit(new BigDecimal(rsf.getCumulativeAmountLow()[i]));

                            if (rsf.getCumulativeAmountHigh()[i] != null && rsf.getCumulativeAmountHigh()[i].length() > 0)
                                dedDetail5.setCumulativeHighLimit(new BigDecimal(rsf.getCumulativeAmountHigh()[i]));

                            if (rsf.getITPercentage()[i] != null && rsf.getITPercentage()[i].length() > 0)
                                dedDetail5.setIncometax(new BigDecimal(rsf.getITPercentage()[i]));
                            else
                                dedDetail5.setIncometax(null);

                            if (rsf.getSurPercentage()[i] != null && rsf.getSurPercentage()[i].length() > 0)
                                dedDetail5.setSurcharge(new BigDecimal(rsf.getSurPercentage()[i]));
                            else
                                dedDetail5.setSurcharge(null);

                            if (rsf.getEduCessPercentage()[i] != null && rsf.getEduCessPercentage()[i].length() > 0)
                                dedDetail5.setEducation(new BigDecimal(rsf.getEduCessPercentage()[i]));
                            else
                                dedDetail5.setEducation(null);

                            if (rsf.getFlatAmount()[i] != null && rsf.getFlatAmount()[i].length() > 0)
                                dedDetail5.setFlatAmount(new BigDecimal(rsf.getFlatAmount()[i]));
                            else
                                dedDetail5.setFlatAmount(null);

                            if (rsf.getRecovDateFrom()[i] != null && rsf.getRecovDateFrom()[i].length() > 0) {
                                dt = new Date();
                                dt = sdf.parse(rsf.getRecovDateFrom()[i]);
                                final String fromDate = formatter.format(dt);

                                dedDetail5.setDatefrom(new Date(fromDate));
                            }
                            if (rsf.getRecovDateTo()[i] != null && rsf.getRecovDateTo()[i].length() > 0) {
                                dt = new Date();
                                dt = sdf.parse(rsf.getRecovDateTo()[i]);
                                final String toDate = formatter.format(dt);

                                dedDetail5.setDateto(new Date(toDate));
                            } else
                                dedDetail5.setDateto(null);

                            dedDetail5.setLastmodifieddate(curDate);

                            recoveryService.updateEgDeductionDetails(dedDetail5);

                            j = i + 1;
                            k = i + 1;
                        } // if

                    } // for
                    for (; j < recoveryDetailList.size(); j++) {
                        final EgDeductionDetails dedDetail2 = recoveryDetailList.get(j);

                        recoveryService.deleteEgDeductionDetails(dedDetail2);
                    }
                    for (; k < rsf.getId().length; k++) {
                        EgDeductionDetails dedDetail6 = null;
                        dedDetail6 = new EgDeductionDetails();

                        dedDetail6.setRecovery(tds1);
                        if (rsf.getPartyType() != null) {
                            if (rsf.getPartyType()[k] != null && !rsf.getPartyType()[k].equals("0")
                                    && rsf.getPartyType()[k].length() > 0)
                                dedDetail6.setEgpartytype((EgPartytype) partyTypeDAO.findById(
                                        Integer.parseInt(rsf.getPartyType()[k]), false));
                            else
                                dedDetail6.setEgpartytype(null);
                        } else
                            dedDetail6.setEgpartytype(null);

                        if (rsf.getDocType() != null) {
                            if (rsf.getDocType()[k] != null && !rsf.getDocType()[k].equals("0")
                                    && rsf.getDocType()[k].length() > 0)
                                dedDetail6.setWorkDocType(typeOfWrkDAO.getTypeOfWorkById(Long.parseLong(rsf
                                        .getDocType()[k])));
                            else
                                dedDetail6.setWorkDocType(null);
                        } else
                            dedDetail6.setWorkDocType(null);

                        /*
                     * if (rsf.getDocType() != null) { if (rsf.getDocType()[k] != null && !rsf.getDocType()[k].equals("0") &&
                     * rsf.getDocType()[k].length() > 0) { if (rsf.getSubType()[k] != null && !rsf.getSubType()[k].equals("0") &&
                     * rsf.getSubType()[k].length() > 0) dedDetail6.setWorkDocSubType((EgwTypeOfWork)
                     * cmnMngr.getTypeOfWorkById(Long.parseLong(rsf.getSubType()[k]))); else dedDetail6.setWorkDocSubType(null); }
                     * else dedDetail6.setWorkDocSubType(null); } else dedDetail6.setWorkDocSubType(null);
                     */

                        if (rsf.getLowAmount()[k] != null && rsf.getLowAmount()[k].length() > 0)
                            dedDetail6.setLowlimit(new BigDecimal(rsf.getLowAmount()[k]));

                        if (rsf.getHighAmount()[k] != null && rsf.getHighAmount()[k].length() > 0)
                            dedDetail6.setHighlimit(new BigDecimal(rsf.getHighAmount()[k]));
                        else
                            dedDetail6.setHighlimit(null);

                        if (rsf.getCumulativeAmountLow()[k] != null && rsf.getCumulativeAmountLow()[k].length() > 0)
                            dedDetail6.setCumulativeLowLimit(new BigDecimal(rsf.getCumulativeAmountLow()[k]));

                        if (rsf.getCumulativeAmountHigh()[k] != null && rsf.getCumulativeAmountHigh()[k].length() > 0)
                            dedDetail6.setCumulativeHighLimit(new BigDecimal(rsf.getCumulativeAmountHigh()[k]));

                        if (rsf.getITPercentage()[k] != null && rsf.getITPercentage()[k].length() > 0)
                            dedDetail6.setIncometax(new BigDecimal(rsf.getITPercentage()[k]));
                        else
                            dedDetail6.setIncometax(null);

                        if (rsf.getSurPercentage()[k] != null && rsf.getSurPercentage()[k].length() > 0)
                            dedDetail6.setSurcharge(new BigDecimal(rsf.getSurPercentage()[k]));
                        else
                            dedDetail6.setSurcharge(null);

                        if (rsf.getEduCessPercentage()[k] != null && rsf.getEduCessPercentage()[k].length() > 0)
                            dedDetail6.setEducation(new BigDecimal(rsf.getEduCessPercentage()[k]));
                        else
                            dedDetail6.setEducation(null);

                        if (rsf.getFlatAmount()[k] != null && rsf.getFlatAmount()[k].length() > 0)
                            dedDetail6.setFlatAmount(new BigDecimal(rsf.getFlatAmount()[k]));
                        else
                            dedDetail6.setFlatAmount(null);

                        if (rsf.getRecovDateFrom()[k] != null && rsf.getRecovDateFrom()[k].length() > 0) {
                            dt = new Date();
                            dt = sdf.parse(rsf.getRecovDateFrom()[k]);
                            final String fromDate = formatter.format(dt);

                            dedDetail6.setDatefrom(new Date(fromDate));
                        }
                        if (rsf.getRecovDateTo()[k] != null && rsf.getRecovDateTo()[k].length() > 0) {
                            dt = new Date();
                            dt = sdf.parse(rsf.getRecovDateTo()[k]);
                            final String toDate = formatter.format(dt);

                            dedDetail6.setDateto(new Date(toDate));
                        } else
                            dedDetail6.setDateto(null);

                        dedDetail6.setLastmodifieddate(curDate);

                        recoveryService.createEgDeductionDetails(dedDetail6);
                    }

                } // main if

            /* NOT EQUAL (MODEL SIZE < FORM BEAN SIZE)--- UPDATE,DELETE,INSERT END */
        }

        EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeMaster");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-partyTypeAllChild");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-typeOfWorkParent");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodesForLiability");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-AllCoaCodesOfEarning");

        EgovMasterDataCaching.getInstance().removeFromCache("egi-tds");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-tdsType");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-recovery");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-egwTypeOfWork");
        EgovMasterDataCaching.getInstance().removeFromCache("egi-egwSubTypeOfWork");

    }

    public void getRecoveryAndPopulateBean(final RecoverySetupForm rsf) throws Exception {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("inside getRecoveryAndPopulateBean");

        final Recovery tds = recoveryService.findById(Long.parseLong(rsf.getTdsTypeId()));
        rsf.setTdsIdHidden(tds.getId().toString());
        rsf.setRecovCode(tds.getType());
        rsf.setRecovName(tds.getRecoveryName());
        rsf.setRecovIFSCCode(tds.getIfscCode());
        rsf.setRecovBankAccount(tds.getAccountNumber());
        rsf.setRecovAccCodeId(tds.getChartofaccounts().getId().toString());
        rsf.setRecAccDesc(tds.getChartofaccounts().getName());
        rsf.setRecovAppliedTo(tds.getEgPartytype().getId().toString());
        if (tds.getCaplimit() != null)
            rsf.setCapLimit(tds.getCaplimit().toString());
        //rsf.setIsEarning(tds.getIsEarning());

        rsf.setCalculationType(tds.getCalculationType());
       // rsf.setSection(tds.getSection());
        rsf.setDescription(tds.getDescription());

        final List<EgDeductionDetails> recoveryDetailList = recoveryService.findByTds(tds);
        if (!recoveryDetailList.isEmpty()) {
            final int listSize = recoveryDetailList.size();
            rsf.setRecMode("Automatic");
            rsf.setBankLoan("off");
            rsf.setRecovRemitTo(tds.getRemitted());
            //rsf.setRecovBSRCode(tds.getBsrcode());

            final String[] id = new String[listSize];
            final String[] appliedToHiddenId = new String[listSize];
            final String[] partyType = new String[listSize];
            final String[] docType = new String[listSize];
            final String[] subType = new String[listSize];
            final String[] recovDateFrom = new String[listSize];
            final String[] recovDateTo = new String[listSize];

            final String[] lowAmount = new String[listSize];
            final String[] highAmount = new String[listSize];
            final String[] ITPercentage = new String[listSize];
            final String[] surPercentage = new String[listSize];
            final String[] eduCessPercentage = new String[listSize];
            final String[] flatAmount = new String[listSize];
            final String[] cumulativeAmtHigh = new String[listSize];
            final String[] cumulativeAmtLow = new String[listSize];

            for (int n = 0; n < listSize; n++) {
                final EgDeductionDetails ded = recoveryDetailList.get(n);
                id[n] = ded.getId().toString();
                appliedToHiddenId[n] = ded.getRecovery().getEgPartytype().getId().toString();
                if (ded.getEgpartytype() != null) {
                    if (ded.getEgpartytype().getId() != null)
                        partyType[n] = ded.getEgpartytype().getId().toString();
                } else
                    partyType[n] = "";

                if (ded.getWorkDocType() != null) {
                    if (ded.getWorkDocType().getId() != null)
                        docType[n] = ded.getWorkDocType().getId().toString();
                } else
                    docType[n] = "";

                if (ded.getWorkDocSubType() != null) {
                    if (ded.getWorkDocSubType().getId() != null)
                        subType[n] = ded.getWorkDocSubType().getId().toString();
                } else
                    subType[n] = "";

                if (ded.getDatefrom() != null) {
                    final String fromDate = formatter2.format(ded.getDatefrom());
                    recovDateFrom[n] = fromDate;
                } else
                    recovDateFrom[n] = "";

                if (ded.getDateto() != null) {
                    final String toDate = formatter2.format(ded.getDateto());
                    recovDateTo[n] = toDate;
                } else
                    recovDateTo[n] = "";

                lowAmount[n] = ded.getLowlimit().toString();

                if (ded.getHighlimit() != null)
                    highAmount[n] = ded.getHighlimit().toString();
                else
                    highAmount[n] = "";

                if (ded.getIncometax() != null)
                    ITPercentage[n] = ded.getIncometax().toString();
                else
                    ITPercentage[n] = "";

                if (ded.getSurcharge() != null)
                    surPercentage[n] = ded.getSurcharge().toString();
                else
                    surPercentage[n] = "";

                if (ded.getEducation() != null)
                    eduCessPercentage[n] = ded.getEducation().toString();
                else
                    eduCessPercentage[n] = "";

                if (ded.getFlatAmount() != null)
                    flatAmount[n] = ded.getFlatAmount().toString();
                else
                    flatAmount[n] = "";

                if (ded.getCumulativeHighLimit() != null)
                    cumulativeAmtHigh[n] = ded.getCumulativeHighLimit().toString();
                else
                    cumulativeAmtHigh[n] = "";

                if (ded.getCumulativeLowLimit() != null)
                    cumulativeAmtLow[n] = ded.getCumulativeLowLimit().toString();
                else
                    cumulativeAmtLow[n] = "";

            } // for

            rsf.setId(id);
            rsf.setAppliedToHiddenId(appliedToHiddenId);
            rsf.setPartyType(partyType);
            rsf.setDocType(docType);
            rsf.setSubType(subType);
            rsf.setRecovDateFrom(recovDateFrom);
            rsf.setRecovDateTo(recovDateTo);
            rsf.setLowAmount(lowAmount);
            rsf.setHighAmount(highAmount);

            rsf.setITPercentage(ITPercentage);
            rsf.setSurPercentage(surPercentage);
            rsf.setEduCessPercentage(eduCessPercentage);
            rsf.setFlatAmount(flatAmount);

            rsf.setCumulativeAmountLow(cumulativeAmtLow);
            rsf.setCumulativeAmountHigh(cumulativeAmtHigh);

        } // main if
        else {
            rsf.setRecMode("Manual");
            rsf.setRecovRemitTo(tds.getRemitted());
            //rsf.setRecovBSRCode(tds.getBsrcode());
            if (tds.getBank() != null) {
                rsf.setBankLoan("on");
                rsf.setBank(tds.getBank().getId().toString());
            } else
                rsf.setBankLoan("off");
        }
    }// method

}