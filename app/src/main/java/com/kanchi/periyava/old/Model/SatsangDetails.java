package com.kanchi.periyava.old.Model;

import android.graphics.Bitmap;

/**
 * Created by m84098 on 9/14/15.
 */
public class SatsangDetails {
  String satsangid;
  String profileid;
  String name;
  String description;
  String contactno;
  String emailid;
  String city;
  String state;
  String country;
  String zipcode;
  Bitmap image;

  public String getSatsangid() {
    return satsangid;
  }

  public void setSatsangid(String satsangid) {
    this.satsangid = satsangid;
  }

  public String getProfileid() {
    return profileid;
  }

  public void setProfileid(String profileid) {
    this.profileid = profileid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getContactno() {
    return contactno;
  }

  public void setContactno(String contactno) {
    this.contactno = contactno;
  }

  public String getEmailid() {
    return emailid;
  }

  public void setEmailid(String emailid) {
    this.emailid = emailid;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public Bitmap getImage() {
    return image;
  }

  public void setImage(Bitmap image) {
    this.image = image;
  }
}
