package me.realseek.kookwebhook.bean;

import lombok.Data;

@Data
public class Repository {
    private RepositoryInfo repository;

    /**
     * 分支名称
     */
    private String ref;

    @Data
    public static class RepositoryInfo {
        /**
         * 仓库名称
         */
        private String name;

        /**
         * 完整仓库名
         */
        private String full_name;

        /**
         * 是否为私有
         */
        private boolean privately_owned;

        /**
         * 项目 url
         */
        private String url;

        /**
         * 仓库介绍
         */
        private String description;
    }
}

