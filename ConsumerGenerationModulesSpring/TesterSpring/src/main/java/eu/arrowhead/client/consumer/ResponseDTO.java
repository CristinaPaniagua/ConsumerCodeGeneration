// Auto generated
package eu.arrowhead.client.consumer;

import java.lang.Override;
import java.lang.String;

public class ResponseDTO {
  private String manufacturer;

  public ResponseDTO() {
  }

  public ResponseDTO(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "manufacturer=" + manufacturer +"}";
  }

  public String getmanufacturer() {
    return manufacturer;
  }

  public void setmanufacturer(String manufacturer) {
    this.manufacturer=manufacturer;
  }
}
