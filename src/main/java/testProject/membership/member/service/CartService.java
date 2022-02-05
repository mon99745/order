package testProject.membership.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import testProject.membership.member.domain.*;
import testProject.membership.member.domain.member.MemberInfo;
import testProject.membership.member.dto.CartInfoDTO;
import testProject.membership.member.dto.CartOrderDTO;
import testProject.membership.member.dto.OrderInfoDTO;
import testProject.membership.member.exception.ProductNotFoundException;
import testProject.membership.member.repository.CartRepository;
import testProject.membership.member.repository.member.MemberRepository;
import testProject.membership.member.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {

    @Autowired
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    //장바구니 추가
    public Long addCart(CartInfoDTO infoDto, String member_id) {
        ProductInfo productInfo = productRepository.findById(infoDto.getProduct_num()).orElseThrow(() -> new ProductNotFoundException("오류: 상품 정보가 없습니다."));
        MemberInfo memberInfo = memberRepository.findById(member_id).orElseThrow(() -> new UsernameNotFoundException(member_id));

        CartInfo cartInfo = CartInfo.createCartInfo(memberInfo, productInfo, infoDto.getCart_quantity()); //장바구니 생성
        cartRepository.save(cartInfo);

        return cartInfo.getId();
    }

    //카트의 상품 주문로직
    public Long orderCartInfo(List<CartOrderDTO> cartOrderDTOList, String member_id){
        List<OrderInfoDTO> orderInfoDTOList = new ArrayList<>(); //장바구니 리스트

        for(CartOrderDTO cartOrderDTO : cartOrderDTOList){ //장바구니 항목들 정리
            CartInfo cartInfo = cartRepository.findById(cartOrderDTO.getCart_num()).orElseThrow();//고객이 담은 장바구니 항목 불러오기
            OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
            orderInfoDTO.setProduct_num(cartInfo.getProductInfo().getId()); //상품번호
            orderInfoDTO.setOrder_quantity(cartInfo.getCart_quantity()); //수량
            orderInfoDTOList.add(orderInfoDTO);
        }

        //주문 로직
        Long orderId = orderService.orders(orderInfoDTOList, member_id);

        //주문완료 후 장바구니 삭제
        for (CartOrderDTO cartOrderDTO : cartOrderDTOList){
            CartInfo cartInfo = cartRepository.findById(cartOrderDTO.getCart_num()).orElseThrow();
            cartRepository.delete(cartInfo);
        }
        return orderId;
    }
    //나의 장바구니
    public List<CartInfo> findMyCart(MemberInfo memberInfo) {
        return cartRepository.findByMemberInfo(memberInfo);
    }

    //장바구니 삭제
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }
}
