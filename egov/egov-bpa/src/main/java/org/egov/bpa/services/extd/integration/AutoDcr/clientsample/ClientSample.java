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
package org.egov.bpa.services.extd.integration.AutoDcr.clientsample;

import org.egov.bpa.services.extd.integration.AutoDcr.*;

public class ClientSample {

	public static void main(String[] args) {
		try{
			String url="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
			
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	           Service1 service1 = new Service1();
	        System.out.println("Create Web Service...");
	        Service1Soap port1 = service1.getService1Soap();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.getProposalStatus("COC/13782/14"));
	         //System.out.println("Server said: " + port1.getProposalDetails("COC/14133/14"));
	        System.out.println("Create Web Service...");
	        Service1Soap port2 = service1.getService1Soap12();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.getProposalStatus("COC/13782/14"));
	        System.out.println("Server said: " + port2.getProposalDetails("COC/13782/14"));
	        System.out.println("Create Web Service...");
	        
	        
	        String proposalDetails = port2.getProposalDetails("COC/13782/14");
		    String proposalStatus=port1.getProposalStatus("COC/13782/14");
		    
			 // String proposalDetails ="<AUTODCRBPSIntegration  xmlns=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap11-enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"http://tempuri.org/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><GetFileDetails><ProposalInfo FileNO=\"COC/13782/14\" CaseType=\"Demolition and Reconstruction\" BuildingCategory=\"NA\" LandUseZone=\"Detached Area\" ProposlaType=\"Residential\" InwardDate=\"8/30/2014 10:25:06 AM\" Zone=\"Zone-08\" Division=\"Div - 108\" PlotNO=\"1\" RoadName=\"VEERAPANDI NAGAR 2ND STREET\" DoorNo=\"NEW NO:2, OLD NO:14\" SurveyNo=\"T.S.NO:27\" RevenueVillage=\"PULIYUR\" BlockNo=\"10\" ApplicantName=\"S.SADANAD(G.P.A OF T.S.PRABHAKARAN)\" MobileNo=\"9940320671\" EmailId=\" \" UniqueId=\"COC/1551577473\" PattaPlotArea=\"222.96\" DocumentPlotArea=\"222.96\" SitePlotArea=\"222.96\" /></GetFileDetails></AUTODCRBPSIntegration>";
			  
		//		  String proposalDetails ="<AUTODCRBPSIntegration  xmlns=\"http://tempuri.org/\" ><GetFileDetails><ProposalInfo FileNO=\"COC/13782/14\" CaseType=\"Demolition and Reconstruction\" BuildingCategory=\"NA\" LandUseZone=\"Detached Area\" ProposlaType=\"Residential\" InwardDate=\"8/30/2014 10:25:06 AM\" Zone=\"Zone-08\" Division=\"Div - 108\" PlotNO=\"1\" RoadName=\"VEERAPANDI NAGAR 2ND STREET\" DoorNo=\"NEW NO:2, OLD NO:14\" SurveyNo=\"T.S.NO:27\" RevenueVillage=\"PULIYUR\" BlockNo=\"10\" ApplicantName=\"S.SADANAD(G.P.A OF T.S.PRABHAKARAN)\" MobileNo=\"9940320671\" EmailId=\" \" UniqueId=\"COC/1551577473\" PattaPlotArea=\"222.96\" DocumentPlotArea=\"222.96\" SitePlotArea=\"222.96\" /></GetFileDetails></AUTODCRBPSIntegration>";  
			//  	  String proposalStatus="<AUTODCRBPSIntegration  xmlns=\"http://tempuri.org/\" ><GetFileStatus><ProposalInfo Status=\"Drawing Approved\" /></GetFileStatus></AUTODCRBPSIntegration>";

			System.out.println("proposalDetails..." + proposalDetails);
		    System.out.println("proposalStatus..." +proposalStatus);
		   
		   // System.out.println("fetFileDtls..." + proposalDetails.replace("\"","'").replace("<","<{http://tempuri.org/}"));
		    //System.out.println("status..." +proposalStatus.replace("\"","'").replace("<","<{http://tempuri.org/}"));
		  
		    AutoDcrBpaIntegration fetFileDtls = (AutoDcrBpaIntegration) AutoDcrXMLConverter.unmarshall(AutoDcrBpaIntegration.class,((proposalDetails.replace("\"","'").replace("<AUTODCRBPSIntegration>","<AUTODCRBPSIntegration  xmlns=\"http://tempuri.org/\" >"))));
		   AutoDcrBpaIntegration status = (AutoDcrBpaIntegration) AutoDcrXMLConverter.unmarshall(AutoDcrBpaIntegration.class, url.concat(proposalStatus.replace("\"","'").replace("<AUTODCRBPSIntegration>","<AUTODCRBPSIntegration  xmlns=\"http://tempuri.org/\" >")));
		    /* AutoDcrBpaIntegration fetFileDtls = (AutoDcrBpaIntegration) AutoDcrXMLConverter.unmarshall(AutoDcrBpaIntegration.class, proposalDetails.replace("\"","'").replace("<","<{http://tempuri.org/}"));
		    AutoDcrBpaIntegration status = (AutoDcrBpaIntegration) AutoDcrXMLConverter.unmarshall(AutoDcrBpaIntegration.class, proposalStatus.replace("\"","'").replace("<","<{http://tempuri.org/}"));
			  */ 
		   System.out.println("file Status ..." +status.getFileStatus().getProposalInfo().getStatus());
		   
		     System.out.println("fetFileDtl..." +fetFileDtls.getGetFileDetails().getProposalInfo().getFileNumber());
		     System.out.println("revenue village..." +fetFileDtls.getGetFileDetails().getProposalInfo().getRevenueVillage());
		     System.out.println("Building Name ..." +fetFileDtls.getBuildingDetails().getBuildingInfo().getBuildingName());
		     
		    System.out.println("plotinfo Status ..." +fetFileDtls.getPlotDetails().getPlotInfo().getTotalBuildUpArea());
		     System.out.println("floor size ..." +fetFileDtls.getFloorDetails().getFloorInfos().get(0).getFloorName());
		       
	       /* Service1HttpGet port3 = service1.getService1HttpGet();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port3.getProposalStatus("COC/14133/14"));
	        System.out.println("Server said: " + port3.getProposalDetails("COC/14133/14"));
	        System.out.println("Create Web Service...");
	        Service1HttpPost port4 = service1.getService1HttpPost();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port4.getProposalStatus("COC/14133/14"));
	        System.out.println("Server said: " + port4.getProposalDetails("COC/14133/14"));*/
	        System.out.println("***********************");
	        System.out.println("Call Over!");
		} catch(ExceptionInInitializerError ex)
	    {
			//ex.printStackTrace();
		System.out.println("error in ExceptionInInitializerError" + ex.getMessage());
	    }
		 catch(Exception ex)
		    {
				//ex.printStackTrace();
			System.out.println("error in Exception" + ex.getMessage());
		    }
	}
}
