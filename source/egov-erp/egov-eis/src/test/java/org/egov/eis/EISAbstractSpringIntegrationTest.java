/**
 * 
 */
package org.egov.eis;

import javax.transaction.Transactional;

import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * @author Vaibhav.K
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback=true)
@ActiveProfiles("test")
@ContextConfiguration(
        loader = SpringockitoContextLoader.class,
        locations = {
                "classpath*:config/spring/applicationContext-properties.xml",
                "classpath*:config/spring/test-applicationContext-hibernate.xml",
                "classpath*:config/spring/applicationContext-egi.xml",
                "classpath*:config/spring/applicationContext-eis.xml"
        })
public abstract class EISAbstractSpringIntegrationTest {

}
