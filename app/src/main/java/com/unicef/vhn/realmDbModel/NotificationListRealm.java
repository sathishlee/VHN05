package com.unicef.vhn.realmDbModel;

import io.realm.RealmObject;

public class NotificationListRealm extends RealmObject {

    private String mtype;
    private String noteStartDateTime;
    private String noteId;
    private String migratedmId;
    private String clickHeremId;
    private String deleveryDate;
    private String mMotherMobile;
    private String subject;
    private String vLongitude;
    private String vLatitude;
    private String mLongitude;
    private String mLatitude;
    private String vhnId;
    private String mid;
    private String mPicmeId;
    private String mName;
    private String mPhoto;
    private String message;
    private String noteType;

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }


    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public String getNoteStartDateTime() {
        return noteStartDateTime;
    }

    public void setNoteStartDateTime(String noteStartDateTime) {
        this.noteStartDateTime = noteStartDateTime;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getMigratedmId() {
        return migratedmId;
    }

    public void setMigratedmId(String migratedmId) {
        this.migratedmId = migratedmId;
    }

    public String getClickHeremId() {
        return clickHeremId;
    }

    public void setClickHeremId(String clickHeremId) {
        this.clickHeremId = clickHeremId;
    }

    public String getDeleveryDate() {
        return deleveryDate;
    }

    public void setDeleveryDate(String deleveryDate) {
        this.deleveryDate = deleveryDate;
    }

    public String getMMotherMobile() {
        return mMotherMobile;
    }

    public void setMMotherMobile(String mMotherMobile) {
        this.mMotherMobile = mMotherMobile;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getVLongitude() {
        return vLongitude;
    }

    public void setVLongitude(String vLongitude) {
        this.vLongitude = vLongitude;
    }

    public String getVLatitude() {
        return vLatitude;
    }

    public void setVLatitude(String vLatitude) {
        this.vLatitude = vLatitude;
    }

    public String getMLongitude() {
        return mLongitude;
    }

    public void setMLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getMLatitude() {
        return mLatitude;
    }

    public void setMLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getVhnId() {
        return vhnId;
    }

    public void setVhnId(String vhnId) {
        this.vhnId = vhnId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMPicmeId() {
        return mPicmeId;
    }

    public void setMPicmeId(String mPicmeId) {
        this.mPicmeId = mPicmeId;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

}
