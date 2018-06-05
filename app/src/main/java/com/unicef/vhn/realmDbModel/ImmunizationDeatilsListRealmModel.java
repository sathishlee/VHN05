package com.unicef.vhn.realmDbModel;

import java.util.List;

import io.realm.RealmObject;

public class ImmunizationDeatilsListRealmModel extends RealmObject {

        private String immCarePovidedDate;
        private String immDueDate;
        private String immIpvStatus;
        private String immRotaStatus;
        private String immPentanvalentStatus;
        private String immOpvStatus;
        private String immActualDate;
        private String immDoseNumber;
        private String immDoseId;
        private String immId;
        private String deleveryDate;
        private String mPicmeId;
        private String mName;
        private String mid;

        public String getImmCarePovidedDate() {
            return immCarePovidedDate;
        }

        public void setImmCarePovidedDate(String immCarePovidedDate) {
            this.immCarePovidedDate = immCarePovidedDate;
        }

        public String getImmDueDate() {
            return immDueDate;
        }

        public void setImmDueDate(String immDueDate) {
            this.immDueDate = immDueDate;
        }

        public String getImmIpvStatus() {
            return immIpvStatus;
        }

        public void setImmIpvStatus(String immIpvStatus) {
            this.immIpvStatus = immIpvStatus;
        }

        public String getImmRotaStatus() {
            return immRotaStatus;
        }

        public void setImmRotaStatus(String immRotaStatus) {
            this.immRotaStatus = immRotaStatus;
        }

        public String getImmPentanvalentStatus() {
            return immPentanvalentStatus;
        }

        public void setImmPentanvalentStatus(String immPentanvalentStatus) {
            this.immPentanvalentStatus = immPentanvalentStatus;
        }

        public String getImmOpvStatus() {
            return immOpvStatus;
        }

        public void setImmOpvStatus(String immOpvStatus) {
            this.immOpvStatus = immOpvStatus;
        }

        public String getImmActualDate() {
            return immActualDate;
        }

        public void setImmActualDate(String immActualDate) {
            this.immActualDate = immActualDate;
        }

        public String getImmDoseNumber() {
            return immDoseNumber;
        }

        public void setImmDoseNumber(String immDoseNumber) {
            this.immDoseNumber = immDoseNumber;
        }

        public String getImmDoseId() {
            return immDoseId;
        }

        public void setImmDoseId(String immDoseId) {
            this.immDoseId = immDoseId;
        }

        public String getImmId() {
            return immId;
        }

        public void setImmId(String immId) {
            this.immId = immId;
        }

        public String getDeleveryDate() {
            return deleveryDate;
        }

        public void setDeleveryDate(String deleveryDate) {
            this.deleveryDate = deleveryDate;
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

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

}
