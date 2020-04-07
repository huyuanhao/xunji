package com.jimetec.xunji.rx.event;

/**
 * 作者:capTain
 * 时间:2019-06-19 16:40
 * 描述:
 */
public class LoginEvent {
    String  phone;

    public LoginEvent() {
    }

    public LoginEvent(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
