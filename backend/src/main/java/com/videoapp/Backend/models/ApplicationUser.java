package com.videoapp.Backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer userID;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private Boolean isEnabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role_junction",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> authorities;

    @Column(name = "avatar", columnDefinition = "longblob")
    private byte[] avatar;

    public ApplicationUser(){
        super();
        this.authorities = new HashSet<Role>();
    }

    public ApplicationUser(Integer userID, String username, String password, Set<Role> authorities, byte[] avatar) {
        super();
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.avatar = avatar;
    }

    public Integer getUserID() {
        return  this.userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void  setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public  void  setUsername(String username){
        this.username = username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public  void  setIsEnabled(boolean value) {
        this.isEnabled = value;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
