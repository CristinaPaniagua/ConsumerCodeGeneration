// Auto generated
package eu.arrowhead.client.consumer;

import java.lang.Double;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;

public class pressure_information {
  private Double value;

  private Long time_stamp;

  private String physical_unit;

  public pressure_information() {
  }

  public pressure_information(Double value, Long time_stamp, String physical_unit) {
    this.value = value;
    this.time_stamp = time_stamp;
    this.physical_unit = physical_unit;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "value=" + value+ "time_stamp=" + time_stamp+ "physical_unit=" + physical_unit +"}";
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

  public String getphysical_unit() {
    return physical_unit;
  }

  public void setphysical_unit(String physical_unit) {
    this.physical_unit=physical_unit;
  }
}
