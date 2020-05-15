package travel.service;


import travel.domain.User;

public interface UserService {
    /**
     * 注册用户
     * @param user 注册信息
     * @return 返回成功失败
     */
    boolean register(User user);

    int active(String code);

    boolean login(User tryLoginUser);
}
