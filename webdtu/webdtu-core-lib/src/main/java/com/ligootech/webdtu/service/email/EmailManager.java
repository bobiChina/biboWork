package com.ligootech.webdtu.service.email;

/**
 * Created by Administrator on 2016/7/18.
 */
public interface EmailManager {
    /**
     * 邮件发送（wanglyue@126.com）
     * @param subject
     * @param content
     * @param toUser
     */
    public void  sendMail(String subject, String content, String toUser);

    /**
     * 邮件发送（群发）
     * @param subject
     * @param content
     * @param toUser
     */
    public void  sendMail(String subject, String content, String[] toUser);

    /**
     * 邮件发送（按用户类型）
     * @param subject
     * @param content
     * @param userType
     * @param userId
     */
    public void  sendMail(String subject, String content, int userType, Long userId);
}
