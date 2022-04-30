package com.wind.community.dao;

public class UserDao {
    private String login;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return login;
    }

    public void setLoginName(String loginName) {
        this.login = loginName;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", id=" + id +
                '}';
    }
}
