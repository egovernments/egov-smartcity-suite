/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.bpms.config;

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
import org.egov.bpms.config.auth.ProcessEngineAuthConfigurator;
import org.egov.bpms.config.multitenant.AsyncExecuterPerTenant;
import org.egov.bpms.config.multitenant.DBSqlSessionFactory;
import org.egov.bpms.config.multitenant.TenantIdentityHolder;
import org.egov.bpms.config.multitenant.TenantawareDatasource;
import org.egov.infra.utils.ResourceFinderUtil;
import org.egov.infra.web.filter.ApplicationTenantResolverFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.activiti.engine.ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE;
import static org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl.DATABASE_TYPE_POSTGRES;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Configuration
@Order(LOWEST_PRECEDENCE)
public class ProcessEngineConfiguration {

    private static final String BPMN_FILE_CLASSPATH_LOCATION = "classpath:processes/%s/*.bpmn";
    private static final String BPMN20_FILE_CLASSPATH_LOCATION = "classpath:processes/%s/*.bpmn20.xml";

    @Autowired
    ProcessEngineAuthConfigurator processAuthConfigurator;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ResourceFinderUtil resourceFinderUtil;

    @Bean
    @DependsOn("tenants")
    MultiSchemaMultiTenantProcessEngineConfiguration processEngineConfig(EntityManagerFactory entityManagerFactory,
                                                                         TenantIdentityHolder tenantIdentityHolder,
                                                                         TenantawareDatasource tenantawareDatasource) {
        MultiSchemaMultiTenantProcessEngineConfiguration processEngineConfig = new MultiSchemaMultiTenantProcessEngineConfiguration(tenantIdentityHolder);
        processEngineConfig.setDataSource(tenantawareDatasource);
        processEngineConfig.setTransactionsExternallyManaged(true);
        processEngineConfig.setAsyncExecutorActivate(true);
        processEngineConfig.setAsyncExecutor(new AsyncExecuterPerTenant(tenantIdentityHolder));
        processEngineConfig.setJpaCloseEntityManager(false);
        processEngineConfig.setJpaHandleTransaction(false);
        processEngineConfig.setJpaEntityManagerFactory(entityManagerFactory);
        processEngineConfig.setDatabaseSchemaUpdate(DB_SCHEMA_UPDATE_TRUE);
        processEngineConfig.setDatabaseType(DATABASE_TYPE_POSTGRES);
        processEngineConfig.setHistory(HistoryLevel.FULL.getKey());
        processEngineConfig.setDbSqlSessionFactory(new DBSqlSessionFactory());
        processEngineConfig.setTablePrefixIsSchema(true);
        processEngineConfig.setExpressionManager(new SpringExpressionManager(applicationContext, null));
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
        List<Resource> commonBpmnResources =
                resourceFinderUtil.getResources("classpath:processes/common/*.bpmn",
                        "classpath:processes/common/*.bpmn20.xml");

        for (String tenant : tenantIdentityHolder.getAllTenants()) {
            tenantIdentityHolder.setCurrentTenantId(tenant);

            List<Resource> resources = resourceFinderUtil.getResources(format(BPMN_FILE_CLASSPATH_LOCATION, tenant),
                    format(BPMN20_FILE_CLASSPATH_LOCATION, tenant));
            List<String> resourceNames = resources.stream().map(Resource::getFilename).collect(Collectors.toList());
            resources.addAll(commonBpmnResources.stream().
                    filter(rsrc -> !resourceNames.contains(rsrc.getFilename())).
                    collect(Collectors.toList()));
            for (Resource resource : resources) {
                processEngine.getRepositoryService().createDeployment().
                        enableDuplicateFiltering().name(resource.getFilename()).
                        addInputStream(resource.getFilename(), resource.getInputStream()).deploy();
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
