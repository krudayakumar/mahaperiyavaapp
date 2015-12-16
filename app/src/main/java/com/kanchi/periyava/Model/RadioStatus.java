package com.kanchi.periyava.Model;

import com.google.gson.annotations.SerializedName;

public class RadioStatus {

  @SerializedName("collaborators")
  public String[] collaborators;
  @SerializedName("source")
  public Source source;
  @SerializedName("status")
  public String status;
  @SerializedName("current_track")
  public CurrentTrack current_track;


  @Override
  public String toString() {
    return "ClassPojo [collaborators = " + collaborators + ", source = " + source + ", status = " + status + ", current_track = " + current_track + "]";
  }

  public class Source {
    @SerializedName("collaborator")
    public String collaborator;
    @SerializedName("type")
    public String type;

    @Override
    public String toString() {
      return "ClassPojo [collaborator = " + collaborator + ", type = " + type + "]";
    }
  }

  public class CurrentTrack {
    @SerializedName("title")
    public String title;
    @SerializedName("start_time")
    public String start_time;
    @SerializedName("artwork_url")
    public String artwork_url;

    @Override
    public String toString() {
      return "ClassPojo [title = " + title + ", start_time = " + start_time + ", artwork_url = " + artwork_url + "]";
    }
  }
}
