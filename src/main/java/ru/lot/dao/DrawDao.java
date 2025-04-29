package ru.lot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lot.entity.Draw;
import ru.lot.enums.DrawStatus;

import java.util.List;

public interface DrawDao extends JpaRepository<Draw, Long> {

    List<Draw> findByStatus(DrawStatus status);
}
