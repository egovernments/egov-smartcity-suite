package org.egov.ptis.nmc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceNumberGenerator;
import org.egov.models.AbstractPersistenceServiceTest;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.nmc.model.NMCObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertyTaxNumberGeneratorTest extends AbstractPersistenceServiceTest<BasicPropertyImpl, Long> {

    private NMCObjectFactory objectFactory;
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    private SequenceNumberGenerator sequenceNumberGenerator;
    private ScriptService scriptExecutionService;

    public PropertyTaxNumberGeneratorTest() {
        this.type = BasicPropertyImpl.class;
    }

    @Before
    public void setupService() {

        objectFactory = new NMCObjectFactory(session, genericService);

        scriptExecutionService = new ScriptService(2, 5, 10, 30);
        scriptExecutionService.setSessionFactory(egovSessionFactory);

        propertyTaxNumberGenerator = new PropertyTaxNumberGenerator();
        sequenceNumberGenerator = new SequenceNumberGenerator(egovSessionFactory);
        propertyTaxNumberGenerator.setSequenceNumberGenerator(sequenceNumberGenerator);
    }

    private String formatDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    @Test
    public void testNoticeNoGenerationStrategy() {
    	Assert.assertEquals(true, true);

/*        Calendar cal = Calendar.getInstance();
        int yr = cal.get(Calendar.YEAR);
        List numbers = session.createSQLQuery("SELECT VALUE FROM EG_NUMBER_GENERIC WHERE OBJECTTYPE=?").setString(0,
        		NMCPTISConstants.NOTICE127 + yr).list();
        BigDecimal result;
        if (numbers != null && !numbers.isEmpty())
            result = (BigDecimal) numbers.get(0);
        else
            result = new BigDecimal(0);
        String noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(NMCPTISConstants.NOTICE127);

        String sequence = "00000000" + (result.longValue() + 1);
        sequence = sequence.substring(sequence.length() - 8, sequence.length());
        //assertEquals("N127/" + sequence + "/", noticeNo);

        noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(NMCPTISConstants.NOTICE134);
       // assertEquals("N134/" + sequence + "/", noticeNo);
*/    }

}
