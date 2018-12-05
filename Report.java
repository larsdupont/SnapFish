package dk.ikas.lcd.examproject;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Date;

public class Report extends ReportExtension {

    private String date;
    private String time;
    private String place;
    private String weather;
    private Double visibility;
    private Double temperature;
    private String species;
    private Double weight;
    private Double length;
    private Integer number;
    private String notes;
    private String remarks;
    private Uri uri;
    private String image;
    private Bitmap thumbnail;

    public Report(){

    }

    public Report(String date, String time, String place, String weather, Double visibility, Double temperature, String species, Double weight, Double length, Integer number, String notes, String remarks, Uri picture) {
        this.date = date;
        this.time = time;
        this.place = place;
        this.weather = weather;
        this.visibility = visibility;
        this.temperature = temperature;
        this.species = species;
        this.weight = weight;
        this.length = length;
        this.number = number;
        this.notes = notes;
        this.remarks = remarks;
        this.uri = picture;
    }

    public String getTime() {
        if(this.time == null){
            return "";
        }
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getWeather() {
        return this.weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Double getVisibility() {
        if(this.visibility == null){
            return 0.00;
        }
        return this.visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    public Double getTemperature() {
        if(this.temperature == null){
            return 0.00;
        }
        return this.temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getSpecies() {
        return this.species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Double getWeight() {
        if(this.weight == null){
            return 0.00;
        }
        return this.weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getLength() {
        if(this.length == null){
            return 0.00;
        }
        return this.length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getNumber() {
        if(this.number == null){
            return 0;
        }
        return this.number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Uri getUri() {
        if(this.uri == null){
            return null;
        }
        return this.uri;
    }

    public void setUri(Uri picture) {
        this.uri = picture;
    }

    public String getDate() {
        if(this.date == null){
            return new Date().toString();
        }
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
