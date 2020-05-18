package travel.service.impl;

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
    @Override
    public List<Category> findAll() {
        Jedis jedis= JedisUtil.getJedis();
        Set<Tuple> categories=jedis.zrangeWithScores("category",0,-1);
        List<Category> list=null;
        if(categories==null||categories.size()==0){
            list=categoryDao.findAll();
            for(Category c:list){
                jedis.zadd("category",c.getCid(),c.getCname());
            }
        }else{
            list=new ArrayList<Category>();
            for(Tuple t:categories){
                list.add(new Category((int)t.getScore(),t.getElement()));
            }
        }
        return list;
    }
}
