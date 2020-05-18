package travel.service.impl;

import travel.dao.RouteDao;
import travel.dao.impl.RouteDaoImpl;
import travel.domain.Route;
import travel.domain.RoutePage;
import travel.service.RouteService;
import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao=new RouteDaoImpl();

    @Override
    public List<Route> findRouteLimitByCID(int cID, int pageIndex, int pageCount) {
        int totalCount=this.findRouteCountByCID(cID);
        if(totalCount==0){
            return null;
        }
        int searchIndex=pageCount*pageIndex;
        return null;
    }

    @Override
    public int findRouteCountByCID(int cID) {
        return routeDao.findCountByCID(cID);
    }

    /**
     * 根据cID分页查找route信息，并将信息封装到routePage对象中
     * @param cID cID
     * @param pageIndex 第几页
     * @param pageCount 每页的条数
     * @return
     */
    @Override
    public RoutePage findRoutePageByCID(int cID, int pageIndex, int pageCount) {
        int totalCount=this.findRouteCountByCID(cID);
        RoutePage page=new RoutePage();
        if(pageIndex<1) pageIndex=1;
        if(totalCount==0){
            page.setPageCount(0);
            page.setRouteCount(0);
            return page;
        }

        int beginIndex=0;
        int limitCount=pageCount;
        beginIndex=(pageIndex-1)*pageCount;
        int maxPage=0;
        if(totalCount%pageCount==0){//每一页都有pageCount条
            maxPage=totalCount/pageCount;
            Math.min(pageIndex, maxPage);

            page.setPageCount(maxPage);
            page.setRouteCount(pageCount);
        }else{
            maxPage=totalCount/pageCount+1;
            page.setPageCount(maxPage);
            if(pageIndex<=maxPage-1){
                page.setRouteCount(pageCount);
            }else{
                Math.min(pageIndex, maxPage);
                limitCount=totalCount%pageCount;
                page.setRouteCount(limitCount);
            }
        }
        page.setPageIndex(Math.min(pageIndex, maxPage));
        page.setRoutes(routeDao.findRouteLimitByCID(cID, beginIndex, limitCount));

        return page;
    }


}
