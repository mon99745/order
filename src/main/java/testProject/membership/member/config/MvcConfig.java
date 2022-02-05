package testProject.membership.member.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // 요청 - 뷰 연결
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("main");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/admin").setViewName("admin/admin");
        registry.addViewController("/signup").setViewName("signup");
        registry.addViewController("/memberInfoModify").setViewName("memberInfoModify");
        registry.addViewController("/updatePassword").setViewName("updatePassword");

        //상품

        registry.addViewController("/orderList").setViewName("admin/orderList");
        registry.addViewController("/productList").setViewName("product/productList");
        registry.addViewController("/productUpload").setViewName("admin/productUpload");
        //registry.addViewController("/order").setViewName("order/order");
    }
}