/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.eis.utils;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;

@Service
@Transactional(readOnly = true)
public class EisUtils {

    public Style getTextStyleLeftBorder() {
        final Style textStyle = getTextStyle();
        textStyle.setName("textStyleLeftBorder");
        textStyle.setBorderLeft(Border.THIN());
        return textStyle;
    }
    
    public Style getTextStyle() {
        final Style textStyle = new Style("textStyle");
        textStyle.setTextColor(Color.BLACK);
        textStyle.setHorizontalAlign(HorizontalAlign.LEFT);
        textStyle.setFont(new Font(10, Font._FONT_VERDANA, false));
        textStyle.setPaddingLeft(5);
        textStyle.setTransparency(Transparency.OPAQUE);
        textStyle.setBorderRight(Border.THIN());
        textStyle.setBorderBottom(Border.THIN());
        textStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        textStyle.setStretchWithOverflow(true);
        return textStyle;
    }

    public Style getHeaderStyle() {
        final Style headerStyle = new Style("header");
        headerStyle.setFont(new Font(10, Font._FONT_ARIAL, true, false, false));
        headerStyle.setBorder(Border.THIN());
        headerStyle.setTextColor(Color.BLACK);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);
        headerStyle.setStretchWithOverflow(true);
        headerStyle.setPaddingLeft(5);
        return headerStyle;
    }

    public Style getSubTitleStyle() {
        final Style subTitleStyle = new Style("subTitleStyle");
        subTitleStyle.setFont(new Font(12, Font._FONT_ARIAL, true, false, false));
        subTitleStyle.setTextColor(Color.BLACK);
        subTitleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        return subTitleStyle;
    }

    public Style getTitleStyle() {
        final Style titleStyle = new Style("titleStyle");
        titleStyle.setFont(Font.VERDANA_BIG_BOLD);
        titleStyle.setTextColor(Color.BLACK);
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        titleStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        return titleStyle;
    }
    
    public Style getHeaderStyleLeftAlign() {
        final Style headerStyle = getHeaderStyle();
        headerStyle.setName("headerStyleLeftAlign");
        headerStyle.setHorizontalAlign(HorizontalAlign.LEFT);
        return headerStyle;
    }
}
