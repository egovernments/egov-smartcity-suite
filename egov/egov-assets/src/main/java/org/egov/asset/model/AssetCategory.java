/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.asset.model;

import org.egov.asset.util.AssetConstants;
import org.egov.common.entity.UOM;
import org.egov.commons.CChartOfAccounts;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Unique(fields = { "code" }, id = "id", tableName = "EGASSET_ASSET_CATEGORY", columnName = {
        "CODE" }, message = "assetcat.code.isunique")
public class AssetCategory extends BaseModel {

    private static final long serialVersionUID = 4664412673598282808L;

    /** default constructor */
    public AssetCategory() {
    }

    @Required(message = "assetcat.assetaccountcode.null")
    private CChartOfAccounts assetAccountCode;

    private Long maxLife;
    private CChartOfAccounts accDepAccountCode;

    @Required(message = "assetcat.revaccountcode.null")
    private CChartOfAccounts revAccountCode;

    private DepreciationMethod depreciationMethod;

    private CChartOfAccounts depExpAccountCode;

    @Required(message = "assetcat.code.null")
    @Length(max = 50, message = "assetcat.code.length")
    @OptionalPattern(regex = AssetConstants.alphaNumericwithspecialchar, message = "assetcat.code.alphaNumericwithspecialchar")
    private String code;

    @Required(message = "assetcat.name.null")
    @Length(max = 100, message = "assetcat.name.length")
    @OptionalPattern(regex = AssetConstants.alphaNumericwithspecialchar, message = "assetcat.name.alphaNumericwithspecialchar")
    private String name;

    private String catAttrTemplate;

    @Required(message = "assetcat.assettype.null")
    private AssetType assetType;

    @Required(message = "assetcat.uom.null")
    private UOM uom;

    private AssetCategory parent;

    @Valid
    private List<DepreciationMetaData> depreciationMetaDataList = new LinkedList<DepreciationMetaData>();

    private List<Asset> assets = new LinkedList<Asset>();

    public CChartOfAccounts getAssetAccountCode() {
        return assetAccountCode;
    }

    public void setAssetAccountCode(final CChartOfAccounts assetAccountCode) {
        this.assetAccountCode = assetAccountCode;
    }

    public Long getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(final Long maxLife) {
        this.maxLife = maxLife;
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

    public String getCatAttrTemplate() {
        return catAttrTemplate;
    }

    public void setCatAttrTemplate(final String catAttrTemplate) {
        this.catAttrTemplate = catAttrTemplate;
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

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(final List<Asset> assets) {
        this.assets = assets;
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

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> errorList = new ArrayList<ValidationError>();
        if (depreciationMetaDataList != null && !depreciationMetaDataList.isEmpty())
            for (final DepreciationMetaData depMetaData : depreciationMetaDataList)
                errorList.addAll(depMetaData.validate());
        return errorList;
    }
}