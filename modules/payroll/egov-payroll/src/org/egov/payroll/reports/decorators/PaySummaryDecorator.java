package org.egov.payroll.reports.decorators;
/**
 * Licensed under the Artistic License; you may not use this file
 * except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://displaytag.sourceforge.net/license.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
//package org.displaytag.decorator;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.decorator.TableDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.model.HeaderCell;
import org.displaytag.model.TableModel;


/**
 * A table decorator which adds rows with totals (for column with the "total" attribute set) and subtotals (grouping by
 * the column with a group="1" attribute).
 * @author Fabrizio Giustina
 * @version $Id$
 */
public class PaySummaryDecorator extends TableDecorator
{

    /**
     * Logger.
     */
    private static final Logger log = Logger.getLogger(PaySummaryDecorator.class);

    /**
     * total amount.
     */
    private Map grandTotals = new HashMap();
    
    /**
     * earnings total amount
     */
    private Map earningsTotals = new HashMap();
    
    /**
     * deductions total amount
     */
    private Map deductionsTotals = new HashMap();

    /**
     * total amount for current group.
     */
    private Map subTotals = new HashMap();

    /**
     * Previous values needed for grouping.
     */
    private Map previousValues = new HashMap();

    /**
     * Name of the property used for grouping.
     */
    private String groupPropertyName;

    /**
     * Label used for subtotals. Default: "{0} total".
     */
    private String subtotalLabel = "Total {0}";

    /**
     * Label used for totals. Default: "Total".
     */
    private String totalLabel = "Total";
    
    /**
     * Label used for net pay. Default: "Net Pay".
     */
    private String netPayLabel = "Net Pay";

    /**
     * Setter for <code>subtotalLabel</code>.
     * @param subtotalLabel The subtotalLabel to set.
     */
    public void setSubtotalLabel(String subtotalLabel)
    {
        this.subtotalLabel = subtotalLabel;
    }

    /**
     * Setter for <code>totalLabel</code>.
     * @param totalLabel The totalLabel to set.
     */
    public void setTotalLabel(String totalLabel)
    {
        this.totalLabel = totalLabel;
    }

    /**
     * @see org.displaytag.decorator.Decorator#init(PageContext, Object, TableModel)
     */
    public void init(PageContext context, Object decorated, TableModel tableModel)
    {
        super.init(context, decorated, tableModel);

        // reset
        groupPropertyName = null;
        grandTotals.clear();
        subTotals.clear();
        previousValues.clear();

        for (Iterator it = tableModel.getHeaderCellList().iterator(); it.hasNext();)
        {
            HeaderCell cell = (HeaderCell) it.next();
            if (cell.getGroup() == 1)
            {
                groupPropertyName = cell.getBeanPropertyName();
            }
        }
    }

    public String startRow()
    {
    	TableModel tableModelObj = getTableModel();
    	String groupPropertyNameObj = getGroupPropertyName();
        String subtotalRow = null;
        log.info("groupPropertyName  === "+groupPropertyNameObj);
        if (groupPropertyNameObj != null)
        {
        	Object groupedPropertyValue = evaluate(groupPropertyNameObj);
            Object previousGroupedPropertyValue = previousValues.get(groupPropertyNameObj);
            // subtotals 
            if (previousGroupedPropertyValue != null
                && !ObjectUtils.equals(previousGroupedPropertyValue, groupedPropertyValue))
            {   
                for (Iterator it = tableModelObj.getHeaderCellList().iterator(); it.hasNext();)
                {
                    HeaderCell cell = (HeaderCell) it.next();
                    if (cell.isTotaled())
                    {
                        String totalPropertyName = cell.getBeanPropertyName();                        
                        earningsTotals.put(totalPropertyName,subTotals.get(totalPropertyName));
                    }
                }
                subtotalRow = createTotalRow(false,false);
            }            
            previousValues.put(groupPropertyNameObj, groupedPropertyValue);
        }

        for (Iterator it = tableModelObj.getHeaderCellList().iterator(); it.hasNext();)
        {
            HeaderCell cell = (HeaderCell) it.next();
            if (cell.isTotaled())
            {
                String totalPropertyName = cell.getBeanPropertyName();
                Number amount = (Number) evaluate(totalPropertyName);

                Number previousSubTotal = (Number) subTotals.get(totalPropertyName);
                Number previousGrandTotals = (Number) grandTotals.get(totalPropertyName);

                subTotals.put(totalPropertyName, new Double((previousSubTotal != null
                    ? previousSubTotal.doubleValue()
                    : 0)
                    + (amount != null ? amount.doubleValue() : 0)));

                grandTotals.put(totalPropertyName, new Double((previousGrandTotals != null ? previousGrandTotals
                    .doubleValue() : 0)
                    + (amount != null ? amount.doubleValue() : 0)));
            }
            
            Object groupedPropertyValue = evaluate(groupPropertyNameObj);
            Object previousGroupedPropertyValue = previousValues.get(groupPropertyNameObj);
            if (previousGroupedPropertyValue != null
                    && !ObjectUtils.equals(previousGroupedPropertyValue, groupedPropertyValue))
            {
            	log.info("nothing to write");
            }
        }

        return subtotalRow;
    }

