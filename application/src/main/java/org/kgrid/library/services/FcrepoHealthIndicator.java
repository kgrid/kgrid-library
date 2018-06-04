package org.kgrid.library.services;

import org.kgrid.library.exception.LibraryException;
import org.kgrid.library.fedoraGateway.FCRepoService;
import org.kgrid.library.fusekiGateway.FusekiService;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class FcrepoHealthIndicator extends AbstractHealthIndicator {

    private static final Logger log = Logger.getLogger(FcrepoHealthIndicator.class);

    FedoraConfiguration fedoraConfiguration;

    FCRepoService fcrepoService;

    FusekiService fusekiService;

    public FcrepoHealthIndicator(FCRepoService fcRepoService, FedoraConfiguration fedoraConfiguration, FusekiService fusekiService) {
        this.fcrepoService = fcRepoService;
        this.fedoraConfiguration = fedoraConfiguration;
        this.fusekiService = fusekiService;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        builder
                .withDetail("library.fusekiUrl", fedoraConfiguration.getFusekiServerConfiguration().getUrl())
                .withDetail("library.fedoraUrl", fedoraConfiguration.getFedoraServerConfiguration().getUrl())
                .up();

        try {
            fcrepoService.ping();
        } catch (Exception e){
            builder.down(e);
        }

        try {
            Document page = getHomePage();
            builder.withDetail("fcrepo.info", getVersionInfo(page));
        } catch (IOException e) {
            builder.withDetail("fcrepo.info", e.getMessage());
        }

        builder.withDetail("Unique Ark Ids in Fuseki", fusekiService.countUniqueArkIds());
        log.info(builder.build());
    }

    private Properties getVersionInfo(Document page) {
        Properties versionInfo = new Properties();

        versionInfo.setProperty("release", page.select(".navbar .navbar-text").text()); // old format
        versionInfo.setProperty("version", page.select("#version").text());
        versionInfo.setProperty("build", page.select("#build").text());
        versionInfo.setProperty("timestamp", page.select("#timestamp").text());

        return versionInfo;
    }

    private Document getHomePage() throws LibraryException, IOException {

        // fetch the base page and extract the release info, use fcrepo service
        // to configure the request and authorization
        HttpGet request = new HttpGet(fcrepoService.getBaseURI().resolve(".."));
        Header authHeader = fcrepoService.authenticate(request);
        request.addHeader(authHeader);
        HttpClient httpClient = HttpClientBuilder.create().build();

        String content = httpClient.execute(request, new <String>BasicResponseHandler());

        return Jsoup.parse(content);
    }
}