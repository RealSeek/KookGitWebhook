package me.realseek.kookwebhook.message;

import com.google.common.collect.Multimap;
import me.realseek.kookwebhook.Main;
import me.realseek.kookwebhook.bean.Commit;
import me.realseek.kookwebhook.bean.Push;
import me.realseek.kookwebhook.database.SQLiteJDBCDriverConnection;
import me.realseek.kookwebhook.util.ConvertUtil;
import snw.jkook.HttpAPI;
import snw.jkook.entity.abilities.Accessory;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.element.MarkdownElement;
import snw.jkook.message.component.card.element.PlainTextElement;
import snw.jkook.message.component.card.module.ContextModule;
import snw.jkook.message.component.card.module.SectionModule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Card {
    static HttpAPI httpAPI = Main.getInstance().getCore().getHttpAPI();

    // 构建卡片消息
    public static MultipleCardComponent gitPushCard(Push push){
        CardBuilder cardBuilder = new CardBuilder();

        cardBuilder.setTheme(Theme.INFO).setSize(Size.LG);

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**[" + push.getRepository().getRepository().getFull_name() + "](" + push.getRepository().getRepository().getUrl() + ")** 有新的推送\n---"), null, Accessory.Mode.RIGHT
        ));

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**发布者 : [" + push.getSender().getLogin() + "](" + push.getSender().getHtml_url() + ")\n分支 : " + push.getRepository().getRef().substring(push.getRepository().getRef().lastIndexOf("/") + 1)+ "**"),
                new ImageElement(httpAPI.uploadFile("pic", push.getSender().getAvatar_url()), null , false),
                Accessory.Mode.LEFT
        ));

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---"), null, Accessory.Mode.RIGHT
        ));

        for (Commit.Commits commits : push.getCommit().getCommits()) {
            cardBuilder.addModule(new SectionModule(
                    new MarkdownElement("**" + commits.getAuthor().getName() +"** : [" + commits.getId().substring(0, 7) + "](" + commits.getUrl() + ") " + commits.getMessage()), null, Accessory.Mode.RIGHT
            ));
        }

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---"), null, Accessory.Mode.RIGHT
        ));

        cardBuilder.addModule(new ContextModule(
                Arrays.asList(
                        new PlainTextElement(push.getRepository().getRepository().getDescription())
                )
        ));

        return cardBuilder.build();
    }

    // 展示绑定的频道数量
    public static MultipleCardComponent bindCard(Multimap<String, String> map, String repoName){
        CardBuilder cardBuilder = new CardBuilder();

        cardBuilder.setTheme(Theme.INFO).setSize(Size.LG);

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**" + repoName + " 推送列表**\n---"), null, Accessory.Mode.RIGHT
        ));

        for (Map.Entry<String, String> entry : map.entries()) {
            cardBuilder.addModule(new SectionModule(
                    new MarkdownElement("**" + entry.getKey() + "** : " + entry.getValue()), null, Accessory.Mode.RIGHT
            ));
        }

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---"), null, Accessory.Mode.RIGHT
        ));

        return cardBuilder.build();
    }

    // 展示全部的仓库
    public static MultipleCardComponent allRepoCard(List<String> list){
        CardBuilder cardBuilder = new CardBuilder();

        cardBuilder.setTheme(Theme.INFO).setSize(Size.LG);

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("**仓库列表**\n---"), null, Accessory.Mode.RIGHT
        ));

        for (String selectRepository : list) {
            cardBuilder.addModule(new SectionModule(
                    new MarkdownElement("**" + ConvertUtil.toRealRepo(selectRepository) + "**"), null, Accessory.Mode.RIGHT
            ));
        }

        cardBuilder.addModule(new SectionModule(
                new MarkdownElement("---"), null, Accessory.Mode.RIGHT
        ));

        return cardBuilder.build();
    }
}
