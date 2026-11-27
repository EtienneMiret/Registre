package fr.elimerl.registre.security;

import java.util.Collection;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

public class ClientConfiguration {

  private String id;

  private String issuerUri;

  private String clientId;

  private String clientSecret;

  private String redirectUri;

  private Collection<String> scope;

  private String clientName;

  public ClientRegistration buildClientRegistration() {
    return ClientRegistrations.fromIssuerLocation(issuerUri)
        .registrationId(id)
        .clientId(clientId)
        .clientSecret(clientSecret)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .redirectUri(redirectUri)
        .scope(scope)
        .clientName(clientName)
        .build();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIssuerUri() {
    return issuerUri;
  }

  public void setIssuerUri(String issuerUri) {
    this.issuerUri = issuerUri;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public Collection<String> getScope() {
    return scope;
  }

  public void setScope(Collection<String> scope) {
    this.scope = scope;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

}
