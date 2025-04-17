package ru.lot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lot.entity.Draw;
import ru.lot.enums.DrawStatus;

import java.util.List;

public interface DrawRepository extends JpaRepository<Draw, Long> {

    List<Draw> findByStatus(DrawStatus status);
}
