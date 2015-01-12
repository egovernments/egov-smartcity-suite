package org.egov.infstr.mail;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailServiceTest {

	private final MailService mailService = new MailService();

	@Before
	public void setUp() throws Exception {
		this.mailService.setMailMessage(new SimpleMailMessage());
		final JavaMailSenderImpl sender = Mockito
				.mock(JavaMailSenderImpl.class);
		Mockito.doThrow(new RuntimeException("")).when(sender)
				.send(Matchers.any(SimpleMailMessage.class));
		this.mailService.setMailSender(sender);
		this.mailService.setMailType("feedbackmail");
	}

	@Test
	public void testSentMail() {
		final boolean val = this.mailService.sendMail("message", "subject");
		assertFalse(val);
	}
}
