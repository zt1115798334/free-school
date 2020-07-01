package com.example.school.common.tools;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.NettyHttpClient;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.report.ReceivedsResult;
import com.example.school.common.constant.properties.JPushProperties;
import com.example.school.common.mysql.service.UserRegistrationService;
import com.example.school.common.mysql.service.UserService;
import com.example.school.common.utils.UserUtils;
import com.google.common.collect.Maps;
import io.netty.handler.codec.http.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/30 10:15
 * description: 极光推送工具类
 */
@AllArgsConstructor
@Slf4j
@Component
public class JPushTool {

    private final JPushProperties jPushProperties;

    private final UserService userService;

    private final UserRegistrationService userRegistrationService;

    /**
     * 发送自定义推送，由APP端拦截信息后再决定是否创建通知(目前APP用此种方式)
     *
     * @param title     App通知栏标题
     * @param content   App通知栏内容（为了单行显示全，尽量保持在22个汉字以下）
     * @param extrasMap 额外推送信息（不会显示在通知栏，传递数据用）
     * @param alias     别名数组，设定哪些用户手机能接收信息（为空则所有用户都推送）
     * @return PushResult
     */
    private PushResult sendCustomPush(String title, String content, Map<String, String> extrasMap, String... alias) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setTimeToLive(Long.valueOf(jPushProperties.getLiveTime()));
        // 使用NativeHttpClient网络客户端，连接网络的方式，不提供回调函数
        JPushClient jpushClient = new JPushClient(jPushProperties.getMasterSecret(), jPushProperties.getAppKey(), null,
                clientConfig);
        // 设置为消息推送方式为仅推送消息，不创建通知栏提醒
        PushPayload payload = buildCustomPushPayload(title, content, extrasMap, alias);
        PushResult result = null;
        try {
            result = jpushClient.sendPush(payload);
            log.info("极光推送结果 - " + result + ",接收推送的别名列表:" + String.join(",", alias));
        } catch (APIConnectionException e) {
            log.error("极光推送连接错误，请稍后重试 ", e);
            log.error("SendNo: " + payload.getSendno());
        } catch (APIRequestException e) {
            log.error("极光服务器响应出错，请修复！ ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            log.info("以下存在不能识别的别名: " + String.join(",", alias));
            log.error("SendNo: " + payload.getSendno());
        }
        return result;
    }

    /**
     * 原生方式推送
     *
     * @param title     App通知栏标题
     * @param content   App通知栏内容（为了单行显示全，尽量保持在22个汉字以下）
     * @param extrasMap 额外推送信息（不会显示在通知栏，传递数据用）
     * @param alias     别名数组，设定哪些用户手机能接收信息（为空则所有用户都推送）
     */
    private PushResult sendPush(String title, String content, Map<String, String> extrasMap, String... alias) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setTimeToLive(Long.valueOf(jPushProperties.getLiveTime()));
        // 使用NativeHttpClient网络客户端，连接网络的方式，不提供回调函数
        JPushClient jpushClient = new JPushClient(jPushProperties.getMasterSecret(), jPushProperties.getAppKey(), null,
                clientConfig);
        // 设置推送方式
        PushPayload payload = buildPushPayload(title, content, extrasMap, alias);
        PushResult result = null;
        try {
            result = jpushClient.sendPush(payload);
            log.info("极光推送结果 - " + result);
        } catch (APIConnectionException e) {
            log.error("极光推送连接错误，请稍后重试 ", e);
            log.error("SendNo: " + payload.getSendno());
        } catch (APIRequestException e) {
            log.error("极光服务器响应出错，请修复！ ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            log.info("以下存在不能识别别名: " + Arrays.toString(alias));
            log.error("SendNo: " + payload.getSendno());
        }
        return result;
    }

