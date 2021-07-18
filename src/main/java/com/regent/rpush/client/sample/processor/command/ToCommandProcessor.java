package com.regent.rpush.client.sample.processor.command;

import com.regent.rpush.client.RpushClient;
import com.regent.rpush.client.sample.processor.msg.MsgPrinter;
import com.regent.rpush.client.sample.utils.ScannerUtils;
import com.regent.rpush.dto.message.RpushMessageDTO;
import com.regent.rpush.sdk.RpushMessage;
import com.regent.rpush.sdk.RpushService;
import org.apache.commons.lang3.StringUtils;
import org.summer.container.annotation.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 命令：to
 *
 * @author 钟宝林
 * @since 2021/7/13/013 13:28
 **/
@Component
public class ToCommandProcessor implements CommandProcessor {

    @Resource
    private RpushService rpushService;
    @Resource
    private RpushClient rpushClient;
    @Resource
    private MsgPrinter msgPrinter;

    @Override
    public void process(List<String> params) {
        if (params == null || params.size() <= 0) {
            System.out.println("错误：请指定消息接收人...");
            System.out.println("eg: to user1 user2 user3");
            return;
        }
        msgPrinter.stop();
        String content = null;
        while (StringUtils.isBlank(content)) {
            content = ScannerUtils.nextLine("请输入消息内容：");
        }
        RpushMessageDTO msg = RpushMessage.RPUSH_SERVER().fromTo(rpushClient.getRegistrationId()).receiverIds(params).content(content).build();
        rpushService.sendMessage(msg);
        msgPrinter.start();
    }

    @Override
    public String command() {
        return "to";
    }

    @Override
    public String usage() {
        return "使用to命令，指定要发送消息的对象，多个则用空格隔开。\n" +
                "比如：to user1 user2 user3";
    }
}
