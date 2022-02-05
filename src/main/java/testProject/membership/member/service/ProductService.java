package testProject.membership.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testProject.membership.member.domain.ProductInfo;
import testProject.membership.member.dto.ProductInfoDTO;
import testProject.membership.member.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public Long save(ProductInfoDTO infoDto) {

        return productRepository.save(ProductInfo.builder()
                .product_category(infoDto.getProduct_category())
                .product_name(infoDto.getProduct_name())
                .product_price(infoDto.getProduct_price())
                .product_stock(infoDto.getProduct_stock())
                .product_detail(infoDto.getProduct_detail())
                .product_date(infoDto.getProduct_date()).build()).getId();
    }
    //일반 상품 조회
    public Optional<ProductInfo> findById(Long num){ //Long or String??
        return productRepository.findById(num);
    }
    //전체 상품 조회
    public List<ProductInfo> findAll(){return productRepository.findAll();}

    public int updateById(Long num, String name, int price){
            return productRepository.updateById(num, name, price);
    }
}
