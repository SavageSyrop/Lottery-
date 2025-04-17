package ru.lot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lot.entity.DrawResult;

public interface DrawResultRepository extends JpaRepository<DrawResult, Long> {
    DrawResult findByDrawId(Long drawId);
}