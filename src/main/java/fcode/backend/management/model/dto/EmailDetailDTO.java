package fcode.backend.management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetailDTO {
    private String recipient;
    private String subject;
    private String msgBody;
}
