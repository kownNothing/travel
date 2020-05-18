package travel.service;

import travel.domain.Route;
import travel.domain.RoutePage;

import java.util.List;

public interface RouteService {
    List<Route> findRouteLimitByCID(int cID,int pageIndex,int pageCount);

    int findRouteCountByCID(int cID);

    RoutePage findRoutePageByCID(int cID,int pageIndex,int pageCount);
}
