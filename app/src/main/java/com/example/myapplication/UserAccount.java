package com.example.myapplication;
import java.util.Date;

import com.google.firebase.firestore.ServerTimestamp;

public class UserAccount {
    private String id;
    private String name;
    private String email;
    private String question1;
    private String question2;
    private String question3;
    private String question4;
    private String pdf_url;

    @ServerTimestamp
    private Date timestamp;

    public UserAccount() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    public String getQuestion4() {
        return question4;
    }


    public String getPdfUrl() {
        return pdf_url;
    }

    public void setPdfUrl(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


}
