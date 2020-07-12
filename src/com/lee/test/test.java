package com.lee.test;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class test {
    public static void main(String[] args) throws Exception{
        Properties prop = new Properties();
        //设置QQ邮件服务器
        prop.setProperty("mail.host", "smtp.qq.com");
        //邮件发送协议
        prop.setProperty("mail.transport.protocol", "stmp");
        //需要验证用户名和密码
        prop.setProperty("mail.smtp.auth", "true");

        //对于一些权威性比较高的邮箱，需要设置SSL加密以保证安全性
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);

        /*使用JavaMail邮件发送的五个步骤*/

        //1. 创建定义整个应用程序所需的环境信息的session对象

        //QQ特有
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //发件人用户名，授权码
                return new PasswordAuthentication("2466486858@qq.com", "");
            }
        });

        //开启session的debug模式
        session.setDebug(true);

        //2. 通过session得到transport对象
        Transport ts = session.getTransport();

        //3. 使用邮箱的用户名和授权码连上邮件服务器
        ts.connect("stmp.qq.com", "2466486858@qq.com", "");

        //4. 创建邮件

        //创建邮件对象
        MimeMessage message = new MimeMessage(session);

        //指明邮件的发件人
        message.setFrom(new InternetAddress("2466486858@qq.com"));
        //指明收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("2466486858@qq.com"));

        //邮件的标题
        message.setSubject("");

        //邮件的内容
        //图片
        MimeBodyPart body1 = new MimeBodyPart();
        body1.setDataHandler(new DataHandler(new FileDataSource("")));
        body1.setContentID("");
        //文本
        MimeBodyPart body2 = new MimeBodyPart();
        body2.setContent("", "text/html;charset=utf-8");
        //附件
        MimeBodyPart body3 = new MimeBodyPart();
        body3.setDataHandler(new DataHandler(new FileDataSource("")));
        body3.setFileName("");

        //拼装正文内容
        MimeMultipart multipart1 = new MimeMultipart();
        multipart1.addBodyPart(body1);
        multipart1.addBodyPart(body2);
        multipart1.setSubType("related");//文本和图片的结合类型

        //将上面拼接好的内容设置为一个主体
        MimeBodyPart contentText = new MimeBodyPart();
        contentText.setContent(multipart1);

        //拼接附件
        MimeMultipart addFile = new MimeMultipart();
        addFile.addBodyPart(body3);//添加附件
        addFile.addBodyPart(contentText);//添加正文
        addFile.setSubType("mixed");//混合类型

        //放到Message中
        message.setContent(addFile);
        //保存修改
        message.saveChanges();


        //5. 发送邮件
        ts.sendMessage(message, message.getAllRecipients());

        //6. 关闭连接
        ts.close();
    }
}
