/*
 * Created on July 25, 2008
 * @author Iliyaraja.S
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;


import com.exilant.exility.common.TaskFailedException;

public class SchemeUtilizationReport
{

	private static final Logger LOGGER = Logger.getLogger(SchemeUtilizationReport.class);
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	String filterGlcode="";
	List part1 = new ArrayList();
	List part2 = new ArrayList();
	List part3 = new ArrayList();
	List partTotalReceipts = new ArrayList();
	List part4 = new ArrayList();
	List part5 = new ArrayList();
	List partTotalClosingBal = new ArrayList();
	CallableStatement cstmt = null;
	CallableStatement cstmt2 = null;
	String dynamicSubSchemName[];
	private static TaskFailedException taskExc;

	public ArrayList getSchemeUtilizationReport(int pschemeid,String startDate, String endDate) throws Exception
	{

		Connection con = null;
		try
		{
			con = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();

			LinkedHashMap data = null;
			
				int majorCodeLength=Integer.parseInt(EGovConfig.getProperty("egf_config.xml","majorcodevalue","","AccountCode"));

				filterGlcode=EGovConfig.getProperty("egf_config.xml","filterGlcodeForScheme","","SchemeUtilization");
				if(LOGGER.isInfoEnabled())     LOGGER.info("SchemeUtilizationReport filterGlcode--->"+filterGlcode);
				if(LOGGER.isInfoEnabled())     LOGGER.info("pschemeid="+pschemeid+",startDate="+formatter.format(sdf.parse(startDate))+",endDate="+formatter.format(sdf.parse(endDate))+",majorcodelength="+majorCodeLength);

	/*  For Opening balance Scheme and Sub scheme wise procedure  start */

				if(LOGGER.isInfoEnabled())     LOGGER.info("For Opening balance Scheme and Sub scheme wise procedure  start");


				CallableStatement cstmt = con.prepareCall("{? = call EGF_REPORT.OPBALSCHEMEREPORT(?,?,?,?)}");
				cstmt.setFetchSize(1000);
				//This fix is for Phoenix Migration.
				//cstmt.registerOutParameter(1,OracleTypes.CURSOR);

				cstmt.setInt(2, pschemeid);
				cstmt.setString(3,formatter.format(sdf.parse(startDate)));
				//cstmt.setString(4,formatter.format(sdf.parse(endDate)));


				cstmt.setInt(4, majorCodeLength);
				cstmt.setString(5,filterGlcode);

				if(LOGGER.isInfoEnabled())     LOGGER.info("BEFORE EXECUTE PROCEDURE for Opening Balance");
				cstmt.executeQuery();
				if(LOGGER.isInfoEnabled())     LOGGER.info("AFTER EXECUTE PROCEDURE for Opening Balance");

				ResultSet rs = (ResultSet)cstmt.getObject(1);
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount =rsmd.getColumnCount();

				if(LOGGER.isInfoEnabled())     LOGGER.info("Total result set column count-->"+columnCount);

				//Here number of static column is "1",that's why we have to subtract "1" from total column count,now we will get the dynamic Column count
				int dynamicColumn = columnCount - 1;
				//int count=0;
				//double totalSubScheme =0;
				//Double netTotalSubScheme=0.0;
				LinkedHashMap columnTotalMap = new LinkedHashMap();
				String columnLabel="",amt="";

				if(rs.next())
				{
						if(LOGGER.isInfoEnabled())     LOGGER.info("Inside while-for Opening balance Row");
						data = new LinkedHashMap();
						data.put("particulars", "<b>Opening Balance</b>");

						for(int j=0;j<dynamicColumn;j++)
						{
							//if(LOGGER.isInfoEnabled())     LOGGER.info("dynamicColumn amount--->"+rs.getString(j+1));
							//Here dynamic Column is starting from "1",that's why we have to start  dynamic Column count from "1" onwards.
							amt = rs.getString(j+1);
							if(amt == null)
							amt="0.00";
							columnLabel = rsmd.getColumnName(j+1);
							data.put(columnLabel, formatAmtTwoDecimal(amt));
						}// for
						if(LOGGER.isInfoEnabled())     LOGGER.info("scheme total--->"+rs.getString("SCHAMT"));
						if (rs.getString("SCHAMT") != null)
						{
							data.put("schemetotal", formatAmtTwoDecimal(rs.getString("SCHAMT")));
						}
						else
							data.put("schemetotal","0.00" );

						part1.add(data);
				}
				else	//if no records in opening balance
				{
					if(LOGGER.isInfoEnabled())     LOGGER.info("Inside else--No records for Opening balance ");
					data = new LinkedHashMap();
					data.put("particulars", "<b>Opening Balance</b>");

					for(int j=0;j<dynamicColumn;j++)
					{
						columnLabel = rsmd.getColumnName(j+1);
						data.put(columnLabel,"0.00");
					}// for

					//data.put("subschemetotal",formatAmtTwoDecimal("0.00"));
					data.put("schemetotal", formatAmtTwoDecimal("0.00"));
					part1.add(data);
				}

				if(LOGGER.isInfoEnabled())     LOGGER.info("For Opening balance Scheme and Sub scheme wise procedure  end");

	/*  For Opening balance Scheme and Sub scheme wise procedure  end */


	/*  For Scheme and Sub scheme wise procedure  start */
			if(LOGGER.isInfoEnabled())     LOGGER.info("For Scheme and Sub scheme wise procedure start");

							CallableStatement cstmt2 = con.prepareCall("{? = call EGF_REPORT.SCHEMEREPORT(?,?,?,?,?)}");
							cstmt2.setFetchSize(1000);
							//This fix is for Phoenix Migration.
							//cstmt2.registerOutParameter(1,OracleTypes.CURSOR);

							cstmt2.setInt(2, pschemeid);
							cstmt2.setString(3,formatter.format(sdf.parse(startDate)));
							cstmt2.setString(4,formatter.format(sdf.parse(endDate)));


							cstmt2.setInt(5, majorCodeLength);
							cstmt2.setString(6,filterGlcode);

							if(LOGGER.isInfoEnabled())     LOGGER.info("BEFORE EXECUTE PROCEDURE for Scheme and Sub scheme");
							cstmt2.executeQuery();
							if(LOGGER.isInfoEnabled())     LOGGER.info("AFTER EXECUTE PROCEDURE for Scheme and Sub scheme");

							ResultSet rs2 = (ResultSet)cstmt2.getObject(1);
							ResultSetMetaData rsmd2 = rs2.getMetaData();
							int columnCount2 =rsmd2.getColumnCount();

							if(LOGGER.isInfoEnabled())     LOGGER.info("Total result set column count-->"+columnCount2);

							//Here number of static column is "4",that's why we have to subtract "4" from total column count,now we will get the dynamic Column count.
							int dynamicColumn2 = columnCount2 - 4;
							//int count=0;
							//double totalSubScheme2 =0;
							//Double netTotalSubScheme=0.0;
							LinkedHashMap columnTotalMap2 = new LinkedHashMap();
							//String columnLabel2="",amt2="";

							String addCapitalReceiptsSubHead="";
							String addRevenueReceiptsSubHead="";
							String totalReceiptsSubHead="";

							String lessCapitalExpSubHead="";
							String lessRevenueExpSubHead="";
							String closingBalanceSubHead="";

							dynamicSubSchemName=new String[dynamicColumn2];
							for(int j=0;j<dynamicColumn2;j++)
							{

								//Here dynamic Column is starting from "3",that's why we have to start  dynamic Column count from "3" onwards.
								dynamicSubSchemName[j] = rsmd2.getColumnName(j+3);
								if(LOGGER.isInfoEnabled())     LOGGER.info("ARRAY VALUE dynamicSubSchemName[j]--->"+dynamicSubSchemName[j]);

							}

				while(rs2.next())
				{

								if(LOGGER.isInfoEnabled())     LOGGER.info("Inside while-for Scheme and sub scheme row");

								//double totalSubScheme2 =0;
								String columnLabel2="",amt2="";

								if(rs2.getString("CTYPE").equalsIgnoreCase("L"))
								{
									if(!addCapitalReceiptsSubHead.equals("CRECEIPTS"))
									{
											data = new LinkedHashMap();


											data.put("particulars", "<b>Add: Capital Receipts</b>");

											for(int j=0;j<dynamicColumn2;j++)
											{
												columnLabel2 = rsmd2.getColumnName(j+3);
												data.put(columnLabel2,"&nbsp;");
											}
											//data.put("subschemetotal","&nbsp;");
											data.put("schemetotal", "&nbsp;");

											part2.add(data);
											addCapitalReceiptsSubHead="CRECEIPTS";
									}

									data = new LinkedHashMap();

									data.put("particulars",rs2.getString("MAJORCODE")+"-"+rs2.getString("COANAME"));

									for(int j=0;j<dynamicColumn2;j++)
									{
										//if(LOGGER.isInfoEnabled())     LOGGER.info("dynamicColumn amount--->"+rs2.getString(j+3));
										//Here dynamic Column is starting from "3",that's why we have to start  dynamic Column count from "3" onwards.
										amt2 = rs2.getString(j+3);


										if(amt2 == null)
										amt2="0.00";

										columnLabel2 = rsmd2.getColumnName(j+3);
										data.put(columnLabel2, formatAmtTwoDecimal(amt2));
										//totalSubScheme2= totalSubScheme2 + Double.parseDouble(amt2);


									}// for
									if(LOGGER.isInfoEnabled())     LOGGER.info("scheme total--->"+rs2.getString("SCHAMT"));

									if (rs2.getString("SCHAMT") != null)
									{
										data.put("schemetotal", formatAmtTwoDecimal(rs2.getString("SCHAMT")));
									}
									else
										data.put("schemetotal","0.00" );

									part2.add(data);
								} // for "Liability code"
								else if(rs2.getString("CTYPE").equalsIgnoreCase("I"))
								{
									if(!addRevenueReceiptsSubHead.equals("RENENUERECEIPTS"))
									{
											data = new LinkedHashMap();
											data.put("particulars", "<b>Add: Revenue Receipts</b>");
											for(int j=0;j<dynamicColumn2;j++)
											{
												columnLabel2 = rsmd2.getColumnName(j+3);
												data.put(columnLabel2,"&nbsp;");
											}
											data.put("schemetotal", "&nbsp;");
											part3.add(data);
											addRevenueReceiptsSubHead="RENENUERECEIPTS";
										}
										data = new LinkedHashMap();
										data.put("particulars",rs2.getString("MAJORCODE")+"-"+rs2.getString("COANAME"));
										for(int j=0;j<dynamicColumn2;j++)
										{
											//if(LOGGER.isInfoEnabled())     LOGGER.info("dynamicColumn amount--->"+rs2.getString(j+3));
											amt2 = rs2.getString(j+3);


											if(amt2 == null)
											amt2="0.00";

											columnLabel2 = rsmd2.getColumnName(j+3);
											data.put(columnLabel2, formatAmtTwoDecimal(amt2));
											//totalSubScheme2= totalSubScheme2 + Double.parseDouble(amt2);


										}// for

										if(LOGGER.isInfoEnabled())     LOGGER.info("scheme total--->"+rs2.getString("SCHAMT"));

										if (rs2.getString("SCHAMT") != null)
										{
										data.put("schemetotal", formatAmtTwoDecimal(rs2.getString("SCHAMT")));
										}
										else
										data.put("schemetotal","0.00" );

										part3.add(data);

								} // for "Income code"
								else if(rs2.getString("CTYPE").equalsIgnoreCase("A"))
								{
									if(!lessCapitalExpSubHead.equals("CEXPENDITURE"))
									{
											data = new LinkedHashMap();


											data.put("particulars", "<b>Less: Capital Expenditure</b>");

											for(int j=0;j<dynamicColumn2;j++)
											{
												columnLabel2 = rsmd2.getColumnName(j+3);
												data.put(columnLabel2,"&nbsp;");
											}
											//data.put("subschemetotal","&nbsp;");
											data.put("schemetotal", "&nbsp;");

											part4.add(data);
											lessCapitalExpSubHead="CEXPENDITURE";
										}

										data = new LinkedHashMap();
										data.put("particulars",rs2.getString("MAJORCODE")+"-"+rs2.getString("COANAME"));

										for(int j=0;j<dynamicColumn2;j++)
										{
											//if(LOGGER.isInfoEnabled())     LOGGER.info("dynamicColumn amount--->"+rs2.getString(j+3));
											amt2 = rs2.getString(j+3);


											if(amt2 == null)
											amt2="0.00";

											columnLabel2 = rsmd2.getColumnName(j+3);
											data.put(columnLabel2, formatAmtTwoDecimal(amt2));
											//totalSubScheme2= totalSubScheme2 + Double.parseDouble(amt2);


										}// for

										//if(LOGGER.isInfoEnabled())     LOGGER.info("totalSubScheme2--->"+totalSubScheme2);
										//data.put("subschemetotal", formatAmtTwoDecimal(totalSubScheme2+""));

										if(LOGGER.isInfoEnabled())     LOGGER.info("scheme total--->"+rs2.getString("SCHAMT"));

										if (rs2.getString("SCHAMT") != null)
										{
										data.put("schemetotal", formatAmtTwoDecimal(rs2.getString("SCHAMT")));
										}
										else
										data.put("schemetotal","0.00" );

										part4.add(data);

								} // for "Asset code"
								else if(rs2.getString("CTYPE").equalsIgnoreCase("E"))
								{
									if(!lessRevenueExpSubHead.equals("RENENUEEXPENDITURE"))
									{
											data = new LinkedHashMap();


											data.put("particulars", "<b>Less: Revenue Expenditure</b>");

											for(int j=0;j<dynamicColumn2;j++)
											{
												columnLabel2 = rsmd2.getColumnName(j+3);
												data.put(columnLabel2,"&nbsp;");
											}
											//data.put("subschemetotal","&nbsp;");
											data.put("schemetotal", "&nbsp;");

											part5.add(data);
											lessRevenueExpSubHead="RENENUEEXPENDITURE";
										}

										data = new LinkedHashMap();
										data.put("particulars",rs2.getString("MAJORCODE")+"-"+rs2.getString("COANAME"));

										for(int j=0;j<dynamicColumn2;j++)
										{
											//if(LOGGER.isInfoEnabled())     LOGGER.info("dynamicColumn amount--->"+rs2.getString(j+3));
											amt2 = rs2.getString(j+3);


											if(amt2 == null)
											amt2="0.00";

											columnLabel2 = rsmd2.getColumnName(j+3);
											data.put(columnLabel2, formatAmtTwoDecimal(amt2));
											//totalSubScheme2= totalSubScheme2 + Double.parseDouble(amt2);


										}// for

										//if(LOGGER.isInfoEnabled())     LOGGER.info("totalSubScheme2--->"+totalSubScheme2);
										//data.put("subschemetotal", formatAmtTwoDecimal(totalSubScheme2+""));

										if(LOGGER.isInfoEnabled())     LOGGER.info("scheme total--->"+rs2.getString("SCHAMT"));

										if (rs2.getString("SCHAMT") != null)
										{
										data.put("schemetotal", formatAmtTwoDecimal(rs2.getString("SCHAMT")));
										}
										else
										data.put("schemetotal","0.00" );

										part5.add(data);

								} // for "Expense code"



				}//while



				if(LOGGER.isInfoEnabled())     LOGGER.info("For Scheme and Sub scheme wise procedure end");

				/*  For Scheme and Sub scheme wise procedure  End */


				// If it is no record for that particular ctype,we have to show empty row start

				if(addCapitalReceiptsSubHead.equals(""))
				{

					if(LOGGER.isInfoEnabled())     LOGGER.info("Inside If it is No Capital Receipts---------->");

					data = new LinkedHashMap();
					data.put("particulars", "<b>Add: Capital Receipts</b>");

					for(int j=0;j<dynamicSubSchemName.length;j++)
					{
						//if(LOGGER.isInfoEnabled())     LOGGER.info("dynamicSubSchemName[j]--->"+dynamicSubSchemName[j]);
						data.put(dynamicSubSchemName[j],formatAmtTwoDecimal("0.00"));
					}
					//data.put("subschemetotal","0.00");
					data.put("schemetotal", formatAmtTwoDecimal("0.00"));

					part2.add(data);

				}
				if(addRevenueReceiptsSubHead.equals(""))
				{
					if(LOGGER.isInfoEnabled())     LOGGER.info("Inside If it is No Revenue Receipts----------->");

					data = new LinkedHashMap();
					data.put("particulars", "<b>Add: Revenue Receipts</b>");

					for(int j=0;j<dynamicSubSchemName.length;j++)
					{
						//if(LOGGER.isInfoEnabled())     LOGGER.info("dynamicSubSchemName[j]--->"+dynamicSubSchemName[j]);
						data.put(dynamicSubSchemName[j],formatAmtTwoDecimal("0.00"));
					}
					//data.put("subschemetotal","0.00");
					data.put("schemetotal", formatAmtTwoDecimal("0.00"));

					part3.add(data);

				}
				if(lessCapitalExpSubHead.equals(""))
				{

					if(LOGGER.isInfoEnabled())     LOGGER.info("Inside If it is No Capital Expenditure---------->");
					data = new LinkedHashMap();

					data.put("particulars", "<b>Less: Capital Expenditure</b>");

					for(int j=0;j<dynamicSubSchemName.length;j++)
					{
						//if(LOGGER.isInfoEnabled())     LOGGER.info("dynamicSubSchemName[j]--->"+dynamicSubSchemName[j]);
						data.put(dynamicSubSchemName[j],formatAmtTwoDecimal("0.00"));
					}
					//data.put("subschemetotal","0.00");
					data.put("schemetotal", formatAmtTwoDecimal("0.00"));

					part4.add(data);

				}
				if(lessRevenueExpSubHead.equals(""))
				{
					if(LOGGER.isInfoEnabled())     LOGGER.info("Inside If it is No Revenue Expenditure---------->");
					data = new LinkedHashMap();

					data.put("particulars", "<b>Less: Revenue Expenditure</b>");

					for(int j=0;j<dynamicSubSchemName.length;j++)
					{
						//if(LOGGER.isInfoEnabled())     LOGGER.info("dynamicSubSchemName[j]--->"+dynamicSubSchemName[j]);
						data.put(dynamicSubSchemName[j],formatAmtTwoDecimal("0.00"));
					}
					//data.put("subschemetotal","0.00");
					data.put("schemetotal", formatAmtTwoDecimal("0.00"));

					part5.add(data);
				}
				// If it is no record for that particular ctype,we have to show empty row end



				// cumulative for Total Receipts including Opening Balance start

					LinkedHashMap totalOpeningBal=new LinkedHashMap();


					Iterator itr = part1.iterator();
						while(itr.hasNext())
						{
							LinkedHashMap hm =(LinkedHashMap)itr.next();
							Set keySet = hm.keySet();
							Iterator itr1 = keySet.iterator();
							while (itr1.hasNext())
							{

								String str = (String)itr1.next();
								for (int i = 0; i < dynamicSubSchemName.length; i++)
								{
									if(totalOpeningBal.containsKey(dynamicSubSchemName[i]))
									{
										BigDecimal temp=new BigDecimal("0.00");
										if(!hm.get(dynamicSubSchemName[i]).equals("&nbsp;") && str.equals(dynamicSubSchemName[i]))
											temp=new BigDecimal((totalOpeningBal.get(dynamicSubSchemName[i]).toString())).add(new BigDecimal((hm.get(dynamicSubSchemName[i]).toString())));
										else
											temp=new BigDecimal((totalOpeningBal.get(dynamicSubSchemName[i]).toString()));
										totalOpeningBal.put(dynamicSubSchemName[i], temp);
									}
									else
									{

										if(str.equals(dynamicSubSchemName[i]) && !hm.get(dynamicSubSchemName[i]).equals("&nbsp;"))
										{
											totalOpeningBal.put(dynamicSubSchemName[i], (hm.get(dynamicSubSchemName[i]).toString()));
										}
									}
								}//end for

									// for scheme total
									if(totalOpeningBal.containsKey("schemetotal"))
									{

										BigDecimal temp=new BigDecimal("0.00");
										if(!hm.get("schemetotal").equals("&nbsp;") && str.equals("schemetotal"))
											temp=new BigDecimal((totalOpeningBal.get("schemetotal").toString())).add(new BigDecimal((hm.get("schemetotal").toString())));
										else
											temp=new BigDecimal((totalOpeningBal.get("schemetotal").toString()));
										totalOpeningBal.put("schemetotal", temp);
									}
									else
									{

										if(str.equals("schemetotal") && !hm.get("schemetotal").equals("&nbsp;"))
										{
											totalOpeningBal.put("schemetotal", (hm.get("schemetotal").toString()));
										}
									}

									//
							}//end while 2
						}//end while 1



						Iterator itr2 = part2.iterator();
						while(itr2.hasNext())
						{
							LinkedHashMap hm =(LinkedHashMap)itr2.next();
							Set keySet = hm.keySet();
							Iterator itr1 = keySet.iterator();
							while (itr1.hasNext())
							{
								String str = (String)itr1.next();
								for (int i = 0; i < dynamicSubSchemName.length; i++)
								{
									if(totalOpeningBal.containsKey(dynamicSubSchemName[i]))
									{
										BigDecimal temp=new BigDecimal("0.00");
										if(!hm.get(dynamicSubSchemName[i]).equals("&nbsp;") && str.equals(dynamicSubSchemName[i]))
											temp=new BigDecimal((totalOpeningBal.get(dynamicSubSchemName[i]).toString())).add(new BigDecimal((hm.get(dynamicSubSchemName[i]).toString())));
										else
											temp=new BigDecimal((totalOpeningBal.get(dynamicSubSchemName[i]).toString()));
										totalOpeningBal.put(dynamicSubSchemName[i], temp);
									}
									else
									{
										if(str.equals(dynamicSubSchemName[i]) && !hm.get(dynamicSubSchemName[i]).equals("&nbsp;"))
										{
											totalOpeningBal.put(dynamicSubSchemName[i], (hm.get(dynamicSubSchemName[i]).toString()));
										}
									}
								}//end for

									// for scheme total
									if(totalOpeningBal.containsKey("schemetotal"))
									{
										BigDecimal temp=new BigDecimal("0.00");
										if(!hm.get("schemetotal").equals("&nbsp;") && str.equals("schemetotal"))
											temp=new BigDecimal((totalOpeningBal.get("schemetotal").toString())).add(new BigDecimal((hm.get("schemetotal").toString())));
										else
											temp=new BigDecimal((totalOpeningBal.get("schemetotal").toString()));
										totalOpeningBal.put("schemetotal", temp);
									}
									else
									{
										if(str.equals("schemetotal") && !hm.get("schemetotal").equals("&nbsp;"))
										{
											totalOpeningBal.put("schemetotal", (hm.get("schemetotal").toString()));
										}
									}

									//
							}//end while 2
						}//end while 1


						Iterator itr3 = part3.iterator();
						while(itr3.hasNext())
						{
							LinkedHashMap hm =(LinkedHashMap)itr3.next();
							Set keySet = hm.keySet();
							Iterator itr1 = keySet.iterator();
							while (itr1.hasNext())
							{
								String str = (String)itr1.next();
								for (int i = 0; i < dynamicSubSchemName.length; i++)
								{
									if(totalOpeningBal.containsKey(dynamicSubSchemName[i]))
									{
										BigDecimal temp=new BigDecimal("0.00");
										if(!hm.get(dynamicSubSchemName[i]).equals("&nbsp;") && str.equals(dynamicSubSchemName[i]))
											temp=new BigDecimal((totalOpeningBal.get(dynamicSubSchemName[i]).toString())).add(new BigDecimal((hm.get(dynamicSubSchemName[i]).toString())));
										else
											temp=new BigDecimal((totalOpeningBal.get(dynamicSubSchemName[i]).toString()));
										totalOpeningBal.put(dynamicSubSchemName[i], temp);
									}
									else
									{
										if(str.equals(dynamicSubSchemName[i]) && !hm.get(dynamicSubSchemName[i]).equals("&nbsp;"))
										{
											totalOpeningBal.put(dynamicSubSchemName[i], (hm.get(dynamicSubSchemName[i]).toString()));
										}
									}
								}//end for

									// for scheme total
									if(totalOpeningBal.containsKey("schemetotal"))
									{
										BigDecimal temp=new BigDecimal("0.00");
										if(!hm.get("schemetotal").equals("&nbsp;") && str.equals("schemetotal"))
											temp=new BigDecimal((totalOpeningBal.get("schemetotal").toString())).add(new BigDecimal((hm.get("schemetotal").toString())));
										else
											temp=new BigDecimal((totalOpeningBal.get("schemetotal").toString()));
										totalOpeningBal.put("schemetotal", temp);
									}
									else
									{
										if(str.equals("schemetotal") && !hm.get("schemetotal").equals("&nbsp;"))
										{
											totalOpeningBal.put("schemetotal", (hm.get("schemetotal").toString()));
										}
									}

									//
							}//end while 2
						}//end while 1


					if(!totalReceiptsSubHead.equals("TotalReceipts"))
					{
							data = new LinkedHashMap();


							data.put("particulars", "<b>Total Receipts including Opening Balance</b>");

							for(int j=0;j<dynamicSubSchemName.length;j++)
							{
								 data.put(dynamicSubSchemName[j], "<b>"+(totalOpeningBal.get(dynamicSubSchemName[j]).toString())+"</b>");
							}
							//data.put("subschemetotal","<b>"+(totalOpeningBal.get("subschemetotal").toString())+"</b>");
							data.put("schemetotal","<b>"+(totalOpeningBal.get("schemetotal").toString())+"</b>");

							partTotalReceipts.add(data);
							totalReceiptsSubHead="TotalReceipts";
					}

				// cumulative for Total Receipts including Opening Balance end


				// cumulative for Total Closing Balance start

										Iterator itr4 = part4.iterator();
										while(itr4.hasNext())
										{
											LinkedHashMap hm =(LinkedHashMap)itr4.next();
											Set keySet = hm.keySet();
											Iterator itr1 = keySet.iterator();
											while (itr1.hasNext())
											{
												String str = (String)itr1.next();
												for (int i = 0; i < dynamicSubSchemName.length; i++)
												{
													if(totalOpeningBal.containsKey(dynamicSubSchemName[i]))
													{
														BigDecimal temp=new BigDecimal("0.00");
														if(!hm.get(dynamicSubSchemName[i]).equals("&nbsp;") && str.equals(dynamicSubSchemName[i]))
															temp=new BigDecimal((totalOpeningBal.get(dynamicSubSchemName[i]).toString())).subtract(new BigDecimal((hm.get(dynamicSubSchemName[i]).toString())));
														else
															temp=new BigDecimal((totalOpeningBal.get(dynamicSubSchemName[i]).toString()));
														totalOpeningBal.put(dynamicSubSchemName[i], temp);
													}
													else
													{
														if(str.equals(dynamicSubSchemName[i]) && !hm.get(dynamicSubSchemName[i]).equals("&nbsp;"))
														{
															totalOpeningBal.put(dynamicSubSchemName[i], (hm.get(dynamicSubSchemName[i]).toString()));
														}
													}
												}//end for

													// for scheme total
													if(totalOpeningBal.containsKey("schemetotal"))
													{
														BigDecimal temp=new BigDecimal("0.00");
														if(!hm.get("schemetotal").equals("&nbsp;") && str.equals("schemetotal"))
															temp=new BigDecimal((totalOpeningBal.get("schemetotal").toString())).subtract(new BigDecimal((hm.get("schemetotal").toString())));
														else
															temp=new BigDecimal((totalOpeningBal.get("schemetotal").toString()));
														totalOpeningBal.put("schemetotal", temp);
													}
													else
													{
														if(str.equals("schemetotal") && !hm.get("schemetotal").equals("&nbsp;"))
														{
															totalOpeningBal.put("schemetotal", (hm.get("schemetotal").toString()));
														}
													}

													//
											}//end while 2
										}//end while 1



										Iterator itr5 = part5.iterator();
										while(itr5.hasNext())
										{
											LinkedHashMap hm =(LinkedHashMap)itr5.next();
											Set keySet = hm.keySet();
											Iterator itr1 = keySet.iterator();
											while (itr1.hasNext())
											{
												String str = (String)itr1.next();
												for (int i = 0; i < dynamicSubSchemName.length; i++)
												{
													if(totalOpeningBal.containsKey(dynamicSubSchemName[i]))
													{
														BigDecimal temp=new BigDecimal(0);
														if(!hm.get(dynamicSubSchemName[i]).equals("&nbsp;") && str.equals(dynamicSubSchemName[i]))
															temp=new BigDecimal((totalOpeningBal.get(dynamicSubSchemName[i]).toString())).subtract(new BigDecimal((hm.get(dynamicSubSchemName[i]).toString())));
														else
															temp=new BigDecimal((totalOpeningBal.get(dynamicSubSchemName[i]).toString()));
														totalOpeningBal.put(dynamicSubSchemName[i], temp);
													}
													else
													{
														if(str.equals(dynamicSubSchemName[i]) && !hm.get(dynamicSubSchemName[i]).equals("&nbsp;"))
														{
															totalOpeningBal.put(dynamicSubSchemName[i], (hm.get(dynamicSubSchemName[i]).toString()));
														}
													}
												}//end for

													
													// for scheme total
													if(totalOpeningBal.containsKey("schemetotal"))
													{
														BigDecimal temp=new BigDecimal(0);
														if(!hm.get("schemetotal").equals("&nbsp;") && str.equals("schemetotal"))
															temp=new BigDecimal((totalOpeningBal.get("schemetotal").toString())).subtract(new BigDecimal((hm.get("schemetotal").toString())));
														else
															temp=new BigDecimal((totalOpeningBal.get("schemetotal").toString()));
														totalOpeningBal.put("schemetotal", temp);
													}
													else
													{
														if(str.equals("schemetotal") && !hm.get("schemetotal").equals("&nbsp;"))
														{
															totalOpeningBal.put("schemetotal", (hm.get("schemetotal").toString()));
														}
													}

													//
											}//end while 2
										}//end while 1


									if(!closingBalanceSubHead.equals("ClosingBalance "))
									{
											data = new LinkedHashMap();


											data.put("particulars", "<b>Closing Balance</b>");

											for(int j=0;j<dynamicSubSchemName.length;j++)
											{
												 data.put(dynamicSubSchemName[j], "<b>"+(totalOpeningBal.get(dynamicSubSchemName[j]).toString())+"</b>");
											}
											//data.put("subschemetotal","<b>"+(totalOpeningBal.get("subschemetotal").toString())+"</b>");
											data.put("schemetotal","<b>"+(totalOpeningBal.get("schemetotal").toString())+"</b>");

											partTotalClosingBal.add(data);
											totalReceiptsSubHead="ClosingBalance";
									}



				// cumulative for Total Closing Balance end




			
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in Scheme Report=="+e.getMessage());
			throw taskExc;
		}

				//ArrayList finalReportList=(ArrayList)part1;

				ArrayList finalReportList=new ArrayList();


				Iterator itrOpBal = part1.iterator();
				while(itrOpBal.hasNext())
				{
					LinkedHashMap hm =(LinkedHashMap)itrOpBal.next();
					finalReportList.add(hm);
				}


				Iterator itr1 = part2.iterator();
				while(itr1.hasNext())
				{
					LinkedHashMap hm =(LinkedHashMap)itr1.next();
					finalReportList.add(hm);
				}
				Iterator itr2 = part3.iterator();
				while(itr2.hasNext())
				{
					LinkedHashMap hm =(LinkedHashMap)itr2.next();
					finalReportList.add(hm);
				}

				Iterator itrTotalR = partTotalReceipts.iterator();
				while(itrTotalR.hasNext())
				{
					LinkedHashMap hm =(LinkedHashMap)itrTotalR.next();
					finalReportList.add(hm);
				}


				Iterator itr3 = part4.iterator();
				while(itr3.hasNext())
				{
					LinkedHashMap hm =(LinkedHashMap)itr3.next();
					finalReportList.add(hm);
				}

				Iterator itr4 = part5.iterator();
				while(itr4.hasNext())
				{
					LinkedHashMap hm =(LinkedHashMap)itr4.next();
					finalReportList.add(hm);
				}

				Iterator itrClosingTotal = partTotalClosingBal.iterator();
				while(itrClosingTotal.hasNext())
				{
					LinkedHashMap hm =(LinkedHashMap)itrClosingTotal.next();
					finalReportList.add(hm);
				}



		return finalReportList;
	}
public String formatAmtTwoDecimal(String amt)
				    {
				   //     BigDecimal ammt= new BigDecimal(0.000);
				        NumberFormat formatter;
				        formatter = new DecimalFormat("##############0.00");

			        return formatter.format(new BigDecimal(amt));
		   }


} // CLASS
