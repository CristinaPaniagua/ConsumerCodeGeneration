// Auto generated
package eu.arrowhead.client.consumer;

import java.lang.Override;
import java.lang.String;

public class static_motor_information {
  private String manufacturer;

  private String order_number;

  public static_motor_information() {
  }

  public static_motor_information(String manufacturer, String order_number) {
    this.manufacturer = manufacturer;
    this.order_number = order_number;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "manufacturer=" + manufacturer+ "order_number=" + order_number +"}";
  }

  public String getmanufacturer() {
    return manufacturer;
  }

  public void setmanufacturer(String manufacturer) {
    this.manufacturer=manufacturer;
  }

  public String getorder_number() {
    return order_number;
  }

  public void setorder_number(String order_number) {
    this.order_number=order_number;
  }
}
