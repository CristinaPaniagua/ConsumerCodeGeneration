// Auto generated
package ai.aitia.demo.car_provider.controller;

import java.lang.Override;
import java.lang.String;
import java.util.List;

public class Contracts {
  private String TemplatedID;

  private List<Arguments> Arguments;

  public Contracts() {
  }

  public Contracts(String TemplatedID, List<Arguments> Arguments) {
    this.TemplatedID = TemplatedID;
    this.Arguments = Arguments;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "TemplatedID=" + TemplatedID+ "Arguments=" + Arguments +"}";
  }

  public String getTemplatedID() {
    return TemplatedID;
  }

  public void setTemplatedID(String TemplatedID) {
    this.TemplatedID=TemplatedID;
  }

  public List<Arguments> getArguments() {
    return Arguments;
  }

  public void setArguments(List<Arguments> Arguments) {
    this.Arguments=Arguments;
  }
}
