package com.atguigu.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.client.utils.StringUtils;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bohui.common.utils.PageUtils;
import com.bohui.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    // private Map<String,Object> cache=new HashMap<>();
//    @Autowired
//    CategoryDao categoryDao;


    @Autowired
    RedissonClient redissonClient;

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树形结构

        //2.1）、找到所有的一级分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());


        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO  1、检查当前删除的菜单，是否被别的地方引用

        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    //[2,25,225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);


        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     *CacheEvict失效模式
     * 1.同时进行多种缓存操作@Caching()
     * 2.删除某个分区下的所有数据 @CacheEvict(value = "category",allEntries = true)
     * 3.存储同一类型的数据，都可以指定成同一个分区,分区名就是缓存的前缀
     * @param category
     */


//    @Caching(evict = {
//            @CacheEvict(value = "category",key = "'getLevel1Categorys'"),
//            @CacheEvict(value = "category",key = "'getCateLogJson'")
//    })
    @CacheEvict(value = "category",allEntries = true)
    //@CachePut//双写模式
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        //修改缓存中的数据
        //删掉缓存中的数据redis.del("catalogJson");等待下次主动查询，进行更新

        //双写模式：脏数据 加锁 满足最终一致性，看能不能容忍延迟
        //失效模式：写完了删

        //过期时间+读写锁
        //1.缓存的所有数据都有过期时间，过期下一次查询促发主动更新
        //2.读写数据的时候，加上分布式的多写锁，经常写，经常读


        //springcache
    }

    //1.每一个需要缓存的数c据我们都来指定要放到那个名字的缓存。【缓存的分区(按照业务类型分)】
    //
    //2。@Cacheable代表当前方法的结果需要缓存，如果缓存中有，方法不用调用。
    // 如果缓存中没有，会调用方法，最后将方法的结果放入缓存
    //3.默认行为
    //1).如果缓存中有，方法不能调用
    //2).key默认自动生成:缓存的名字：category::SimpleKey[]
    //3)Value的值：默认使用json序列化机制。将序列化后的数据存到redis
    //4默认ttl时间 -1
    //自定义：
    //1）。指定生成的缓存使用key：key属性zhiding spel表达式
    //2）。指定缓存的数据的存活时间 :配置文件中修改ttl
    //3）。将数据保存为json格式
    /**
     * 4spring-cache的不足
     * 1.读mo
     */

    /**
     * AutoConfigruation
     * @return
     */

    //    @Cacheable({"category"})
