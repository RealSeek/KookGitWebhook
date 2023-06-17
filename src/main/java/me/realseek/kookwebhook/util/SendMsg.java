package me.realseek.kookwebhook.util;

import me.realseek.kookwebhook.Main;
import me.realseek.kookwebhook.message.Card;
import snw.jkook.entity.channel.Channel;
import snw.jkook.entity.channel.TextChannel;

public class SendMsg {
    public static void send(String channelId, String msg){
        Channel channel = Main.getInstance().getCore().getHttpAPI().getChannel(channelId);
        if (channel instanceof TextChannel){
            TextChannel textChannel = (TextChannel) channel;
            textChannel.sendComponent(msg);
        }
    }
}
