package com.example.myapplication;

public class SellerReviewItem {
    private String sri_commenter_id;
    private String sri_score;
    private String sri_comment;


    public SellerReviewItem(String sri_commenter_id, String sri_score, String sri_comment) {
        this.sri_commenter_id = sri_commenter_id;
        this.sri_score = sri_score;
        this.sri_comment = sri_comment;
    }

    public void setSri_commenter_id(String owner_id) {
        this.sri_commenter_id = owner_id;
    }

    public void setSri_score(String opposit_id) {
        this.sri_score = opposit_id;
    }

    public void setSri_comment(String sri_comment) {
        this.sri_comment = sri_comment;
    }


    public String getSri_commenter_id(){
        return sri_commenter_id;
    }

    public String getSri_score() {
        return sri_score;
    }

    public String getSri_comment() {
        return sri_comment;
    }

}
