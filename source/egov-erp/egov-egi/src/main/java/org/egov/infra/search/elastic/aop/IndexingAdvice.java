package org.egov.infra.search.elastic.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.search.domain.Document;
import org.egov.search.service.IndexService;
import org.egov.search.service.ResourceGenerator;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class IndexingAdvice {

    @Autowired
    private IndexService indexService;

    @Pointcut("@annotation(org.egov.infra.search.elastic.annotation.Indexing)")
    private void methodAnnotatedWithIndexing() {
    }

    @AfterReturning(pointcut = "methodAnnotatedWithIndexing() && @annotation(indexing)", returning = "retVal")
    public void indexForSearch(final Indexing indexing, final Object retVal) {
        final JSONObject resourceJSON = new ResourceGenerator<>(retVal.getClass(), retVal).generate();
        final Document document = new Document(indexing.name().toString(), indexing.type().toString(),
                ((AbstractPersistable<Long>) retVal).getId().toString(), resourceJSON);
        indexService.index(document);
    }

}
