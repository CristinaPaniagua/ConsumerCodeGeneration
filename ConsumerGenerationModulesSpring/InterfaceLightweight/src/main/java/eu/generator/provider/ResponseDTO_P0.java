// Auto generated
package eu.generator.provider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.Override;
import java.lang.String;

@JsonIgnoreProperties(
    ignoreUnknown = true
)
public class ResponseDTO_P0 {
  private String response;

  public ResponseDTO_P0() {
  }

  public ResponseDTO_P0(String response) {
    this.response = response;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "response=" + response+ ",  " +"}";
  }

  public String getresponse() {
    return response;
  }

  public void setresponse(String response) {
    this.response=response;
  }
}
