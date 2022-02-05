package testProject.membership.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testProject.membership.member.domain.member.MemberInfo;
import testProject.membership.member.dto.member.MemberInfoDTO;
import testProject.membership.member.repository.member.MemberRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService implements UserDetailsService {

    @Autowired
    private final MemberRepository memberRepository;

    /**
     * Spring Security 필수 메소드 구현
     *
     * @param id
     * @return UserDetails
     * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
     */
    @Override // 기본적인 반환 타입은 UserDetails, UserDetails 를 상속받은 UserInfo 로 반환 타입 지정 (자동으로 다운 캐스팅됨)
    public MemberInfo loadUserByUsername(String id) throws UsernameNotFoundException { // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
        return memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException((id)));
    }

    public Long save(MemberInfoDTO infoDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        infoDto.setPassword(encoder.encode(infoDto.getPassword()));

        return memberRepository.save(MemberInfo.builder()
                .id(infoDto.getId())
                .auth(infoDto.getAuth())
                .password(infoDto.getPassword())
                .name(infoDto.getName()).build()).getCode();
    }
    //일반 회원조회 (회원 정보 수정) -> loadUserByUsername 사용하기
    public Optional<MemberInfo> findOne(String memberId){ //Long or String??
        return memberRepository.findById(memberId);
    }

    //전체 회원 조회
    public List<MemberInfo> findMembers(){
        return memberRepository.findAll();
    }

    public void updateMemberById(String id, String name, String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // 비밀번호 매치 로직 (였던것)
        MemberInfo memberInfo = loadUserByUsername(id);

        if(encoder.matches(password, memberInfo.getPassword())){
            memberInfo.updateById(name);
        } else {throw new IllegalStateException("비밀번호가 일치하지 않습니다.");}//https://kimcoder.tistory.com/249
    }

    public void updateMemberPassword(String id, String password, String newPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // 비밀번호 매치 로직 (였던것)
        MemberInfo memberInfo = loadUserByUsername(id);
        if(encoder.matches(password, memberInfo.getPassword())){
            newPassword = encoder.encode(newPassword);
            memberInfo.updatePassword(newPassword);//새로운 비밀번호 암호화
        } else {throw new IllegalStateException("비밀번호가 일치하지 않습니다.");}//https://kimcoder.tistory.com/249
    }
   /* public int updatePassword(String id, String password, String newPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // 비밀번호 매치 로직 (였던것)

        if(encoder.matches(password, loadUserByUsername(id).getPassword())){
            newPassword = encoder.encode(newPassword); //새로운 비밀번호 암호화
            return memberRepository.updatePassword(id, newPassword);
        } else {throw new IllegalStateException("비밀번호가 일치하지 않습니다.");}//https://kimcoder.tistory.com/249
    }*/
}