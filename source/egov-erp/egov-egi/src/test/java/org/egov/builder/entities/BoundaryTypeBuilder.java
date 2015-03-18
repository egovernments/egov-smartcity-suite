package org.egov.builder.entities;

import java.lang.reflect.Field;
import java.util.Date;

import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeImpl;
import org.egov.lib.admbndry.HeirarchyType;

public class BoundaryTypeBuilder {

    private final BoundaryTypeImpl boundaryTypeImpl;

    // use this count where unique names,desciptions etc required
    private static int count;

    public BoundaryTypeBuilder() {
        boundaryTypeImpl = new BoundaryTypeImpl();
        count++;
    }

    public BoundaryTypeBuilder withUpdatedTime(final Date updatedTime) {
        boundaryTypeImpl.setUpdatedTime(updatedTime);
        return this;
    }

    public BoundaryTypeBuilder withId(final Integer id) {
        boundaryTypeImpl.setId(id);
        return this;
    }

    public BoundaryTypeBuilder withParentBndryType(final BoundaryType parentBndryType) {
        boundaryTypeImpl.setParent(parentBndryType);
        return this;
    }

    public BoundaryTypeBuilder withName(final String name) {
        boundaryTypeImpl.setName(name);
        return this;
    }

    public BoundaryTypeBuilder withParentName(final String parentName) {
        boundaryTypeImpl.setParentName(parentName);
        return this;
    }

    public BoundaryTypeBuilder withBndryTypeLocal(final String bndryTypeLocal) {
        boundaryTypeImpl.setBndryTypeLocal(bndryTypeLocal);
        return this;
    }

    public BoundaryTypeBuilder withHierarchy(final short hierarchy) {
        boundaryTypeImpl.setHierarchy(hierarchy);
        return this;
    }

    public BoundaryTypeBuilder withHeirarchyType(final HeirarchyType heirarchyType) {
        boundaryTypeImpl.setHeirarchyType(heirarchyType);
        return this;
    }

    public BoundaryTypeBuilder withId(final long id) {
        try {
            final Field idField = boundaryTypeImpl.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(boundaryTypeImpl, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public BoundaryTypeBuilder withDefaults() {
        withId(count);
        if (null == boundaryTypeImpl.getUpdatedTime())
            withUpdatedTime(new Date());

        if (null == boundaryTypeImpl.getName())
            withName("test-BoundaryType-" + count);

        if (null == boundaryTypeImpl.getBndryTypeLocal())
            withBndryTypeLocal("test-local");
        if (0 == boundaryTypeImpl.getHierarchy())
            withHierarchy(Integer.valueOf(count).shortValue());
        if (null != boundaryTypeImpl.getHeirarchyType())
            withHeirarchyType(new HeirarchyTypeBuilder().withDefaults().build());
        return this;
    }

    public BoundaryTypeBuilder withDbDefaults() {
        if (null != boundaryTypeImpl.getUpdatedTime())
            withUpdatedTime(new Date());

        if (null == boundaryTypeImpl.getName())
            withName("test-BoundaryType-" + count);

        if (null == boundaryTypeImpl.getBndryTypeLocal())
            withBndryTypeLocal("test-local");

        if (0 == boundaryTypeImpl.getHierarchy())
            withHierarchy(Integer.valueOf(count).shortValue());

        if (null == boundaryTypeImpl.getHeirarchyType())
            withHeirarchyType(new HeirarchyTypeBuilder().withDbDefaults().build());

        return this;
    }

    public BoundaryTypeImpl build() {
        return boundaryTypeImpl;
    }
}