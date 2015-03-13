package org.egov.infra.web.support.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class JodaDateTimeEditor extends PropertyEditorSupport {
    private final boolean allowEmpty;
    private final String datePattern;

    public JodaDateTimeEditor(final String datePattern, final boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
        this.datePattern = datePattern;
    }

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        if (allowEmpty && StringUtils.isBlank(text))
            setValue(null);
        else
            setValue(DateTimeFormat.forPattern(datePattern).parseDateTime(text));
    }

    @Override
    public String getAsText() {
        final DateTime value = (DateTime) getValue();
        return value == null ? StringUtils.EMPTY : DateTimeFormat.forPattern(datePattern).print(value);
    }
}
