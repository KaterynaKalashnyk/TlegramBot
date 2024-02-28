package com.io.deutsch_steuerrechner.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity(name = "usersData")
public class User {
    @Id
    private Long cahtId;

    private String UserName;

    public void setCahtId(Long cahtId) {
        this.cahtId = cahtId;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }


    public Long getCahtId() {
        return cahtId;
    }

    public String getUserName() {
        return UserName;
    }
}
