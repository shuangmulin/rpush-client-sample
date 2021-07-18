package com.regent.rpush.client.sample.processor.command;

import java.util.List;

/**
 * 命令处理器
 *
 * @author 钟宝林
 * @since 2021/7/13/013 11:48
 **/
public interface CommandProcessor {

    /**
     * 处理命令
     *
     * @param params 参数列表
     */
    void process(List<String> params);

    /**
     * 指定能处理的命令
     */
    String command();

    /**
     * 使用说明，可以不实现，默认返回{@link CommandProcessor#command()}
     */
    default String usage() {
        return command();
    }

}
