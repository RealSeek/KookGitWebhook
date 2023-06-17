<img src="https://capsule-render.vercel.app/api?type=transparent&fontColor=703ee5&text=KookGitWebhook&height=150&fontSize=60&desc=随便写的Webhook推送插件&descAlignY=75&descAlign=60&animation=fadeIn" />

<h1 align="center">
  KookGitWebhook
</h1>

<p align='center'>
    <a  href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8+-brightgreen.svg"  alt=""/>
	</a>
    <a  href="https://github.com/SNWCreations/JKook">
		<img src="https://img.shields.io/badge/JKook%20API-0.49.0-brightgreen"  alt=""/>
	</a>
    <a href="https://github.com/SNWCreations/KookBC">
        <img src="https://img.shields.io/badge/KookBC-0.27.0 releases-brightgreen" alt=""/>
    </a>
    <a href="https://opensource.org/licenses/MIT">
        <img src="https://img.shields.io/badge/license-MIT-brightgreen.svg" alt=""/>
    </a>
    <a href="https://www.codefactor.io/repository/github/realseek/rubbishkooksong"><img src="https://www.codefactor.io/repository/github/realseek/rubbishkooksong/badge" alt="CodeFactor" />
    </a>
</p>

## 使用说明

本插件会在本地创建一个http服务器,端口可以在配置文件内修改,插件内所有功能只有插件管理员可以使用

## 配置文件

```bash
# HTTP 服务器端口
port: 25565

# url 路径配置
url_parameter: ""

# 插件管理员ID
BotAdmin: ["2038278961", "管理员ID"]
```

## 指令

`/git bind User/repo` 作用:绑定一个 repo 到频道内,触发 webhook 后会在绑定的频道推送。例如: /git bind RealSeek/KookGitWebhook

![img.png](img/bind.png)

`/git query User/repo` 作用:查询一个 repo 绑定了多少个服务器频道 例如: /git query RealSeek/KookGitWebhook

![img.png](img/query.png)

`/git queall` 作用:查询 Bot 绑定了多少个 repo 例如: /git queall

![img.png](img/queall.png)

`/git del User/repo` 作用:删除一个 repo 的推送 例如: /git del RealSeek/KookGitWebhook

![img.png](img/del.png)

## 推送样式

![img.png](img/push.png)