//    @Cacheable(value = {"category"},key = "'level1Categorys'")
    @Cacheable(value = {"category"},key = "#root.method.name")
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        System.out.println("getLevel1Categorys...");
        long l = System.currentTimeMillis();
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        //System.out.println("消耗时间："+(System.currentTimeMillis()-l));
        return categoryEntities;
    }


    @Cacheable(value = "category",key = "#root.method.name")
    @Override
    public Map<String, List<Catelog2Vo>> getCateLogJson()
    {
        List<CategoryEntity> selectList = baseMapper.selectList(null);


        //1.查出所有1级别分类
        // List <CategoryEntity> level1Categorys = getLevel1Categorys();
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        //2.封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // List <CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper <CategoryEntity>().eq("parent_cid", v.getCatId()));
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1.找当前二级分类的三级分类

                    //  List <CategoryEntity> level3category = baseMapper.selectList(new QueryWrapper <CategoryEntity>().eq("parent_cid", l2.getCatId()));
                    List<CategoryEntity> level3category = getParent_cid(selectList, l2.getCatId());
                    if (level3category != null) {
                        List<Catelog2Vo.Catelog3Vo> collect = level3category.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());

                        catelog2Vo.setCatalog3List(collect);

                    }

                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));

        return parent_cid;
    }

    //堆外内存溢出-redis:lettuce的bug.没指定，netty默认用xmx
    //@Override
    public Map<String, List<Catelog2Vo>> getCateLogJson2() {
        //给缓存中放json字符串，拿出json字符串，还用逆转位能用的对象类型；序列号与反序列化

        /**
         * 1.空结果缓存：解决缓存穿透
         * 2.设置过期时间（加随机值）：解决缓存雪崩
         * 3.加锁：解决缓存击穿
         */

        //1.加入缓存逻辑，缓存中存的数据是json字符串
        //json跨语言，跨平台兼容
        String catelogJson = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catelogJson)) {
            //2.缓存种没有，查询数据库
            System.out.println("缓存不命中....将要查询数据库..");
            Map<String, List<Catelog2Vo>> cateLogJsonFromDb = getCateLogJsonFromDbWithRedisLock();

            return cateLogJsonFromDb;

        }
        System.out.println("缓存命中....直接返回..");
        //转换为我们指定的对象
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catelogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return result;
    }

    //    @Override
    public Map<String, List<Catelog2Vo>> getCateLogJsonFromDb() {
//        //1/如果缓存种有就用缓存的
//        Map<String, List<Catelog2Vo>> catalogJson = (Map<String, List<Catelog2Vo>>) cache.get("catalogJson");
//        if(cache.get("catalogJson")==null)
//        {
//
//        }
//        return  catalogJson;
/**
 *1.将数据库的多次查询变位一次
 */
        //只要是同一把锁，就能锁住需要这个锁的所有线程 juc
        //1.synchronized(this),springboot所有组件，在容器中都是单例的
        //todo本地锁：synchronized(this)，juc（lock），在分布式情况下，想要锁住所有，必须使用分布式锁
        synchronized (this)//当前实例对象，适合一个springboot，只能锁住当前进程，多个要用分布式锁
        {


            //得到锁以后，再去缓存中确定一次，如果没有才需要继续查询
            String catelogJson = stringRedisTemplate.opsForValue().get("catalogJSON");
            if (!StringUtils.isEmpty(catelogJson)) {
                //缓存不为null直接返回
                Map<String, List<Catelog2Vo>> result = JSON.parseObject(catelogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
                });
                return result;
            }
            System.out.println("查询了数据库......");


            List<CategoryEntity> selectList = baseMapper.selectList(null);
            //1.查出所有1级别分类
            List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
            //2.封装数据
            Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());

                List<Catelog2Vo> catelog2Vos = null;
                if (categoryEntities != null) {
                    catelog2Vos = categoryEntities.stream().map(l2 -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                        //1.找当前二级分类的三级分类

                        List<CategoryEntity> level3category = getParent_cid(selectList, l2.getCatId());
                        if (level3category != null) {
                            List<Catelog2Vo.Catelog3Vo> collect = level3category.stream().map(l3 -> {
                                Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                return catelog3Vo;
                            }).collect(Collectors.toList());

                            catelog2Vo.setCatalog3List(collect);

                        }

                        return catelog2Vo;
                    }).collect(Collectors.toList());
                }

                return catelog2Vos;
            }));
            // cache.put("catalogJson",parent_cid);

            //3.查到的数据再放入缓存,将对象转为json放再缓存中
            String s = JSON.toJSONString(parent_cid);
            stringRedisTemplate.opsForValue().set("catalogJSON", s, 1, TimeUnit.DAYS);
            return parent_cid;

        }

    }

    //缓存里的数据如何和数据库保持一致
    //缓存数据一致性
    public Map<String, List<Catelog2Vo>> getCateLogJsonFromDbWithRedissonLock() {

        //1.锁的名字。锁的粒度，越细越快 
        //锁的粒度：具体缓存的是某个数据，11-号商品：product-11-lock product-12-lock product-lock
        RLock lock = redissonClient.getLock("CateLogJson-lock");
        lock.lock();

        Map<String, List<Catelog2Vo>> dataFromDb;
        try {
            dataFromDb = getDataFromDb();//导致死锁容易，应设置过期时间
        } finally {
            lock.unlock();
        }


        return dataFromDb;


    }


    public Map<String, List<Catelog2Vo>> getCateLogJsonFromDbWithRedisLock() {

        //1.占分布式锁。去redis占坑
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);

        if (lock) {
            System.out.println("获取分布式锁成功");
            //加锁成功。。。执行业务
            //2.设置过期时间
            //stringRedisTemplate.expire("lock",30,TimeUnit.SECONDS);
            Map<String, List<Catelog2Vo>> dataFromDb;
            try {
                dataFromDb = getDataFromDb();//导致死锁容易，应设置过期时间
            } finally {
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then \n" +
                        "   return redis.call('del',KEYS[1]) \n" +
                        "else\n" +
                        "   return 0\n" +
                        "end;";
                Long lock1 = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }

            //  stringRedisTemplate.delete("lock");//删除锁

            //原子操作  lua脚本解锁
//            String lockValue=stringRedisTemplate.opsForValue().get("lock");
//            if(uuid.equals(lockValue))
//            {
//                //删除自己的锁
//                stringRedisTemplate.delete("lock");//删除锁
//            }

            return dataFromDb;
        } else {
            System.out.println("获取分布式锁不成功，等待重试");
            try {
                Thread.sleep(200);
            } catch (Exception e) {

            }

            //加锁失败。。重试
            //休眠100ms重试
            return getCateLogJsonFromDbWithRedisLock();//自旋的方式
        }


    }


    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        String catelogJson = stringRedisTemplate.opsForValue().get("catelogJson");
        if (!StringUtils.isEmpty(catelogJson)) {

            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catelogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return result;
        }
        System.out.println("查询了数据库......getDataFromDb");

        List<CategoryEntity> selectList = baseMapper.selectList(null);


        //1.查出所有1级别分类
        // List <CategoryEntity> level1Categorys = getLevel1Categorys();
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        //2.封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // List <CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper <CategoryEntity>().eq("parent_cid", v.getCatId()));
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1.找当前二级分类的三级分类

                    //  List <CategoryEntity> level3category = baseMapper.selectList(new QueryWrapper <CategoryEntity>().eq("parent_cid", l2.getCatId()));
                    List<CategoryEntity> level3category = getParent_cid(selectList, l2.getCatId());
                    if (level3category != null) {
                        List<Catelog2Vo.Catelog3Vo> collect = level3category.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());

                        catelog2Vo.setCatalog3List(collect);

                    }

                    return catelog2Vo;
                }).collect(Collectors.toList());
            }

            return catelog2Vos;
        }));

        String s = JSON.toJSONString(parent_cid);
        stringRedisTemplate.opsForValue().set("catelogJson", s, 1, TimeUnit.DAYS);
        return parent_cid;
    }


    public Map<String, List<Catelog2Vo>> getCateLogJsonFromDbWithLocalLock() {
//        //1/如果缓存种有就用缓存的
//        Map<String, List<Catelog2Vo>> catalogJson = (Map<String, List<Catelog2Vo>>) cache.get("catalogJson");
//        if(cache.get("catalogJson")==null)
//        {
//
//        }
//        return  catalogJson;
/**
 *1.将数据库的多次查询变位一次
 */
        //只要是同一把锁，就能锁住需要这个锁的所有线程 juc
        //1.synchronized(this),springboot所有组件，在容器中都是单例的
        //todo本地锁：synchronized(this)，juc（lock），在分布式情况下，想要锁住所有，必须使用分布式锁
        synchronized (this)//当前实例对象，适合一个springboot，只能锁住当前进程，多个要用分布式锁
        {


            //得到锁以后，再去缓存中确定一次，如果没有才需要继续查询
            String catelogJson = stringRedisTemplate.opsForValue().get("catalogJSON");
            if (!StringUtils.isEmpty(catelogJson)) {
                //缓存不为null直接返回
                Map<String, List<Catelog2Vo>> result = JSON.parseObject(catelogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
                });
                return result;
            }
            System.out.println("查询了数据库......");


            List<CategoryEntity> selectList = baseMapper.selectList(null);
            //1.查出所有1级别分类
            List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
            //2.封装数据
            Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());

                List<Catelog2Vo> catelog2Vos = null;
                if (categoryEntities != null) {
                    catelog2Vos = categoryEntities.stream().map(l2 -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                        //1.找当前二级分类的三级分类

                        List<CategoryEntity> level3category = getParent_cid(selectList, l2.getCatId());
                        if (level3category != null) {
                            List<Catelog2Vo.Catelog3Vo> collect = level3category.stream().map(l3 -> {
                                Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                return catelog3Vo;
                            }).collect(Collectors.toList());

                            catelog2Vo.setCatalog3List(collect);

                        }

                        return catelog2Vo;
                    }).collect(Collectors.toList());
                }

                return catelog2Vos;
            }));
            // cache.put("catalogJson",parent_cid);

            //3.查到的数据再放入缓存,将对象转为json放再缓存中
            String s = JSON.toJSONString(parent_cid);
            stringRedisTemplate.opsForValue().set("catalogJSON", s, 1, TimeUnit.DAYS);
            return parent_cid;

        }

    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
        return collect;

        //return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", l2.getCatId()));
    }

    //225,25,2
    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1、收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;

    }


    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //1、找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //2、菜单的排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }


}