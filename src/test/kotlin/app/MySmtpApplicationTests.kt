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

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [MySmtpApplication::class, MySmtpApplicationTests::class])
@TestPropertySource(locations = ["classpath:test.properties"])
@Configuration
class MySmtpApplicationTests {

    @Test
    fun contextLoads() {
    }

    @Test
    fun sendSimpleMessage() {
        val simpleMailMessage = SimpleMailMessage().apply {
            setFrom("sender@example.com")
            setTo("recipient@example.com")
            setSubject("Test Subject")
            setText("This is a test mail.\n\nRegards,\nSender\n")
        }
        mailSender.send(simpleMailMessage)
    }

    @Test
    fun sendMimeMessage() {
        val helper = MimeMessageHelper(mailSender.createMimeMessage(), true).apply {
            setFrom("sender@example.com")
            setTo("recipient@gmail.com")
            setText("This is a test email with attachments.\n\nRegards,\nSender\n")
            addAttachment("application.properties", FileSystemResource("src/main/resources/application.properties"))
        }
        mailSender.send(helper.mimeMessage)
    }

    @Autowired
    lateinit var mailSender: JavaMailSenderImpl

    @Bean
    fun mailSender(@Value("\${smtp.port:2525}") smtpPort: Int): JavaMailSenderImpl {
        return JavaMailSenderImpl().apply {
            host = "localhost"
            port = smtpPort
        }
    }
}
