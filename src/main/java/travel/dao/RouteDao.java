package travel.dao;

import travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 分页查询route
     * @param cID 分类的id
     * @param index
     * @param limit
     * @return
     */
    List<Route> findRouteLimitByCID(int cID,int index,int limit);

    int findCountByCID(int cID);
}
