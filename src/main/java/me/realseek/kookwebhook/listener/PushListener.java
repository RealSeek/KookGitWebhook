package me.realseek.kookwebhook.listener;

import me.realseek.kookwebhook.Main;
import me.realseek.kookwebhook.database.SQLiteJDBCDriverConnection;
import me.realseek.kookwebhook.event.PushEvent;
import me.realseek.kookwebhook.message.Card;
import me.realseek.kookwebhook.util.ConvertUtil;
import me.realseek.kookwebhook.util.MessageUtil;
import snw.jkook.entity.channel.Channel;
import snw.jkook.entity.channel.TextChannel;
import snw.jkook.event.EventHandler;
import snw.jkook.event.Listener;

import java.util.List;

public class PushListener implements Listener {
    @EventHandler
    public void PushEvent(PushEvent event) {
        try {
            String repoName = ConvertUtil.toDbCompatible(event.getPush().getRepository().getRepository().getFull_name());
            if (SQLiteJDBCDriverConnection.repositoryExists(repoName)){
                // 若数据库内有 则遍历频道ID
                List<String> channelIds = SQLiteJDBCDriverConnection.selectChannels(repoName);
                for (String channelId : channelIds) {
                    Channel channel = Main.getInstance().getCore().getHttpAPI().getChannel(channelId);
                    if (channel instanceof TextChannel){
                        TextChannel textChannel = (TextChannel) channel;
                        textChannel.sendComponent(Card.gitPushCard(event.getPush()));
                    }
                }
            }
        }catch (NullPointerException e){
            // repoName 为 null 多半是 github ping 了一下
        }
    }
}
