package org.egov.builder.entities;

import java.lang.reflect.Field;
import java.util.Date;

import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.joda.time.DateTime;

public class BoundaryTypeBuilder {

    private final BoundaryType boundaryTypeImpl;

    // use this count where unique names,desciptions etc required
    private static int count;

    public BoundaryTypeBuilder() {
        boundaryTypeImpl = new BoundaryType();
        count++;
    }

    public BoundaryTypeBuilder withUpdatedTime(final Date updatedTime) {
        boundaryTypeImpl.setLastModifiedDate(new DateTime(updatedTime));;
        return this;
    }

    public BoundaryTypeBuilder withId(final Integer id) {
        try {
            final Field idField = boundaryTypeImpl.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(boundaryTypeImpl, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
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
        //boundaryTypeImpl.setBndryTypeLocal(bndryTypeLocal);
        return this;
    }

    public BoundaryTypeBuilder withHierarchy(final Integer hierarchy) {
        boundaryTypeImpl.setHierarchy(hierarchy);
        return this;
    }

    public BoundaryTypeBuilder withHeirarchyType(final HierarchyType hierarchyType) {
        boundaryTypeImpl.setHierarchyType(hierarchyType);
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
        if (null == boundaryTypeImpl.getLastModifiedDate())
            withUpdatedTime(new Date());

        if (null == boundaryTypeImpl.getName())
            withName("test-BoundaryType-" + count);

        if (0 == boundaryTypeImpl.getHierarchy())
            withHierarchy(count);
        if (null != boundaryTypeImpl.getHierarchyType())
            withHeirarchyType(new HeirarchyTypeBuilder().withDefaults().build());
        return this;
    }

    public BoundaryTypeBuilder withDbDefaults() {
        if (null != boundaryTypeImpl.getLastModifiedDate())
            withUpdatedTime(new Date());

        if (null == boundaryTypeImpl.getName())
            withName("test-BoundaryType-" + count);

        
        if (null == boundaryTypeImpl.getHierarchy())
            withHierarchy(count);

        if (null == boundaryTypeImpl.getHierarchyType())
            withHeirarchyType(new HeirarchyTypeBuilder().withDbDefaults().build());

        return this;
    }

    public BoundaryType build() {
        return boundaryTypeImpl;
    }
}