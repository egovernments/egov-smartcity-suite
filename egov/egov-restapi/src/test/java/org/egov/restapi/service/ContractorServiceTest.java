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
package org.egov.restapi.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.dao.BankHibernateDAO;
import org.egov.restapi.model.ContractorHelper;
import org.egov.restapi.model.RestErrors;
import org.egov.restapi.web.rest.AbstractContextControllerTest;
import org.egov.works.master.service.ContractorService;
import org.egov.works.models.masters.Contractor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ContractorServiceTest extends AbstractContextControllerTest<ExternalContractorService> {

    @Mock
    private ContractorService contractorService;

    @Mock
    private BankHibernateDAO bankHibernateDAO;

    @Mock
    private HttpServletRequest request;

    private List<RestErrors> errors;

    private Contractor contractor;

    private List<Contractor> contractors;

    private ContractorHelper contractorHelper;

    @InjectMocks
    private ExternalContractorService externalContractorService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Override
    protected ExternalContractorService initController() {
        MockitoAnnotations.initMocks(this);
        return externalContractorService;
    }

    @Before
    public void setUp() throws IOException {
        errors = new ArrayList<>();
        contractor = new Contractor();
        fillContractor();

        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(new Contractor());
        when(contractorService.createContractor(Matchers.anyObject())).thenReturn(new Contractor());
        when(contractorService.updateContractor(Matchers.anyObject())).thenReturn(new Contractor());
        when(contractorService.getAllContractors()).thenReturn(contractors);

    }

    private void fillContractor() {
        contractorHelper = new ContractorHelper();
        contractorHelper.setCode("gdfkhsfkhsd");
        contractorHelper.setName("contractorName");
        contractorHelper.setBankName("SBI");
        contractorHelper.setBankAccount("Bankaccount");
        contractorHelper.setContactPerson("Manoj");
        contractorHelper.setCorrespondenceAddress("jp nagar");
        contractorHelper.setPaymentAddress("Chalkere");
        contractorHelper.setEmail("abc@gmail.com");
        contractorHelper.setMobileNumber("1234567890");
        contractorHelper.setExemptionName("VAT");
        contractorHelper.setIfscCode("SBIN0005534");
        contractorHelper.setPanNumber("AVCXS2345S");
        contractorHelper.setTinNumber("TIN0123456");
        contractorHelper.setPwdApprovalCode("PWDCode");
        contractorHelper.setNarration("Narration");
        contractorHelper.setStatus("Active");
        contractorHelper.setContractorCategory("Public Health Engineering");
        contractorHelper.setContractorClass("Class-I");
    }

    @Test
    public void shouldValidateContractor() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(0, errors.size());
    }

    @Test
    public void shouldValidateContractorCodeAndName() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setCode("");
        contractorHelper.setName("");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateContractorCodeAndNameLength() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setCode(
                "The quick, brown fox jumps over a lazy dog. DJs flock by when MTV ax quiz prog. Junk MTV quiz graced by fox whelps. Bawds jog, flick quartz, vex nymphs. Waltz, bad nymph, for quick jigs vex! Fox nymphs grab quick-jived waltz. Brick quiz whangs jumpy veldt fox. Bright vixens jump; dozy fowl quack. Quick wafting zephyrs vex bold Jim.");
        contractorHelper.setName(
                "One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections. The bedding was hardly able to cover it and seemed ready to slide off any moment. His many legs, pitifully thin compared with the size of the rest of him, waved about helplessly as he looked. cWhat's happened to me?  he thought. It wasn't a dream. His room, a proper human room although a little too small, lay peacefully between its four familiar walls.");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(4, errors.size());

    }

    @Test
    public void shouldValidateContractorCorrespondenceAddress() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setCorrespondenceAddress("Kurnool' CorrespondenceAddress");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldValidateContractorPaymentAddress() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setPaymentAddress("Kurnool' PaymentAddress");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldValidateContractorNarration() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setPaymentAddress("Kurnool' Narration");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldValidateContractorContactPerson() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setContactPerson("Ritesh !@#$%^&");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldValidateContractorEmail() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setEmail("Ritesh@gmail !#$%^&");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldValidateContractorPanNumber() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setPanNumber("Ritesh !#$%^&");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldValidateContractorTinNumber() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setTinNumber("TIN1245 !#$%^&");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldValidateContractorIFSCCode() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setIfscCode("SBIN !#$%^&");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldValidateContractorBankAccount() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setBankAccount("BankAccount !#$%^&");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldValidateContractorPwdApprovalCode() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setPwdApprovalCode("Ritesh@123 !#$%^&");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldValidateContractorMobileNumber() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setMobileNumber("12345678");
        errors = externalContractorService.validateContactorToCreate(contractorHelper);
        assertEquals(1, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorCodeAndName() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setCode("");
        contractorHelper.setName("");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorCodeAndNameLength() {
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setCode(
                "The quick, brown fox jumps over a lazy dog. DJs flock by when MTV ax quiz prog. Junk MTV quiz graced by fox whelps. Bawds jog, flick quartz, vex nymphs. Waltz, bad nymph, for quick jigs vex! Fox nymphs grab quick-jived waltz. Brick quiz whangs jumpy veldt fox. Bright vixens jump; dozy fowl quack. Quick wafting zephyrs vex bold Jim.");
        contractorHelper.setName(
                "One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections. The bedding was hardly able to cover it and seemed ready to slide off any moment. His many legs, pitifully thin compared with the size of the rest of him, waved about helplessly as he looked. cWhat's happened to me?  he thought. It wasn't a dream. His room, a proper human room although a little too small, lay peacefully between its four familiar walls.");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(5, errors.size());

    }

    @Test
    public void shouldValidateModifyContractorCorrespondenceAddress() {
        contractorHelper.setCode("");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setCorrespondenceAddress("Kurnool' CorrespondenceAddress");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorPaymentAddress() {
        contractorHelper.setCode("");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setPaymentAddress("Kurnool' PaymentAddress");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorNarration() {
        contractorHelper.setCode("");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setPaymentAddress("Kurnool' Narration");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorContactPerson() {
        contractorHelper.setCode("");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setContactPerson("Ritesh !@#$%^&");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorEmail() {
        contractorHelper.setCode("");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setEmail("Ritesh@gmail !#$%^&");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorPanNumber() {
        contractorHelper.setCode("");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setPanNumber("Ritesh !#$%^&");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorTinNumber() {
        contractorHelper.setCode("");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setTinNumber("TIN1245 !#$%^&");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorIFSCCode() {
        contractorHelper.setCode("");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setIfscCode("SBIN !#$%^&");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorBankAccount() {
        contractorHelper.setCode("");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setBankAccount("BankAccount !#$%^&");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorPwdApprovalCode() {
        contractorHelper.setCode("");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setPwdApprovalCode("Ritesh@123 !#$%^&");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

    @Test
    public void shouldValidateModifyContractorMobileNumber() {
        contractorHelper.setCode("");
        when(contractorService.getContractorByCode(Matchers.anyString())).thenReturn(null);
        contractorHelper.setMobileNumber("12345678");
        errors = externalContractorService.validateContactorToUpdate(contractorHelper);
        assertEquals(2, errors.size());
    }

}
