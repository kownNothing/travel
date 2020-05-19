package travel.service.impl;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import travel.dao.CategoryDao;
import travel.dao.impl.CategoryDaoImpl;
import travel.domain.Category;
import travel.service.CategoryService;
import travel.util.JedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao=new CategoryDaoImpl();
    private Logger logger=Logger.getLogger(CategoryServiceImpl.class);

    @Override
    public List<Category> findAll() {
        //从redis中查询,并按照数据库中存储的顺序作为查询的展示顺序
        Jedis jedis=JedisUtil.getJedis();
        //使用sortedSet查询,需要将score也一起查询出来
//        Set<String> categorys = jedis.zrange("category",0,-1);
        Set<Tuple> categorys=jedis.zrangeWithScores("category",0,-1);
        List<Category> cs=null;
        //如果为null
        if(categorys==null||categorys.size()==0){
            logger.info("CategoryService.findAll---------from database");
            cs=categoryDao.findAll();
            for(Category c:cs){
                jedis.zadd("category",c.getCid(),c.getCname());
            }
        }else{
            //将set的数据存入list
            logger.info("CategoryService.findAll---------from redis");
            cs=new ArrayList<Category>();
            for(Tuple c:categorys){
                cs.add(new Category((int)c.getScore(),c.getElement()));
            }
        }
        return cs;
    }
}
