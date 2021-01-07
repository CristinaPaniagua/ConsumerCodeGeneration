// Auto generated
package eu.arrowhead.client.consumer;

import java.lang.Override;
import java.lang.String;

public class ResponseDTO1 {
  private rotation_speed rotation_speed;

  public ResponseDTO1() {
  }

  public ResponseDTO1(rotation_speed rotation_speed) {
    this.rotation_speed = rotation_speed;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "rotation_speed=" + rotation_speed +"}";
  }

  public rotation_speed getrotation_speed() {
    return rotation_speed;
  }

  public void setrotation_speed(rotation_speed rotation_speed) {
    this.rotation_speed=rotation_speed;
  }
}
