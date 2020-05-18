package travel.dao;

import travel.domain.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findAll();

}
