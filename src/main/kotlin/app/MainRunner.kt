package app

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.subethamail.smtp.server.SMTPServer
import java.io.File

@Component
class MainRunner(
        @Value("\${smtp.port:2525}")
        val smtpPort: Int,
        @Value("\${smtp.message.dir}")
        val smtpMessageDir: String
): CommandLineRunner {

    @Override
    override fun run(vararg args: String) {
        File(smtpMessageDir).mkdirs()

        SMTPServer
            .port(smtpPort)
            .messageHandler(MyMessageHandler(smtpMessageDir))
            .build()
            .start()
    }
}
