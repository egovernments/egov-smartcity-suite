package org.egov.builder.entities;

import java.lang.reflect.Field;
import java.util.Date;

import org.egov.infra.admin.master.entity.HierarchyType;
import org.joda.time.DateTime;

public class HeirarchyTypeBuilder {

    private final HierarchyType heirarchyTypeImpl;

    // use this count where unique names,desciptions etc required
    private static int count;

    public HeirarchyTypeBuilder() {
        heirarchyTypeImpl = new HierarchyType();
        count++;
    }

    public HeirarchyTypeBuilder withName(final String name) {
        heirarchyTypeImpl.setName(name);
        return this;
    }

    public HeirarchyTypeBuilder withId(final Integer id) {
        try {
            final Field idField = heirarchyTypeImpl.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(heirarchyTypeImpl, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public HeirarchyTypeBuilder withUpdatedTime(final Date updatedTime) {
        heirarchyTypeImpl.setLastModifiedDate(new DateTime(updatedTime));
        return this;
    }

    public HeirarchyTypeBuilder withCode(final String code) {
        heirarchyTypeImpl.setCode(code);
        return this;
    }

    public HeirarchyTypeBuilder withDefaults() {
        withId(count);

        if (null != heirarchyTypeImpl.getName())
            withName("Test-Hierrachy" + count);

        if (null != heirarchyTypeImpl.getLastModifiedDate())
            withUpdatedTime(new Date());
        if (null != heirarchyTypeImpl.getCode())
            withCode("Test-" + count);
        return this;
    }

    public HeirarchyTypeBuilder withDbDefaults() {
        if (null != heirarchyTypeImpl.getName())
            withName("Test-Hierrachy" + count);

        if (null != heirarchyTypeImpl.getLastModifiedDate())
            withUpdatedTime(new Date());

        if (null != heirarchyTypeImpl.getCode())
            withCode("Test-" + count);

        return this;
    }

    public HierarchyType build() {
        return heirarchyTypeImpl;
    }
}