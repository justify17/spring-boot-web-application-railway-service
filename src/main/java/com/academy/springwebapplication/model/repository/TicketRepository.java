package com.academy.springwebapplication.model.repository;

import com.academy.springwebapplication.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("SELECT t FROM Ticket t" +
            " WHERE t.departure.id = :departureId and t.carriageNumber = :carriageNumber and t.seatNumber = :seatNumber" +
            " and t.userArrivalDate > :departureDate and t.userDepartureDate < :arrivalDate")
    List<Ticket> findByTicketData(Integer departureId, Integer carriageNumber, Integer seatNumber,
                                  LocalDateTime departureDate, LocalDateTime arrivalDate);

    List<Ticket> findByUser_Username(String username);

    int countAllByDeparture_Id(Integer departureId);

    List<Ticket> findByDeparture_Id(Integer departureId);
}
