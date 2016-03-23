package ee.ttu.vk.attendance.repository;

import ee.ttu.vk.attendance.domain.Group;
import ee.ttu.vk.attendance.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import java.util.Collection;
import java.util.List;

/**
 * Created by strukov on 3/13/16.
 */
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByGroupIn(Collection<Group> group);

    @Override
    @EntityGraph(value = "student.group", type = EntityGraph.EntityGraphType.LOAD)
    Page<Student> findAll(Pageable pageable);

    @Query("select s from Student s where s.code like concat('%',?1,'%') and s.firstname like concat('%',?2,'%') " +
            "and s.lastname like concat('%',?3,'%') and s.group.name like concat('%',?4,'%')")
    @EntityGraph(value = "student.group", type = EntityGraph.EntityGraphType.LOAD)

    Page<Student> findAll(String code, String firstname, String lastname, String group, Pageable pageable);

    Student findByCode(String code);

    @Query("select count(s) from Student s where s.code like concat('%',?1,'%') and s.firstname like concat('%',?2,'%') " +
            "and s.lastname like concat('%',?3,'%') and s.group.name like concat('%',?4,'%')")
    long count(String code, String firstname, String lastname, String group);
}
