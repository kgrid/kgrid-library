package org.uofm.ot.services;

import com.complexible.common.openrdf.model.ModelIO;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openrdf.model.Model;
import org.openrdf.rio.RDFFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.uofm.ot.exception.ObjectNotFoundException;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraGateway.FCRepoService;
import org.uofm.ot.knowledgeObject.ArkId;

/**
 * Created by nggittle on 7/7/17.
 */
@Service
public class VersioningService {

  @Autowired
  private FCRepoService fcRepoService;

  public Model listVersions(URI parentURI) throws ObjectTellerException, URISyntaxException {
    parentURI = new URI(parentURI + "/fcr:versions");
    HttpGet get = new HttpGet(parentURI);
    HttpClient client = HttpClientBuilder.create().build();
    get.addHeader(fcRepoService.authenticate(get));
    get.addHeader("Accept", "application/n-triples");

    try {
      HttpResponse response = client.execute(get);
      if(response.getStatusLine().getStatusCode() == HttpStatus.NOT_FOUND.value()) {
        throw new ObjectNotFoundException("No versions found for object at " + parentURI);
      }
      Model koModel = ModelIO.read(response.getEntity().getContent(), RDFFormat.NTRIPLES);
      return koModel;
    } catch (IOException e) {
      throw new ObjectTellerException("Error retrieving version list at location " + parentURI + " :" + e, e);
    }
  }

  public void createNewVersionSnapshot(URI parentURI, String versionName) throws ObjectTellerException, URISyntaxException {
    HttpPost post = new HttpPost(new URI(parentURI + "/fcr:versions"));
    post.addHeader("Slug", versionName);
    post.addHeader(fcRepoService.authenticate(post));
    HttpClient client = HttpClientBuilder.create().build();
    try {
      HttpResponse response = client.execute(post);
      if(response.getStatusLine().getStatusCode() == HttpStatus.CONFLICT.value()) {
        throw new ObjectTellerException("Error creating new version, this version already exists!");
      }
      if (response.getStatusLine().getStatusCode() != HttpStatus.CREATED.value()) {
        if(response.getStatusLine().getStatusCode() == HttpStatus.NOT_FOUND.value()) {
          throw new ObjectNotFoundException("Could not find object at URI " + parentURI + " with version " + versionName);
        }
        throw new ObjectTellerException("Error creating new version, expected status of 201 but got " + response.getStatusLine());
      }
    } catch (IOException e) {
      throw new ObjectTellerException("Error creating new version " + e, e);
    }
  }

  public void revertToPreviousVersion(URI parentURI, String versionName) throws ObjectTellerException, URISyntaxException {
    revertToPreviousVersion(new URI(parentURI + "/fcr:versions/" + versionName));
  }

  public void revertToPreviousVersion(URI versionURI) throws ObjectTellerException {
    HttpPatch patch = new HttpPatch(versionURI);
    HttpClient client = HttpClientBuilder.create().build();
    patch.addHeader(fcRepoService.authenticate(patch));
    try {
      HttpResponse response = client.execute(patch);
      if(response.getStatusLine().getStatusCode() == HttpStatus.NOT_FOUND.value()) {
        throw new ObjectNotFoundException("Error reverting to version at " + versionURI + " Version does not exist.");
      } else if (response.getStatusLine().getStatusCode() != HttpStatus.NO_CONTENT.value()) {
        throw new ObjectNotFoundException("Error reverting to version at " + versionURI + " Got error code " +
            response.getStatusLine().getStatusCode());
      }
    } catch (IOException e) {
      throw new ObjectTellerException("Error reverting to version at " + versionURI + " :" + e, e);
    }
  }

  public void deleteVersion(URI parentURI, String versionName) throws ObjectTellerException, URISyntaxException {
    deleteVersion(new URI(parentURI + "/fcr:versions/" + versionName));
  }

  public void deleteVersion(URI versionURI) throws ObjectTellerException {
    HttpDelete delete = new HttpDelete(versionURI);
    HttpClient client = HttpClientBuilder.create().build();
    delete.addHeader(fcRepoService.authenticate(delete));

    try {
      HttpResponse response = client.execute(delete);
      if(response.getStatusLine().getStatusCode() != HttpStatus.NO_CONTENT.value()) {
        if(response.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
          throw new ObjectTellerException("Error deleting version at " + versionURI + " Cannot delete the most recent version.");
        }
        throw new ObjectNotFoundException("Error deleting version at " + versionURI + "Got error code " +
        response.getStatusLine().getStatusCode());
      }
    } catch (IOException e) {
      throw new ObjectTellerException("Error deleting version at " + versionURI + " :" + e, e);
    }
  }
}
