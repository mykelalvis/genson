package com.owlike.genson.ext.jodatime;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;

public class JodatimeRoundTripTest {
  private final Genson genson = new GensonBuilder().withBundle(new JodaTimeBundle()).create();

  @Test
  public void testDateTimeWithDefaultFormat() {
    DateTime now = DateTime.now();
    String json = genson.serialize(now);
    Assert.assertEquals(now, genson.deserialize(json, DateTime.class));
  }

  @Test
  public void testDateTimeWithCustomFormat() {
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
    Genson genson = new GensonBuilder().withBundle(new JodaTimeBundle().useDateFormatter(formatter)).create();

    DateTime date = DateTime.parse("20140101", formatter);
    String json = genson.serialize(date);
    Assert.assertEquals("\"20140101\"", json);
    Assert.assertEquals(date, genson.deserialize(json, DateTime.class));
  }

  @Test
  public void testDateTimeAsTimestamp() {
    Genson genson = new GensonBuilder().useDateAsTimestamp(true).withBundle(new JodaTimeBundle()).create();

    DateTime now = DateTime.now();
    String json = genson.serialize(now);
    Assert.assertEquals("" + now.getMillis(), json);
    Assert.assertEquals(now, genson.deserialize(json, DateTime.class));
  }

  @Test
  public void testInstantWithDefaultFormat() {
    Instant now = Instant.now();
    String json = genson.serialize(now);
    Assert.assertEquals(now, genson.deserialize(json, Instant.class));
  }
  
  @Test public void testDuration() {
    Duration duration = Duration.standardSeconds(3);
    String json = genson.serialize(duration);
    Assert.assertEquals("3000", json);
    Assert.assertEquals(duration, genson.deserialize(json, Duration.class));
  }

  @Test public void testPeriod() {
    Period period = Period.days(3);
    String json = genson.serialize(period);
    Assert.assertEquals("\"P3D\"", json);
    Assert.assertEquals(period, genson.deserialize(json, Period.class));
  }

  @Test public void testInterval() {
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM/dd");
    Genson genson = new GensonBuilder()
      .withBundle(new JodaTimeBundle().useDateFormatter(formatter))
      .create();

    Interval interval = new Interval(DateTime.parse("2014/01/01", formatter), DateTime.parse("2014/01/02", formatter));

    String json = genson.serialize(interval);
    Assert.assertEquals("{\"start\":\"2014/01/01\",\"end\":\"2014/01/02\"}", json);
    Assert.assertEquals(interval, genson.deserialize(json, Interval.class));
  }
}
