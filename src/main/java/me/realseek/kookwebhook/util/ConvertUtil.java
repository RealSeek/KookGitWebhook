package me.realseek.kookwebhook.util;

public class ConvertUtil {
    public static String toDbCompatible(String repoName){
        return repoName.replace('/', '_');
    }

    public static String toRealRepo(String repoName){
        int index = repoName.indexOf('_'); // 找到第一个下划线的位置
        if (index == -1) {
            return repoName; // 如果没有找到下划线，返回原始字符串
        }
        StringBuilder sb = new StringBuilder(repoName);
        sb.setCharAt(index, '/'); // 将下划线替换为正斜杠
        return sb.toString();
    }
}
