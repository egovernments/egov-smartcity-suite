package org.egov.pgr.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:config/spring/test-applicationContext-hibernate.xml",
        "classpath*:config/spring/applicationContext-egi.xml",
        "classpath*:config/spring/applicationContext-pgr.xml"
})
@Transactional
@TransactionConfiguration
@ActiveProfiles("test")
public abstract class PGRAbstractSpringIntegrationTest {
}
