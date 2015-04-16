/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infra.persistence.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import javax.persistence.Table;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SequenceIdGenerator implements IdentifierGenerator {

    private static final String SEQUENCE_PREFIX = "seq_";
    private static final String NEXT_SEQ_SQL_QUERY = "SELECT nextval (?) as nextval";

    @Override
    public Serializable generate(final SessionImplementor session, final Object object) throws HibernateException {

        try {
            final String sequenceName = getSequenceName(object);
            return getNextSequence(session, sequenceName);
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

    private void closeResource(final PreparedStatement ps, final ResultSet rs) throws SQLException {
        if (rs != null)
            rs.close();
        if (ps != null)
            ps.close();
    }

}