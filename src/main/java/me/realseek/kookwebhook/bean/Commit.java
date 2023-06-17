package me.realseek.kookwebhook.bean;

import lombok.Data;

import java.util.List;

@Data
public class Commit {
    private List<Commits> commits;
    @Data
    public static class Commits {
        private Author author;
        /**
         * id
         */
        private String id;

        /**
         * Commits 消息
         */
        private String message;

        /**
         * 更改的 url
         */
        private String url;

        @Data
        public static class Author{
            /**
             * commit 的用户名
             */
            private String name;
        }
    }
}

