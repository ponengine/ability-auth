package security.oauth.authen.config;



import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.AttributeConverter;

public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, Time>{
	@Override
    public Time convertToDatabaseColumn(LocalTime locTime) {
        return (locTime == null ? null : Time.valueOf(locTime));
    }

    @Override
    public LocalTime convertToEntityAttribute(Time sqlTime) {
        return (sqlTime == null ? null : sqlTime.toLocalTime());
    }
}