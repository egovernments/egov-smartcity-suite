package org.egov.eis.utils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import java.awt.Color;

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
