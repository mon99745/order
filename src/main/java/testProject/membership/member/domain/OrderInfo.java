package testProject.membership.member.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import testProject.membership.member.domain.member.MemberInfo;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/*
Spring Data JPA
식별자를 직접 할당하여 관리
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 파라미터가 없는 생성자를 생성
@Entity
@javax.persistence.Table(name="order_info")
@Getter
@Setter
//주문 그 자체, 예: 주문1에 개밥*1 개옷*2 이라면 이들을 포함할 주문1에 해당
public class OrderInfo {

    public enum OrderStatus{
        ORDER, CANCEL
    }
    @Id // 직접할당
    @javax.persistence.Id //Prime Key
    @Column(name = "order_num", unique = true) //Entity Key?
    @GeneratedValue(strategy= GenerationType.IDENTITY)// DB에 위임을 통해 기본 키 생성
    private Long id;

    @ManyToOne
    @JoinColumn(name="id")
    private MemberInfo memberInfo;

    //주문번호 삭제시 주문상세까지 삭제 orphanRemoval
    @OneToMany(mappedBy = "orderInfo", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<OrderDetailInfo> orderDetails = new ArrayList<>();

    private LocalDateTime reg_time;

    private LocalDateTime update_time;

    @Enumerated(EnumType.STRING)
    private OrderStatus order_status;




    @Builder
    public OrderInfo(MemberInfo memberInfo, List<OrderDetailInfo> orderDetails, LocalDateTime reg_time, LocalDateTime update_time, OrderStatus order_status) {

        this.memberInfo = memberInfo;
        this.orderDetails = orderDetails; //order_detail
        this.reg_time = reg_time;
        this.update_time=update_time;
        this.order_status = order_status;
    }

    //주문에 주문상세 주입. 예: 주문번호1에 들어갈 주문상세 개밥*1을 주입하는 과정
    public void addOrderDetailInfo(OrderDetailInfo orderDetailInfo){
        orderDetails.add(orderDetailInfo);
        orderDetailInfo.setOrderInfo(this);
    }

    //첫 주문시 주문생성
    public static OrderInfo createOrderInfo(MemberInfo memberInfo, List<OrderDetailInfo> orderDetails){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setMemberInfo(memberInfo); //멤버 정보 set

        for(OrderDetailInfo orderDetailInfo : orderDetails){ //주문 상세 리스트 주입
            orderInfo.addOrderDetailInfo(orderDetailInfo);
        }

        orderInfo.setOrder_status(OrderStatus.ORDER); //주문상태를 ORDER로 set
        orderInfo.setReg_time(LocalDateTime.now()); //주문시간
        return orderInfo; //완성된 주문정보
    }

    public int getTotalPrice(){
        int totalPrice = 0;

        for(OrderDetailInfo orderDetailInfo : orderDetails){
            totalPrice += orderDetailInfo.getTotalPrice();
        }
        return totalPrice;
    }

    //주문 취소
    public void cancelOrder(){
        this.order_status = OrderStatus.CANCEL; //주문 상태를 CANCEL로

        for(OrderDetailInfo orderDetailInfo : orderDetails){ //주문 취소, 재고 원상복구
            orderDetailInfo.cancel();
        }
    }
}
