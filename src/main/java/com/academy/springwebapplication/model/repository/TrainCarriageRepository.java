package com.academy.springwebapplication.model.repository;

import com.academy.springwebapplication.model.entity.TrainCarriage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainCarriageRepository extends JpaRepository<TrainCarriage,Integer> {
    TrainCarriage findByTrain_IdAndCarriageNumber(Integer trainId, Integer carriageNumber);
}
