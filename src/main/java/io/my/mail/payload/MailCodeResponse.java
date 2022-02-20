package io.my.mail.payload;

import io.my.base.payload.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MailCodeResponse extends BaseResponse {
    private int mailCode;

    public MailCodeResponse() {}
    public MailCodeResponse(int mailCode) {
        this.mailCode = mailCode;
    }
}
