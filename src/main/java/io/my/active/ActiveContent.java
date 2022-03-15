package io.my.active;

import lombok.Getter;

@Getter
public enum ActiveContent {
    ADDED_FRIEND("{}님을 친구로 추가하였습니다."),
    BE_ADDED_FRIEND("{}님이 회원님을 친구로 추가하였습니다.")
    ;

    private String content;

    ActiveContent(String content) {
        this.content = content;
    }
}
