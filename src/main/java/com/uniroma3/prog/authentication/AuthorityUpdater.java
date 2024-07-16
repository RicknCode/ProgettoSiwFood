package com.uniroma3.prog.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthorityUpdater {

    public UserDetails updateUserAuthorities(UserDetails userDetails, String newRole) {
        Collection<GrantedAuthority> updatedAuthorities = new ArrayList<>(userDetails.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority(newRole));
        return new User(userDetails.getUsername(), "", updatedAuthorities);
    }

    public void updateSecurityContext(UserDetails updatedUserDetails) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(updatedUserDetails, updatedUserDetails.getPassword(), updatedUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}