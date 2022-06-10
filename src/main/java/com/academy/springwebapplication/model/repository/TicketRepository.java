package com.academy.springwebapplication.model.repository;

import com.academy.springwebapplication.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Ticket findByDeparture_IdAndCarriageNumberAndSeatNumber(Integer departureId, Integer carriageNumber, Integer seatNumber);

    List<Ticket> findByUser_Username(String username);
}
