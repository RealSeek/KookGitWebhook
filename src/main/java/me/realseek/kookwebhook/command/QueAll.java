package me.realseek.kookwebhook.command;

import me.realseek.kookwebhook.database.SQLiteJDBCDriverConnection;
import me.realseek.kookwebhook.message.Card;
import me.realseek.kookwebhook.util.Config;
import org.jetbrains.annotations.Nullable;
import snw.jkook.command.UserCommandExecutor;
import snw.jkook.entity.User;
import snw.jkook.message.Message;

public class QueAll implements UserCommandExecutor {
    @Override
    public void onCommand(User sender, Object[] arguments, @Nullable Message message) {
        // 遍历 Config 的管理员 ID
        for (String str : Config.getBotAdmin()){
            // 判断发送者ID和配置文件的管理员ID是否相等
            if (sender.getId().equals(str)){
                // 判断这个表是存在
                if (SQLiteJDBCDriverConnection.checkTableExists("repositories")){
                    for (String selectRepository : SQLiteJDBCDriverConnection.selectRepositories()) {

                    }
                    message.reply(Card.allRepoCard(SQLiteJDBCDriverConnection.selectRepositories()));
                }else {
                    message.reply("暂无任何数据");
                }
            }
        }
    }
}
