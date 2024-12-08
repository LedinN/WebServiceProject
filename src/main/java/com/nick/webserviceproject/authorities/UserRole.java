package com.nick.webserviceproject.authorities;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nick.webserviceproject.authorities.UserPermission.*;

public enum UserRole {

    USER(GET, POST),
    ADMIN(GET,POST,PUT,DELETE);

    private final List<String> permission;

    UserRole(UserPermission... permissionList) {
        this.permission = Arrays.stream(permissionList)
                .map(UserPermission::getPermission)
                .toList();
    }

    public List<String> getListOfPermissions() {
        return permission;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();

        simpleGrantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        //simpleGrantedAuthorityList.addAll(getListOfPermissions().stream().map(SimpleGrantedAuthority::new).toList());

        return simpleGrantedAuthorityList;
    }
}
