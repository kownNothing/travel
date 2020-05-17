package travel.web.servlet.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    public Logger logger=Logger.getLogger(BaseServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.service(req, resp);
//        System.out.println("========baseServlet");

        String URI=req.getRequestURI();
        String methodName=URI.substring(URI.lastIndexOf('/')+1);

        logger.info("baseServlet方法调用"+methodName+"  对象"+this);
        try {
            Method method=this.getClass().getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
            method.invoke(this,req,resp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列号为json并写回到resp
     * @param obj
     */
    public void writeValue(Object obj,HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(writeValueAsString(obj));
    }

    public String writeValueAsString(Object obj) throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

}
