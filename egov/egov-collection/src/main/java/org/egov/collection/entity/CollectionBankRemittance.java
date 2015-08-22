/**
 *
 */
package org.egov.collection.entity;

import org.egov.commons.Bank;
import org.egov.commons.Bankaccount;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.ServiceDetails;

/**
 * @author manoranjan
 */

public class CollectionBankRemittance extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Bank depositedInBank;

    private ServiceDetails service;

    private Bankaccount bankAccounttoRemit;

    /**
     * @return the depositedInBank
     */
    public Bank getDepositedInBank() {
        return depositedInBank;
    }

    /**
     * @param depositedInBank
     *            the depositedInBank to set
     */
    public void setDepositedInBank(final Bank depositedInBank) {
        this.depositedInBank = depositedInBank;
    }

    /**
     * @return the service
     */
    public ServiceDetails getService() {
        return service;
    }

    /**
     * @param service
     *            the service to set
     */
    public void setService(final ServiceDetails service) {
        this.service = service;
    }

    /**
     * @return the bankAccounttoRemit
     */
    public Bankaccount getBankAccounttoRemit() {
        return bankAccounttoRemit;
    }

    /**
     * @param bankAccounttoRemit
     *            the bankAccounttoRemit to set
     */
    public void setBankAccounttoRemit(final Bankaccount bankAccounttoRemit) {
        this.bankAccounttoRemit = bankAccounttoRemit;
    }

}
