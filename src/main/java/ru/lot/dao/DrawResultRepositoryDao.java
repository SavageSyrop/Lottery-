package ru.lot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lot.entity.DrawResult;

public interface DrawResultRepositoryDao extends JpaRepository<DrawResult, Long> {
    DrawResult findByDrawId(Long drawId);
}