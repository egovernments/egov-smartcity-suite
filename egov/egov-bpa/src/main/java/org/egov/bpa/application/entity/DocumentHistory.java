/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.
    Copyright (C) <2015>  eGovernments Foundation
    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.
        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.
        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.
  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.application.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EGBPA_DOCUMENTHISTORY")
@SequenceGenerator(name = DocumentHistory.SEQ_DOCUMENTHISTORY, sequenceName = DocumentHistory.SEQ_DOCUMENTHISTORY, allocationSize = 1)
public class DocumentHistory extends AbstractAuditable {

    private static final long serialVersionUID = 3078684328383202788L;
    public static final String SEQ_DOCUMENTHISTORY = "SEQ_EGBPA_DOCUMENTHISTORY";
    @Id
    @GeneratedValue(generator = SEQ_DOCUMENTHISTORY, strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private BpaApplication application;
    @NotNull
    @Length(min = 1, max = 256)
    private String documentNum;
    @ManyToOne(cascade = CascadeType.ALL)
    private User createdUser;
    private BigDecimal docEnclosedNumber;
    private BigDecimal docEnclosedDate;
    private BigDecimal docEnclosedextentInsqmt;
    private BigDecimal layoutdextentInsqmt;
    private Boolean wheatherdocumentEnclosed;
    private Boolean wheatherplotDevelopedBy;
    private Boolean wheatherpartOfLayout;
    @Length(min = 1, max = 256)
    private String plotDevelopedBy;
    private Boolean wheatherFmsOrSketchCopyOfReg;

    @OneToMany(mappedBy = "docHistory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DocumentHistoryDetail> documentHistoryDetail = new ArrayList<DocumentHistoryDetail>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getDocumentNum() {
        return documentNum;
    }

    public void setDocumentNum(final String documentNum) {
        this.documentNum = documentNum;
    }

    public BigDecimal getDocEnclosedextentInsqmt() {
        return docEnclosedextentInsqmt;
    }

    public void setDocEnclosedextentInsqmt(final BigDecimal docEnclosedextentInsqmt) {
        this.docEnclosedextentInsqmt = docEnclosedextentInsqmt;
    }

    public BigDecimal getLayoutdextentInsqmt() {
        return layoutdextentInsqmt;
    }

    public void setLayoutdextentInsqmt(final BigDecimal layoutdextentInsqmt) {
        this.layoutdextentInsqmt = layoutdextentInsqmt;
    }

    public Boolean getWheatherdocumentEnclosed() {
        return wheatherdocumentEnclosed;
    }

    public void setWheatherdocumentEnclosed(final Boolean wheatherdocumentEnclosed) {
        this.wheatherdocumentEnclosed = wheatherdocumentEnclosed;
    }

    public Boolean getWheatherplotDevelopedBy() {
        return wheatherplotDevelopedBy;
    }

    public void setWheatherplotDevelopedBy(final Boolean wheatherplotDevelopedBy) {
        this.wheatherplotDevelopedBy = wheatherplotDevelopedBy;
    }

    public Boolean getWheatherpartOfLayout() {
        return wheatherpartOfLayout;
    }

    public void setWheatherpartOfLayout(final Boolean wheatherpartOfLayout) {
        this.wheatherpartOfLayout = wheatherpartOfLayout;
    }

    public String getPlotDevelopedBy() {
        return plotDevelopedBy;
    }

    public void setPlotDevelopedBy(final String plotDevelopedBy) {
        this.plotDevelopedBy = plotDevelopedBy;
    }

    public Boolean getWheatherFmsOrSketchCopyOfReg() {
        return wheatherFmsOrSketchCopyOfReg;
    }

    public void setWheatherFmsOrSketchCopyOfReg(final Boolean wheatherFmsOrSketchCopyOfReg) {
        this.wheatherFmsOrSketchCopyOfReg = wheatherFmsOrSketchCopyOfReg;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(final User createdUser) {
        this.createdUser = createdUser;
    }

    public BigDecimal getDocEnclosedNumber() {
        return docEnclosedNumber;
    }

    public void setDocEnclosedNumber(final BigDecimal docEnclosedNumber) {
        this.docEnclosedNumber = docEnclosedNumber;
    }

    public BigDecimal getDocEnclosedDate() {
        return docEnclosedDate;
    }

    public void setDocEnclosedDate(final BigDecimal docEnclosedDate) {
        this.docEnclosedDate = docEnclosedDate;
    }

    public BpaApplication getApplication() {
        return application;
    }

    public void setApplication(final BpaApplication application) {
        this.application = application;
    }

    public List<DocumentHistoryDetail> getDocumentHistoryDetail() {
        return documentHistoryDetail;
    }

}