package com.nick.webserviceproject.authorities;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum UserRole {

    USER,
    ADMIN;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }


//    public List<SimpleGrantedAuthority> getAuthorities() {
//
//        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
//
//        simpleGrantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
//        simpleGrantedAuthorityList.addAll(getListOfPermissions().stream().map(SimpleGrantedAuthority::new).toList());
//
//        return simpleGrantedAuthorityList;
//    }
}
