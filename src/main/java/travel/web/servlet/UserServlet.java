package travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService service=new UserServiceImpl();

    /**
     * 注册
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user_check=req.getParameter("check");
        String server_checkCose=(String)req.getSession().getAttribute("CHECKCODE_SERVER");
        req.getSession().removeAttribute("CHECKCODE_SERVER");//用完就直接销毁验证码，以防止验证码可重用
        ResultInfo info=new ResultInfo();

        if(user_check==null||!user_check.equalsIgnoreCase(server_checkCose)){
            logger.info("注册验证码错误 用户输入："+user_check+"     服务器验证码："+server_checkCose);
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            writeValueAsJson(info,resp);
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
        boolean flag=service.register(user);
        if(flag){
            info.setFlag(true);
            logger.info("用户注册成功，用户名"+user.getUsername());
        }else{
            info.setFlag(false);
            info.setErrorMsg("注册失败");
            System.out.println("用户注册失败");
        }

        writeValueAsJson(info,resp);
    }

    /**
     * 激活
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code=req.getParameter("code");
        if(code!=null){
            int statusCode=service.active(code);
            resp.setContentType("text/html;charset=utf-8");
            String msg=null;
            if(statusCode==1){
                logger.info("用户激活成功");
                msg="激活成功，请<a href='http://localhost/travel/login.html'>登录</a>";
            }else if(statusCode== ErrorCode.Repeat_active){
                msg="请勿重复注册";
            }else {
                msg="激活失败，请联系管理员";
            }
            resp.getWriter().write(msg);
        }
    }

    /**
     * 玩家登陆处理
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("用户尝试登录");
        String userCheckCode=(String)req.getParameter("check");
        String serverCheckCode=(String)req.getSession().getAttribute("CHECKCODE_SERVER");
        req.getSession().removeAttribute("CHECKCODE_SERVER");//用完就直接销毁验证码，以防止验证码可重用
        ResultInfo resultInfo=new ResultInfo();

        if(userCheckCode==null||!userCheckCode.equalsIgnoreCase(serverCheckCode)){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
            logger.info("用户登陆验证码校验失败，用户输入："+userCheckCode+"     服务器验证码"+serverCheckCode);
            writeValueAsJson(resultInfo,resp);
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
//        super.doPost(req, resp);
        int returnCode=service.login(tryLoginUser);
        if(returnCode==1){
            tryLoginUser.setPassword(null);//擦除用户密码，以免用户密码直接在json串中发送给浏览器
            req.getSession().setAttribute("user",tryLoginUser);

            resultInfo.setFlag(true);
            logger.info("用户登录成功，用户名："+tryLoginUser.getUsername());

        }else if(returnCode== ErrorCode.Fail_Login){
            System.out.println("帐号或密码错误，登录失败");
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登录失败，账号或密码输入错误");
            writeValueAsJson(resultInfo,resp);
        }else if(returnCode==ErrorCode.Account_Not_Active){
            System.out.println("账号尚未激活");
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登录失败，账号尚未激活");
            writeValueAsJson(resultInfo,resp);
        }
    }

    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user=(User)req.getSession().getAttribute("user");
        if(user!=null){
//            System.out.println();
            writeValueAsJson(user,resp);
        }else {
            logger.info("/user.findOne  Error，用户信息为空");
        }
    }

    /**
     * 用户推出登陆
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("/user.exit 用户推出登陆 用户名："+((User)req.getSession().getAttribute("user")).getUsername());
        req.getSession().invalidate();//销毁session
        //session销毁
        resp.sendRedirect(req.getContextPath()+"/login.html");
    }
}
