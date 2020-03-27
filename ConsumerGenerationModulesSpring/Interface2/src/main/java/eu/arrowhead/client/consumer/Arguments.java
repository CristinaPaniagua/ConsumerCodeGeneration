// Auto generated
package eu.arrowhead.client.consumer;

import java.lang.Override;
import java.lang.String;

public class Arguments {
  private String name;

  private String value;

  public Arguments() {
  }

  public Arguments(String name, String value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "name=" + name+ "value=" + value +"}";
  }

  public String getname() {
    return name;
  }

  public void setname(String name) {
    this.name=name;
  }

  public String getvalue() {
    return value;
  }

  public void setvalue(String value) {
    this.value=value;
  }
}
