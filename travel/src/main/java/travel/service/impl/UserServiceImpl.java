package travel.service.impl;


import travel.dao.UserDao;
import travel.dao.impl.UserDaoImpl;
import travel.define.ErroCode;
import travel.domain.User;
import travel.service.UserService;
import travel.util.MailUtils;
import travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao=new UserDaoImpl();
    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        User u=userDao.findUserByUsername(user.getUsername());
        if(u!=null){//用户名存在
            return false;
        }
        user.setCode(UuidUtil.getUuid());
        user.setStatus("N");
        userDao.saveUser(user);

        String content="<a href='http://localhost:/travel/activeUserServlet?code="+user.getCode()+"'>点击激活旅游网账户</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活");
        return true;
    }

    @Override
    public int active(String code) {
        User user=userDao.findUserByCode(code);
        if(user!=null){
            userDao.updateStatus(user);
            if("y".equalsIgnoreCase(user.getStatus()))
                return ErroCode.Repeat_active;
            return 1;
        }
        return ErroCode.Fail_active;
    }
}
