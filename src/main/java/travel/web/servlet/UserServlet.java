package travel.web.servlet;

import org.apache.commons.beanutils.BeanUtils;
import travel.define.ErrorCode;
import travel.domain.ResultInfo;
import travel.domain.User;
import travel.service.UserService;
import travel.service.impl.UserServiceImpl;
import travel.web.servlet.base.BaseServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*") //   匹配/user/add  /user/find....
public class UserServlet extends BaseServlet {
    private UserService userService=new UserServiceImpl();

    public void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user_check=req.getParameter("check");
        String server_checkCode=(String)req.getSession().getAttribute("CHECKCODE_SERVER");
        req.getSession().removeAttribute("CHECKCODE_SERVER");//用完就直接销毁验证码，以防止验证码可重用
        ResultInfo info=new ResultInfo();
        resp.setContentType("application/json;charset=utf-8");

        if(user_check==null||!user_check.equalsIgnoreCase(server_checkCode)){
            logger.info("用户注册验证码，验证码信息"+user_check+"     "+server_checkCode);
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            writeValue(info,resp);
            return;
        }
        Map<String,String[]> params=req.getParameterMap();
        User user=new User();
        try {
            BeanUtils.populate(user,params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        boolean flag=userService.register(user);
        if(flag){
            info.setFlag(true);
            logger.info("====注册成功");
        }else{
            info.setFlag(false);
            info.setErrorMsg("注册失败");
            logger.info("====注册失败");
        }

        writeValue(info,resp);
    }

    public void active(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code=req.getParameter("code");
        if(code!=null){
            int statusCode=userService.active(code);
            resp.setContentType("text/html;charset=utf-8");
            String msg=null;
            if(statusCode==1){
                msg="激活成功，请<a href='http://localhost/travel/login.html'>登录</a>";
            }else if(statusCode== ErrorCode.Repeat_active){
                msg="您已经成功激活，请<a href='http://localhost/travel/login.html'>登录</a>";
            }else {
                msg="激活失败，请联系管理员";
            }
            resp.getWriter().write(msg);
        }
    }

    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("用户登录");
        String userCheckCode=(String)req.getParameter("check");
        String serverCheckCode=(String)req.getSession().getAttribute("CHECKCODE_SERVER");
        req.getSession().removeAttribute("CHECKCODE_SERVER");//用完就直接销毁验证码，以防止验证码可重用

        ResultInfo resultInfo=new ResultInfo();
        resp.setContentType("application/json;charset=utf-8");
        logger.info("用户登录-----验证码校验 服务器验证码:"+serverCheckCode+"   玩家输入验证码:"+userCheckCode);
        if(userCheckCode==null||!userCheckCode.equalsIgnoreCase(serverCheckCode)){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
            logger.info("玩家验证码输入错误");
            writeValue(resultInfo,resp);
            return;
        }

        Map<String,String[]> parameterMap= req.getParameterMap();
        User tryLoginUser=new User();
        try {
            BeanUtils.populate(tryLoginUser,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if(tryLoginUser.getUsername()==null||tryLoginUser.getPassword()==null){
            logger.error("用户登录错误 帐号或密码为空");
            req.getRequestDispatcher("/login.html").forward(req,resp);
            return;
        }
        int returnCode=userService.login(tryLoginUser);
        if(returnCode==1){
            req.getSession().setAttribute("user",tryLoginUser);

            resultInfo.setFlag(true);
            logger.info("用户登录成功："+tryLoginUser.getUsername());
            writeValue(resultInfo,resp);
        }else if(returnCode== ErrorCode.Fail_Login){
            logger.info("帐号或密码错误，登录失败");
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登录失败，账号或密码输入错误");
            writeValue(resultInfo,resp);
        }else if(returnCode== ErrorCode.Account_Not_Active){
            logger.error("用户登录错误，账号尚未激活");
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登录失败，账号尚未激活");
            writeValue(resultInfo,resp);
        }
    }

    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            user.setPassword(null);
            writeValue(user,resp);
        } else {
            logger.info("Error，用户信息为空");
        }
    }

    public void exit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("玩家退出登录");
        req.getSession().invalidate();//销毁session
        //session销毁
        resp.sendRedirect(req.getContextPath()+"/login.html");
    }
}
