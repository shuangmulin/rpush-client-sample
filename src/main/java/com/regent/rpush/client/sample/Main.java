package com.regent.rpush.client.sample;

import com.beust.jcommander.JCommander;
import com.regent.rpush.client.MsgProcessor;
import com.regent.rpush.client.RpushClient;
import com.regent.rpush.client.processor.PingIgnoreMsgProcessor;
import com.regent.rpush.client.sample.processor.command.CommandProcessor;
import com.regent.rpush.client.sample.utils.ScannerUtils;
import com.regent.rpush.sdk.RpushService;
import org.apache.commons.lang3.StringUtils;
import org.summer.container.BeanContainer;
import org.summer.container.ContainerStarter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 入口
 *
 * @author 钟宝林
 * @since 2021/7/6/006 19:24
 **/
public class Main {

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(config)
                .build();
        jCommander.parse(args);

        String servicePath = config.getServicePath();
        String clientId = config.getClientId();
        String clientSecret = config.getClientSecret();
        String loginName = config.getLoginName();
        String password = config.getPassword();
        if (StringUtils.isBlank(servicePath)
                || StringUtils.isBlank(clientId)
                || StringUtils.isBlank(clientSecret)
                || StringUtils.isBlank(loginName)
                || StringUtils.isBlank(password)) {
            jCommander.usage();
            return;
        }

        // 初始化Bean容器
        ContainerStarter containerStarter = new ContainerStarter();
        BeanContainer beanContainer = BeanContainer.getInstance();

        // 把消息sdk放容器里
        RpushService rpushService = RpushService.instance(servicePath, clientId, clientSecret, loginName, password);
        beanContainer.registerSingleton("rpushService", rpushService);

        // 与服务端建立socket连接
        RpushClient rpushClient = new RpushClient(servicePath, rpushService.getCurrentRegistration().getId());
        rpushClient.addMsgProcessor(new PingIgnoreMsgProcessor()); // 忽略心跳消息
        rpushClient.start();
        beanContainer.registerSingleton("rpushClient", rpushClient); // 把客户端放容器里

        containerStarter.start("com.regent.rpush.client.sample"); // 启动容器

        // 初始化所有的消息处理器
        List<MsgProcessor> msgProcessors = beanContainer.getByType(MsgProcessor.class);
        if (msgProcessors != null && msgProcessors.size() > 0) {
            for (MsgProcessor msgProcessor : msgProcessors) {
                rpushClient.addMsgProcessor(msgProcessor);
            }
        }

        List<CommandProcessor> commandProcessors = beanContainer.getByType(CommandProcessor.class);
        printUsage(commandProcessors);

        while (true) {
            try {
                String nextLine = ScannerUtils.nextLine();
                // 约定：输入的命令以空格分隔，第一个为具体命令，剩余的都认为是参数
                String[] commandSplit = nextLine.split(" ");
                String command = commandSplit[0];

                boolean hasProcessor = false;
                for (CommandProcessor commandProcessor : commandProcessors) {
                    if (!Objects.equals(commandProcessor.command(), command)) {
                        continue;
                    }
                    commandProcessor.process(getParams(commandSplit));
                    hasProcessor = true;
                }

                if (!hasProcessor) {
                    printUsage(commandProcessors);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打印使用说明
     *
     * @param commandProcessors 命令处理器
     */
    private static void printUsage(List<CommandProcessor> commandProcessors) {
        for (int i = 0; i < commandProcessors.size(); i++) {
            CommandProcessor commandProcessor = commandProcessors.get(i);
            System.out.println("----------------------------------------------------------------------------");
            System.out.println(commandProcessor.command() + ":");
            System.out.println(commandProcessor.usage());
            if (i == commandProcessors.size() - 1) {
                System.out.println("----------------------------------------------------------------------------");
            }
        }
    }

    private static List<String> getParams(String[] commandSplit) {
        List<String> result = new ArrayList<>();
        if (commandSplit.length <= 1) {
            return result;
        }

        result.addAll(Arrays.asList(commandSplit).subList(1, commandSplit.length));
        return result;
    }
}
