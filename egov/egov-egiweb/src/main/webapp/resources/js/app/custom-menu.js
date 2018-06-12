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
$(document).ready(function () {
    var addfav_li;
    var menuheight = ($(window).height() - 63);
    var ulheight = (menuheight - 93);

    $('#menu').multilevelpushmenu({
        menuWidth: '250px',
        mode: 'cover',
        swipe: 'touchscreen',
        menu: menuItems,
        onItemClick: function (event) {
            event.preventDefault();
            event.stopPropagation();
            var e = arguments[0], $item = arguments[2];
            $('#menu_multilevelpushmenu ul li').removeClass('li-active');
            $(e.target).parent().addClass('li-active');
            if ($(e.target).prop('tagName').toLowerCase() == 'input') {
                $(e.target).focus();
                $(e.target).val('');
                $(e.target).unbind('');
                $(e.target).blur(function () {
                    $(e.target).val('');
                });
            }

            if ($(e.target).hasClass('remove-favourite')) { //Removing from Favourite Menu
                bootbox.confirm({
                    message: "Do you wish to remove this from Favourite ?",
                    buttons: {
                        confirm: {
                            label: 'Yes',
                            className: 'btn-success'
                        },
                        cancel: {
                            label: 'No',
                            className: 'btn-danger'
                        }
                    },
                    callback: function (result) {
                        if (result) {
                            actionId = $(e.target).parent().parent().attr('id').split("-")[1];
                            removeFromFavourites(actionId);
                            $(e.target).parent().parent().remove();
                            $("#id-" + actionId + ' a i').removeClass('added-as-fav');
                        }
                    }
                });
            } else if ($(e.target).hasClass('added-as-fav')) { //Removing from Main Menu while unstaring Favourite
                bootbox.confirm({
                    message: "Do you wish to remove this from Favourite ?",
                    buttons: {
                        confirm: {
                            label: 'Yes',
                            className: 'btn-success'
                        },
                        cancel: {
                            label: 'No',
                            className: 'btn-danger'
                        }
                    },
                    callback: function (result) {
                        if (result) {
                            actionId = $(e.target).parent().parent().attr('id').split("-")[1];
                            removeFromFavourites(actionId);
                            $(e.target).removeClass('added-as-fav');
                            $('#fav-' + actionId).remove();
                        }
                    }
                });
            } else if ($(e.target).hasClass('add-to-favourites')) { // Adding from Main Menu by starring
                $('.favourites').modal('show', {backdrop: 'static'});
                favouriteName = $(e.target).parent().text();
                favouriteURL = $item.find('a:first').attr('href').toString();
                contextRoot = favouriteURL.split("/")[1];
                actionId = $item.attr('id').split("-")[1];
                $('#fav-name').val(favouriteName);
            } else {
                var itemHref = $item.find('a:first').attr('href');
                var width = 900;
                var height = 700;
                var left = parseInt((screen.availWidth / 2) - (width / 2));
                var top = parseInt((screen.availHeight / 2) - (height / 2));
                var windowFeatures = "width=" + width + ",height=" + height + ",status,resizable,left=" + left + ",top=" + top + ",scrollbars=yes";

                var windowObjectReference = window.open(itemHref, '' + $item.attr('id') + '', windowFeatures);
                openedWindows.push(windowObjectReference);
                windowObjectReference.focus();
            }
        },
        onGroupItemClick: function () {
            $('.search').hide();
            menuheight = ($(window).height() - 63);
            ulheight = (menuheight - 115);
            $('#menu').height('' + menuheight + 'px');
            $('#menu_multilevelpushmenu').height('' + menuheight + 'px');
            $('#menu, #menu_multilevelpushmenu').css('min-height', '' + menuheight + 'px');
            var e = arguments[2];
            if (e.children('div').children('ul').height() > ulheight)
                $('#menu_multilevelpushmenu ul').height('' + ulheight + 'px');
            $('#menu_multilevelpushmenu ul').css('overflow-y', 'auto');
        },
        onBackItemClick: function () {
            $('#menu_multilevelpushmenu ul').css('overflow-y', 'auto');
            var event = arguments[0],
                $menuLevelHolder = arguments[1],
                options = arguments[2],
                title = $menuLevelHolder.find('h2:first').text();
            if (title == 'Applications' || title == 'Favourites') {
                $('.search').show();
            }
        },
        onCollapseMenuEnd: function () {
            var w = $('#menu.homepage').width() + 'px';
            $('.inline-main-content').css('width', 'calc(100% - ' + w + ')');
        },
        onExpandMenuEnd: function () {
            var w = $('#menu.homepage').width() + 'px';
            $('.inline-main-content').css('width', 'calc(100% - ' + w + ')');
        },
        onTitleItemClick: function () {
            $('.search').show();
        }
    });

    var actionId = '';
    var favouriteName = '';
    var favouriteURL = '';
    var contextRoot = '';

    $(window).on('resize', function () {
        setmenuheight();
    }).trigger('resize');

    function setmenuheight() {
        menuheight = ($(window).height() - 63);
        $('#menu').height('' + menuheight + 'px');
        $('#menu_multilevelpushmenu').height('' + menuheight + 'px');
        $('#menu, #menu_multilevelpushmenu').css('min-height', '' + menuheight + 'px');
    }

    function removeFromFavourites(actionId) {
        $.ajax({
            type: "GET",
            url: "home/favourite/remove",
            data: {'actionId': actionId}
        }).done(function (value) {
            if (value === true) {
                $('#id-' + actionId + ' a i').addClass('add-to-favourites');
            } else {
                bootbox.alert("Could not delete it from Favourite");
            }
        });
    }

    $('.add-fav').click(function () {
        $('.favourites').modal('hide');
        var fd = new FormData();
        fd.append('actionId', actionId);
        fd.append('name', $("#fav-name").val());
        fd.append('contextRoot', contextRoot);
        $.ajax({
            type: "POST",
            url: "home/favourite/add",
            data: fd,
            processData: false,
            contentType: false,
        }).done(function (value) {
            if (value) {
                $('#id-' + actionId + ' a i').addClass('added-as-fav');
                $('#favMenu ul').append('<li id="fav-' + actionId + '"> <a href="' + favouriteURL + '" class="open-popup"><i class="floatRight fa fa-times-circle remove-favourite"></i>' + $("#fav-name").val() + '</a> </li>')
            } else {
                bootbox.alert("Could not add to Favourite");
            }
            $("#fav-name").val("");
        });

    });

    //Removing freshly added favourite from Favourite menu
    $(document).on('click', '.remove-favourite', function (e) {
        e.stopPropagation();
        e.preventDefault();
        bootbox.confirm({
            message: "Do you wish to remove this from Favourite ?",
            buttons: {
                confirm: {
                    label: 'Yes',
                    className: 'btn-success'
                },
                cancel: {
                    label: 'No',
                    className: 'btn-danger'
                }
            },
            callback: function (result) {
                if (result) {
                    var actionId = $(e.target).parent().parent().attr('id').split("-")[1];
                    $.ajax({
                        type: "GET",
                        url: "home/favourite/remove",
                        data: {'actionId': actionId}
                    }).done(function (value) {
                        if (value === true) {
                            $(e.target).parent().parent().remove();
                            $('#id-' + actionId + ' a i').addClass('add-to-favourites');
                            $("#id-" + actionId + ' a i').removeClass('added-as-fav');
                        } else {
                            bootbox.alert("Could not delete it from Favourite");
                        }
                    });

                }
            }
        });
    });

    $("a.open-popup").click(function (e) {
        // to open in good size for user
        //var width = window.innerWidth /0.66 ;
        // define the height in
        //var height = width * window.innerWidth / window.innerHeight;
        // Ratio the hight to the width as the user screen ratio
        var windowObjectReference = window.open(this.href, '' + $(this).attr('data-strwindname') + '', 'width=900, height=700, top=300, left=260,scrollbars=yes');
        openedWindows.push(windowObjectReference);
        windowObjectReference.focus();
        return false;
    });

    $(document).on('click', 'a.open-popup', function () {
        var windowObjectReference = window.open(this.href, '' + $(this).attr('data-strwindname') + '', 'width=900, height=700, top=300, left=260,scrollbars=yes');
        openedWindows.push(windowObjectReference);
        windowObjectReference.focus();
        return false;
    });

});	
