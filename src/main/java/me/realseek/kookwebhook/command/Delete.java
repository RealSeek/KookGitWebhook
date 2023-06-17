package me.realseek.kookwebhook.command;

import com.google.common.collect.Multimap;
import me.realseek.kookwebhook.database.SQLiteJDBCDriverConnection;
import me.realseek.kookwebhook.message.Card;
import me.realseek.kookwebhook.util.Config;
import me.realseek.kookwebhook.util.ConvertUtil;
import me.realseek.kookwebhook.util.MessageUtil;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.entity.channel.TextChannel;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;

public class Delete implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        // 遍历 Config 的管理员 ID
        for (String str : Config.getBotAdmin()) {
            // 判断发送者ID和配置文件的管理员ID是否相等
            if (sender.getId().equals(str)) {
                // 获取绑定的 User/Repo
                boolean containsSlash = MessageUtil.getFullMessage(sender, arguments, message).contains("/");
                // 将"/"转成"_" 数据库不能存"/"
                String repoName = ConvertUtil.toDbCompatible(MessageUtil.getFullMessage(sender, arguments, message));

                // 若传入的结构正确
                if (containsSlash) {
                    // 判断是否有相对应的表
                    if (SQLiteJDBCDriverConnection.repositoryExists(repoName)) {
                        // 存在
                        // 拿消息类型转
                        if (message instanceof TextChannelMessage) {
                            TextChannel channel = ((TextChannelMessage) message).getChannel();
                            // 遍历表内频道 id
                            for (String selectChannel : SQLiteJDBCDriverConnection.selectChannels(repoName)) {
                                // 如果相同
                                if (selectChannel.equals(channel.getId())) {
                                    // 删除
                                    SQLiteJDBCDriverConnection.deleteChannel(repoName, channel.getId());
                                    SQLiteJDBCDriverConnection.deleteDetail(repoName, channel.getName());
                                    message.reply("删除成功");
                                }
                            }
                        }
                    }
                } else {
                    message.reply("请输入 User/Repo 结构");
                }
            }
        }
    }
}