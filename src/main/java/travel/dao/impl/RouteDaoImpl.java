package travel.dao.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import travel.dao.RouteDao;
import travel.domain.Category;
import travel.domain.Route;
import travel.domain.RouteImg;
import travel.domain.Seller;
import travel.util.JDBCUtils;

import java.util.List;
import java.util.Map;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据cID进行分页查询
     * @param cID 分类的id
     * @param index
     * @param limit
     * @return
     */
    @Override
    public List<Route> findRouteLimitByCID(int cID, int index, int limit) {
        String sql="select * from tab_route where cid = ? limit ?,?";
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), cID,index,limit);
    }

    @Override
    public int findCountByCID(int cID) {
        String sql="select count(*) from tab_route where cid = ?";

        return template.queryForObject(sql,Integer.class,cID);
    }

    @Override
    public Route findRouteByRID(int rID) {
        String sql="select * from tab_route where rid = ?";
        Route route=null;
        try {
            route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rID);
        }catch (Exception e){
            e.printStackTrace();
        }
        return route;
    }

    @Override
    public Category findCategoryByCID(int cID) {
        String sql="select * from tab_category where rid = ?";
        Category category=null;
        try {
            category= template.queryForObject(sql, new BeanPropertyRowMapper<Category>(Category.class), cID);
        }catch (Exception e){
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public Seller findSellerBySID(int sID) {
        String sql="select * from tab_seller where sid = ?";
        Seller seller=null;
        try {
            seller= template.queryForObject(sql, new BeanPropertyRowMapper<Seller>(Seller.class), sID);
        }catch (Exception e){
            e.printStackTrace();
        }

        return seller;
    }

    @Override
    public List<RouteImg> findImgListByRID(int rID) {
        String sql="select * from tab_route_img where rid = ?";
        List<RouteImg> list=null;
        try{
            list= template.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class), rID);
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }


}
