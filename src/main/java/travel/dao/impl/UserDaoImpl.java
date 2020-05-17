package travel.dao.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import travel.dao.UserDao;
import travel.domain.User;
import travel.util.JDBCUtils;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public User findUserByUsername(String username) {
        User user=null;
        String sql = "select * from tab_user where username = ?";
        try{
            //如果查不到，beanPropertyRowMapper没有可装填的数据会抛异常
            user=template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username);
        }catch (Exception e){
            return null;
        }

        return user;
    }

    @Override
    public void saveUser(User user) {
        String sql = "insert into tab_user (username,password,name,birthday,sex,telephone,email,status,code) values " +
                "(?,?,?,?,?,?,?,?,?)";

        template.update(sql,user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }

    /**
     * 根据激活码查询用户对象
     * @param code
     * @return
     */
    @Override
    public User findUserByCode(String code) {
        String sql = "select * from tab_user where code = ?";
        User user=null;
        try{
            user=template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),code);
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 修改用户激活状态
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        String sql="update tab_user set status = 'Y' where uid = ?";
        template.update(sql,user.getUid());
    }

    @Override
    public User findUserByUsernameAndPassword(String  username, String password) {
        String sql= "select * from tab_user where username = ? and password = ?";
        User user=null;
        try{
            user=template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        User u=new User();
        u.setUsername("lisi");
        u.setPassword("12345678");
        u.setName("faiufhaw");
        u.setBirthday("2020-01-01");
        u.setTelephone("13515164578");
        u.setEmail("gfs@qq.com");

        UserDao dao=new UserDaoImpl();
        dao.saveUser(u);
    }


}
