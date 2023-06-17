package me.realseek.kookwebhook.bean;

import lombok.Data;

@Data
public class Sender {
    /**
     * 账号名
     */
    private String login;

    /**
     * 头像 url
     */
    private String avatar_url;

    /**
     * 账号 url
     */
    private String html_url;
}
