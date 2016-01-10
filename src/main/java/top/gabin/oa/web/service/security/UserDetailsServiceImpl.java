/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.gabin.oa.web.service.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import top.gabin.oa.web.entity.Admin;
import top.gabin.oa.web.entity.Permission;
import top.gabin.oa.web.service.AdminService;
import top.gabin.oa.web.service.PermissionService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource(name = "adminService")
    private AdminService adminService;
    @Resource(name = "permissionService")
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        Admin adminUser = adminService.findByName(username);
        if (adminUser == null) {
            throw new UsernameNotFoundException("The user was not found");
        }
        List<Permission> permissionList;
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("permission_default"));
        if (adminUser.getId() == -1) {//全部的权限
            permissionList = permissionService.findAll();
        } else {
            permissionList = adminUser.getPermissionList();
        }
        for (Permission permission : permissionList) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }
        return new User(username, adminUser.getPassword(), true, true, true, true, authorities);
    }

}
