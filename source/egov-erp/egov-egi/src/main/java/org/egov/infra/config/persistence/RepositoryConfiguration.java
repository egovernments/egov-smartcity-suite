package org.egov.infra.config.persistence;


import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories({"org.egov.**.repository"})
@EnableJpaAuditing
public class RepositoryConfiguration {
 
    @Autowired
    private SecurityUtils securityUtils;
    
    @Bean
    public  AuditorAware<User> springSecurityAwareAuditor() {
        return () -> securityUtils.getCurrentUser();
    }
}
