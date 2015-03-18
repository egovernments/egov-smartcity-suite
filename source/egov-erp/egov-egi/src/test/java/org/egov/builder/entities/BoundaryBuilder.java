package org.egov.builder.entities;

import java.math.BigInteger;
import java.util.Date;

import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;

public class BoundaryBuilder {

    private final BoundaryImpl boundaryImpl;

    // use this count where unique names,descriptions etc required
    private static int count;

    public BoundaryBuilder() {
        boundaryImpl = new BoundaryImpl();
        count++;
    }

    public BoundaryBuilder withUpdatedTime(final Date updatedTime) {
        boundaryImpl.setUpdatedTime(updatedTime);
        return this;
    }

    public BoundaryBuilder withId(final Integer id) {
        boundaryImpl.setId(id);
        return this;
    }

    public BoundaryBuilder withBoundaryType(final BoundaryType boundaryType) {
        boundaryImpl.setBoundaryType(boundaryType);
        return this;
    }

    public BoundaryBuilder withParent(final Boundary parent) {
        boundaryImpl.setParent(parent);
        return this;
    }

    public BoundaryBuilder withName(final String name) {
        boundaryImpl.setName(name);
        return this;
    }

    public BoundaryBuilder withBoundaryNum(final BigInteger boundaryNum) {
        boundaryImpl.setBoundaryNum(boundaryNum);
        return this;
    }

    public BoundaryBuilder withParentBoundaryNum(final BigInteger parentBoundaryNum) {
        boundaryImpl.setParentBoundaryNum(parentBoundaryNum);
        return this;
    }

    public BoundaryBuilder withTopLevelBoundaryID(final Integer topLevelBoundaryID) {
        boundaryImpl.setTopLevelBoundaryID(topLevelBoundaryID);
        return this;
    }

    public BoundaryBuilder withBndryTypeHeirarchyLevel(final Integer bndryTypeHeirarchyLevel) {
        boundaryImpl.setBndryTypeHeirarchyLevel(bndryTypeHeirarchyLevel);
        return this;
    }

    public BoundaryBuilder withFromDate(final Date fromDate) {
        boundaryImpl.setFromDate(fromDate);
        return this;
    }

    public BoundaryBuilder withToDate(final Date toDate) {
        boundaryImpl.setToDate(toDate);
        return this;
    }

    public BoundaryBuilder withIsHistory(final Character isHistory) {
        boundaryImpl.setIsHistory(isHistory);
        return this;
    }

    public BoundaryBuilder withBndryId(final Integer bndryId) {
        boundaryImpl.setBndryId(bndryId);
        return this;
    }

    public BoundaryBuilder withBndryNameLocal(final String bndryNameLocal) {
        boundaryImpl.setBndryNameLocal(bndryNameLocal);
        return this;
    }

    public BoundaryBuilder withLng(final Float lng) {
        boundaryImpl.setLng(lng);
        return this;
    }

    public BoundaryBuilder withLat(final Float lat) {
        boundaryImpl.setLat(lat);
        return this;
    }

    public BoundaryBuilder withMaterializedPath(final String materializedPath) {
        boundaryImpl.setMaterializedPath(materializedPath);
        return this;
    }

    public BoundaryBuilder withDefaults() {
        withId(count);
        if (null == boundaryImpl.getUpdatedTime())
            withUpdatedTime(new Date());

        if (null == boundaryImpl.getBoundaryType())
            withBoundaryType(new BoundaryTypeBuilder().withDefaults().build());

        if (null == boundaryImpl.getName())
            withName("test-boundary-" + count);
        if (null == boundaryImpl.getBoundaryNum())
            withBoundaryNum(BigInteger.valueOf(Integer.valueOf(count).longValue()));

        if (null == boundaryImpl.getFromDate())
            withFromDate(new Date());
        if (null == boundaryImpl.getToDate())
            withToDate(new Date());
        if (null == boundaryImpl.getIsHistory())
            withIsHistory('N');
        if (null == boundaryImpl.getBndryId())
            withBndryId(count);
        if (null == boundaryImpl.getBndryNameLocal())
            withBndryNameLocal("test-local");
        if (null == boundaryImpl.getLng())
            withLng(123232f);
        if (null == boundaryImpl.getLat())
            withLat(1423423f);
        if (null == boundaryImpl.getMaterializedPath())
            withMaterializedPath("1");

        return this;
    }

    public BoundaryBuilder withDbDefaults() {
        if (null == boundaryImpl.getUpdatedTime())
            withUpdatedTime(new Date());

        if (null == boundaryImpl.getBoundaryType())
            withBoundaryType(new BoundaryTypeBuilder().withDbDefaults().build());

        if (null == boundaryImpl.getName())
            withName("test-boundary-" + count);
        if (null == boundaryImpl.getBoundaryNum())
            withBoundaryNum(BigInteger.valueOf(Integer.valueOf(count).longValue()));

        if (null == boundaryImpl.getFromDate())
            withFromDate(new Date());
        if (null == boundaryImpl.getToDate())
            withToDate(new Date());
        if (null == boundaryImpl.getIsHistory())
            withIsHistory('N');
        if (null == boundaryImpl.getBndryId())
            withBndryId(count);
        if (null == boundaryImpl.getBndryNameLocal())
            withBndryNameLocal("test-local");
        if (null == boundaryImpl.getLng())
            withLng(123232f);
        if (null == boundaryImpl.getLat())
            withLat(1423423f);
        if (null == boundaryImpl.getMaterializedPath())
            withMaterializedPath("1");
        return this;
    }

    public BoundaryImpl build() {
        return boundaryImpl;
    }
}