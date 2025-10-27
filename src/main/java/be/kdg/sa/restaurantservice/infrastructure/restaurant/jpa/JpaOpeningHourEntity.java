package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;

import be.kdg.sa.restaurantservice.domain.restaurant.OpeningHour;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Embeddable
@Getter
public class JpaOpeningHourEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    protected JpaOpeningHourEntity() {
    }

    private JpaOpeningHourEntity(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public static JpaOpeningHourEntity fromDomain(OpeningHour openingHour) {
        return new JpaOpeningHourEntity(
                openingHour.getDayOfWeek(),
                openingHour.getOpeningTime(),
                openingHour.getClosingTime()
        );
    }
}


