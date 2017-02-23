package com.ligootech.webdtu.service.impl.email;

import com.ligootech.webdtu.service.email.EmailManager;
import com.ligootech.webdtu.util.BhGenerator;
import com.ligootech.webdtu.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/7/18.
 */
@Service("emailManager")
public class EmailManagerImpl implements EmailManager {

    @Autowired
    private MailSender mailSender;
    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void sendMail(String subject, String content, String toUser) {
        /*simpleMailMessage.setSubject(subject);  // 设置邮件主题
        simpleMailMessage.setTo(toUser);             // 设定收件人
        simpleMailMessage.setText(content);   // 设置邮件主题内容
        mailSender.send(simpleMailMessage);  // 发送邮件*/

        JavaMailSender javaMailSender = (JavaMailSender) mailSender;
        MimeMessage mime = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            /*helper = new MimeMessageHelper(mime, true, "utf-8");
            helper.setFrom("jakin@xxx.com");
            helper.setTo("lilei@xxx.com");
            helper.setCc("hanmeimei@xxx.com");
            helper.setSubject("第二封測試郵件");
            helper.setText("Dear All：\n" + "這是一封測試郵件!");*/

            helper = new MimeMessageHelper(mime, true, "utf-8");
            helper.setFrom(simpleMailMessage.getFrom());
            helper.setTo(toUser);               // 设定收件人
            helper.setSubject(subject);         // 设置邮件主题
            helper.setText(content);            // 设置邮件主题内容

        } catch (MessagingException me) {
            me.printStackTrace();
        }
        javaMailSender.send(mime);

    }

    @Override
    public void sendMail(String subject, String content, String[] toUser) {
        /*try {
            simpleMailMessage.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));  // 设置邮件主题
            simpleMailMessage.setText(MimeUtility.encodeText(content, "UTF-8", "B"));  // 设置邮件主题内容
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        simpleMailMessage.setTo(toUser);             // 设定收件人
        mailSender.send(simpleMailMessage);  // 发送邮件*/

        JavaMailSender javaMailSender = (JavaMailSender) mailSender;
        MimeMessage mime = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            /*helper = new MimeMessageHelper(mime, true, "utf-8");
            helper.setFrom("jakin@xxx.com");
            helper.setTo("lilei@xxx.com");
            helper.setCc("hanmeimei@xxx.com");
            helper.setSubject("第二封測試郵件");
            helper.setText("Dear All：\n" + "這是一封測試郵件!");*/

            helper = new MimeMessageHelper(mime, true, "utf-8");
            helper.setFrom(simpleMailMessage.getFrom());
            helper.setTo(toUser);               // 设定收件人
            helper.setSubject(subject);         // 设置邮件主题
            helper.setText(content);            // 设置邮件主题内容

        } catch (MessagingException me) {
            me.printStackTrace();
        }
        javaMailSender.send(mime);
    }

    @Override
    public void sendMail(final String subject, final String content, int userType, Long userId) {
        // SELECT user_email FROM t_email_user WHERE user_type=1;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT user_email FROM t_email_user WHERE user_type=?");

        Object[] arg = new Object[]{userType};
        List<String> list = jdbcTemplate.query(sql.toString(), arg, new RowMapper(){

            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                return StringUtil.null2String(resultSet.getString("user_email"));
            }
        });

        /***************************************
         * 组装用户
         ****************************************/
        if (null != list && list.size() > 0){
            final String[] userEmailArr = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                userEmailArr[i] = list.get(i);
            }
            /*********************************************************************
             * 建议使用较为方便的 Executors 工厂方法
             * Executors.newCachedThreadPool()（无界线程池，可以进行自动线程回收）
             * Executors.newFixedThreadPool(int)（固定大小线程池）和
             * Executors.newSingleThreadExecutor()（单个后台线程）
             * mailMessage.setSubject(MimeUtility.encodeText(mailInfo.getSubject(), "UTF-8", "B"));
             ********************************************************************/
            Executors.newCachedThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    sendMail(subject, content, userEmailArr);
                }
            });
            //本地数据库记录
            String bh = BhGenerator.getBh();
            StringBuffer sqlBuff = new StringBuffer();
            sqlBuff.append("INSERT INTO t_email (batch_no, email_type, to_user, email_content, status, opt_user, opt_time) ");
            sqlBuff.append("VALUES ('").append(bh).append("','").append(userType).append("','").append(StringUtil.join(userEmailArr, ",")).append("','")
                .append(content).append("','0','").append(userId).append("',NOW() )");
            jdbcTemplate.execute(sqlBuff.toString());
        }
    }
}