    /**
     * After every row completes we evaluate to see if we should be drawing a new total line and summing the results
     * from the previous group.
     * @return String
     */
    public final String finishRow()
    {
    	TableModel tableModelObj = getTableModel();
    	String groupPropertyNameObj = getGroupPropertyName();
        StringBuffer buffer = new StringBuffer(1000);
        //earningsTotals
        // Grand totals...
        if (getViewIndex() == ((List) getDecoratedObject()).size() - 1)
        {
            if (groupPropertyNameObj != null)
            {
            	for (Iterator it = tableModelObj.getHeaderCellList().iterator(); it.hasNext();)
                {
                    HeaderCell cell = (HeaderCell) it.next();
                    if (cell.isTotaled())
                    {
                        String totalPropertyName = cell.getBeanPropertyName();                        
                        deductionsTotals.put(totalPropertyName,subTotals.get(totalPropertyName));
                    }
                }
            	buffer.append(createTotalRow(false,false));
            	for (Iterator it = tableModelObj.getHeaderCellList().iterator(); it.hasNext();)
                {
                    HeaderCell cell = (HeaderCell) it.next();
                    if (cell.isTotaled())
                    {
                        String totalPropertyName = cell.getBeanPropertyName();
                        Double tempearamt = (Double)(earningsTotals.get(totalPropertyName) ==null ? 0.0  :(Double)earningsTotals.get(totalPropertyName));
                        Double tempdedamt = (Double)(deductionsTotals.get(totalPropertyName) ==null ? 0.0  :(Double)deductionsTotals.get(totalPropertyName));
                        subTotals.put(totalPropertyName,new Double(tempearamt.doubleValue()-tempdedamt.doubleValue()));
                    }
                }
                buffer.append(createTotalRow(false,true));
            }
           // buffer.append(createTotalRow(true));
        }
        return buffer.toString();

    }

    protected String createTotalRow(boolean grandTotal,boolean netPay)
    {
    	TableModel tableModelObj = getTableModel();
    	String groupPropertyNameObj = getGroupPropertyName();
    	
        StringBuffer buffer = new StringBuffer(1000);
        buffer.append("\n<tr class=\"total\">"); //$NON-NLS-1$

        List headerCells = tableModelObj.getHeaderCellList();

        for (Iterator it = headerCells.iterator(); it.hasNext();)
        {
            HeaderCell cell = (HeaderCell) it.next();
            String cssClass = ObjectUtils.toString(cell.getHtmlAttributes().get("class"));

            buffer.append("<td"); //$NON-NLS-1$
            if (StringUtils.isNotEmpty(cssClass))
            {
                buffer.append(" class=\""); //$NON-NLS-1$
                buffer.append(cssClass);
                buffer.append("\""); //$NON-NLS-1$
            }
            buffer.append(">"); //$NON-NLS-1$

            if (cell.isTotaled())
            {
                String totalPropertyName = cell.getBeanPropertyName();
                Object total = grandTotal ? grandTotals.get(totalPropertyName) : subTotals.get(totalPropertyName);

                DisplaytagColumnDecorator[] decorators = cell.getColumnDecorators();
                for (int j = 0; j < decorators.length; j++)
                {
                    try
                    {
                        total = decorators[j].decorate(total, this.getPageContext(), tableModelObj.getMedia());
                    }
                    catch (DecoratorException e)
                    {
                        log.warn(e.getMessage(), e);
                        // ignore, use undecorated value for totals
                    }
                }
                buffer.append(total);
            }
            else if (groupPropertyNameObj != null && groupPropertyNameObj.equals(cell.getBeanPropertyName()))
            {
            	if(netPay){
                buffer.append( netPayLabel );
            	}
            	else{
            		buffer.append(grandTotal ? totalLabel : MessageFormat.format(subtotalLabel, new Object[]{previousValues
                            .get(groupPropertyNameObj)}));
            	}
            }

            buffer.append("</td>"); //$NON-NLS-1$

        }

        buffer.append("</tr>"); //$NON-NLS-1$

        // reset subtotal
        this.subTotals.clear();

        return buffer.toString();
    }
    
    protected TableModel getTableModel(){
    	return tableModel;
    }
    
    protected String getGroupPropertyName(){
    	return groupPropertyName;
    }

}

