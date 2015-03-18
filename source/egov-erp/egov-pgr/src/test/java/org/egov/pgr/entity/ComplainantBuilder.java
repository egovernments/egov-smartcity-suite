package org.egov.pgr.entity;

import java.lang.reflect.Field;

import org.egov.lib.rjbac.user.UserImpl;

public class ComplainantBuilder {

    private final Complainant complainant;

    public ComplainantBuilder() {
        complainant = new Complainant();
    }

    public ComplainantBuilder withName(final String name) {
        complainant.setName(name);
        return this;
    }

    public ComplainantBuilder withMobile(final String mobile) {
        complainant.setMobile(mobile);
        return this;
    }

    public ComplainantBuilder withEmail(final String email) {
        complainant.setEmail(email);
        return this;
    }

    public ComplainantBuilder withUserDetail(final UserImpl userDetail) {
        complainant.setUserDetail(userDetail);
        return this;
    }

    public ComplainantBuilder withId(final long id) {
        try {
            final Field idField = complainant.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(complainant, id);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ComplainantBuilder withDefaults() {
        withId(22l);
        withName("Test-Complainant");
        withMobile("9980770587");
        withEmail("mani@123.com");
        // withUserDetail(userDetail);
        return this;
    }

    public Complainant build() {
        return complainant;
    }
}