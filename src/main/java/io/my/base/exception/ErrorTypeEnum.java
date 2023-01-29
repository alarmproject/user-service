package io.my.base.exception;

import lombok.Getter;

@Getter
public enum ErrorTypeEnum {
    SERVER_ERROR(1, "서버 에러"),
    JWT_EXCEPTION(2, "JWT TOKEN 에러"),
    MAIL_EXCEPTION(3, "Mail Sender 에러"),
    DATABASE_EXCEPTION(4, "데이터 베이스 에러"),
    NOT_JOIN_USER(101, "가입하지 않은 회원입니다."),
    WRONG_PASSWORD(102, "비밀번호가 다릅니다."),
    FAIL_TO_JOIN(103, "회원가입에 실패했습니다."),
    EXIST_BACKUP_EMAIL(104, "해당 이메일로 등록된 백업용 이메일이 있습니다."),
    FAIL_TO_CHANGE_PASSWORD(105, "비밀번호 변경에 실패했습니다."),
    FAIL_TO_CHANGE_IMAGE(106, "이미지 변경에 실패했습니다."),
    FAIL_TO_CHANGE_NICKNAME(107, "닉네임 변경에 실패했습니다."),
    NOT_FOUND_EXCEPTION(-1, "경로를 찾지 못했습니다."),
    FAIL_TO_APPLE_CONNECT(108, "애플 연동에 실패했습니다."),

    ;

    private final Integer code;
    private final String result;

    ErrorTypeEnum(Integer code, String result) {
        this.code = code;
        this.result = result;
    }
}
