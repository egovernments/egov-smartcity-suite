package org.egov.infra.config.process;

import static java.lang.String.format;
import static org.activiti.engine.ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE;
import static org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl.DATABASE_TYPE_POSTGRES;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.multitenant.MultiSchemaMultiTenantProcessEngineConfiguration;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.spring.SpringExpressionManager;
import org.egov.infra.config.process.auth.ProcessAuthConfigurator;
import org.egov.infra.config.process.multitenant.activiti.AsyncExecuterPerTenant;
import org.egov.infra.config.process.multitenant.activiti.DBSqlSessionFactory;
import org.egov.infra.config.process.multitenant.activiti.TenantIdentityHolder;
import org.egov.infra.config.process.multitenant.activiti.TenantawareDatasource;
import org.egov.infra.utils.ResourceFinderUtil;
import org.egov.infra.web.filter.ApplicationTenantResolverFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

@Configuration
@Order(LOWEST_PRECEDENCE)
public class ProcessConfig {

    private static final String BPMN_FILE_CLASSPATH_LOCATION =  "classpath:processes/%s/*.bpmn";
    private static final String BPMN20_FILE_CLASSPATH_LOCATION =  "classpath:processes/%s/*.bpmn20.xml";

    @Autowired
    ProcessAuthConfigurator processAuthConfigurator;

    @Autowired
    private ApplicationContext appContext;

    @Bean
    @DependsOn("tenants")
    MultiSchemaMultiTenantProcessEngineConfiguration processEngineConfiguration(EntityManagerFactory entityManagerFactory,
                                                                                TenantIdentityHolder tenantIdentityHolder,
                                                                                TenantawareDatasource tenantawareDatasource) {
        MultiSchemaMultiTenantProcessEngineConfiguration processEngineConfig = new MultiSchemaMultiTenantProcessEngineConfiguration(tenantIdentityHolder);
        processEngineConfig.setDataSource(tenantawareDatasource);
        processEngineConfig.setTransactionsExternallyManaged(true);
        processEngineConfig.setAsyncExecutorActivate(true);
        //processEngineConfig.setAsyncExecutorEnabled(true);
        processEngineConfig.setAsyncExecutor(new AsyncExecuterPerTenant(tenantIdentityHolder));
        processEngineConfig.setJpaCloseEntityManager(false);
        processEngineConfig.setJpaHandleTransaction(false);
        processEngineConfig.setJpaEntityManagerFactory(entityManagerFactory);
        processEngineConfig.setDatabaseSchemaUpdate(DB_SCHEMA_UPDATE_TRUE);
        processEngineConfig.setDatabaseType(DATABASE_TYPE_POSTGRES);
        processEngineConfig.setHistory(HistoryLevel.FULL.getKey());
        processEngineConfig.setDbSqlSessionFactory(new DBSqlSessionFactory());
        processEngineConfig.setTablePrefixIsSchema(true);
        processEngineConfig.setExpressionManager(new SpringExpressionManager(appContext, null));
        // processEngineConfig.setDeploymentMode("resource-parent-folder");
        processEngineConfig.setConfigurators(Arrays.asList(processAuthConfigurator));
        tenantIdentityHolder.getAllTenants().stream().filter(Objects::nonNull).forEach(tenant ->
                processEngineConfig.registerTenant(tenant, tenantawareDatasource)
        );

        return processEngineConfig;
    }


    @Bean
    ProcessEngine processEngine(MultiSchemaMultiTenantProcessEngineConfiguration processEngineConfiguration,
                                TenantIdentityHolder tenantIdentityHolder) throws IOException {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        ResourceFinderUtil resourceResolver = new ResourceFinderUtil();

        List<Resource> commonBpmnResources =
                resourceResolver.getResources("classpath:processes/common/*.bpmn",
                        "classpath:processes/common/*.bpmn20.xml");

        for (String tenant : tenantIdentityHolder.getAllTenants()) {
        	System.out.println(tenant);
            tenantIdentityHolder.setCurrentTenantId(tenant);

            List<Resource> resources = resourceResolver.getResources(format(BPMN_FILE_CLASSPATH_LOCATION, tenant),
                    format(BPMN20_FILE_CLASSPATH_LOCATION, tenant));
            List<String> resourceNames = resources.stream().map(Resource::getFilename).collect(Collectors.toList());
            resources.addAll(commonBpmnResources.stream().
                    filter(rsrc -> !resourceNames.contains(rsrc.getFilename())).
                    collect(Collectors.toList()));
            for (Resource resource : resources) {
            	System.out.println(resource.getFilename());
                processEngine.getRepositoryService().createDeployment().
                        enableDuplicateFiltering().name(resource.getFilename()).
                        addInputStream(resource.getFilename(), resource.getInputStream()).deploy();
                System.out.println("Tenant: " + tenant + " File: " + resource.getFilename());
            }
            tenantIdentityHolder.clearCurrentTenantId();
        }
        return processEngine;
    }

    @Bean
    ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }

    @Bean
    IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

    @Bean
    ApplicationTenantResolverFilter tenantAwareProcessFilter() {
        return new ApplicationTenantResolverFilter();
    }
}
