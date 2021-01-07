// Auto generated
package eu.arrowhead.client.consumer;

import java.lang.Override;
import java.lang.String;

public class ResponseDTO3 {
  private dynamic_motor_information dynamic_motor_information;

  public ResponseDTO3() {
  }

  public ResponseDTO3(dynamic_motor_information dynamic_motor_information) {
    this.dynamic_motor_information = dynamic_motor_information;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "dynamic_motor_information=" + dynamic_motor_information +"}";
  }

  public dynamic_motor_information getdynamic_motor_information() {
    return dynamic_motor_information;
  }

  public void setdynamic_motor_information(dynamic_motor_information dynamic_motor_information) {
    this.dynamic_motor_information=dynamic_motor_information;
  }
}
