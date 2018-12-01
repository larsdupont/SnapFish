package dk.ikas.lcd.examproject;

import android.net.Uri;

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
            return null;
        }
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Double getVisibility() {
        if(this.visibility == null){
            return null;
        }
        return visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    public Double getTemperature() {
        if(this.temperature == null){
            return null;
        }
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Double getWeight() {
        if(this.weight == null){
            return null;
        }
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getLength() {
        if(this.length == null){
            return null;
        }
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getNumber() {
        if(this.number == null){
            return null;
        }
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Uri getUri() {
        if(this.uri == null){
            return null;
        }
        return uri;
    }

    public void setUri(Uri picture) {
        this.uri = picture;
    }

    public String getDate() {
        if(this.date == null){
            return null;
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
