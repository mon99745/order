package testProject.membership.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import testProject.membership.member.domain.CartInfo;
import testProject.membership.member.domain.member.MemberInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartInfo, Long> {

    Optional<CartInfo> findById(Long num);
    List<CartInfo> findAll();
    List<CartInfo> findByMemberInfo(MemberInfo memberInfo);
    void deleteById(Long id);
}
