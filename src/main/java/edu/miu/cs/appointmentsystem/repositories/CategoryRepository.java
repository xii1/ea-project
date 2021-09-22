package edu.miu.cs.appointmentsystem.repositories;

import edu.miu.cs.appointmentsystem.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findCategoryByName(String name);
}
