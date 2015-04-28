/*
#   eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# */
/*
 Based on code from
 http://www.melrosesolutions.com/blog/index.cfm/2010/7/16/Turn-SELECTs-into-Comboboxes-with-jQuery-UI-Autocomplete
 */

(function($) {

    $.widget("ui.combobox", {

        _create: function() {
            var select = this.element;

            if (select.is(":disabled") 
                    || select.hasClass("inputOverlayCreated")) {
                return;
            }

            select.hide();

            // set up input text element
            var input = $("<input type='text'>");
            input.insertAfter(select);

            // remember that combobox is creates (to avoid odd duplicates)
            select.addClass("inputOverlayCreated");

            // the class ui-combobox-content is required for proper
            // highlighting of data changes
            input.addClass("ui-combobox-content ui-widget ui-corner-left");

            //clear text when user clicks in text input
            input.click(function() {
                $(this).val("");
            });

            input.attr("menustatus", "closed");

            // over-ride form submit, so it can't submit
            // if the menu is open
            var form = $(input).parents('form:first');
            $(form).submit(function(e) {
                return (input.attr('menustatus') == 'closed');
            });

            // set up button for fake 'select'
            var btn = $("<button>&nbsp;</button>");
            btn.attr("tabIndex", -1);
            btn.attr("title", "Show All Items");
            btn.insertAfter(input);
            btn.button({
                icons: {
                    primary: "ui-icon-triangle-1-s"
                },
                text: false
            });
            btn.removeClass("ui-corner-all");
            btn.addClass("ui-corner-right ui-button-icon");
            btn.click(function() {
                //event.preventDefault();
                // close if already visible
                if (input.autocomplete("widget").is(":visible")) {
                    input.autocomplete("close");
                    return false; // return false, so form isn't automatically submitted
                }
                // pass empty string as value to search for, displaying all results
                input.autocomplete("search", "");
                input.focus();
                return false; // return false, so form isn't automatically submitted
            });

            // add some styles
            btn.css("margin-left", "-1px");
            btn.css("padding", 0);
            $('span.ui-button-text', btn).css("padding", 0);

            input.css("margin", 0);
            input.css("padding", "0 0.4em 0 0.4em");
            input.css("width", select.outerWidth() - btn.outerWidth(true) - 10);// match the width
        },

        _init : function() {
            var select = this.element;

            if (select.is(":disabled")) {
                // we don't apply any fancy combobox behaviour at all
                // if the underlying drop-down list is disabled
                return;
            }

            var opts = new Array();
            $('option', select).each(function(index) {
                var opt = new Object();
                opt.val = $(this).val();
                opt.label = $(this).text();
                opts[opts.length] = opt;
            });

            var input = select.next();

            // initialise text with what's currently selected
            input.val($(':selected', select).text());

            input.autocomplete({
                source: opts,
                delay: 0,
                change: function(event, ui) {
                    if (!ui.item) {
                        // user didn't select an option, but what they typed may still match
                        var enteredString = $(this).val();
                        var stringMatch = false;
                        for (var i = 0; i < opts.length; i++) {
                            if (opts[i].label.toLowerCase() == enteredString.toLowerCase()) {
                                select.val(opts[i].val);// update (hidden) select
                                $(this).val(opts[i].label);// corrects any incorrect case
                                select.trigger("change");
                                stringMatch = true;
                                break;
                            }
                        }
                        if (!stringMatch) {
                            // remove invalid value, as it didn't match anything
                            $(this).val($(':selected', select).text());
                        }
                        return true;
                    }
                },
                select: function(event, ui) {
                    select.val(ui.item.val);// update (hidden) select
                    select.trigger("change");
                    return true;
                },
                // stop parent form from being while menu is open
                open: function(event, ui) {
                    input.attr("menustatus", "open");
                },
                close: function(event, ui) {
                    input.attr("menustatus", "closed");
                },
                minLength: 0
            });
        }
    });

})(jQuery);
