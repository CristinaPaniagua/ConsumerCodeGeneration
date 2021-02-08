// Auto generated
package eu.generator.consumer;

import java.lang.Override;
import java.lang.String;

public class Value {
  private float temp;

  private String unit;

  public Value() {
  }

  public Value(float temp, String unit) {
    this.temp = temp;
    this.unit = unit;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "temp=" + temp+ ",  "+ "unit=" + unit+ ",  " +"}";
  }

  public float gettemp() {
    return temp;
  }

  public void settemp(float temp) {
    this.temp=temp;
  }

  public String getunit() {
    return unit;
  }

  public void setunit(String unit) {
    this.unit=unit;
  }
}
