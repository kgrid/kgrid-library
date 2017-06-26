package org.uofm.ot.knowledgeObject;

import java.util.Comparator;

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

  /**
   * Default starting version is 0.1.0
   */
  public Version() {
    major = 0;
    minor = 1;
    patch = 0;
    tag = "";
  }

  public Version(int major, int minor, int patch, String tag) {
    if(major < 0 || minor < 0 || patch < 0) {
      throw new IllegalArgumentException("Major, minor and patch version numbers cannot be negative.");
    }

    this.major = major;
    this.minor = minor;
    this.patch = patch;
    this.tag = tag;
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
  }

  public Version(String version) {
    // Version string must be in the format "##.##.##" with optional alphanumeric "-tag"
    if(!version.matches("\\d+\\.\\d+\\.\\d+(?:-\\w*)?")) {
      throw new IllegalArgumentException("Invalid version string " + version);
    }
    String[] parts = version.split("[\\.-]");
    if(parts.length < 3 || parts.length > 4) {
      throw new IllegalArgumentException("Invalid version string: " + version);
    }

    this.major = Integer.getInteger(parts[0]);
    this.minor = Integer.getInteger(parts[1]);
    this.patch = Integer.getInteger(parts[2]);
    if(parts.length == 4) {
      this.tag = parts[3];
    }

  }

  public Version incMajor() {
    nextVersion = new Version(major + 1, minor, patch, tag, this);
    return nextVersion;
  }

  public Version incMinor() {
    nextVersion = new Version(major, minor + 1, patch, tag, this);
    return nextVersion;
  }

  public Version incPatch() {
    nextVersion = new Version(major, minor, patch + 1, tag, this);
    return nextVersion;
  }

  public Version setTag(String newTag) {
    nextVersion = new Version(major, minor, patch, newTag, this);
    return nextVersion;
  }

  public Version rollback() {
    return previousVersion;
  }

  public Version undoRollback() {
    return nextVersion;
  }

  public int compareTo(Version other) {
    int result = major - other.major;
    if(result == 0) {
      result = minor - other.minor;
      if(result == 0) {
        result = patch - other.patch;
      }
    }
    return result;
  }

  @Override
  public int compare(Version o1, Version o2) {
    return o1.compareTo(o2);
  }

  @Override
  public String toString() {
    return major + "." + minor + "." + patch + (tag != null && !tag.equals("") ? "-" + tag : "");
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
