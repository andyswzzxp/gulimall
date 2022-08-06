package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.dao.SkuSaleAttrValueDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import com.atguigu.gulimall.product.vo.SkuItemSaleAttrVo;
import com.atguigu.gulimall.product.vo.SkuItemVo;
import com.atguigu.gulimall.product.vo.SpuItemAttrGroupVo;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.ZParams;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {

        //1.查询所有1级分类
        List<CategoryEntity> categoryEntitys = categoryService.getLevel1Categorys();
        model.addAttribute("categorys", categoryEntitys);

        //视图解析器进行拼串

        return "index";

    }

    //index/catalog.json
    //堆外内存溢出-redis
    @ResponseBody
    @GetMapping("/index/catelog.json")
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        Map<String, List<Catelog2Vo>> cateLogJson = categoryService.getCateLogJson();
        return cateLogJson;
    }


    @ResponseBody
    @GetMapping("/hello")
    public String hello(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //保存
        ops.set("hello", "world_" + UUID.randomUUID().toString());
//查询
        String hello = ops.get("hello");
        System.out.println("之前保存的数据是：" + hello);
        return "hello";
    }


    @Autowired
    RedissonClient redissonClient;

    @ResponseBody
    @GetMapping("/redssion")
    public void redssion() {
        System.out.println(redissonClient);
    }

    @Autowired
    AttrGroupDao attrGroupDao;

    @GetMapping("/testdetail")
    public String testdetail()
    {


        List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupDao.getAttrGroupWithAttrsBySpuId(3L, 225L);
       System.out.println(attrGroupWithAttrsBySpuId);
        return "1";
    }


    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @GetMapping("/testdetail2")
    public void testdetail2()
    {
        List<SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(4L);
        System.out.println(saleAttrsBySpuId);
//        List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupDao.getAttrGroupWithAttrsBySpuId(3L, 225L);
//        System.out.println(attrGroupWithAttrsBySpuId);
//        return "1";
    }

    @ResponseBody
    @GetMapping("/hello2")
    public String hello2() {
        //1.获取一把锁，只要锁名一样，就是同一把锁
        RLock lock = redissonClient.getLock("my-lock");
        //2.加锁
        //lock.lock();//阻塞式等待。默认加的锁都是30秒时间
        //1）。锁的自动续期，如果业务超长，运行期间自动给锁续上新的30s.不用担心业务时间长，锁自动过期被删掉
        //2）加锁的业务只要运行完成，就不会给当前锁续期，即使不手动解锁，锁默认在30s以后自动删除
        lock.lock(10, TimeUnit.SECONDS);//10秒自动解锁-这时没有自动续期，自动解锁时间一定要大于业务的执行时间
        //问题lock.lock(10, TimeUnit.SECONDS);//在锁时间到了 以后，不会自动续期
        //1.如果我们传递了锁的超时时间，就发送给redis执行脚本，进行占锁，默认超时就是我们指定的时间
        //2.如果我们未指定锁的超时时间，就使用30*1000lockWatchdogTimeout看门狗时间
        //只要占锁成功，就会启动一个定时任务【重新给锁设置过期时间，新的过期时间就是看门狗的默认时间】,每隔10s都会自动再次续期，续成30s
        //internallockleaseTime[看门狗时间]/3,10s
        //最佳实战
        //1)、 lock.lock(10, TimeUnit.SECONDS);省掉了整个续期操作。手动解锁

        try {
            System.out.println("加锁成功，执行业务..." + Thread.currentThread().getId());
            Thread.sleep(30000);
        } catch (Exception e) {

        } finally {
            //3.解锁
            System.out.println("释放锁..." + Thread.currentThread().getId());
            lock.unlock();
        }


        return "hello2";
    }

//保证一定能读到最新数据，修改期间，写锁是一个排他锁（互斥锁、独享锁）。读锁是一个共享锁
    //写锁没释放就读就必须等待
//读+读；相当于无锁， 并发读，只会在redis中记录好，所有当前的读锁。他们都会同时加锁成功

    //写+读；等待写锁释放
    //写+写；阻塞方式
    //读+写：有读锁。写也需要等待
    //只要有写的存在，都必须等待
    @ResponseBody
    @GetMapping("/write")
    public String write() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");

        String s = "";
        RLock rLock = lock.writeLock();
        try {
            //1.改数据加写锁，读数据加读锁

            rLock.lock();
            System.out.println("写加锁成功" + Thread.currentThread().getId());
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            stringRedisTemplate.opsForValue().set("writevalue", s);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            rLock.unlock();
            System.out.println("写锁释放" + Thread.currentThread().getId());
        }
        return s;
    }

    @GetMapping("/read")
    @ResponseBody
    public  String Read()
    {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");

        String s = "";
        //加读锁
        RLock rLock = lock.readLock();
        rLock.lock();
        System.out.println("读加锁成功" + Thread.currentThread().getId());
        try {
            //1.改数据加写锁，读数据加读锁


           // s = UUID.randomUUID().toString();
           Thread.sleep(30000);
            s=stringRedisTemplate.opsForValue().get("writevalue");

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            rLock.unlock();
            System.out.println("读锁释放" + Thread.currentThread().getId());
        }
        return s;
    }

    /**
     * 车库停车
     * 3车位
     * 信号量也可以用作分布式限流；
     */
    @GetMapping("/park")
    @ResponseBody
    public  String park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
        //park.acquire();//获取一个信号，获取一个值，占一个车位
        boolean b = park.tryAcquire();
        if(b){
            //执行业务
        }
        else
        {
            return  "error";
        }
        return "ok=>"+b;
    }

    @GetMapping("/go")
    @ResponseBody
    public  String go() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release();
        return "ok";
    }


    /**
     * 放假，锁门
     * 1班没人了，2
     * 5哥班全部走完，我们可以锁大门
     */

    @GetMapping("/lockDoor")
    @ResponseBody
    public  String lockDoor() throws InterruptedException {

        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();//等待闭锁都完成
        return  "放假了...";
    }


    @GetMapping("/gogo/{id}")
    public  String lockDoor(@PathVariable("id") Long id)
    {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();
        return  id+"班的人都走了...";
    }

}
