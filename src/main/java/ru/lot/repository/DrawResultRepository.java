package ru.lot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lot.entity.DrawResult;

@Repository
public interface DrawResultRepository extends JpaRepository<DrawResult, Long> {
    DrawResult findByDrawId(Long drawId);
}