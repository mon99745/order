package testProject.membership.member.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import testProject.membership.member.domain.member.MemberInfo;
import testProject.membership.member.dto.member.MemberInfoDTO;
import testProject.membership.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberController {

    @Autowired
    private final MemberService memberService;

    //return "/경로"; = html 문서 경로
    //ruturn "redirect: /값"; = 맵핑 액션 이름

    /*
    /user로 Post 요청이 들어오면
    UserServicedml의 save()를 호출한 뒤에 /login으로 이동
    */
    @GetMapping("/") //메인 페이지
    public String main_info(Model model){
        MemberInfo member = (MemberInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //현재 로그인 정보 불러와 member에 저장
       // member = memberService.loadUserByUsername(member.getId()); // 이 코드 없이 돌려봤는데 잘 작동. 윗 줄에서 충분히 member 불러오기가 가능한 듯
        model.addAttribute("member", member);
        return "main";
    }
    @PostMapping("/user") //회원가입 완료 버튼 누르면
    public String signup(MemberInfoDTO infoDto) { // 회원 추가
        memberService.save(infoDto); //MemberInfoDTO의 양식에 맞는 정보가 담긴 infoDto를 memberService에서 DB에 save처리함
        return "redirect:/login"; //그리고 로그인 페이지로 이동
    }

    @PostMapping("/memberInfoMod") //회원 정보 수정 완료버튼 누르면
    public String updateById(String password, String name) { // 패스워드, 이름만 바꾼다는 가정, //DTO로 받도록 수정하기

        MemberInfo member = (MemberInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //현재 로그인한 사용자 정보 불러오기
        System.out.println(member.getId()+" "+password+" "+name); //테스트
        memberService.updateMemberById(member.getId(), name, password); //memberService에 정의한 update메소드에 name 과 password 전송

        return "redirect:/"; //
    }

    @PostMapping("/updatePW") //패스워드 재설정
    public String requestUpdatePassword(String password, String newPassword) { // 정보 수정, 비밀번호 변경 할 경우 어떻게 할 것인가

        MemberInfo member = (MemberInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //현재 멤버 정보
        System.out.println(member.getId()+" "+password+" "+newPassword); //테스트
        memberService.updateMemberPassword(member.getId(), password, newPassword); //비밀번호 변경 메소드에 사용자가 입력한 현재비밀번호와 새비밀번호 전송

        return "redirect:/logout"; //
    }

    // 로그아웃 처리 SecurityContextLogoutHandler() 사용
    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/admin/members") //전체 회원 목록
    public String memberList(Model model){
        List<MemberInfo> members = memberService.findMembers(); //멤버들 불러오기
        model.addAttribute("members", members); //멤버들 리스트를 모델로 뷰로 전송
        return "admin/memberList"; //멤버리스트 페이지로 이동
    }
}