package com.nemustech.platform.lbs.common.service;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//공통 mail service
@SuppressWarnings("serial")
@Service(value = "mailService")
public class MailService extends HttpServlet {

	@Autowired
	protected PropertyService mailpProps;

	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	public static final String MAIL_SMTP_SOCKET_FACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";

	/*
	 * 공통 메일 service from_mail : 보내는 사람 to_mail : 받는 사람 title : 제목 content : 내용
	 */
	public String sendMail(String from_mail, String to_mail, String title, String content)
			throws ServletException, IOException {

		String from_email = from_mail;
		String to_email = to_mail;
		String mail_subtitle = title;
		String mail_content = content;

		from_email = mailpProps.getString("mail.from.email");

		logger.info("from_email = " + from_email);
		logger.info("to_email = " + to_email);

		// 먼저 환경 정보를 설정해야 한다.
		// 메일 서버 주소를 IP 또는 도메인 명으로 지정한다.
		Properties props = System.getProperties();
		props.setProperty("mail.transport.protocol", mailpProps.getString("mail.transport.protocol"));
		props.setProperty("mail.host", mailpProps.getString("mail.smtp.host"));
		props.put("mail.smtp.auth", false);
		props.put("mail.smtp.ssl.enable", false);
		props.put("mail.smtp.port", mailpProps.getString("mail.smtp.port"));
		props.put("mail.smtp.starttls.enable", true);
		// props.put("mail.smtp.socketFactory.port",
		// mailpProps.getString("mail.smtp.port"));
		// props.put("mail.smtp.socketFactory.class",
		// MAIL_SMTP_SOCKET_FACTORY_CLASS);
		// props.put("mail.smtp.socketFactory.fallback", false);
		logger.debug("mail props: " + props);

		Authenticator authenticator = new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailpProps.getString("mail.from.email"), "");
			}
		};

		// 위 환경 정보로 "메일 세션"을 만들고 빈 메시지를 만든다
		Session session = Session.getDefaultInstance(props, authenticator);
		session.setDebug(true);

		MimeMessage msg = new MimeMessage(session);

		try {
			// 발신자, 수신자, 참조자, 제목, 본문 내용 등을 설정한다
			msg.setFrom(new InternetAddress(from_email, mailpProps.getString("from.system.name")));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to_email, to_email));

			msg.setSubject(mail_subtitle, "UTF-8");
			msg.setContent(mail_content, "text/html; charset=utf-8");

			// 메일을 발신한다
			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}

		return "SUCCESS";
	}

}