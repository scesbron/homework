package utils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import play.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * We use a date time with according formatter here because java date formatter
 * does not handle well the timezone as specified by github
 *
 * @author sebastien
 *
 */
public class DateTimeDeserializer implements JsonDeserializer<DateTime> {

    public static final DateTimeFormatter DATE_TIME_FORMAT = ISODateTimeFormat.dateTimeNoMillis();

	@Override
	public DateTime deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
    	String date = element.getAsString();
		try {
			return DATE_TIME_FORMAT.parseDateTime(date);
		} catch (Exception e) {
			Logger.warn("Unparseable date : %s", date);
			return null;
		}
	}

}
