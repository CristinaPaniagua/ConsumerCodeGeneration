// Auto generated
package eu.arrowhead.client.consumer;

import java.lang.Double;
import java.lang.Override;
import java.lang.String;

public class dynamic_motor_information {
  private Double rotation_speed;

  private Double electrical_current;

  public dynamic_motor_information() {
  }

  public dynamic_motor_information(Double rotation_speed, Double electrical_current) {
    this.rotation_speed = rotation_speed;
    this.electrical_current = electrical_current;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "rotation_speed=" + rotation_speed+ "electrical_current=" + electrical_current +"}";
  }

  public Double getrotation_speed() {
    return rotation_speed;
  }

  public void setrotation_speed(Double rotation_speed) {
    this.rotation_speed=rotation_speed;
  }

  public Double getelectrical_current() {
    return electrical_current;
  }

  public void setelectrical_current(Double electrical_current) {
    this.electrical_current=electrical_current;
  }
}
