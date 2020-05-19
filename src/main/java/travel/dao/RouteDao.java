package travel.dao;

import travel.domain.Category;
import travel.domain.Route;
import travel.domain.RouteImg;
import travel.domain.Seller;
import java.util.List;

public interface RouteDao {
    /**
     * 分页查询route
     * @param cID 分类的id
     * @param index
     * @param limit
     * @return
     */
    List<Route> findRouteLimitByCID(int cID, int index, int limit);

    int findCountByCID(int cID);

    Route findRouteByRID(int rID);

    Category findCategoryByCID(int cID);

    Seller findSellerBySID(int sID);

    List<RouteImg> findImgListByRID(int rID);
}
