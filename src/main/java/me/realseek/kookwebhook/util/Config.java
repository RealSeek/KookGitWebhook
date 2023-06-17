package me.realseek.kookwebhook.util;

import me.realseek.kookwebhook.Main;
import snw.jkook.plugin.BasePlugin;

import java.util.List;

public class Config {
    static BasePlugin plugin = Main.getInstance();

    /**
     * 从配置文件获取端口号
     * @return
     */
    public static int getPort(){
        return plugin.getConfig().getInt("port", 12138);
    }

    public static String getUrlParameter(){
        return plugin.getConfig().getString("url_parameter", "");
    }

    public static List<String> getBotAdmin(){
        return plugin.getConfig().getStringList("BotAdmin");
    }
}
