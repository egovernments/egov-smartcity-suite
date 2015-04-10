package org.egov.builder.entities;

import java.lang.reflect.Field;
import java.util.Date;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.joda.time.DateTime;

public class BoundaryBuilder {

    private final Boundary boundary;

    // use this count where unique names,descriptions etc required
    private static int count;

    public BoundaryBuilder() {
        boundary = new Boundary();
        count++;
    }

    public BoundaryBuilder withUpdatedTime(final Date updatedTime) {
        boundary.setLastModifiedDate(new DateTime(updatedTime));
        return this;
    }

    public BoundaryBuilder withId(final Integer id) {
        try {
            final Field idField = boundary.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(boundary, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public BoundaryBuilder withBoundaryType(final BoundaryType boundaryType) {
        boundary.setBoundaryType(boundaryType);
        return this;
    }

    public BoundaryBuilder withParent(final Boundary parent) {
        boundary.setParent(parent);
        return this;
    }

    public BoundaryBuilder withName(final String name) {
        boundary.setName(name);
        return this;
    }

    public BoundaryBuilder withBoundaryNum(final Long boundaryNum) {
        boundary.setBoundaryNum(boundaryNum);
        return this;
    }

    public BoundaryBuilder withParentBoundaryNum(final Long parentBoundaryNum) {
        boundary.setParentBoundaryNum(parentBoundaryNum);
        return this;
    }


    public BoundaryBuilder withFromDate(final Date fromDate) {
        boundary.setFromDate(fromDate);
        return this;
    }

    public BoundaryBuilder withToDate(final Date toDate) {
        boundary.setToDate(toDate);
        return this;
    }

    public BoundaryBuilder withIsHistory(final boolean isHistory) {
        boundary.setHistory(isHistory);
        return this;
    }

    public BoundaryBuilder withBndryId(final Long bndryId) {
        boundary.setBndryId(bndryId);
        return this;
    }

    public BoundaryBuilder withBndryNameLocal(final String bndryNameLocal) {
        boundary.setBoundaryNameLocal(bndryNameLocal);
        return this;
    }

    public BoundaryBuilder withLng(final Float lng) {
        boundary.setLongitude(lng);
        return this;
    }

    public BoundaryBuilder withLat(final Float lat) {
        boundary.setLatitude(lat);
        return this;
    }

    public BoundaryBuilder withMaterializedPath(final String materializedPath) {
        boundary.setMaterializedPath(materializedPath);
        return this;
    }

    public BoundaryBuilder withDefaults() {
        withId(count);
        if (null == boundary.getLastModifiedDate())
            withUpdatedTime(new Date());

        if (null == boundary.getBoundaryType())
            withBoundaryType(new BoundaryTypeBuilder().withDefaults().build());

        if (null == boundary.getName())
            withName("test-boundary-" + count);
        if (null == boundary.getBoundaryNum())
            withBoundaryNum(Long.valueOf(count));

        if (null == boundary.getFromDate())
            withFromDate(new Date());
        if (null == boundary.getToDate())
            withToDate(new Date());
        if (boundary.isHistory())
            withIsHistory(false);
        if (null == boundary.getBndryId())
            withBndryId(Long.valueOf(count));
        if (null == boundary.getBoundaryNameLocal())
            withBndryNameLocal("test-local");
        if (null == boundary.getLongitude())
            withLng(123232f);
        if (null == boundary.getLatitude())
            withLat(1423423f);
        if (null == boundary.getMaterializedPath())
            withMaterializedPath("1");

        return this;
    }

    public BoundaryBuilder withDbDefaults() {
        if (null == boundary.getLastModifiedDate())
            withUpdatedTime(new Date());

        if (null == boundary.getBoundaryType())
            withBoundaryType(new BoundaryTypeBuilder().withDbDefaults().build());

        if (null == boundary.getName())
            withName("test-boundary-" + count);
        if (null == boundary.getBoundaryNum())
            withBoundaryNum(Long.valueOf(count));

        if (null == boundary.getFromDate())
            withFromDate(new Date());
        if (null == boundary.getToDate())
            withToDate(new Date());
        if (boundary.isHistory())
            withIsHistory(false);
        if (null == boundary.getBndryId())
            withBndryId(Long.valueOf(count));
        if (null == boundary.getBoundaryNameLocal())
            withBndryNameLocal("test-local");
        if (null == boundary.getLongitude())
            withLng(123232f);
        if (null == boundary.getLatitude())
            withLat(1423423f);
        if (null == boundary.getMaterializedPath())
            withMaterializedPath("1");
        return this;
    }

    public Boundary build() {
        return boundary;
    }
}