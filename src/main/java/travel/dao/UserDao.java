package travel.dao;


import travel.domain.User;

public interface UserDao {

    User findUserByUsername(String username);

    void saveUser(User user);

    User findUserByCode(String code);

    void updateStatus(User user);

    User findUserByIDAndPassword(int uid, String password);
}
