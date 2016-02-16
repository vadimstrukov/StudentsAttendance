package ee.ttu.vk.sa.repository;

import ee.ttu.vk.sa.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by vadimstrukov on 2/13/16.
 */
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findByName(String name);
    Teacher findByEmail(String email);
    Teacher findByEmailAndPassword(String email, String password);
}
