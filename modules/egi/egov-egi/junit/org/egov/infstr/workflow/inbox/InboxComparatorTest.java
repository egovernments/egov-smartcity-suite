package org.egov.infstr.workflow.inbox;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.pims.commons.Position;
import org.junit.Test;

public class InboxComparatorTest {

	private final InboxComparator comparator = new InboxComparator();

	@Test
	public void testCompare() {
		final StateAware aw = getStateAware();
		final StateAware aw2 = getStateAware();
		aw.getState().setCreatedDate(new Date());

		assertEquals(0, this.comparator.compare(null, null));
		assertEquals(1, this.comparator.compare(aw, null));
		assertEquals(-1, this.comparator.compare(null, aw));
		assertEquals(0, this.comparator.compare(aw, aw));

		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 1);
		aw2.getState().setCreatedDate(cal.getTime());

		assertEquals(1, this.comparator.compare(aw, aw2));
		assertEquals(-1, this.comparator.compare(aw2, aw));
	}

	public StateAware getStateAware() {
		return new StateAware() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			State state = new State("type", "value", new Position(), "");

			@Override
			public String getStateDetails() {
				return null;
			}

			@Override
			public State getState() {
				return this.state;
			}

		};
	}
}
