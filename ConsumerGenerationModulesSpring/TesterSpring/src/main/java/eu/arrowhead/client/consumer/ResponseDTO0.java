// Auto generated
package eu.arrowhead.client.consumer;

import java.lang.Override;
import java.lang.String;

public class ResponseDTO0 {
  private pressure_information pressure_information;

  public ResponseDTO0() {
  }

  public ResponseDTO0(pressure_information pressure_information) {
    this.pressure_information = pressure_information;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "pressure_information=" + pressure_information +"}";
  }

  public pressure_information getpressure_information() {
    return pressure_information;
  }

  public void setpressure_information(pressure_information pressure_information) {
    this.pressure_information=pressure_information;
  }
}
