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
package org.egov.commons;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "BANKBRANCH")
@SequenceGenerator(name = Bankbranch.SEQ_BANKBRANCH, sequenceName = Bankbranch.SEQ_BANKBRANCH, allocationSize = 1)
public class Bankbranch implements java.io.Serializable {

        private static final long serialVersionUID = 1L;
        public static final String SEQ_BANKBRANCH = "SEQ_BANKBRANCH";

        @Id
    	@GeneratedValue(generator = SEQ_BANKBRANCH, strategy = GenerationType.SEQUENCE)
    	private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "BANKID")
        private Bank bank;

        @Length(max = 50)
        @NotNull
        private String branchcode;

        @Length(max = 50)
        @NotNull
        private String branchname;

        @Length(max = 50)
        @NotNull
        private String branchaddress1;

        @Length(max = 50)
        private String branchaddress2;

        @Length(max = 50)
        private String branchcity;

        @Length(max = 50)
        private String branchstate;

        @Length(max = 50)
        private String branchpin;

        @Length(max = 15)
        private String branchphone;

        @Length(max = 15)
        private String branchfax;

        @Length(max = 50)
        private String contactperson;

        @NotNull
        private int isactive;

        @Length(max = 250)
        private String narration;

        @Length(max = 50)
        private String branchMICR;

        @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy="bankbranch", targetEntity=Bankaccount.class)
        @NotNull
        private Set<Bankaccount> bankaccounts = new HashSet<Bankaccount>(0);

        public Bankbranch() {
                //For hibernate to work
        }

        public Bankbranch(String branchcode, String branchname, String branchaddress1, int isactive) {
                this.branchcode = branchcode;
                this.branchname = branchname;
                this.branchaddress1 = branchaddress1;
                this.isactive = isactive;
        }

        public Bankbranch(Bank bank, String branchcode, String branchname, String branchaddress1, String branchaddress2, String branchcity, String branchstate, String branchpin, String branchphone, String branchfax, String contactperson, int isactive,
                        String narration, String branchMICR, Set<Bankaccount> bankaccounts) {
                this.bank = bank;
                this.branchcode = branchcode;
                this.branchname = branchname;
                this.branchaddress1 = branchaddress1;
                this.branchaddress2 = branchaddress2;
                this.branchcity = branchcity;
                this.branchstate = branchstate;
                this.branchpin = branchpin;
                this.branchphone = branchphone;
                this.branchfax = branchfax;
                this.contactperson = contactperson;
                this.isactive = isactive;
                this.narration = narration;
                this.branchMICR = branchMICR;
                this.bankaccounts = bankaccounts;
        }
        public boolean isAccountsExist(){
            return (this.bankaccounts!=null && !this.bankaccounts.isEmpty()) ;
        }
        public Long getId() {
                return this.id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Bank getBank() {
                return this.bank;
        }

        public void setBank(Bank bank) {
                this.bank = bank;
        }

        public String getBranchcode() {
                return this.branchcode;
        }

        public void setBranchcode(String branchcode) {
                this.branchcode = branchcode;
        }

        public String getBranchname() {
                return this.branchname;
        }

        public void setBranchname(String branchname) {
                this.branchname = branchname;
        }

        public String getBranchaddress1() {
                return this.branchaddress1;
        }

        public void setBranchaddress1(String branchaddress1) {
                this.branchaddress1 = branchaddress1;
        }

        public String getBranchaddress2() {
                return this.branchaddress2;
        }

        public void setBranchaddress2(String branchaddress2) {
                this.branchaddress2 = branchaddress2;
        }

        public String getBranchcity() {
                return this.branchcity;
        }

        public void setBranchcity(String branchcity) {
                this.branchcity = branchcity;
        }

        public String getBranchstate() {
                return this.branchstate;
        }

        public void setBranchstate(String branchstate) {
                this.branchstate = branchstate;
        }

        public String getBranchpin() {
                return this.branchpin;
        }

        public void setBranchpin(String branchpin) {
                this.branchpin = branchpin;
        }

        public String getBranchphone() {
                return this.branchphone;
        }

        public void setBranchphone(String branchphone) {
                this.branchphone = branchphone;
        }

        public String getBranchfax() {
                return this.branchfax;
        }

        public void setBranchfax(String branchfax) {
                this.branchfax = branchfax;
        }

        public String getContactperson() {
                return this.contactperson;
        }

        public void setContactperson(String contactperson) {
                this.contactperson = contactperson;
        }

        public String getNarration() {
                return this.narration;
        }

        public void setNarration(String narration) {
                this.narration = narration;
        }

        public String getBranchMICR() {
                return branchMICR;
        }

        public void setBranchMICR(String branchMICR) {
                this.branchMICR = branchMICR;
        }

        public Set<Bankaccount> getBankaccounts() {
                return this.bankaccounts;
        }

        public void setBankaccounts(Set<Bankaccount> bankaccounts) {
                this.bankaccounts = bankaccounts;
        }

        public int getIsactive() {
            return isactive;
        }

        public void setIsactive(int isactive) {
            this.isactive = isactive;
        }

}