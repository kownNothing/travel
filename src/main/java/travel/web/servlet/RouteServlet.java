package travel.web.servlet;

import travel.domain.PageBean;
import travel.domain.Route;
import travel.service.RouteService;
import travel.service.impl.RouteServiceImpl;
import travel.web.servlet.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service=new RouteServiceImpl();

    public void findPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String pageIndexStr=req.getParameter("pageIndex");
        String cIDStr=req.getParameter("cID");
        int cID=0;
        try{
            cID=Integer.parseInt(cIDStr);
        }catch (Exception e){
            logger.error("route分页查询，查询类型错误 cID："+cIDStr);
            writeValue("查询类型错误",resp);
            return;
        }
        int pageIndex=1;
        int pageCount=10;
        if(pageIndexStr!=null){
            pageIndex=Integer.parseInt(pageIndexStr);
        }
        PageBean<Route> page=service.findRoutePageByCID(cID,pageIndex,pageCount);

        writeValue(page,resp);
    }

    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String rIDStr=req.getParameter("rID");
        if(rIDStr==null||rIDStr.length()==0){
            logger.error("route查询路线详情错误");
        }

        int rID=Integer.parseInt(rIDStr);

    }
}
