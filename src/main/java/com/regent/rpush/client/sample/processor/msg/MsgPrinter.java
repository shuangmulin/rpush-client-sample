package com.regent.rpush.client.sample.processor.msg;

import com.regent.rpush.client.Msg;
import com.regent.rpush.client.MsgProcessor;
import com.regent.rpush.common.SingletonUtil;
import com.regent.rpush.dto.rpushserver.RpushServerRegistrationDTO;
import com.regent.rpush.sdk.RpushService;
import org.summer.container.annotation.Component;
import org.summer.container.event.EventListener;
import org.summer.container.event.impl.ContainerStartedEvent;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * 消息打印
 *
 * @author 钟宝林
 * @since 2021/7/12/012 11:08
 **/
@Component
public class MsgPrinter implements Runnable, EventListener<ContainerStartedEvent>, MsgProcessor {
    private volatile boolean startFlag = true;

    private final BlockingQueue<Msg> queue = new LinkedBlockingQueue<>();
    private Thread printThread;

    @Resource
    private RpushService rpushService;

    public void addMsg(Msg msg) {
        queue.offer(msg);
    }

    public void start() {
        startFlag = true;
        LockSupport.unpark(printThread);
    }

    public void stop() {
        this.startFlag = false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Msg msg = queue.take();
                if (!startFlag) {
                    LockSupport.park();
                }
                String fromToStr = "系统消息";
                if (msg.getFromTo() != -1) {
                    long fromTo = msg.getFromTo();
                    RpushServerRegistrationDTO registration = SingletonUtil.get(fromTo + "getRegistration", () -> rpushService.getRegistration(String.valueOf(fromTo)));
                    fromToStr = registration.getName();
                }
                System.out.println("【" + fromToStr + "】: " + msg.getContent());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEvent(ContainerStartedEvent event) {
        // 容器启动，开始打印消息
        printThread = new Thread(this);
        printThread.setDaemon(true);
        printThread.start();
    }

    @Override
    public boolean process(Msg msg) {
        addMsg(msg); // 收到消息直接往队列里扔
        return false;
    }
}
