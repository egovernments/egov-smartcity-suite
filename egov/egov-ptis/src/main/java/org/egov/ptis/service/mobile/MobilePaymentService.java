package org.egov.ptis.service.mobile;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_AUTO;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_TYPE_VACANTLAND_TAX;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.models.BillInfo.COLLECTIONTYPE;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.integration.utils.CollectionHelper;
import org.egov.ptis.client.integration.utils.SpringBeanUtil;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MobilePaymentService {
    
    private static final Logger LOGGER = Logger.getLogger(MobilePaymentService.class);
    
    @Autowired
    private EgBillDao egBillDAO;
    
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    
    @Autowired
    @Qualifier("propertyTaxBillable")
    private PropertyTaxBillable propertyTaxBillable;

    @Autowired
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;

    @Autowired
    private PTBillServiceImpl ptBillServiceImpl;

    
    public String mobileBillPayment(final String assessmentNo, final BigDecimal amountToBePaid,
            final String category) throws ValidationException {
        String redirectUrl = "";
        final BillInfoImpl billInfo = getBillInfo(assessmentNo, amountToBePaid, category);

        final EgBill egBill = egBillDAO.findById(Long.valueOf(billInfo.getPayees().get(0).getBillDetails().get(0).getRefNo()),
                false);

        if (billInfo != null) {
            if (!basicPropertyDAO.isAssessmentNoExist(assessmentNo) || !egBill.getConsumerId().equals(assessmentNo)) {
                LOGGER.error("ULB code or assessment number does not match!");
                throw new ValidationException(
                        Arrays.asList(new ValidationError("ULB code or assessment number does not match",
                                "ULB code or assessment number does not match",
                                new String[] { ApplicationThreadLocals.getCityCode(), assessmentNo })));
            }
            final PaymentRequest paymentRequest = SpringBeanUtil.getCollectionIntegrationService()
                    .processMobilePayments(billInfo);
            if (paymentRequest != null) {
                for (final Object obj : paymentRequest.getRequestParameters().values())
                    redirectUrl = obj.toString();
            }
        }
        return redirectUrl;
    }
    
    /**
     * API to return BillInfoImpl, used in tax payment through Mobile App
     *
     * @param mobilePropertyTaxDetails
     * @return
     */
    public BillInfoImpl getBillInfo(final String assessmentNo, final BigDecimal amountToBePaid, final String category) {
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        propertyTaxBillable.setBasicProperty(basicProperty);
        propertyTaxBillable.setReferenceNumber(propertyTaxNumberGenerator
                .generateBillNumber(basicProperty.getPropertyID().getWard().getBoundaryNum().toString()));
        propertyTaxBillable.setBillType(egBillDAO.getBillTypeByCode(BILLTYPE_AUTO));
        propertyTaxBillable.setLevyPenalty(Boolean.TRUE);
        if (StringUtils.isNotBlank(category) && category.equalsIgnoreCase(CATEGORY_TYPE_VACANTLAND_TAX))
            propertyTaxBillable.setVacantLandTaxPayment(true);

        final EgBill egBill = ptBillServiceImpl.generateBill(propertyTaxBillable);
        final CollectionHelper collectionHelper = new CollectionHelper(egBill);

        return collectionHelper.prepareBillInfo(amountToBePaid, COLLECTIONTYPE.O, null);
    }


}
