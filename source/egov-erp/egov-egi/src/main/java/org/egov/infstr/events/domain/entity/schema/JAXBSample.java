package org.egov.infstr.events.domain.entity.schema;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JAXBSample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance("org.egov.infstr.events.domain.entity.schema");
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			final String text = "<response> <email> <to>ro@coc.gov.in</to> <cc>${cc_address}</cc>  <body> Dear ${to_name}, The following receipt has been cancelled - lorem ipsum ${receipt_no} dolor lorem dolor </body> </email> </response>";

			/*
			 * 12. Convert String to InputStream using ByteArrayInputStream 13. class. This class constructor takes the string byte array 14. which can be done by calling the getBytes() method. 15.
			 */

			final InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
			final Response response = (Response) unmarshaller.unmarshal(new File("src/org/egov/infstr/events/domain/entity/schema/Response.xml"));
			final Response reponseInput = (Response) unmarshaller.unmarshal(is);
			System.out.println("response = " + response.getSms().get(0).getPhonenumber());
			System.out.println("reponseInput = " + reponseInput.getEmail().get(0));
			EmailType email = (EmailType) reponseInput.getEmail().get(0);
			System.out.println("email::::::::::"+email.to.get(0));

		} catch (final JAXBException e) {
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
