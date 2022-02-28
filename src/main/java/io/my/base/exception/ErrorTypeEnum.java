package io.my.base.exception;

import lombok.Getter;

@Getter
public enum ErrorTypeEnum {
    SERVER_ERROR(1, "서버 에러"),
    JWT_EXCEPTION(2, "JWT TOKEN 에러"),
    MAIL_EXCEPTION(3, "Mail Sender 에러"),
    NOT_JOIN_USER(101, "가입하지 않은 회원입니다."),
    WRONG_PASSWORD(102, "비밀번호가 다릅니다."),
    FAIL_TO_JOIN(103, "회원가입에 실패했습니다."),
    EXIST_BACKUP_EMAIL(104, "해당 이메일로 등록된 백업용 이메일이 있습니다."),
    NOT_EXIST_USER(105, "해당 이메일로 등록된 회원이 없습니다."),
    FAIL_TO_CHANGE_IMAGE(106, "이미지 변경에 실패했습니다."),

    ;

    private Integer code;
    private String result;

    ErrorTypeEnum(Integer code, String result) {
        this.code = code;
        this.result = result;
    }
}
