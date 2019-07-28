package com.example.school.common.base.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 18:27
 * description: 前后端统一消息定义协议 Message 之后前后端数据交互都按照规定的类型进行交互
 */
@ApiModel(description = "返回响应数据")
public class ResultMessage {
    // 消息头meta 存放状态信息 code message
    @ApiModelProperty(value = "消息头")
    private JSONObject meta = new JSONObject();
    // 消息内容  存储实体交互数据
    @ApiModelProperty(value = "消息内容")
    private JSONObject data = new JSONObject();

    public JSONObject getMeta() {
        return meta;
    }

    public ResultMessage setMeta(JSONObject meta) {
        this.meta = meta;
        return this;
    }

    public JSONObject getData() {
        return data;
    }

    public ResultMessage setData(Object data) {
        data = JSON.toJSON(data);
        if (data instanceof JSONObject) {
            this.data = TypeUtils.castToJavaBean(data,JSONObject.class);
        }
        if (data instanceof JSONArray) {
            JSONObject list = new JSONObject();
            list.put("list", data);
            this.data = list;
        }
        return this;
    }

    public ResultMessage addMeta(String key, Object object) {
        this.meta.put(key, object);
        return this;
    }

    public ResultMessage addData(String key, Object object) {
        this.data.put(key, object);
        return this;
    }

    public ResultMessage ok(int statusCode) {
        this.addMeta("success", Boolean.TRUE);
        this.addMeta("code", statusCode);
        this.addMeta("timestamp", Timestamp.valueOf(LocalDateTime.now()));
        return this;
    }

    public ResultMessage ok(int statusCode, String statusMsg) {
        this.addMeta("success", Boolean.TRUE);
        this.addMeta("code", statusCode);
        this.addMeta("msg", statusMsg);
        this.addMeta("timestamp", Timestamp.valueOf(LocalDateTime.now()));
        return this;
    }

    public ResultMessage error(int statusCode) {
        this.addMeta("success", Boolean.FALSE);
        this.addMeta("code", statusCode);
        this.addMeta("timestamp", Timestamp.valueOf(LocalDateTime.now()));
        return this;
    }

    public ResultMessage error(int statusCode, String statusMsg) {
        this.addMeta("success", Boolean.FALSE);
        this.addMeta("code", statusCode);
        this.addMeta("msg", statusMsg);
        this.addMeta("timestamp", Timestamp.valueOf(LocalDateTime.now()));
        return this;
    }
}
