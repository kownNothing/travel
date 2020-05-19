package travel.service;

import travel.domain.PageBean;
import travel.domain.Route;

import java.util.List;

public interface RouteService {
    List<Route> findRouteLimitByCID(int cID, int pageIndex, int pageCount);

    int findRouteCountByCID(int cID);

    PageBean<Route> findRoutePageByCID(int cID, int pageIndex, int pageCount);

    Route findRouteByRID(int rID);
}
