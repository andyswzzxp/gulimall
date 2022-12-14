package com.atguigu.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 1、整合MyBatis-Plus
 *      1）、导入依赖
 *      <dependency>
 *             <groupId>com.baomidou</groupId>
 *             <artifactId>mybatis-plus-boot-starter</artifactId>
 *             <version>3.2.0</version>
 *      </dependency>
 *      2）、配置
 *          1、配置数据源；
 *              1）、导入数据库的驱动。https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-versions.html
 *              2）、在application.yml配置数据源相关信息
 *          2、配置MyBatis-Plus；
 *              1）、使用@MapperScan
 *              2）、告诉MyBatis-Plus，sql映射文件位置
 *
 * 2、逻辑删除
 *  1）、配置全局的逻辑删除规则（省略）
 *  2）、配置逻辑删除的组件Bean（省略）
 *  3）、给Bean加上逻辑删除注解@TableLogic
 *
 * 3、JSR303
 *   1）、给Bean添加校验注解:javax.validation.constraints，并定义自己的message提示
 *   2)、开启校验功能@Valid
 *      效果：校验错误以后会有默认的响应；
 *   3）、给校验的bean后紧跟一个BindingResult，就可以获取到校验的结果
 *   4）、分组校验（多场景的复杂校验）
 *         1)、	@NotBlank(message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
 *          给校验注解标注什么情况需要进行校验
 *         2）、@Validated({AddGroup.class})
 *         3)、默认没有指定分组的校验注解@NotBlank，在分组校验情况@Validated({AddGroup.class})下不生效，只会在@Validated生效；
 *
 *   5）、自定义校验
 *      1）、编写一个自定义的校验注解
 *      2）、编写一个自定义的校验器 ConstraintValidator
 *      3）、关联自定义的校验器和自定义的校验注解
         *      @Documented
         * @Constraint(validatedBy = { ListValueConstraintValidator.class【可以指定多个不同的校验器，适配不同类型的校验】 })
         * @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
         * @Retention(RUNTIME)
         * public @interface ListValue {
 *
 * 4、统一的异常处理
 * @ControllerAdvice
 *  1）、编写异常处理类，使用@ControllerAdvice。
 *  2）、使用@ExceptionHandler标注方法可以处理的异常。
 *
 *
 *  6.整合redis
 *  1）引入data-redis-starter
 *  2)简单配置redis的host等信息
 *  3）适用Springboot自动配置好的StringRedisTemplate来操作redis
 *  redis->map
 */

/**
 * 8.springcache简化缓存开发
 * 1）、引入依赖 <artifactId>spring-boot-starter-cache</artifactId>
 * 2）、配置
 * （1）自动配置了哪些
 * cacheautoconfiguration会导入RedisCacheConfiguration
 * 自动配好了缓存管理器RedisCacheManager
 * （2）配置使用redis作为缓存
 * 3）测试使用缓存
 * @cacheable触发将数据保存到缓存的操作
 * @cacheEvict触发将数据从缓存删除的操作
 * @CachePut不影响方法执行更新缓存
 * @Caching组合以上多个操作
 * @CacheConfig在类别共享缓存的相同草错
 * 1）开启缓存功能@EnableCaching
 *2)只需要使用注解
 * 4)CacheAutoConfiguration->RedisCacheConfiguration->
 * 自动配置了RedisCacheManager-》初始化所有缓存-》每个缓存决定使用什么配置
 * -》如果redisCacheConfiguration
 * -》
 */

@EnableRedisHttpSession
@EnableCaching
@EnableFeignClients(basePackages = "com.atguigu.gulimall.product.feign")
@EnableDiscoveryClient
@MapperScan("com.atguigu.gulimall.product.dao")
@SpringBootApplication
public class GulimallProductApplication {

    public static void main(String[] args) {
        System.out.print("dierci");
        System.out.print("disanci5");
        System.out.print("mastertest");
        System.out.print("disanci5-hotfixtest");
        System.out.print("push-testhahaha");
        System.out.print("gitee");
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
