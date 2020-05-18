package travel.web.servlet;

import travel.domain.RoutePage;
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
            writeValueAsJson("查询类型错误",resp);
            return;
        }
        int pageIndex=1;
        int pageCount=10;
        if(pageIndexStr!=null){
            pageIndex=Integer.parseInt(pageIndexStr);
        }
        RoutePage page=service.findRoutePageByCID(cID,pageIndex,pageCount);

        writeValueAsJson(page,resp);
    }
}
