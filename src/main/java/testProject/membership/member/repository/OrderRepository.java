package testProject.membership.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import testProject.membership.member.domain.OrderInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderInfo, Long> {

    Optional<OrderInfo> findById(Long num);
    List<OrderInfo> findAll();
}
