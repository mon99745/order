package testProject.membership.member.dto;

import lombok.Getter;
import lombok.Setter;
import testProject.membership.member.domain.CartInfo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CartOrderDTO {
    private Long cart_num;
    private List<CartOrderDTO> cartOrderDTOList;
}
