package testProject.membership.member.repository.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import testProject.membership.member.domain.member.MemberInfo;

@Repository
public interface MemberRepository extends JpaRepository<MemberInfo, Long> {

    Optional<MemberInfo> findById(String id);
    List<MemberInfo> findAll();

}

