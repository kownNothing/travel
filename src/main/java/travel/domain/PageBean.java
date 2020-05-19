package travel.domain;

import java.util.ArrayList;
import java.util.List;

public class PageBean<T> {
    private int totalCount;//总条数

    private int pageIndex=0;//页数索引

    private int pageCount=0;//总共有几页

    private int routeCount=0;//当前页码的记录数

    private List<T> list=new ArrayList<>();//route信息

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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "totalCount=" + totalCount +
                ", pageIndex=" + pageIndex +
                ", pageCount=" + pageCount +
                ", routeCount=" + routeCount +
                ", list=" + list +
                '}';
    }
}
