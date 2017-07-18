package org.uofm.ot.knowledgeObject;

import java.util.Comparator;
import org.uofm.ot.exception.ObjectTellerException;

/**
 * Created by nggittle on 6/20/17.
 */
public class Version implements Comparator<Version> {

  private int major;
  private int minor;
  private int patch;
  private String tag;
  private Version previousVersion;
  private Version nextVersion;
  private boolean isLatestVersion;
  private boolean isSemVer;
  private String uncompareableVersion;

  /**
   * Default starting version is 0.1.0
   */
  public Version() {
    major = 0;
    minor = 1;
    patch = 0;
    tag = "";
    isLatestVersion = true;
    isSemVer = true;
  }

  public Version(int major, int minor, int patch, String tag) {
    if(major < 0 || minor < 0 || patch < 0) {
      throw new IllegalArgumentException("Major, minor and patch version numbers cannot be negative.");
    }

    this.major = major;
    this.minor = minor;
    this.patch = patch;
    this.tag = tag;
    this.isLatestVersion = true;
    this.isSemVer = true;
  }

  public Version(int major, int minor, int patch, String tag, Version previousVersion) {
    if(major < 0 || minor < 0 || patch < 0) {
      throw new IllegalArgumentException("Major, minor and patch version numbers cannot be negative.");
    }
    this.major = major;
    this.minor = minor;
    this.patch = patch;
    this.tag = tag;
    this.previousVersion = previousVersion;
    this.isLatestVersion = true;
    this.isSemVer = true;
  }

  public Version(String version) {
    // Semantic version string must be in the format "##.##.##" or "##-##-##" with optional alphanumeric "-tag"
    if(!version.matches("(?:auto-snapshot-before-)?v?\\d+\\.\\d+\\.\\d+(?:-\\w*)?") && !version.matches("(?:auto-snapshot-before-)?v?\\d+-\\d+-\\d+(?:-\\w*)?")) {

      this.isSemVer = false;
      this.uncompareableVersion = version;

    } else {
      if(version.startsWith("v")) {
        version = version.substring(1);
      }

      String[] parts = version.split("[\\.-]");

      if(parts.length < 3 || parts.length > 4) {

        this.isSemVer = false;
        this.uncompareableVersion = version;

      } else {

        this.major = Integer.valueOf(parts[0]);
        this.minor = Integer.valueOf(parts[1]);
        this.patch = Integer.valueOf(parts[2]);
        if(parts.length == 4) {
          this.tag = parts[3];
        }
        this.isSemVer = true;
      }
    }

    this.isLatestVersion = true;
  }

  private void incrementableCheck() {
    if(!isSemVer) {
      throw new IllegalStateException("Cannot increment version that does not implement semantic versioning");
    }
  }

  public Version incMajor() {
    incrementableCheck();
    nextVersion = new Version(major + 1, minor, patch, tag, this);
    isLatestVersion = false;
    return nextVersion;

  }

  public Version incMinor() {
    incrementableCheck();
    nextVersion = new Version(major, minor + 1, patch, tag, this);
    isLatestVersion = false;
    return nextVersion;
  }

  public Version incPatch() {
    incrementableCheck();
    nextVersion = new Version(major, minor, patch + 1, tag, this);
    isLatestVersion = false;
    return nextVersion;
  }

  public Version setTag(String newTag) {
    incrementableCheck();
    nextVersion = new Version(major, minor, patch, newTag, this);
    isLatestVersion = false;
    return nextVersion;
  }

  public Version rollback() {
    isLatestVersion = false;
    return previousVersion;
  }

  public Version undoRollback() {
    isLatestVersion = false;
    return nextVersion;
  }

  public void setLatestVersion(boolean isLatestVersion) {
    this.isLatestVersion = isLatestVersion;
  }

  public boolean checkIfLatestVersion() {
    return isLatestVersion;
  }

  public boolean usesSemVer() {
    return isSemVer;
  }

  public int compareTo(Version other) {
    if(!isSemVer || !other.isSemVer ) {
      throw new IllegalArgumentException("Cannot compare versions that do not implement semantic versioning.");
    }
    int result = major - other.major;
    if(result == 0) {
      result = minor - other.minor;
      if(result == 0) {
        result = patch - other.patch;
      }
    }
    return result;
  }

  public String getFedoraVersion(){
    if(isSemVer) {
      return major + "-" + minor + "-" + patch + (tag != null && !tag.equals("") ? "-" + tag : "");
    } else {
      return uncompareableVersion;
    }
  }

  @Override
  public int compare(Version o1, Version o2) {
    return o1.compareTo(o2);
  }

  @Override
  public String toString() {
    if (isSemVer || uncompareableVersion == null) {
      return "v" + major + "." + minor + "." + patch + (tag != null && !tag.equals("") ? "-" + tag : "");
    } else {
      return uncompareableVersion;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Version version = (Version) o;

    if (uncompareableVersion.equals(version.uncompareableVersion)) {
      return true;
    }
    if (major != version.major) {
      return false;
    }
    if (minor != version.minor) {
      return false;
    }
    if (patch != version.patch) {
      return false;
    }
    return tag != null ? tag.equals(version.tag) : version.tag == null;
  }

  @Override
  public int hashCode() {
    int result = major;
    result = 31 * result + minor;
    result = 31 * result + patch;
    result = 31 * result + (tag != null ? tag.hashCode() : 0);
    return result;
  }

}
