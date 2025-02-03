package com.example.myapplication.model;

public class UserAccount {
    private String id;
    private String name;
    private String email;
    private String question1;
    private String question2;
    private String question3;
    private String question4;
    private String pdf_uri;
    private String video_uri;
    private String currentDate;
    private String currentTime;

    public UserAccount() {
    }


    public UserAccount(String id, String name, String email, String question1, String question2, String question3, String question4, String pdf_uri, String currentDate, String currentTime) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
        this.question4 = question4;
        this.pdf_uri = pdf_uri;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }

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


    public String getPdfUri() {
        return pdf_uri;
    }

    public void setPdfUri(String pdf_uri) {
        this.pdf_uri = pdf_uri;
    }

    public String getVideoUri() {
        return video_uri;
    }

    public void setVideoUri(String video_uri) {
        this.pdf_uri = video_uri;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }


}
