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
package org.egov.assets.model;

import org.egov.assets.util.AssetConstants;
import org.egov.common.entity.UOM;
import org.egov.commons.CChartOfAccounts;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.exception.ValidationError;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Entity
@Table(name="EGASSET_ASSET_CATEGORY")
@SequenceGenerator(name = AssetCategory.SEQ, sequenceName = AssetCategory.SEQ, allocationSize = 1)
@Unique(fields = { "code" }, id = "id", tableName = "EGASSET_ASSET_CATEGORY", columnName = {
"CODE" }, message = "assetcat.code.isunique")
public class AssetCategory extends AbstractAuditable {

    private static final long serialVersionUID = 4664412673598282808L;

    /** default constructor */
    public AssetCategory() {
    }
    
    public static enum AssetType {
    	LAND, MOVABLEASSET, IMMOVABLEASSET
    }
    public static enum DepreciationMethod {
    	STRAIGHT_LINE_METHOD, WRITTENDOWN_VALUE_METHOD
    }
    
    public static final String SEQ = "seq_egasset_asset_category";
    
    @Id
    @GeneratedValue(generator =AssetCategory.SEQ ,strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_accountcode")
    @Required(message = "Required")
    private CChartOfAccounts assetAccountCode;

   
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accdep_accountcode")
    private CChartOfAccounts accDepAccountCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revaluation_accountcode")
    @Required(message = "Required")
    private CChartOfAccounts revAccountCode;

    
    @Enumerated(EnumType.STRING)
    @Column(name="depreciation_Method")
    private DepreciationMethod depreciationMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="depexp_accountcode")
    private CChartOfAccounts depExpAccountCode;

//    @Required(message = "assetcat.code.null")
    @Length(max = 50, message = "assetcat.code.length")
    @OptionalPattern(regex = AssetConstants.alphaNumericwithspecialchar, message = "assetcat.code.alphaNumericwithspecialchar")
    private String code;

    @Required(message = "Required")
    @Length(max = 100, message = "assetcat.name.length")
    @OptionalPattern(regex = AssetConstants.alphaNumericwithspecialchar, message = "assetcat.name.alphaNumericwithspecialchar")
    private String name;

    
    @Enumerated(EnumType.STRING)
    @Column(name="asset_type")
    @Required(message = "Required")
    private AssetType assetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="UOM_ID")
    @Required(message = "Required")
    private UOM uom;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="PARENTID")
    private AssetCategory parent;

    //not used as of now
    @OneToMany(orphanRemoval =true,cascade ={CascadeType.ALL,CascadeType.PERSIST})
    @JoinColumn(name ="DEPMD_AC_INDEX")
    private List<DepreciationMetaData> depreciationMetaDataList = new LinkedList<DepreciationMetaData>();
    
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL , fetch=FetchType.LAZY,mappedBy="assetCategory",targetEntity=CategoryPropertyType.class )
    private List<CategoryPropertyType> categoryProperties = new LinkedList<CategoryPropertyType>();


    public CChartOfAccounts getAssetAccountCode() {
        return assetAccountCode;
    }

    public void setAssetAccountCode(final CChartOfAccounts assetAccountCode) {
        this.assetAccountCode = assetAccountCode;
    }

    

    public CChartOfAccounts getAccDepAccountCode() {
        return accDepAccountCode;
    }

    public void setAccDepAccountCode(final CChartOfAccounts accDepAccountCode) {  
        this.accDepAccountCode = accDepAccountCode;
    }

    public CChartOfAccounts getRevAccountCode() {
        return revAccountCode;
    }

    public void setRevAccountCode(final CChartOfAccounts revAccountCode) {
        this.revAccountCode = revAccountCode;
    }

    public DepreciationMethod getDepreciationMethod() {
        return depreciationMethod;
    }

    public void setDepreciationMethod(final DepreciationMethod depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public CChartOfAccounts getDepExpAccountCode() {
        return depExpAccountCode;
    }

    public void setDepExpAccountCode(final CChartOfAccounts depExpAccountCode) {
        this.depExpAccountCode = depExpAccountCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

   

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(final AssetType assetType) {
        this.assetType = assetType;
    }

  public UOM getUom() {
        return uom;
    }

    public void setUom(final UOM uom) {
        this.uom = uom;
    } 

    public AssetCategory getParent() {
        return parent;
    }

    public void setParent(final AssetCategory parent) {
        this.parent = parent;
    }

    public List<DepreciationMetaData> getDepreciationMetaDataList() {
        return depreciationMetaDataList;
    }

    public void setDepreciationMetaDataList(final List<DepreciationMetaData> depreciationMetaDataList) {
        this.depreciationMetaDataList = depreciationMetaDataList;
    }

    public void addDepreciationMetaData(final DepreciationMetaData depreciationMetaData) {
        depreciationMetaDataList.add(depreciationMetaData);
    }

   
    public Long getId() {
        return id;
    }  

    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        final StringBuilder objString = new StringBuilder();
        final String NEW_LINE = System.getProperty("line.separator");
        final String NULL_STRING = "null";

        objString.append(this.getClass().getName() + " Object {" + NEW_LINE);
        objString.append(" Id: " + id + NEW_LINE);
        objString.append(" Name: " + name + NEW_LINE);
        objString.append(" Code: " + code + NEW_LINE);
        objString.append(" Asset Type: " + (assetType == null ? NULL_STRING : assetType.toString()) + NEW_LINE);
        objString.append(" Parent: " + (parent == null ? NULL_STRING : parent.getId()) + NEW_LINE);
        objString.append(" UOM: " + (uom == null ? NULL_STRING : uom.getId()) + NEW_LINE);
        objString.append("}");

        return objString.toString();
    }

   
    public List<ValidationError> validate() {
        final List<ValidationError> errorList = new ArrayList<ValidationError>();
        if (depreciationMetaDataList != null && !depreciationMetaDataList.isEmpty())
            for (final DepreciationMetaData depMetaData : depreciationMetaDataList)
                errorList.addAll(depMetaData.validate());
        return errorList;
    }

	public List<CategoryPropertyType> getCategoryProperties() {
		return categoryProperties;
	}

	public void setCategoryProperties(List<CategoryPropertyType> categoryProperties) {
		this.categoryProperties = categoryProperties;
	}
}