    /**
     * 异步请求推送方式
     *
     * @param title     通知栏标题
     * @param content   通知栏内容（为了单行显示全，尽量保持在22个汉字以下）
     * @param extrasMap 额外推送信息（不会显示在通知栏，传递数据用）
     * @param alias     需接收的用户别名数组（为空则所有用户都推送）
     *                  使用NettyHttpClient,异步接口发送请求，通过回调函数可以获取推送成功与否情况
     */
    private void sendPushWithCallback(String title, String content, Map<String, String> extrasMap, String... alias) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setTimeToLive(Long.valueOf(jPushProperties.getLiveTime()));
        String host = (String) clientConfig.get(ClientConfig.PUSH_HOST_NAME);
        NettyHttpClient client = new NettyHttpClient(
                ServiceHelper.getBasicAuthorization(jPushProperties.getAppKey(), jPushProperties.getMasterSecret()), null,
                clientConfig);
        try {
            URI uri = new URI(host + clientConfig.get(ClientConfig.PUSH_PATH));
            PushPayload payload = buildPushPayload(title, content, extrasMap, alias);
            client.sendRequest(HttpMethod.POST, payload.toString(), uri, responseWrapper -> {
                if (200 == responseWrapper.responseCode) {
                    log.info("极光推送成功");
                } else {
                    log.info("极光推送失败，返回结果: " + responseWrapper.responseContent);
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            // 需要手动关闭Netty请求进程,否则会一直保留
            client.close();
        }

    }

    /**
     * 构建Android和IOS的推送通知对象
     *
     * @return PushPayload
     */
    private PushPayload buildPushPayload(String title, String content, Map<String, String> extrasMap, String... alias) {
        if (extrasMap == null || extrasMap.isEmpty()) {
            extrasMap = Maps.newHashMap();
        }
        // 批量删除数组中空元素
        String[] newAlias = removeArrayEmptyElement(alias);
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                // 别名为空，全员推送；别名不为空，按别名推送
                .setAudience((null == newAlias || newAlias.length == 0) ? Audience.all() : Audience.registrationId(alias))
                .setNotification(Notification.newBuilder().setAlert(content)
                        .addPlatformNotification(
                                AndroidNotification.newBuilder().setTitle(title).addExtras(extrasMap).build())
                        .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).addExtras(extrasMap).build())
                        .build())
                .build();
    }

    /**
     * 构建Android和IOS的自定义消息的推送通知对象
     *
     * @return PushPayload
     */
    private PushPayload buildCustomPushPayload(String title, String content, Map<String, String> extrasMap,
                                               String... alias) {
        // 批量删除数组中空元素
        String[] newAlias = removeArrayEmptyElement(alias);
        return PushPayload.newBuilder().setPlatform(Platform.android_ios())
                .setAudience((null == newAlias || newAlias.length == 0) ? Audience.all() : Audience.registrationId(alias))
                .setMessage(Message.newBuilder().setTitle(title).setMsgContent(content).addExtras(extrasMap).build())
                .build();
    }

    /**
     * 查询记录推送成功条数（暂未使用）
     *
     * @param msg_id 在推送返回结果PushResult中保存
     */
    public void countPush(String msg_id) {
        JPushClient jpushClient = new JPushClient(jPushProperties.getMasterSecret(), jPushProperties.getAppKey());
        try {
            ReceivedsResult result = jpushClient.getReportReceiveds(msg_id);
            ReceivedsResult.Received received = result.received_list.get(0);
            log.debug("Android接受信息:" + received.android_received + "\n IOS端接受信息:" + received.ios_apns_sent);
            log.debug("极光推送返回结果 - " + result);
        } catch (APIConnectionException e) {
            log.error("极光推送连接错误，请稍后重试", e);
        } catch (APIRequestException e) {
            log.error("检查错误，并修复推送请求", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }
    }

    /**
     * 删除别名中的空元素（需删除如：null,""," "）
     *
     * @param strArray strArray
     * @return String[]
     */
    private String[] removeArrayEmptyElement(String... strArray) {
        if (null == strArray || strArray.length == 0) {
            return null;
        }
        List<String> tempList = Arrays.asList(strArray);
        // 若仅输入"",则会将数组长度置为0
        return tempList.stream()
                .map(String::trim)
                .filter(StringUtils::isNotEmpty).toArray(String[]::new);
    }

    public boolean pushZanInfo(Long topicId, Short topicType, Short zanType, Long toUserId, Long fromUserId) {
        List<String> registrationIdList = userRegistrationService.findRegistrationIdByUserId(fromUserId);
        com.example.school.common.mysql.entity.User toUser = userService.findOptByUserId(toUserId).orElse(UserUtils.getDefaultUser());
        String title = "你收到来自@" + toUser.getUserName() + "点赞";
        Map<String, String> extrasMap = Maps.newHashMap();
        extrasMap.put("topicId", String.valueOf(topicId));
        extrasMap.put("topicType", String.valueOf(topicType));
        this.sendPush(title, StringUtils.EMPTY, extrasMap, registrationIdList.toArray(new String[0]));
        return true;
    }

    public boolean pushCommentInfo(Long topicId, Short topicType, String commentContent, Long toUserId, Long fromUserId) {
        List<String> registrationIdList = userRegistrationService.findRegistrationIdByUserId(fromUserId);
        com.example.school.common.mysql.entity.User toUser = userService.findOptByUserId(toUserId).orElse(UserUtils.getDefaultUser());
        String title = "你收到来自@" + toUser.getUserName() + "的消息";
        String content = "评论内容：" + commentContent;
        Map<String, String> extrasMap = Maps.newHashMap();
        extrasMap.put("topicId", String.valueOf(topicId));
        extrasMap.put("topicType", String.valueOf(topicType));
        this.sendPush(title, content, extrasMap, registrationIdList.toArray(new String[0]));
        return true;

    }

}
