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
import snw.jkook.message.Message;

public class Query implements UserCommandExecutor {
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
                        Multimap<String, String> serverInfo = SQLiteJDBCDriverConnection.selectDetails(repoName);
                        message.reply(Card.bindCard(serverInfo, ConvertUtil.toRealRepo(repoName)));
                    }
                } else {
                    message.reply("请输入 User/Repo 结构");
                }
            }
        }
    }
}