package com.herokudevrel.agentforce.collageagent.repositories;

import com.herokudevrel.agentforce.collageagent.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.contact c " +
            "JOIN FETCH b.session s " +
            "JOIN FETCH s.experience e " +
            "WHERE c.externalId = :externalId")
    List<Booking> findBookingsByContactExternalId(@Param("externalId") String externalId);
}
