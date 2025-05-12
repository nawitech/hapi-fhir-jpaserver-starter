package ca.uhn.fhir.jpa.starter;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.storage.interceptor.balp.IBalpAuditContextServices;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.jetbrains.annotations.NotNull;

public class BalpAuditContextService implements IBalpAuditContextServices {

  private static final String BEARER_PREFIX = "Bearer ";
  private static final String CLAIM_NAME = "name";
  private static final String CLAIM_PREFERRED_NAME = "preferred_username";
  private static final String CLAIM_SUBJECT = "sub";

  @Override
  public @NotNull Reference getAgentClientWho(RequestDetails requestDetails) {

    return new Reference()
        .setType("Device")
        .setDisplay("FHIR Info Gateway")
        .setIdentifier(
            new Identifier()
                .setSystem("http://hapi-fhir-server/devices")
                .setValue("hapi-fhir-server-001"));
  }

  @Override
  public @NotNull Reference getAgentUserWho(RequestDetails requestDetails) {

    String username = "demoguy";
    String name = "Demo Guy";

    return new Reference()
        .setReference("Practitioner/1")
        .setType("Practitioner")
        .setDisplay(name)
        .setIdentifier(
            new Identifier()
                .setSystem("http://hapi-fhir-server/practitioners")
                .setValue(username));
  }

  @Override
  public @NotNull String massageResourceIdForStorage(
      @Nonnull RequestDetails theRequestDetails,
      @Nonnull IBaseResource theResource,
      @Nonnull IIdType theResourceId) {

    /**
     * Server not configured to allow external references resulting to InvalidRequestException: HTTP
     * 400 : HAPI-0507: Resource contains external reference to URL. Here we should use relative
     * references instead e.g. Patient/123;
     */
    // String serverBaseUrl = theRequestDetails.getFhirServerBase();
    return theRequestDetails.getId() != null
        ? theRequestDetails.getId().getValue()
        : ""; // For entity POST there will be no agent.who entry reference since not generated yet
  }
}
