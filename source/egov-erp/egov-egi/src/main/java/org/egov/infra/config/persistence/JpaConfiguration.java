package org.egov.infra.config.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.support.ClasspathScanningPersistenceUnitPostProcessor;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableTransactionManagement(proxyTargetClass=true)
@PropertySource({ "classpath:jpaconfiguration.properties" })
@Profile("production")
public class JpaConfiguration {
    @Autowired
    private Environment env;
    @Autowired
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JtaTransactionManager();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setJtaDataSource(dataSource);
        entityManagerFactory.setPersistenceUnitName("EgovPersistenceUnit");
        entityManagerFactory.setPackagesToScan(new String[] { "org.egov.**.entity" });
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdaper());
        entityManagerFactory.setJpaPropertyMap(additionalProperties());
        entityManagerFactory.setValidationMode(ValidationMode.NONE);
        entityManagerFactory.setSharedCacheMode(SharedCacheMode.DISABLE_SELECTIVE);
        final ClasspathScanningPersistenceUnitPostProcessor classpathScanningPPU = new ClasspathScanningPersistenceUnitPostProcessor("org.egov");
        classpathScanningPPU.setMappingFileNamePattern("**/*hbm.xml");
        entityManagerFactory.setPersistenceUnitPostProcessors(classpathScanningPPU);
        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory.getObject();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdaper() {
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(env.getProperty("jpa.database", Database.class));
        vendorAdapter.setShowSql(env.getProperty("jpa.showSql", Boolean.class));
        vendorAdapter.setGenerateDdl(env.getProperty("jpa.generateDdl", Boolean.class));
        return vendorAdapter;
    }

    @Bean(name = "sessionFactory")
    public HibernateJpaSessionFactoryBean sessionFactory() {
        final HibernateJpaSessionFactoryBean hibernateJpaSessionFactoryBean = new HibernateJpaSessionFactoryBean();
        hibernateJpaSessionFactoryBean.setEntityManagerFactory(entityManagerFactory());
        return hibernateJpaSessionFactoryBean;
    }

    private Map<String, Object> additionalProperties() {
        final HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.validator.apply_to_ddl", false);
        properties.put("hibernate.validator.autoregister_listeners", false);
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
        properties.put("hibernate.cache.region.factory_class", env.getProperty("hibernate.cache.region.factory_class"));
        properties.put("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
        properties.put("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
        properties.put("hibernate.cache.use_minimal_puts", env.getProperty("hibernate.cache.use_minimal_puts"));
        properties.put("hibernate.cache.infinispan.cachemanager", env.getProperty("hibernate.cache.infinispan.cachemanager"));
        properties.put("hibernate.search.lucene_version", env.getProperty("hibernate.search.lucene_version"));
        properties.put("hibernate.transaction.jta.platform", env.getProperty("hibernate.transaction.jta.platform"));
        properties.put("hibernate.transaction.auto_close_session", env.getProperty("hibernate.transaction.auto_close_session"));
        properties.put("hibernate.jdbc.use_streams_for_binary", env.getProperty("hibernate.jdbc.use_streams_for_binary"));
        //properties.put("hibernate.enable_lazy_load_no_trans", true);
        return properties;
    }
}
