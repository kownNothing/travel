package travel.web.servlet;

import travel.domain.Category;
import travel.service.CategoryService;
import travel.service.impl.CategoryServiceImpl;
import travel.web.servlet.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    private CategoryService service=new CategoryServiceImpl();

    public void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        List<Category> categories=service.findAll();
        writeValueAsJson(categories,resp);
    }
}
