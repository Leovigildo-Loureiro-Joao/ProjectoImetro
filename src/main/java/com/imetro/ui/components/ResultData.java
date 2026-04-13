package com.imetro.ui.components;
import java.time.LocalDate;

public  class ResultData {
    public String title;
    public String score;
    public LocalDate date;
    public String type;
    public String variation;
    
   public ResultData(String title, String score, LocalDate date, String type, String variation) {
        this.title = title;
        this.score = score;
        this.date = date;
        this.type = type;
        this.variation = variation;
    }
}