package me.realseek.kookwebhook.util;

import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class MessageUtil {
    public static String getFullMessage(User sender, Object[] arguments, Message message) {
        String str = "";
        if (arguments.length == 0) {
            return str;
        } else for (Object argument : arguments) {
            str += argument + " ";
        }
        return str.substring(0, str.length() - 1);
    }
}
