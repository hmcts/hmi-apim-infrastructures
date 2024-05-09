 # renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.5.1
FROM hmctspublic.azurecr.io/base/java:21-distroless

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/hmi-apim-infrastructures.jar /opt/app/

EXPOSE 5544
CMD [ "hmi-apim-infrastructures.jar" ]
