package app

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner)
@SpringBootTest(classes = [MySmtpApplication, MySmtpApplicationTests])
@TestPropertySource(locations = "classpath:test.properties")
@Configuration
class MySmtpApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void sendSimpleMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage(
                from: 'sender@example.com',
                to: ['recipient@example.com'],
                subject: 'Test Subject',
                text: 'This is a test mail.\n\nRegards,\nSender\n'
        )
        mailSender.send(simpleMailMessage)
    }

    @Test
    void sendMimeMessage() {
        MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true)
        helper.from = "sender@example.com"
        helper.to = "recipient@gmail.com"
        helper.text = "This is a test email with attachments.\n\nRegards,\nSender\n"
        helper.addAttachment("application.properties", new FileSystemResource("src/main/resources/application.properties"))
        mailSender.send(helper.mimeMessage)
    }

    @Autowired
    JavaMailSenderImpl mailSender

    @Bean
    JavaMailSenderImpl mailSender(@Value('${smtp.port:2525}') int smtpPort) {
        new JavaMailSenderImpl(host: 'localhost', port: smtpPort)
    }
}
