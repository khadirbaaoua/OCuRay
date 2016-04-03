package com.atlastic.ocuray.text;

import java.util.Date;
import java.util.List;

/**
 * Created by khadirbaaoua on 03/04/2016.
 */
public class Document {
    /**
     * object that contain all "interesting" data for a ticket/document/whatever
     */
    private Date date;
    private List<TVA> tvaList;
    private List<TTC> ttcList;
    private List<Total> totalList;
    private String category;
    private String etablissement;
    private String address;

    public Document() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<TVA> getTvaList() {
        return tvaList;
    }

    public void setTvaList(List<TVA> tvaList) {
        this.tvaList = tvaList;
    }

    public List<TTC> getTtcList() {
        return ttcList;
    }

    public void setTtcList(List<TTC> ttcList) {
        this.ttcList = ttcList;
    }

    public List<Total> getTotalList() {
        return totalList;
    }

    public void setTotalList(List<Total> totalList) {
        this.totalList = totalList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
