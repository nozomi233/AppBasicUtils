package com.zhulang.basic.utils.api.alert;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author zhulang
 * @Date 2023-04-24
 **/
@Slf4j
public class DingTalkService {
    private static final String DING_DING_URL = "https://oapi.dingtalk.com/robot/send?access_token=";

    private static final String MARK_DOWN = "markdown";

    public DingTalkService() {
    }

    public void send(String text, String title, String token, OapiRobotSendRequest.At at) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(MARK_DOWN);
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(text);
        request.setMarkdown(markdown);
        request.setAt(at);

        try {
            DefaultDingTalkClient defaultDingTalkClient = new DefaultDingTalkClient(DING_DING_URL + token);
            OapiRobotSendResponse ret = (OapiRobotSendResponse) defaultDingTalkClient.execute(request);
            log.info("OapiRobotSendResponse={},", JSON.toJSONString(ret));
        } catch (Exception var10) {
            log.warn("DingTalkService send error", var10);
        }

    }
}
