package app

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.subethamail.smtp.server.SMTPServer

@Component
class MainRunner implements CommandLineRunner {
    @Value('${smtp.port:2525}')
    int smtpPort

    @Value('${smtp.message.dir}')
    String smtpMessageDir

    @Override
    void run(String... args) throws Exception {
        new File(smtpMessageDir).mkdirs()
        SMTPServer smtpServer = new SMTPServer({ ctx -> new MyMessageHandler(messageDir: smtpMessageDir) })
        smtpServer.setPort(smtpPort)
        smtpServer.start()
    }
}
