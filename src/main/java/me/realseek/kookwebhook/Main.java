package me.realseek.kookwebhook;

import me.realseek.kookwebhook.command.Bind;
import me.realseek.kookwebhook.command.Delete;
import me.realseek.kookwebhook.command.QueAll;
import me.realseek.kookwebhook.command.Query;
import me.realseek.kookwebhook.database.SQLiteJDBCDriverConnection;
import me.realseek.kookwebhook.http.HttpServer;
import me.realseek.kookwebhook.listener.PushListener;
import snw.jkook.command.JKookCommand;
import snw.jkook.plugin.BasePlugin;

public class Main extends BasePlugin {
    private static Main instance;
    @Override
    public void onLoad() {
        saveDefaultConfig();
        instance = this;
    }

    @Override
    public void onEnable() {
        // 创建 HTTP 服务器
        HttpServer httpServer = new HttpServer();

        // 注册指令
        registerCommands();

        // 初始化数据库
        SQLiteJDBCDriverConnection.createNewTables();

        // 监听器
        getCore().getEventManager().registerHandlers(this, new PushListener());
    }

    @Override
    public void onDisable() {
        getLogger().info("GitWebhook 已卸载");
        super.onDisable();
    }

    private void registerCommands(){
        // 绑定
        new JKookCommand("git")
                .addSubcommand(
                        new JKookCommand("bind")
                                .executesUser(new Bind())
                )
                .addSubcommand(
                        new JKookCommand("query")
                                .executesUser(new Query())
                )
                .addSubcommand(
                        new JKookCommand("queall")
                                .executesUser(new QueAll())
                )
                .addSubcommand(
                        new JKookCommand("del")
                                .executesUser(new Delete())
                )
                .register(this);
    }

    public static Main getInstance() {
        return instance;
    }
}