package me.realseek.kookwebhook.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Push {
    private Repository repository;
    private Sender sender;
    private Commit commit;
}
