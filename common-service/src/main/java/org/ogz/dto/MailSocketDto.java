package org.ogz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailSocketDto {
    private String title;
    private Date sendDate;
    private String mailId;

    private String toUser;
}
