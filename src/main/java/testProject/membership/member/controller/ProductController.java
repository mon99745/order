package testProject.membership.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import testProject.membership.member.domain.ProductInfo;
import testProject.membership.member.dto.ProductInfoDTO;
import testProject.membership.member.service.ProductService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ProductController {

    @Autowired
    private final ProductService productService;

    //return "/경로"; = html 문서 경로
    //ruturn "redirect: /값"; = 맵핑 액션 이름

    @GetMapping("/products") //상품 목록
    public String productList(Model model){
        List<ProductInfo> products = productService.findAll(); //전체 상품 목록 불러오기
        model.addAttribute("products", products); //뷰로 전송
        return "product/productList";
    }

   @PostMapping("/productUploader") //상품 추가
    public String productUpload(ProductInfoDTO infoDto) {
        productService.save(infoDto);
        return "redirect:/products";
    }

    @GetMapping("/productDetail") //상품 상세 페이지
    public String getProductDetail(@RequestParam("SelectedItemNo") Long num, Model model){ //주소창에서의 ?파라미터 num에 따른 상품 id 엑세스
        Optional<ProductInfo> product = productService.findById(num); //상품 id를 통해 상품 상세 정보 가져오기
        model.addAttribute("product", product); //상품 정보 뷰로 전달
        return "product/productPage";
    }
}
