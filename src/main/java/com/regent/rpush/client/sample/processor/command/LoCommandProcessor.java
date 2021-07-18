package com.regent.rpush.client.sample.processor.command;

import com.regent.rpush.dto.rpushserver.PageOnlineDTO;
import com.regent.rpush.dto.rpushserver.RpushServerRegistrationDTO;
import com.regent.rpush.dto.table.Pagination;
import com.regent.rpush.sdk.RpushService;
import org.summer.container.annotation.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 命令：lo
 *
 * @author 钟宝林
 * @since 2021/7/13/013 13:52
 **/
@Component
public class LoCommandProcessor implements CommandProcessor {

    @Resource
    private RpushService rpushService;

    @Override
    public void process(List<String> params) {
        Pagination<RpushServerRegistrationDTO> onlineRegistrations = rpushService.pageOnlineRegistrations(new PageOnlineDTO());
        List<RpushServerRegistrationDTO> dataList = onlineRegistrations.getDataList();
        for (RpushServerRegistrationDTO registrationDTO : dataList) {
            System.out.println("[id=" + registrationDTO.getId() + ", name=" + registrationDTO.getName() + "]");
        }
    }

    @Override
    public String command() {
        return "lo";
    }

    @Override
    public String usage() {
        return "使用本命令可打印当前在线设备列表";
    }
}
