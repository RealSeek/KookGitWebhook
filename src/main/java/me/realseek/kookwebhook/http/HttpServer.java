package me.realseek.kookwebhook.http;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.realseek.kookwebhook.Main;
import me.realseek.kookwebhook.event.PushEvent;

import static me.realseek.kookwebhook.util.Config.getPort;
import static me.realseek.kookwebhook.util.Config.getUrlParameter;

public class HttpServer {
    int port = getPort();
    public HttpServer() {
        HttpUtil.createServer(port)
                .addAction(getUrlParameter(), (request, response) -> {
                    response.setContentType("text/plain ,charset=UTF-8");
                    response.sendOk();
                    JsonObject jsonObj = JsonParser.parseString(request.getBody()).getAsJsonObject();
                    // System.out.println(jsonObj);
                    Main.getInstance().getCore().getEventManager().callEvent(new PushEvent(jsonObj));
                })
                .start();
    }
}
