package team.nine.booknutsbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    JavaMailSender javaMailSender;

    private final RedisService redisService;

    //static으로 선언했으지만 db에 컬럼 추가하는 방식으로 변경 예정
    public String authcode = createKey();

    //메세지 내용 생성
    private MimeMessage createMessage(String to) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to); //보내는 대상
        message.setSubject("Booknuts 회원가입 이메일 인증"); //제목

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
        message.setText(msgg, "utf-8", "html"); //내용
        message.setFrom(new InternetAddress("!!!!!!!!!!application-email.properties의 email address와 일치시키기!!!!!!!")); //보내는 사람
        return message;
    }

    //인증 코드 발급
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    //인증코드 이메일 전송
    public String sendSimpleMessage(String to) throws MessagingException {
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

    //redis 코드와 비교
    public Boolean confirmEmailCode(String email, String code) {
        String redisCode = redisService.getValues(email);
        return redisCode.equals(code);
    }

}
