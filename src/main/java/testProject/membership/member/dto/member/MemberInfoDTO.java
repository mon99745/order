package testProject.membership.member.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberInfoDTO { //회원가입 정보

    private String id;
    private String password;
    private String auth;
    private String name;
}
