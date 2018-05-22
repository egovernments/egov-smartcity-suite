package org.egov.eventnotification.entity;

public enum SchedulerRepeat {
    REPEAT_MONTH("Month"), REPEAT_DAY("Day"), REPEAT_YEAR("Year"), REPEAT_ONETIME("One Time");

    private final String name;

    /**
     * @param name
     */
    private SchedulerRepeat(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
