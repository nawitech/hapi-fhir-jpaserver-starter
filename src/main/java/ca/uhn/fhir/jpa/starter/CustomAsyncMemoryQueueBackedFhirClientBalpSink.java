package ca.uhn.fhir.jpa.starter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.storage.interceptor.balp.AsyncMemoryQueueBackedFhirClientBalpSink;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AuditEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomAsyncMemoryQueueBackedFhirClientBalpSink extends AsyncMemoryQueueBackedFhirClientBalpSink {
	public CustomAsyncMemoryQueueBackedFhirClientBalpSink(@NotNull FhirContext theFhirContext, @NotNull String theTargetBaseUrl) {
		super(theFhirContext, theTargetBaseUrl);
	}

	public CustomAsyncMemoryQueueBackedFhirClientBalpSink(@NotNull FhirContext theFhirContext, @NotNull String theTargetBaseUrl, @Nullable List<Object> theClientInterceptors) {
		super(theFhirContext, theTargetBaseUrl, theClientInterceptors);
	}

	public CustomAsyncMemoryQueueBackedFhirClientBalpSink(IGenericClient theClient) {
		super(theClient);
	}

	@Override
	protected void recordAuditEvent(IBaseResource theAuditEvent) {
		//Fixes recursive AuditEvent creation triggered by creating a new AuditEvent
		if(!"transaction".equals(((AuditEvent)theAuditEvent).getSubtype().get(0).getCode()))
				super.recordAuditEvent(theAuditEvent);
	}
}
