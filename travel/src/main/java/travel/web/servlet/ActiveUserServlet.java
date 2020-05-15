package travel.web.servlet;

import cn.itcast.travel.define.ErroCode;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code=req.getParameter("code");
        if(code!=null){
            UserService userService=new UserServiceImpl();
            int statusCode=userService.active(code);
            resp.setContentType("text/html;charset=utf-8");
            String msg=null;
            if(statusCode==1){
                msg="激活成功，请<a href='http://localhost/travel/login.html'>登录</a>";
            }else if(statusCode== ErroCode.Repeat_active){
                msg="请勿重复注册";
            }else {
                msg="激活失败，请联系管理员";
            }
            resp.getWriter().write(msg);
        }
    }
}
