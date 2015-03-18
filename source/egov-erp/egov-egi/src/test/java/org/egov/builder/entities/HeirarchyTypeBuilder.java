package org.egov.builder.entities;

import java.util.Date;

import org.egov.lib.admbndry.HeirarchyTypeImpl;

public class HeirarchyTypeBuilder {

    private final HeirarchyTypeImpl heirarchyTypeImpl;

    // use this count where unique names,desciptions etc required
    private static int count;

    public HeirarchyTypeBuilder() {
        heirarchyTypeImpl = new HeirarchyTypeImpl();
        count++;
    }

    public HeirarchyTypeBuilder withName(final String name) {
        heirarchyTypeImpl.setName(name);
        return this;
    }

    public HeirarchyTypeBuilder withId(final Integer id) {
        heirarchyTypeImpl.setId(id);
        return this;
    }

    public HeirarchyTypeBuilder withUpdatedTime(final Date updatedTime) {
        heirarchyTypeImpl.setUpdatedTime(updatedTime);
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

        if (null != heirarchyTypeImpl.getUpdatedTime())
            withUpdatedTime(new Date());
        if (null != heirarchyTypeImpl.getCode())
            withCode("Test-" + count);
        return this;
    }

    public HeirarchyTypeBuilder withDbDefaults() {
        if (null != heirarchyTypeImpl.getName())
            withName("Test-Hierrachy" + count);

        if (null != heirarchyTypeImpl.getUpdatedTime())
            withUpdatedTime(new Date());

        if (null != heirarchyTypeImpl.getCode())
            withCode("Test-" + count);

        return this;
    }

    public HeirarchyTypeImpl build() {
        return heirarchyTypeImpl;
    }
}