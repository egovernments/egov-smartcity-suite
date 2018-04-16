/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.infra.web.support.ui.menu;

import java.util.List;

public class Menu {
    public static final String NAVIGATION_NONE = "javascript:void(0);";
    public static final String APP_MENU_MAIN_ICON = "fa fa-reply-all";
    public static final String APP_MENU_ICON = "fa fa-th floatLeft";
    public static final String FAV_MENU_ICON = "fa fa-star floatLeft";
    public static final String SELFSERVICE_MENU_ICON = "fa fa-ellipsis-h floatLeft";

    private String id;
    private String title;
    private String name;
    private String link;
    private String icon;
    private List<Menu> items;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }

    public List<Menu> getItems() {
        return items;
    }

    public void setItems(final List<Menu> items) {
        this.items = items;
    }

    public static final class MenuBuilder {
        private Menu menu;

        private MenuBuilder() {
            menu = new Menu();
        }

        public static MenuBuilder aMenu() {
            return new MenuBuilder();
        }

        public MenuBuilder withId(String id) {
            menu.setId(id);
            return this;
        }

        public MenuBuilder withTitle(String title) {
            menu.setTitle(title);
            return this;
        }

        public MenuBuilder withName(String name) {
            menu.setName(name);
            return this;
        }

        public MenuBuilder withLink(String link) {
            menu.setLink(link);
            return this;
        }

        public MenuBuilder withIcon(String icon) {
            menu.setIcon(icon);
            return this;
        }

        public MenuBuilder withItems(List<Menu> items) {
            menu.setItems(items);
            return this;
        }

        public Menu build() {
            return menu;
        }
    }
}
