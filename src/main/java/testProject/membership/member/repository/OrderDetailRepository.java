package testProject.membership.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import testProject.membership.member.domain.member.MemberInfo;
import testProject.membership.member.domain.OrderDetailInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailInfo, Long> {

    Optional<OrderDetailInfo> findById(Long num);
    List<OrderDetailInfo> findAll();
    List<OrderDetailInfo> findByOrderInfoMemberInfo(MemberInfo memberInfo);
}
