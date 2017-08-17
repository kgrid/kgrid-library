package edu.umich.lhs.library.services;

import edu.umich.lhs.library.exception.LibraryException;
import edu.umich.lhs.library.fedoraGateway.FCRepoService;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class FcrepoHealthIndicator extends AbstractHealthIndicator {

    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    FedoraConfiguration fedoraConfiguration;

    @Autowired
    FCRepoService fcrepoService;

    @Value("${library.name:Library}")
    String libraryName;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        builder
                .withDetail("library.name", libraryName)
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