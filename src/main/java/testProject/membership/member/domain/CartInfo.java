package testProject.membership.member.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import testProject.membership.member.domain.member.MemberInfo;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)// 파라미터가 없는 생성자를 생성
@Entity
@javax.persistence.Table(name="cart")
@Getter
@Setter
public class CartInfo {

    @Id // 직접할당
    @javax.persistence.Id
    @Column(name = "cart_num", unique = true) //Entity Key?
    @GeneratedValue(strategy= GenerationType.IDENTITY)// DB에 위임을 통해 기본 키 생성
    private Long id;

    @ManyToOne
    @JoinColumn(name="id")
    private MemberInfo memberInfo;

    private int cart_quantity;

    @ManyToOne
    @JoinColumn(name="product_num")
    private ProductInfo productInfo;





    //장바구니 생성
    public static CartInfo createCartInfo(MemberInfo memberInfo, ProductInfo productInfo, int cart_quantity){
        CartInfo cartInfo = new CartInfo(); //새로운 장바구니
        cartInfo.setMemberInfo(memberInfo);
        cartInfo.setProductInfo(productInfo);//담은 상품 정보
        cartInfo.setCart_quantity(cart_quantity); //장바구니에 담은 상품 개수

        return cartInfo;
    }

    //총액
    public int getTotalPrice(){
        return productInfo.getProduct_price() * cart_quantity;
    }
}