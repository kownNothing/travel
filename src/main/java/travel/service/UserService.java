package travel.service;


import org.apache.log4j.Logger;
import travel.domain.User;

public interface UserService {
    Logger logger=Logger.getLogger(UserService.class);
    /**
     * 注册用户
     * @param user 注册信息
     * @return 返回成功失败
     */
    boolean register(User user);

    int active(String code);

    int login(User tryLoginUser);
}
