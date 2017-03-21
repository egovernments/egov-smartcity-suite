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
package org.egov.ptis.bean.dashboard;

public class MonthlyDCB {
    private String boundaryName;
    private DemandCollectionMIS januaryDCB;
    private DemandCollectionMIS februaryDCB;
    private DemandCollectionMIS marchDCB;
    private DemandCollectionMIS aprilDCB;
    private DemandCollectionMIS mayDCB;
    private DemandCollectionMIS juneDCB;
    private DemandCollectionMIS julyDCB;
    private DemandCollectionMIS augustDCB;
    private DemandCollectionMIS septemberDCB;
    private DemandCollectionMIS octoberDCB;
    private DemandCollectionMIS novemberDCB;
    private DemandCollectionMIS decemberDCB;

    public String getBoundaryName() {
        return boundaryName;
    }

    public void setBoundaryName(String boundaryName) {
        this.boundaryName = boundaryName;
    }

    public DemandCollectionMIS getJanuaryDCB() {
        if(januaryDCB == null){
            januaryDCB = new DemandCollectionMIS();
            januaryDCB.setIntervalCount(1);
        }
        return januaryDCB;
    }

    public void setJanuaryDCB(DemandCollectionMIS januaryDCB) {
        januaryDCB.setIntervalCount(1);
        this.januaryDCB = januaryDCB;
    }

    public DemandCollectionMIS getFebruaryDCB() {
        if(februaryDCB == null){
            februaryDCB = new DemandCollectionMIS();
            februaryDCB.setIntervalCount(2);
        }
        return februaryDCB;
    }

    public void setFebruaryDCB(DemandCollectionMIS februaryDCB) {
        februaryDCB.setIntervalCount(2);
        this.februaryDCB = februaryDCB;
    }

    public DemandCollectionMIS getMarchDCB() {
        if(marchDCB == null){
            marchDCB = new DemandCollectionMIS();
            marchDCB.setIntervalCount(3);
        }
        return marchDCB;
    }

    public void setMarchDCB(DemandCollectionMIS marchDCB) {
        marchDCB.setIntervalCount(3);
        this.marchDCB = marchDCB;
    }

    public DemandCollectionMIS getAprilDCB() {
        if(aprilDCB == null){
            aprilDCB = new DemandCollectionMIS();
            aprilDCB.setIntervalCount(4);
        }
        return aprilDCB;
    }

    public void setAprilDCB(DemandCollectionMIS aprilDCB) {
        aprilDCB.setIntervalCount(4);
        this.aprilDCB = aprilDCB;
    }

    public DemandCollectionMIS getMayDCB() {
        if(mayDCB == null){
            mayDCB = new DemandCollectionMIS();
            mayDCB.setIntervalCount(5);
        }
        return mayDCB;
    }

    public void setMayDCB(DemandCollectionMIS mayDCB) {
        mayDCB.setIntervalCount(5);
        this.mayDCB = mayDCB;
    }

    public DemandCollectionMIS getJuneDCB() {
        if(juneDCB == null){
            juneDCB = new DemandCollectionMIS();
            juneDCB.setIntervalCount(6);
        }
        return juneDCB;
    }

    public void setJuneDCB(DemandCollectionMIS juneDCB) {
        juneDCB.setIntervalCount(6);
        this.juneDCB = juneDCB;
    }

    public DemandCollectionMIS getJulyDCB() {
        if(julyDCB == null){
            julyDCB = new DemandCollectionMIS();
            julyDCB.setIntervalCount(7);
        }
        return julyDCB;
    }

    public void setJulyDCB(DemandCollectionMIS julyDCB) {
        julyDCB.setIntervalCount(7);
        this.julyDCB = julyDCB;
    }

    public DemandCollectionMIS getAugustDCB() {
        if(augustDCB == null){
            augustDCB = new DemandCollectionMIS();
            augustDCB.setIntervalCount(8);
        }
        return augustDCB;
    }

    public void setAugustDCB(DemandCollectionMIS augustDCB) {
        augustDCB.setIntervalCount(8);
        this.augustDCB = augustDCB;
    }

    public DemandCollectionMIS getSeptemberDCB() {
        if(septemberDCB == null){
            septemberDCB = new DemandCollectionMIS();
            septemberDCB.setIntervalCount(9);
        }
        return septemberDCB;
    }

    public void setSeptemberDCB(DemandCollectionMIS septemberDCB) {
        septemberDCB.setIntervalCount(9);
        this.septemberDCB = septemberDCB;
    }

    public DemandCollectionMIS getOctoberDCB() {
        if(octoberDCB == null){
            octoberDCB = new DemandCollectionMIS();
            octoberDCB.setIntervalCount(10);
        }
        return octoberDCB;
    }

    public void setOctoberDCB(DemandCollectionMIS octoberDCB) {
        octoberDCB.setIntervalCount(10);
        this.octoberDCB = octoberDCB;
    }

    public DemandCollectionMIS getNovemberDCB() {
        if(novemberDCB == null){
            novemberDCB = new DemandCollectionMIS();
            novemberDCB.setIntervalCount(11);
        }
        return novemberDCB;
    }

    public void setNovemberDCB(DemandCollectionMIS novemberDCB) {
        novemberDCB.setIntervalCount(11);
        this.novemberDCB = novemberDCB;
    }

    public DemandCollectionMIS getDecemberDCB() {
        if(decemberDCB == null){
            decemberDCB = new DemandCollectionMIS();
            decemberDCB.setIntervalCount(12);
        }
        return decemberDCB;
    }

    public void setDecemberDCB(DemandCollectionMIS decemberDCB) {
        decemberDCB.setIntervalCount(12);
        this.decemberDCB = decemberDCB;
    }

}
