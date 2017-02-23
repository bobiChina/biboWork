package com.ligootech.ota.web.mqtt;

import com.ligootech.webdtu.util.StringUtil;
import net.sf.json.JSONObject;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/29.
 */
public class MqttSendUtil {
    private final Logger logger = LoggerFactory.getLogger(MqttSendUtil.class);//指定初始化位置

    private static final long serialVersionUID = 1L;

    private MqttClient client;
    private final String HOST = "tcp://192.168.1.22:1883";
    private final String USER_NAME = "master";
    private final String PASS_WORD = "master123";
    private final String TOPIC_PUBLISH = "topic_task_publish";
    private final String TOPIC_CLOSE = "topic_task_ack";
    private final String clientId = StringUtil.getLocalMac();//使用当前机器的MAC地址，不至于产生多个soket
    //private final String clientId = StringUtil.generalMacString();

    private List<String> uuids;
    private String fileCode;

    public MqttSendUtil(List<String> uuids, String fileCode){
        this.uuids = uuids;
        this.fileCode = fileCode;

        try {
            client = new MqttClient(HOST, clientId, new MemoryPersistence());
            connect();

            client.subscribe(TOPIC_CLOSE);

        } catch (MqttException e) {
            logger.error("");
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uuids", uuids);
        map.put("fileCode", fileCode);
        String mapStr = JSONObject.fromObject(map).toString();
        logger.debug("mqtt发送内容：" + mapStr);

        MqttMessage message = new MqttMessage();
        message.setQos(2);  // “只有一次”，确保消息到达一次。
        message.setRetained(true);
        message.setPayload(mapStr.getBytes());
        try {
            client.publish(TOPIC_PUBLISH, message);
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭
     */
    private void closeClient(){
        /*****************************
         * 消息发送结束后关闭连接
         ****************************/
        try {
            logger.debug("关闭连接：" + client + "---" + client.isConnected());
            Thread.sleep(1000L);//延迟一秒
            if (client != null && client.isConnected()) {
                logger.debug("执行关闭连接操作");
                client.close();
            }else{
                logger.debug("关闭连接失败， client为null");
            }
        } catch (MqttException e) {
            //e.printStackTrace();
            logger.debug("已连接客户机 (32100)");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * MQTT连接
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        //设置为 true，在客户机建立连接时，将除去客户机的任何旧预订。当客户机断开连接时，会除去客户机在会话期间创建的任何新预订
        options.setCleanSession(true);
        options.setUserName(USER_NAME);
        options.setPassword(PASS_WORD.toCharArray());
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(1 * 60);
        try {
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    logger.debug("mqttClient新连接接入，原连接自动断开");
                }

                /**
                 * 订阅收到消息
                 * @param topic
                 * @param mqttMessage
                 * @throws Exception
                 */
                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    logger.debug("收到消息，内容为：" + topic + "   \nMqttMessage:" + mqttMessage.getPayload());
                    /**
                     * 关闭本次连接
                     */
                    if (TOPIC_CLOSE.equals(topic)){
                        closeClient();
                    }
                }

                /**
                 * 发送消息后调用
                 */
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                    if (token.isComplete()){
                        logger.debug("信息向服务端交付完成，内容为：" + StringUtil.getStrsplit(uuids) + " \nfileCode:" + fileCode);
                    }
                    /*for (String str : token.getTopics()) {
                        System.out.println("getTopics:" + str);
                    }*/
                }
            });

            client.connect(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
