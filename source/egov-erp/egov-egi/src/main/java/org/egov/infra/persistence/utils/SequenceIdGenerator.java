package org.egov.infra.persistence.utils;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SequenceIdGenerator implements IdentifierGenerator {

    private static final String SEQUENCE_PREFIX = "seq_";
    private static final String NEXT_SEQ_SQL_QUERY = "SELECT nextval (?) as nextval";

    @Override
    public Serializable generate(final SessionImplementor session, final Object object) throws HibernateException {

        try {
            final String sequenceName = getSequenceName(object);
            final Savepoint savepoint = session.connection().setSavepoint();
            try {
                return getNextSequence(session, sequenceName);
            } catch (final SQLException e) {
                session.connection().rollback(savepoint);
                return createAndGetNextSequence(session, sequenceName);
            }
        } catch (final Exception e) {
            throw new HibernateException("Error occurred while generating ID", e);
        }
    }

    private String getSequenceName(final Object object) {
        String sequenceName = SEQUENCE_PREFIX;
        if (object.getClass().isAnnotationPresent(Table.class))
            sequenceName += object.getClass().getAnnotation(Table.class).name();
        else
            sequenceName += object.getClass().getSimpleName().toLowerCase();
        return sequenceName;
    }

    private Serializable getNextSequence(final SessionImplementor session, final String sequenceName) throws SQLException {
        final PreparedStatement ps = session.connection().prepareStatement(NEXT_SEQ_SQL_QUERY);
        ps.setString(1, sequenceName);
        final ResultSet rs = ps.executeQuery();
        Serializable sequence = 0;
        if (rs.next())
            sequence = rs.getLong("nextval");
        closeResource(ps, rs);
        return sequence;
    }

    private Serializable createAndGetNextSequence(final SessionImplementor session, final String sequenceName) throws SQLException {
        Statement statement = session.connection().createStatement();
        try {
            statement.execute("create sequence " + sequenceName);
        } finally {
            if (statement != null) statement.close();
        }

        return getNextSequence(session, sequenceName);

    }

    private void closeResource(final PreparedStatement ps, final ResultSet rs) throws SQLException {
        if (rs != null)
            rs.close();
        if (ps != null)
            ps.close();
    }

}