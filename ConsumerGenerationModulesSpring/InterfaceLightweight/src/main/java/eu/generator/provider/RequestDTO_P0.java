// Auto generated
package eu.generator.provider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.Override;
import java.lang.String;

@JsonIgnoreProperties(
    ignoreUnknown = true
)
public class RequestDTO_P0 {
  private String n;

  private int v;

  private String u;

  private double t;

  public RequestDTO_P0() {
  }

  public RequestDTO_P0(String n, int v, String u, double t) {
    this.n = n;
    this.v = v;
    this.u = u;
    this.t = t;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "n=" + n+ ",  "+ "v=" + v+ ",  "+ "u=" + u+ ",  "+ "t=" + t+ ",  " +"}";
  }

  public String getn() {
    return n;
  }

  public void setn(String n) {
    this.n=n;
  }

  public int getv() {
    return v;
  }

  public void setv(int v) {
    this.v=v;
  }

  public String getu() {
    return u;
  }

  public void setu(String u) {
    this.u=u;
  }

  public double gett() {
    return t;
  }

  public void sett(double t) {
    this.t=t;
  }
}
