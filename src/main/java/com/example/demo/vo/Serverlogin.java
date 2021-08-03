package com.example.demo.vo;

public class Serverlogin {

    private String address;

    private String session;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "Serverlogin{" +
                "address='" + address + '\'' +
                ", session='" + session + '\'' +
                '}';
    }
}
