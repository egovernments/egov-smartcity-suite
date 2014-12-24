package org.egov.works.services;

import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.egov.infstr.utils.StringUtils;
import org.egov.works.models.masters.ContractorQuote;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class TenderResponseClient {

	private ContractorService contractorBidderService;

	public List<ContractorQuote> getResponseFromeTender(String noticeNumber) throws Exception
	{
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		//String responseXML =  service.path("rest").path("contractor").accept(MediaType.TEXT_XML).get(String.class);
		//System.out.println("<===Response XML===>"+responseXML);

		String responseXML = 
				"<TenderResponse>"+
						"<AdvertisementNumber></AdvertisementNumber>"+
						"<NITNumber>6/BI/264</NITNumber>"+
						"<Contractors>"+
						"<Contractor>"+
						"<ContractorCode>NMC-459/1311</ContractorCode>"+
						"<Percentage>-2</Percentage>"+
						"</Contractor>"+
						"<Contractor>"+
						"<ContractorCode>NMC-370/1222</ContractorCode>"+
						"<Percentage>2</Percentage>"+
						"</Contractor>"+
						"</Contractors>"+
						"</TenderResponse>";

		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(responseXML));
		Document doc = db.parse(is);
		NodeList nodes = doc.getElementsByTagName("Contractor");
		NodeList noticeNumberNodes = doc.getElementsByTagName("NITNumber");

		for (int i = 0; i < noticeNumberNodes.getLength(); i++) {
			Element element = (Element) noticeNumberNodes.item(i);
			noticeNumber = element.getTextContent();
		}
		
		List<ContractorQuote> contractorQuoteList = new ArrayList<ContractorQuote>();

		for (int i = 0; i < nodes.getLength(); i++) {
			ContractorQuote cQuote = new ContractorQuote();
			Element element = (Element) nodes.item(i);
			NodeList name = element.getElementsByTagName("ContractorCode");
			Element code = (Element) name.item(0);
			String contractorCode = getCharacterDataFromElement(code);
			cQuote.setContractor(contractorBidderService.getBidderByCode(contractorCode));
			NodeList title = element.getElementsByTagName("Percentage");
			Element line = (Element) title.item(0);
			cQuote.setPercentage(new BigDecimal(getCharacterDataFromElement(line)));
			contractorQuoteList.add(cQuote);
		}
		
		return contractorQuoteList;
	}


	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:9880/egworks").build();
	}


	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return StringUtils.EMPTY;
	}


	public ContractorService getContractorBidderService() {
		return contractorBidderService;
	}


	public void setContractorBidderService(ContractorService contractorBidderService) {
		this.contractorBidderService = contractorBidderService;
	}

	
	

}
