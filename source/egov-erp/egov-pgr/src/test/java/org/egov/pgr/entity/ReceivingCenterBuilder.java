package org.egov.pgr.entity;

import java.lang.reflect.Field;

public class ReceivingCenterBuilder {

    private final ReceivingCenter receivingCenter;
    public static int count;

    public ReceivingCenterBuilder() {
        receivingCenter = new ReceivingCenter();
        count++;
    }

    public ReceivingCenterBuilder withName(final String name) {
        receivingCenter.setName(name);
        return this;
    }

    public ReceivingCenterBuilder withIsCrnRequired(final boolean isCrnRequired) {
        receivingCenter.setCrnRequired(isCrnRequired);
        return this;
    }

    public ReceivingCenterBuilder withId(final long id) {
        try {
            final Field idField = receivingCenter.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(receivingCenter, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ReceivingCenterBuilder withDefaults() {

        withId(count);
        if (null == receivingCenter.getName())
            withName("Test-ReceivingCenter-" + count);
        if (false == receivingCenter.isCrnRequired())
            withIsCrnRequired(false);
        return this;
    }

    public ReceivingCenterBuilder withDbDefaults() {
        if (null == receivingCenter.getName())
            withName("Test-ReceivingCenter-" + count);
        if (false == receivingCenter.isCrnRequired())
            withIsCrnRequired(false);
        return this;
    }

    public ReceivingCenter build() {
        return receivingCenter;
    }
}