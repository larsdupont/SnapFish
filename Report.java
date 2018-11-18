package dk.ikas.lcd.examproject;

import android.media.Image;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Report {

    private LocalDate date;
    private LocalDateTime time;
    private String place;
    private String weather;
    private Float visibility;
    private Float temperature;
    private String species;
    private Float weight;
    private Float length;
    private Integer number;
    private String notes;
    private String remarks;
    private Image picture;

    public Report(){

    }

    public Report(LocalDate date, LocalDateTime time, String place, String weather, Float visibility, Float temperature, String species, Float weight, Float length, Integer number, String notes, String remarks, Image picture) {
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
        this.picture = picture;
    }

    public LocalDateTime getTime() {
        if(this.time == null){
            return null;
        }
        return time;
    }

    public void setTime(LocalDateTime time) {
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

    public Float getVisibility() {
        if(this.visibility == null){
            return null;
        }
        return visibility;
    }

    public void setVisibility(Float visibility) {
        this.visibility = visibility;
    }

    public Float getTemperature() {
        if(this.temperature == null){
            return null;
        }
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Float getWeight() {
        if(this.weight == null){
            return null;
        }
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getLength() {
        if(this.length == null){
            return null;
        }
        return length;
    }

    public void setLength(Float length) {
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

    public Image getPicture() {
        if(this.picture == null){
            return null;
        }
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public LocalDate getDate() {
        if(this.date == null){
            return null;
        }
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
