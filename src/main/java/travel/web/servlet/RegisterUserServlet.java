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

@WebServlet("/registerUserServlet")
public class RegisterUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        String user_check=req.getParameter("check");
        String server_checkCose=(String)req.getSession().getAttribute("CHECKCODE_SERVER");
        req.getSession().removeAttribute("CHECKCODE_SERVER");//用完就直接销毁验证码，以防止验证码可重用
        ResultInfo info=new ResultInfo();
        if(user_check==null||!user_check.equalsIgnoreCase(server_checkCose)){
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            ObjectMapper mapper=new ObjectMapper();
            String json=mapper.writeValueAsString(info);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write(json);
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

        UserService service=new UserServiceImpl();
        boolean flag=service.register(user);


        if(flag){
            info.setFlag(true);
            System.out.println("====注册成功");
        }else{
            info.setFlag(false);
            info.setErrorMsg("注册失败");
            System.out.println("====注册失败");
        }

        //序列号为jason
        ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(info);
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);
    }
}
