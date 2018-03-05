package app

import org.subethamail.smtp.MessageHandler
import java.io.File
import java.io.InputStream

import javax.mail.Multipart
import javax.mail.Part
import javax.mail.Session
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyMessageHandler(
        val messageDir: String
): MessageHandler {
    @Override
    override fun from(from: String) {
        println("FROM: $from")
    }

    @Override
    override fun recipient(recipient: String) {
        println("RECIPIENT: $recipient")
    }

    @Override
    override fun data(data: InputStream) {
        val session = Session.getDefaultInstance(System.getProperties())
        val mimeMessage = MimeMessage(session, data)
        val prefix = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(LocalDateTime.now())
        dumpPart(mimeMessage, prefix, 1, 1)
    }

    protected fun dumpPart(p: Part, prefix: String, level: Int, num: Int) {
        val postfix = if (p.fileName != null) "_${p.fileName}" else ""
        val filename = "${prefix}_${level}_${num}${postfix}"
        println("${level}_${num}: ${getContentType(p)}, ${filename} (${p.size})")
        val file = File(messageDir, filename)

        when {
            p.isMimeType("text/plain") -> file.writeText(p.content as String)
            p.isMimeType("multipart/*") -> {
                val mp = p.content as Multipart
                (0 until mp.count).forEach {
                    dumpPart(mp.getBodyPart(it), prefix, level + 1, it + 1)
                }
            }
            p.isMimeType("message/rfc822") -> dumpPart(p.content as Part, prefix, level + 1, 1)
        }

        if (level > 1 && p is MimeBodyPart && !p.isMimeType("multipart/*")) {
            val disposition = p.disposition
            if (disposition == null || disposition.equals(Part.ATTACHMENT, true)) {
                p.saveFile(file)
            }
        }
    }

    private fun getContentType(p: Part): String {
        return p.contentType.lines()[0].replace(";", "")
    }

    @Override
    override fun done() {
        println("Finished")
    }
}
