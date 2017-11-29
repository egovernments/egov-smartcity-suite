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
package org.egov.commons;


import org.egov.infra.persistence.entity.AbstractAuditable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "chequeformat")
@SequenceGenerator(name = ChequeFormat.SEQ, sequenceName = ChequeFormat.SEQ, allocationSize = 1)
public class ChequeFormat extends AbstractAuditable{
    private static final long serialVersionUID = 1L;
    public static final String SEQ = "seq_chequeformat";

    @Id
    @GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
    private Long id;
    private String chequeName;
    private String chequeType;
    private Double chequeLength;
    private Double chequeWidth;
    private String accountPayeeCoordinate;// Value to be printed is A/C Payee
    private String dateFormat;
    private String dateCoordinate;
    private Double payeeNameLength;
    private String payeeNameCoordinate;
    private String amountNumberingFormat;
    private String amountInWordsFirstLineCoordinate;
    private Double amountInWordsFirstLineLength;
    private Double amountInWordsSecondLineLength;
    private String amountInWordsSecondLineCoordinate;
    private Double amountLength;
    private String amountCoordinate;
    private boolean formatStatus;
    
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static String getSeq() {
        return SEQ;
    }

    public Long getId() {
        return id;
    }

    public String getChequeName() {
        return chequeName;
    }

    public String getChequeType() {
        return chequeType;
    }

    public Double getChequeLength() {
        return chequeLength;
    }

    public Double getChequeWidth() {
        return chequeWidth;
    }

    public String getAccountPayeeCoordinate() {
        return accountPayeeCoordinate;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getDateCoordinate() {
        return dateCoordinate;
    }

    public Double getPayeeNameLength() {
        return payeeNameLength;
    }

    public String getPayeeNameCoordinate() {
        return payeeNameCoordinate;
    }

    public String getAmountNumberingFormat() {
        return amountNumberingFormat;
    }

    public String getAmountInWordsFirstLineCoordinate() {
        return amountInWordsFirstLineCoordinate;
    }

    public Double getAmountInWordsFirstLineLength() {
        return amountInWordsFirstLineLength;
    }

    public Double getAmountInWordsSecondLineLength() {
        return amountInWordsSecondLineLength;
    }

    public String getAmountInWordsSecondLineCoordinate() {
        return amountInWordsSecondLineCoordinate;
    }

    public Double getAmountLength() {
        return amountLength;
    }

    public String getAmountCoordinate() {
        return amountCoordinate;
    }

    public boolean isFormatStatus() {
        return formatStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChequeName(String chequeName) {
        this.chequeName = chequeName;
    }

    public void setChequeType(String chequeType) {
        this.chequeType = chequeType;
    }

    public void setChequeLength(Double chequeLength) {
        this.chequeLength = chequeLength;
    }

    public void setChequeWidth(Double chequeWidth) {
        this.chequeWidth = chequeWidth;
    }

    public void setAccountPayeeCoordinate(String accountPayeeCoordinate) {
        this.accountPayeeCoordinate = accountPayeeCoordinate;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setDateCoordinate(String dateCoordinate) {
        this.dateCoordinate = dateCoordinate;
    }

    public void setPayeeNameLength(Double payeeNameLength) {
        this.payeeNameLength = payeeNameLength;
    }

    public void setPayeeNameCoordinate(String payeeNameCoordinate) {
        this.payeeNameCoordinate = payeeNameCoordinate;
    }

    public void setAmountNumberingFormat(String amountNumberingFormat) {
        this.amountNumberingFormat = amountNumberingFormat;
    }

    public void setAmountInWordsFirstLineCoordinate(String amountInWordsFirstLineCoordinate) {
        this.amountInWordsFirstLineCoordinate = amountInWordsFirstLineCoordinate;
    }

    public void setAmountInWordsFirstLineLength(Double amountInWordsFirstLineLength) {
        this.amountInWordsFirstLineLength = amountInWordsFirstLineLength;
    }

    public void setAmountInWordsSecondLineLength(Double amountInWordsSecondLineLength) {
        this.amountInWordsSecondLineLength = amountInWordsSecondLineLength;
    }

    public void setAmountInWordsSecondLineCoordinate(String amountInWordsSecondLineCoordinate) {
        this.amountInWordsSecondLineCoordinate = amountInWordsSecondLineCoordinate;
    }

    public void setAmountLength(Double amountLength) {
        this.amountLength = amountLength;
    }

    public void setAmountCoordinate(String amountCoordinate) {
        this.amountCoordinate = amountCoordinate;
    }

    public void setFormatStatus(boolean formatStatus) {
        this.formatStatus = formatStatus;
    }
    

}
