package ru.lot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lot.entity.Draw;
import ru.lot.entity.DrawStatus;

import java.util.List;

@Repository
public interface DrawRepository extends JpaRepository<Draw, Long> {

    List<Draw> findByStatus(DrawStatus status);
}
