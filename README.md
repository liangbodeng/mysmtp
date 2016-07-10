# A simple mock SMTP server that saves all incoming emails to local file system

## What is this?

This is a simple mock SMTP server that saves all the incoming emails to a folder in the local file system.
It was built with:

  * [Spring Boot] (http://projects.spring.io/spring-boot/)
  * [SubEtha SMTP] (https://github.com/voodoodyne/subethasmtp)

## Usage

  * Build the project
    - `gradle clean build`

  * Copy the build/libs/mysmtp*.jar to any folder, and then run the following command:
    - `java -jar mysmtp*.jar`

  * Override the configuration using any approach listed at [Spring Boot Externalized Configuration] (http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html).
    The property names are:
    - smtp.port: the port that the SMTP server runs on, default to 2525.
    - smtp.message.dir: where the incoming emails are saved, default to $CWD/msg.
