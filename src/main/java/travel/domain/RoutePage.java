package travel.domain;

import java.util.ArrayList;
import java.util.List;

public class RoutePage {
    private int pageIndex=0;//页数索引

    private int pageCount=0;//总共有几页

    private int routeCount=0;//当前页码的记录数

    private List<Route> routes=new ArrayList<>();//route信息

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getRouteCount() {
        return routeCount;
    }

    public void setRouteCount(int routeCount) {
        this.routeCount = routeCount;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "RoutePage{" +
                "pageIndex=" + pageIndex +
                ", pageCount=" + pageCount +
                ", routeCount=" + routeCount +
                ", routes=" + routes +
                '}';
    }
}
