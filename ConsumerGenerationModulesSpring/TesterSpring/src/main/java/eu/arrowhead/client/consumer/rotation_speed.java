// Auto generated
package eu.arrowhead.client.consumer;

import java.lang.Double;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;

public class rotation_speed {
  private Double value;

  private Long time_stamp;

  private String description;

  public rotation_speed() {
  }

  public rotation_speed(Double value, Long time_stamp, String description) {
    this.value = value;
    this.time_stamp = time_stamp;
    this.description = description;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "value=" + value+ "time_stamp=" + time_stamp+ "description=" + description +"}";
  }

  public Double getvalue() {
    return value;
  }

  public void setvalue(Double value) {
    this.value=value;
  }

  public Long gettime_stamp() {
    return time_stamp;
  }

  public void settime_stamp(Long time_stamp) {
    this.time_stamp=time_stamp;
  }

  public String getdescription() {
    return description;
  }

  public void setdescription(String description) {
    this.description=description;
  }
}
