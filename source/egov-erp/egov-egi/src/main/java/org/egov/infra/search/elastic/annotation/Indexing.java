package org.egov.infra.search.elastic.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;

@Target(METHOD)
@Retention(RUNTIME)
public @interface Indexing {
    Index name();
    IndexType type();
}
