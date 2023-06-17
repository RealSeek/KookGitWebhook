package me.realseek.kookwebhook.command;

import me.realseek.kookwebhook.database.SQLiteJDBCDriverConnection;
import me.realseek.kookwebhook.util.Config;
import me.realseek.kookwebhook.util.ConvertUtil;
import me.realseek.kookwebhook.util.MessageUtil;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.entity.channel.TextChannel;
import snw.jkook.message.Message;
import snw.jkook.message.TextChannelMessage;

import javax.annotation.Nullable;


public class Bind implements UserCommandExecutor {
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
                    // 检查是否存在对应的表
                    if (!SQLiteJDBCDriverConnection.repositoryExists(repoName)) {
                        // 若没有这个表
                        // 创建对应仓库的数据库表
                        SQLiteJDBCDriverConnection.insertRepository(repoName);
                        if (message instanceof TextChannelMessage) {
                            TextChannel channel = ((TextChannelMessage) message).getChannel();
                            // 添加频道ID到对应仓库
                            SQLiteJDBCDriverConnection.insertChannel(repoName, channel.getId());
                            // 添加服务器名和频道名到表内用于输出
                            SQLiteJDBCDriverConnection.insertDetail(repoName, channel.getGuild().getName(), channel.getName());
                        }
                    } else {
                        // 若有这个表 则直接添加
                        // 方法内有判断是否重复 这边不做判断
                        if (message instanceof TextChannelMessage) {
                            TextChannel channel = ((TextChannelMessage) message).getChannel();
                            SQLiteJDBCDriverConnection.insertChannel(repoName, channel.getId());
                            SQLiteJDBCDriverConnection.insertDetail(repoName, channel.getGuild().getName(), channel.getName());
                        }
                    }
                } else {
                    message.reply("请绑定 User/Repo 结构");
                }
            }
        }
    }
}
