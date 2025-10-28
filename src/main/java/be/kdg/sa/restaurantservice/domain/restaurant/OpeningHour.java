package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.ActionNotPossibleException;
import lombok.Getter;
import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.time.DayOfWeek;
import java.time.LocalTime;

@ValueObject
@Getter
public class OpeningHour {
    private final DayOfWeek dayOfWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;

    public OpeningHour(DayOfWeek dayOfWeek, LocalTime openingTime, LocalTime closingTime) {
        Assert.isTrue(openingTime.isBefore(closingTime), "Start time should be before end time");

        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public boolean isOpenAt(LocalTime time) {
        return !time.isBefore(openingTime) && !time.isAfter(closingTime);
    }

    public void changeOpeningTime(LocalTime time) {
        if(time.isAfter(closingTime))
            throw new ActionNotPossibleException("Opening time should be after closing time");

        this.openingTime = time;
    }
    public void changeClosingTIme(LocalTime time) {
        if(time.isBefore(closingTime))
            throw new ActionNotPossibleException("Start time should be before end time");
        this.closingTime = time;
    }
}

