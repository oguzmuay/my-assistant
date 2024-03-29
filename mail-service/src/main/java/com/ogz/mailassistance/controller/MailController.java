package com.ogz.mailassistance.controller;


import com.ogz.mailassistance.dto.*;
import org.ogz.model.Mail;
import com.ogz.mailassistance.service.MailService;
import io.swagger.v3.oas.annotations.Hidden;
import org.ogz.client.UserServiceClient;
import org.ogz.model.AwaitUser;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

    private final UserServiceClient userServiceClient;
    private final MailService service;

    public MailController(UserServiceClient userServiceClient, MailService service) {
        this.userServiceClient = userServiceClient;
        this.service = service;
    }


    @GetMapping("/{id}")
    ResponseEntity<Mail> getUserEmailDetail (@PathVariable String id) {
        var mail = service.getMailById(id);
        mail.setContent(new String(Base64.getDecoder().decode(mail.getContent().replaceAll("-",
                "+").replaceAll("_", "/"))));
        return new ResponseEntity<>(mail,HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/getUsersEmailsBackend")
    ResponseEntity<Integer> getUserEmailsBackend(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException,
            GeneralSecurityException {
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(service.getUserEmails(user), HttpStatus.OK);
    }

    @GetMapping("/getUsersEmails")
    ResponseEntity<List<MailWithoutDetails>> getUserEmails(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                           @RequestParam Date lastMailDate) {

        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(service.getUserMailsFromLastDate(user,lastMailDate).stream().map(MailToUndetailedMailConverter::convert).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/getUsersEmails/all")
    ResponseEntity<AllMailDto> getUserEmailsAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        User user = userServiceClient.findUserByGoogleId(token).getBody();

        if (Objects.nonNull(user))
            return new ResponseEntity<>(new AllMailDto(service.getAllUserMails(user).stream().map(MailToUndetailedMailConverter::convert).collect(Collectors.toList())),
                    HttpStatus.OK);;

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*

    (mail)-> {
                var copyMail = new Mail(mail);
//                System.out.println(copyMail.getContent());
//                System.out.println(new String(Base64.getDecoder().decode(copyMail.getContent().replaceAll("-",
//                        "+").replaceAll("_", "/"))));
                copyMail.setContent(new String(Base64.getDecoder().decode(copyMail.getContent().replaceAll("-",
                        "+").replaceAll("_", "/"))));
//                System.out.println(copyMail.getContent());
                return copyMail;
            }
    * */

    @Hidden
    @GetMapping("/getAwaitUser")
    ResponseEntity<AwaitUser> getAwaitUser() {
        return new ResponseEntity<>(userServiceClient.getFirstAwaitUser().getBody(),HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/watch/{id}")
    ResponseEntity<String> watchUser(@PathVariable String id) throws IOException, GeneralSecurityException {
        User user = userServiceClient.findUserByGoogleId(id).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(service.watch(user),HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/unWatch/{id}")
    ResponseEntity<Boolean> unWatchUser(@PathVariable String id) throws IOException, GeneralSecurityException {
        User user = userServiceClient.findUserByGoogleId(id).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(service.unWatch(user),HttpStatus.OK);
    }

    @PostMapping("/send")
    ResponseEntity<Mail> sendMail(@RequestBody SendMailDto sendMailDto,@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws MessagingException, IOException, GeneralSecurityException {
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Mail mail = service.sendMail(sendMailDto,user);
        if (Objects.isNull(mail)) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(mail,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Mail> deleteMail(@PathVariable String id,@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws GeneralSecurityException, IOException {
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Mail mail = service.deleteMailFromGmail(user,id);
        if (Objects.isNull(mail)) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(mail,HttpStatus.OK);

    }

}
