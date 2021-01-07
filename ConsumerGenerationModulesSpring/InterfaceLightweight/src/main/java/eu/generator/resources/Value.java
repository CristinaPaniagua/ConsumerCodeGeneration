// Auto generated
package eu.generator.resources;

import java.lang.Integer;
import java.lang.Override;
import java.lang.String;

public class Value {
  private Integer temp;

  private String unit;

  public Value() {
  }

  public Value(Integer temp, String unit) {
    this.temp = temp;
    this.unit = unit;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "temp=" + temp+ ",  "+ "unit=" + unit+ ",  " +"}";
  }

  public Integer gettemp() {
    return temp;
  }

  public void settemp(Integer temp) {
    this.temp=temp;
  }

  public String getunit() {
    return unit;
  }

  public void setunit(String unit) {
    this.unit=unit;
  }
}
