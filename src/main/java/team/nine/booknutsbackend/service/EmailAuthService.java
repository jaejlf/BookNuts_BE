package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailAuthService {

    JavaMailSender javaMailSender;
    private final RedisService redisService;

    public String authcode = createKey();

    private MimeMessage createMessage(String to) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("Booknuts 회원가입 이메일 인증");

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 안녕하세요 알차고 고소한 지식 공유 시간, 북넛츠입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += authcode + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("sender's email"));
        return message;
    }

    public static String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) (rnd.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) (rnd.nextInt(26) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }
        return key.toString();
    }

    public String sendAuthCode(String to) throws MessagingException {
        MimeMessage message = createMessage(to);
        try {
            javaMailSender.send(message);
            redisService.setValues(to, authcode, Duration.ofMillis(600000));
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return authcode;
    }

    public Boolean confirmAuthCode(String email, String code) {
        String redisCode = redisService.getValues(email);
        return redisCode.equals(code);
    }

}
