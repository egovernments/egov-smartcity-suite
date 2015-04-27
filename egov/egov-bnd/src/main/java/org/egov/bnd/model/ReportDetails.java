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
package org.egov.bnd.model;

import org.egov.infra.workflow.entity.StateAware;

@SuppressWarnings("serial")
public class ReportDetails extends StateAware {

    /**
     *
     */
    private static final long serialVersionUID = -3575241288223620580L;
    private String month;
    private String regUnit;
    private Integer birthMale;
    private Integer birthFemale;
    private Integer birthTotal;
    private Integer slNo;
    private Integer birthMaleprogressive;
    private Integer birthTotalprogressive;
    private Integer deathMale;
    private Integer deathFemale;
    private Integer deathTotal;
    private Integer deathMaleprogressive;
    private Integer deathFemaleprogressive;
    private Integer deathTotalprogressive;
    private Integer stillbirth;
    private Integer stillbirthprogressive;
    private Integer infantdeath;
    private Integer infantdeathprogressive;
    private Integer deliverydeath;
    private Integer deliverydeathprogressive;
    private Integer certifieddeath;
    private Integer certifieddeathprogressive;
    private Integer stillbirthmale;
    private Integer stillbirthmaleprogressive;
    private Integer stillbirthfemale;
    private Integer stillbirthfemaleprogressive;
    private Integer birthFemaleprogressive;

    public Integer getStillbirthmale() {
        return stillbirthmale;
    }

    public void setStillbirthmale(final Integer stillbirthmale) {
        this.stillbirthmale = stillbirthmale;
    }

    public Integer getStillbirthmaleprogressive() {
        return stillbirthmaleprogressive;
    }

    public void setStillbirthmaleprogressive(final Integer stillbirthmaleprogressive) {
        this.stillbirthmaleprogressive = stillbirthmaleprogressive;
    }

    public Integer getStillbirthfemale() {
        return stillbirthfemale;
    }

    public void setStillbirthfemale(final Integer stillbirthfemale) {
        this.stillbirthfemale = stillbirthfemale;
    }

    public Integer getStillbirthfemaleprogressive() {
        return stillbirthfemaleprogressive;
    }

    public void setStillbirthfemaleprogressive(final Integer stillbirthfemaleprogressive) {
        this.stillbirthfemaleprogressive = stillbirthfemaleprogressive;
    }

    public Integer getBirthMaleprogressive() {
        return birthMaleprogressive;
    }

    public void setBirthMaleprogressive(final Integer birthMaleprogressive) {
        this.birthMaleprogressive = birthMaleprogressive;
    }

    public Integer getBirthFemaleprogressive() {
        return birthFemaleprogressive;
    }

    public void setBirthFemaleprogressive(final Integer birthFemaleprogressive) {
        this.birthFemaleprogressive = birthFemaleprogressive;
    }

    public Integer getBirthTotalprogressive() {
        return birthTotalprogressive;
    }

    public void setBirthTotalprogressive(final Integer birthTotalprogressive) {
        this.birthTotalprogressive = birthTotalprogressive;
    }

    public Integer getDeathMaleprogressive() {
        return deathMaleprogressive;
    }

    public void setDeathMaleprogressive(final Integer deathMaleprogressive) {
        this.deathMaleprogressive = deathMaleprogressive;
    }

    public Integer getDeathFemaleprogressive() {
        return deathFemaleprogressive;
    }

    public void setDeathFemaleprogressive(final Integer deathFemaleprogressive) {
        this.deathFemaleprogressive = deathFemaleprogressive;
    }

    public Integer getDeathTotalprogressive() {
        return deathTotalprogressive;
    }

    public void setDeathTotalprogressive(final Integer deathTotalprogressive) {
        this.deathTotalprogressive = deathTotalprogressive;
    }

    public String getRegUnit() {
        return regUnit;
    }

    public void setRegUnit(final String regUnit) {
        this.regUnit = regUnit;
    }

    public Integer getStillbirth() {
        return stillbirth;
    }

    public void setStillbirth(final Integer stillbirth) {
        this.stillbirth = stillbirth;
    }

    public Integer getStillbirthprogressive() {
        return stillbirthprogressive;
    }

    public void setStillbirthprogressive(final Integer stillbirthprogressive) {
        this.stillbirthprogressive = stillbirthprogressive;
    }

    public Integer getInfantdeath() {
        return infantdeath;
    }

    public void setInfantdeath(final Integer infantdeath) {
        this.infantdeath = infantdeath;
    }

    public Integer getInfantdeathprogressive() {
        return infantdeathprogressive;
    }

    public void setInfantdeathprogressive(final Integer infantdeathprogressive) {
        this.infantdeathprogressive = infantdeathprogressive;
    }

    public Integer getDeliverydeath() {
        return deliverydeath;
    }

    public void setDeliverydeath(final Integer deliverydeath) {
        this.deliverydeath = deliverydeath;
    }

    public Integer getDeliverydeathprogressive() {
        return deliverydeathprogressive;
    }

    public void setDeliverydeathprogressive(final Integer deliverydeathprogressive) {
        this.deliverydeathprogressive = deliverydeathprogressive;
    }

    public Integer getCertifieddeath() {
        return certifieddeath;
    }

    public void setCertifieddeath(final Integer certifieddeath) {
        this.certifieddeath = certifieddeath;
    }

    public Integer getCertifieddeathprogressive() {
        return certifieddeathprogressive;
    }

    public void setCertifieddeathprogressive(final Integer certifieddeathprogressive) {
        this.certifieddeathprogressive = certifieddeathprogressive;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(final String month) {
        this.month = month;
    }

    public Integer getBirthMale() {
        return birthMale;
    }

    public void setBirthMale(final Integer birthMale) {
        this.birthMale = birthMale;
    }

    public Integer getBirthFemale() {
        return birthFemale;
    }

    public void setBirthFemale(final Integer birthFemale) {
        this.birthFemale = birthFemale;
    }

    public Integer getBirthTotal() {
        return birthTotal;
    }

    public void setBirthTotal(final Integer birthTotal) {
        this.birthTotal = birthTotal;
    }

    public Integer getDeathMale() {
        return deathMale;
    }

    public void setDeathMale(final Integer deathMale) {
        this.deathMale = deathMale;
    }

    public Integer getDeathFemale() {
        return deathFemale;
    }

    public void setDeathFemale(final Integer deathFemale) {
        this.deathFemale = deathFemale;
    }

    public Integer getDeathTotal() {
        return deathTotal;
    }

    public void setDeathTotal(final Integer deathTotal) {
        this.deathTotal = deathTotal;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(final Integer slNo) {
        this.slNo = slNo;
    }

    @Override
    public String getStateDetails() {
        // TODO Auto-generated method stub
        return null;
    }

}
