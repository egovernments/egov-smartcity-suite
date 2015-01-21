package org.egov;

import org.egov.infstr.workflow.CustomizedWorkFlowService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"classpath*:config/spring/applicationContext-hibernate.xml", "classpath*:config/spring/applicationContext-egi.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@Transactional(readOnly = true)
@TransactionConfiguration
@Ignore
public class EgiContextTest {

    @Autowired
    CustomizedWorkFlowService springInstance;

    @Test
    public void shouldLoadEGISpringContext() {
        assertTrue(true);
        assertNotNull(springInstance);
        springInstance.getSession();
    }
}
