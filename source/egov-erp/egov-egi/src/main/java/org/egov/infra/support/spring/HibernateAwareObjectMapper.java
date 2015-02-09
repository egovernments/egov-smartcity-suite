package org.egov.infra.support.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class HibernateAwareObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = -634721091120261971L;

    public HibernateAwareObjectMapper() {
        registerModule(new Hibernate4Module());
    }
}