package me.realseek.kookwebhook.event;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.realseek.kookwebhook.Main;
import me.realseek.kookwebhook.bean.Commit;
import me.realseek.kookwebhook.bean.Push;
import me.realseek.kookwebhook.bean.Repository;
import me.realseek.kookwebhook.bean.Sender;
import snw.jkook.event.Event;

public class PushEvent extends Event {
    private Push push;

    public PushEvent(JsonObject jsonObject){
        if (jsonObject.has("hook")){
            // 如果有 hook
            String repoName = jsonObject.getAsJsonObject("repository").get("full_name").getAsString();
            Main.getInstance().getLogger().info("仓库: " + repoName + " 已成功通过 Github Webhook ping");
        }else {
            System.out.println("进入解析");
            // Repos 部分
            Gson gson = new Gson();
            Repository repos = gson.fromJson(jsonObject, Repository.class);
            Sender sender = gson.fromJson(jsonObject.get("sender"), Sender.class);
            // 把 repos 的公私有取出
            repos.getRepository().setPrivately_owned(jsonObject.getAsJsonObject("repository").get("private").getAsBoolean());
            Commit commit = gson.fromJson(jsonObject, Commit.class);
            push = new Push(repos, sender, commit);
            System.out.println("分支: " + repos.getRef().substring(repos.getRef().lastIndexOf("/") + 1));
            System.out.println("仓库名: " + repos.getRepository().getName());
            System.out.println("完整名: " + repos.getRepository().getFull_name());
            System.out.println("是否为私有: " + repos.getRepository().isPrivately_owned());
            System.out.println("项目url: " + repos.getRepository().getUrl());
            System.out.println("项目描述: " + repos.getRepository().getDescription());
            System.out.println("Push 的用户:" + sender.getLogin());
            System.out.println("头像 url : " + sender.getAvatar_url());
            for (Commit.Commits commits : commit.getCommits()) {
                System.out.println("md5-hash: " + commits.getId().substring(0, 7));
                System.out.println("消息: " + commits.getMessage());
                System.out.println("更改 url: " + commits.getUrl());
                System.out.println("提交 commit 的用户: " + commits.getAuthor().getName());
            }
        }
    }

    public Push getPush() {
        return push;
    }
}
