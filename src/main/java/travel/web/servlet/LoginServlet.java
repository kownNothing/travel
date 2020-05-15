package travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
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
        String userCheckCode=(String)req.getAttribute("check");
        String serverCheckCode=(String)req.getSession().getAttribute("CHECKCODE_SERVER");
        ResultInfo resultInfo=new ResultInfo();
        ObjectMapper mapper=new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");

        if(userCheckCode==null||!userCheckCode.equalsIgnoreCase(serverCheckCode)){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
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
        boolean flag=userService.login(tryLoginUser);
        if(flag){
            resultInfo.setFlag(true);
            System.out.println("用户登录成功 用户ID"+tryLoginUser.getUid());
        }else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登录失败，账号或密码输入错误");
            String json=mapper.writeValueAsString(resultInfo);
            resp.getWriter().write(json);
        }
    }
}
