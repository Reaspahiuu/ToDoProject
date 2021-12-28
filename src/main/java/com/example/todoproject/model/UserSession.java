package com.example.todoproject.model;

import javax.persistence.*;

@Entity
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String sessionHashCode;
    private String ipAddress;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSessionHashCode() {
        return sessionHashCode;
    }

    public void setSessionHashCode(String sessionHashCode) {
        this.sessionHashCode = sessionHashCode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }
}
