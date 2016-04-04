package com.atlastic.ocuray.text;

import java.util.*;

/**
 * Created by khadirbaaoua on 03/04/2016.
 */
public class Document {
    /**
     * object that contain all "interesting" data for a ticket/document/whatever
     */
    private Date date;
    private Map<Double, Double> tvaList = new HashMap<>();
    private Map<Double, Double> ttcList  = new HashMap<>();;
    private Map<Double, Double> totalList = new HashMap<>();;
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

    public Map<Double, Double> getTvaList() {
        return tvaList;
    }

    public Map<Double, Double> getTtcList() {
        return ttcList;
    }

    public Map<Double, Double> getTotalList() {
        return totalList;
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

    @Override
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("Document :\n")
                .append("Date = "+this.date)
                .append("\nCategory = " + this.category)
                .append("\nEtablissement = "+this.etablissement)
                .append("\nAddress = " + this.address);
        for (Map.Entry<Double, Double> tva : tvaList.entrySet()) {
            strBuf.append("\nTVA = ["+tva.getKey()+"%, "+tva.getValue()+"]");
        }
        for (Map.Entry<Double, Double> ttc : ttcList.entrySet()) {
            strBuf.append("\nTTC = ["+ttc.getKey()+"%, "+ttc.getValue()+"]");
        }
        for (Map.Entry<Double, Double> total : totalList.entrySet()) {
            strBuf.append("\nTOTAL = ["+total.getKey()+"%, "+total.getValue()+"]");
        }
        return strBuf.toString();
    }
}
