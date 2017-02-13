package com.talebase.cloud.os.examer.vo;

/**
 * @author erid.du
 * @date
 */
public class ExcelEntity {
    public String projectName;
    public String productName;
    public String account ;
    public String name;
    public String type;
    public String stem;
    public String index;
    public String[] answer;
    public double[]  score;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getAnswer() {
        return answer;
    }

    public void setAnswer(String[] answer) {
        this.answer = answer;
    }

    public double[] getScore() {
        return score;
    }

    public void setScore(double[] score) {
        this.score = score;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public ExcelEntity(String projectName, String productName, String account, String name, String type, String stem, String[] answer, double[] score) {
        this.projectName = projectName;
        this.productName = productName;
        this.account = account;
        this.name = name;
        this.type = type;
        this.stem = stem;
        this.answer = answer;
        this.score = score;
        this.index ="";
    }
}
