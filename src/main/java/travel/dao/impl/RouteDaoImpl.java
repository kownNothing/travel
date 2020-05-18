package travel.dao.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import travel.dao.RouteDao;
import travel.domain.Route;
import travel.util.JDBCUtils;
import java.util.List;

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
    public List<Route> findRouteLimitByCID(int cID,int index,int limit) {
        String sql="select * from tab_route where cid = ? limit ?,?";
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), cID,index,limit);
    }

    @Override
    public int findCountByCID(int cID) {
        String sql="select count(rid) from tab_route where cid = ?";
        return template.queryForInt(sql,cID);
    }


}
