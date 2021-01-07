// Auto generated
package eu.arrowhead.client.consumer;

import java.lang.Integer;
import java.lang.Override;
import java.lang.String;

public class RequestDTO0 {
  private Integer SessionId;

  private String OfferorName;

  private String RecieverName;

  private Contracts Contracts;

  public RequestDTO0() {
  }

  public RequestDTO0(Integer SessionId, String OfferorName, String RecieverName,
      Contracts Contracts) {
    this.SessionId = SessionId;
    this.OfferorName = OfferorName;
    this.RecieverName = RecieverName;
    this.Contracts = Contracts;
  }

  @Override
  public String toString() {
    return "ProviderPayload{" + "SessionId=" + SessionId+ "OfferorName=" + OfferorName+ "RecieverName=" + RecieverName+ "Contracts=" + Contracts +"}";
  }

  public Integer getSessionId() {
    return SessionId;
  }

  public void setSessionId(Integer SessionId) {
    this.SessionId=SessionId;
  }

  public String getOfferorName() {
    return OfferorName;
  }

  public void setOfferorName(String OfferorName) {
    this.OfferorName=OfferorName;
  }

  public String getRecieverName() {
    return RecieverName;
  }

  public void setRecieverName(String RecieverName) {
    this.RecieverName=RecieverName;
  }

  public Contracts getContracts() {
    return Contracts;
  }

  public void setContracts(Contracts Contracts) {
    this.Contracts=Contracts;
  }
}
