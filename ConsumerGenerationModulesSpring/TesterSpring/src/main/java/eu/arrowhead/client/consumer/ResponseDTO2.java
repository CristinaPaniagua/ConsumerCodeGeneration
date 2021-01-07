// Auto generated
package eu.arrowhead.client.consumer;

import java.lang.Override;
import java.lang.String;

public class ResponseDTO2 {
  private static_motor_information static_motor_information;

  public ResponseDTO2() {
  }

  public ResponseDTO2(static_motor_information static_motor_information) {
    this.static_motor_information = static_motor_information;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "static_motor_information=" + static_motor_information +"}";
  }

  public static_motor_information getstatic_motor_information() {
    return static_motor_information;
  }

  public void setstatic_motor_information(static_motor_information static_motor_information) {
    this.static_motor_information=static_motor_information;
  }
}
