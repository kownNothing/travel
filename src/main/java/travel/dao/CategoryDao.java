package travel.dao;

import travel.domain.Category;

import java.util.List;

public interface CategoryDao {

    /**
     * ²éÑ¯ËùÓĞ
     * @return
     */
    List<Category> findAll();

}
