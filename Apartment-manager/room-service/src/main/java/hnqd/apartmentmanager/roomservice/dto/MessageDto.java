package hnqd.apartmentmanager.roomservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private String from;
    private String to;
    private String toName;
    private String subject;
    private String content;
}
