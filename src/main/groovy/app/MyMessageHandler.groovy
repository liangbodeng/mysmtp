package app

import org.subethamail.smtp.MessageHandler
import org.subethamail.smtp.RejectException

import javax.mail.Multipart
import javax.mail.Part
import javax.mail.Session
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyMessageHandler implements MessageHandler {
    String messageDir

    @Override
    void from(String from) throws RejectException {
        println "FROM: ${from}"
    }

    @Override
    void recipient(String recipient) throws RejectException {
        println "RECIPIENT: ${recipient}"
    }

    @Override
    void data(InputStream data) throws IOException {
        Session session = Session.getDefaultInstance(System.getProperties())
        MimeMessage mimeMessage = new MimeMessage(session, data)
        String prefix = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(LocalDateTime.now())
        dumpPart(mimeMessage, prefix, 1, 1)
    }

    protected void dumpPart(Part p, String prefix, int level, int num) throws Exception {
        String postfix = p.fileName ? "_${p.fileName}" : ""
        String filename = "${prefix}_${level}_${num}${postfix}"
        println "${level}_${num}: ${getContentType(p)}, ${filename} (${p.size})"
        File file = new File(messageDir, filename)

        if (p.isMimeType("text/plain")) {
            file.text = p.content
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.content
            for (int i = 0; i < mp.count; ++i) {
                dumpPart(mp.getBodyPart(i), prefix, level + 1, i + 1)
            }
        } else if (p.isMimeType("message/rfc822")) {
            dumpPart((Part) p.content, prefix, level + 1, 1)
        }

        if (level > 1 && p instanceof MimeBodyPart && !p.isMimeType("multipart/*")) {
            MimeBodyPart mbp = (MimeBodyPart) p
            String disposition = p.disposition
            if (disposition == null || disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
                mbp.saveFile(file)
            }
        }
    }

    private String getContentType(Part p) {
        p.contentType.readLines()[0].replaceAll(';', '')
    }

    @Override
    void done() {
        println "Finished"
    }
}
