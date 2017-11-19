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

package org.egov.ptis.client.model.calculator;

import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.wtms.ConsumerConsumption;
import org.egov.ptis.wtms.PropertyWiseConsumptions;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DemandNoticeInfo {
    private BasicProperty basicProperty;
    private String billNo;
    private String oldAssessmentNo;
    private String noOfTap;
    private String sewarageConnectionNo;
    private String rentPaid;
    private String locality;
    private List<DemandNoticeDetailsInfo> demandNoticeDetailsInfo;
    private ReportUtil reportUtil;
    private PropertyWiseConsumptions propertyWiseConsumptions;
    private String billPeriod;
    private Boolean isVacancyRemissionDone;
    
    
	@Autowired
    @Qualifier("cityService")
    private CityService cityService;

    // reading cityname and logo from citywebsiteservice to support bulkbillgeneration through schedular
    public City getcityWebsite() {
        final City cw = cityService.findAll().get(0);
        return cw;
    }

    public String getCityName() {
        final City cw = getcityWebsite();
        if (cw != null && cw.getPreferences() != null)
            return cw != null ? cw.getPreferences().getMunicipalityName() : null;
            return null;
    }

    public String getCityLogo() {
        String path = null;
        try {
            path = ReportUtil.logoBasePath();
        } catch (final Exception e) {
            Log.error(e.getMessage());
        }
        return path;
    }

    public String getWardNo() {
        return getBasicProperty().getPropertyID().getWard().getBoundaryNum().toString();
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(final String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    public String getName() {
        return basicProperty.getFullOwnerName();
    }

    public String getAssessmentNo() {
        return getBasicProperty().getUpicNo();
    }

    public String getOldAssessmentNo() {
        return oldAssessmentNo;
    }

    public void setOldAssessmentNo(final String oldAssessmentNo) {
        this.oldAssessmentNo = oldAssessmentNo;
    }

    public String getWaterConnectionNo() {
        String waterConnectionNo = "";
        if (propertyWiseConsumptions != null)
            if (propertyWiseConsumptions.getConsumerConsumptions() != null
            && !propertyWiseConsumptions.getConsumerConsumptions().isEmpty()) {
                String hscno = "";
                for (final ConsumerConsumption cc : propertyWiseConsumptions.getConsumerConsumptions())
                    if (cc != null)
                        if (hscno != null && hscno != "")
                            hscno = hscno + cc.getHscno() + ",";
                if (hscno != "" && hscno != null)
                    waterConnectionNo = hscno.substring(0, hscno.length() - 1);
            }

        return waterConnectionNo;
    }

    public String getNoOfTap() {
        if (propertyWiseConsumptions != null)
            if (propertyWiseConsumptions.getConsumerConsumptions() != null
            && !propertyWiseConsumptions.getConsumerConsumptions().isEmpty())
                noOfTap = Integer.toString(propertyWiseConsumptions.getConsumerConsumptions().size());
        return noOfTap;
    }

    public String getSewarageConnectionNo() {
        return sewarageConnectionNo;
    }

    public void setSewarageConnectionNo(final String sewarageConnectionNo) {
        this.sewarageConnectionNo = sewarageConnectionNo;
    }

    public String getRentPaid() {
        return rentPaid;
    }

    public void setRentPaid(final String rentPaid) {
        this.rentPaid = rentPaid;
    }

    public String getStreetName() {
        return basicProperty.getPropertyID().getStreet() != null ? basicProperty.getPropertyID().getStreet().getName()
                : null;
    }

    public String getHouseNo() {
        return getBasicProperty().getAddress().getHouseNoBldgApt();
    }

    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(final BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
    }

    public List<DemandNoticeDetailsInfo> getDemandNoticeDetailsInfo() {
        return demandNoticeDetailsInfo;
    }

    public void setDemandNoticeDetailsInfo(final List<DemandNoticeDetailsInfo> demandNoticeDetailsInfo) {
        this.demandNoticeDetailsInfo = demandNoticeDetailsInfo;
    }

    public ReportUtil getReportUtil() {
        return reportUtil;
    }

    public void setReportUtil(final ReportUtil reportUtil) {
        this.reportUtil = reportUtil;
    }

    public PropertyWiseConsumptions getPropertyWiseConsumptions() {
        return propertyWiseConsumptions;
    }

    public void setPropertyWiseConsumptions(final PropertyWiseConsumptions propertyWiseConsumptions) {
        this.propertyWiseConsumptions = propertyWiseConsumptions;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(final String locality) {
        this.locality = locality;
    }

    public String getBillPeriod() {
        return billPeriod;
    }

    public void setBillPeriod(final String billPeriod) {
        this.billPeriod = billPeriod;
    }

    public CityService getCityService() {
        return cityService;
    }

    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    public Boolean getIsVacancyRemissionDone() {
		return isVacancyRemissionDone;
	}

	public void setIsVacancyRemissionDone(Boolean isVacancyRemissionDone) {
		this.isVacancyRemissionDone = isVacancyRemissionDone;
	}
}
