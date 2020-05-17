package travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import travel.define.ErrorCode;
import travel.domain.ResultInfo;
import travel.domain.User;
import travel.service.UserService;
import travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=====玩家尝试登录");
        String userCheckCode=(String)req.getParameter("check");
        String serverCheckCode=(String)req.getSession().getAttribute("CHECKCODE_SERVER");
        req.getSession().removeAttribute("CHECKCODE_SERVER");//用完就直接销毁验证码，以防止验证码可重用
        ResultInfo resultInfo=new ResultInfo();
        ObjectMapper mapper=new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");

        if(userCheckCode==null||!userCheckCode.equalsIgnoreCase(serverCheckCode)){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
            System.out.println("验证码信息"+userCheckCode+"     "+serverCheckCode);
            String json=mapper.writeValueAsString(resultInfo);
            resp.getWriter().write(json);
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
        UserService userService=new UserServiceImpl();
        int returnCode=userService.login(tryLoginUser);
        if(returnCode==1){
            tryLoginUser.setPassword(null);
            req.getSession().setAttribute("user",tryLoginUser);

            resultInfo.setFlag(true);
            System.out.println("用户登录成功");
            String json=mapper.writeValueAsString(resultInfo);
            resp.getWriter().write(json);
        }else if(returnCode== ErrorCode.Fail_Login){
            System.out.println("帐号或密码错误，登录失败");
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登录失败，账号或密码输入错误");
            String json=mapper.writeValueAsString(resultInfo);
            resp.getWriter().write(json);
        }else if(returnCode==ErrorCode.Account_Not_Active){
            System.out.println("账号尚未激活");
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登录失败，账号尚未激活");
            String json=mapper.writeValueAsString(resultInfo);
            resp.getWriter().write(json);
        }
    }
}
