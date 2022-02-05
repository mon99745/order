package testProject.membership.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import testProject.membership.member.domain.*;
import testProject.membership.member.domain.member.MemberInfo;
import testProject.membership.member.dto.OrderInfoDTO;
import testProject.membership.member.exception.ProductNotFoundException;
import testProject.membership.member.repository.*;
import testProject.membership.member.repository.member.MemberRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    //단순 주문 로직
    public Long order(OrderInfoDTO infoDto, String member_id) {
        ProductInfo productInfo = productRepository.findById(infoDto.getProduct_num()).orElseThrow(() -> new ProductNotFoundException("오류: 상품 정보가 없습니다."));
        MemberInfo memberInfo = memberRepository.findById(member_id).orElseThrow(() -> new UsernameNotFoundException(member_id));

        List<OrderDetailInfo> orderDetails = new ArrayList<>();
        OrderDetailInfo orderDetailInfo = OrderDetailInfo.createOrderDetailInfo(productInfo, infoDto.getOrder_quantity());
        orderDetails.add(orderDetailInfo);
        OrderInfo orderInfo = OrderInfo.createOrderInfo(memberInfo, orderDetails);
        orderRepository.save(orderInfo);

        orderDetailInfo.addOrderNum(orderInfo);
        orderDetailRepository.save(orderDetailInfo);

        return orderInfo.getId();
    }

    //주문 취소
    public void cancelOrder(Long order_num){
        OrderInfo orderInfo = orderRepository.findById(order_num).orElseThrow(EntityNotFoundException::new);
        orderInfo.cancelOrder();
    }

    //다량 주문 로직
    public Long orders(List<OrderInfoDTO> orderInfoDTOList, String member_id) {

        // 로그인한 유저 조회
        MemberInfo memberInfo = memberRepository.findById(member_id).orElseThrow();

        // orderInfoDTO 객체를 이용하여 item 객체와 count 값을 얻어낸 뒤, 이를 이용하여 OrderDetailInfo 객체(들) 생성
        List<OrderDetailInfo> orderDetailInfoList = new ArrayList<>();
        for (OrderInfoDTO orderInfoDTO : orderInfoDTOList) {
            ProductInfo productInfo = productRepository.findById(orderInfoDTO.getProduct_num()).orElseThrow(EntityNotFoundException::new);
            OrderDetailInfo orderDetailInfo = OrderDetailInfo.createOrderDetailInfo(productInfo, orderInfoDTO.getOrder_quantity());
            orderDetailInfoList.add(orderDetailInfo);
        }

        //OrderInfo Entity 클래스에 존재하는 createOrderInfo 메소드로 OrderInfo 생성 및 저장
        OrderInfo order = OrderInfo.createOrderInfo(memberInfo, orderDetailInfoList);
        orderRepository.save(order);
        return order.getId();
    }

    //전체 주문 조회
    public List<OrderInfo> findAll(){return orderRepository.findAll();}

    //전체 주문 상세 조회
    public List<OrderDetailInfo> findAllDetails(){return orderDetailRepository.findAll();}

    //나의 주문 조회
    public List<OrderDetailInfo> findMyDetails(MemberInfo memberInfo){
        return orderDetailRepository.findByOrderInfoMemberInfo(memberInfo);
    }

    public Optional<OrderInfo> findById(Long order_num) {return orderRepository.findById(order_num);}
}