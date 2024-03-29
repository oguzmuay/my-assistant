package com.ogz.mailassistance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ogz.model.Mail;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllMailDto {
    private List<MailWithoutDetails> mails;

}
