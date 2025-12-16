 # renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.7.6
FROM hmctspublic.azurecr.io/base/java:21-distroless

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/hmi-apim-infrastructures.jar /opt/app/

USER 65532:65532

EXPOSE 5544
CMD [ "hmi-apim-infrastructures.jar" ]
