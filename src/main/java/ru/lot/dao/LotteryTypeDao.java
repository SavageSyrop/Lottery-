package ru.lot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lot.entity.LotteryType;
import ru.lot.enums.LotteryName;

import java.util.Optional;

public interface LotteryTypeDao extends JpaRepository<LotteryType, Long> {
    Optional<LotteryType> findByName(LotteryName name);
}
