/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.bpms.service;

import org.egov.bpms.entity.Group;
import org.egov.bpms.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public Group getGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    public List<Group> getGroupsByNameLike(String name) {
        return groupRepository.findByNameContains(name);
    }

    @Transactional
    public Group create(final Group group) {
        return groupRepository.save(group);
    }

    @Transactional
    public Group update(final Group group) {
        return groupRepository.save(group);
    }

    public List<Group> findAll() {
        return groupRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Group findByName(String name) {
        return groupRepository.findByName(name);
    }

    public Group findOne(Long id) {
        return groupRepository.findOne(id);
    }

    public List<Group> search() {
        return groupRepository.findAll();
    }

    public List<Group> findGroupsByUser(String userName) {
        /*User userById = userService.getUserByUsername(userName);
        List<User> users = new ArrayList();
    	users.add(userById);*/
        return groupRepository.findGroupByUserName(userName);
    }


}
