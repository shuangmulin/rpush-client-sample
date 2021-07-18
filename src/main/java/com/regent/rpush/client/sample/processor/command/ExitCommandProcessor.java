package com.regent.rpush.client.sample.processor.command;

import org.summer.container.annotation.Component;

import java.util.List;

/**
 * 命令：exit
 *
 * @author 钟宝林
 * @since 2021/7/17/017 15:28
 **/
@Component
public class ExitCommandProcessor implements CommandProcessor {
    @Override
    public void process(List<String> params) {
        System.exit(0);
    }

    @Override
    public String command() {
        return "exit";
    }

    @Override
    public String usage() {
        return "使用本命令退出程序";
    }
}
