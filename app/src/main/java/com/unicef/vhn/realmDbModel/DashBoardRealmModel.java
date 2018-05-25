package com.unicef.vhn.realmDbModel;

import io.realm.RealmObject;

public class DashBoardRealmModel extends RealmObject {
    private int pnhbncCount;
    private int ANTT2;
    private int ANTT1;
    private int termsCount;
    private int PNMotherRiskCount;
    private int PNMotherCount;
    private int ANMotherRiskCount;
    private int ANMothersCount;
    private int infantCount;
    private int riskMothersCount;
    private int sosCount;
    private int mothersCount;
//    private PhcDetails phcDetails;
    private String message;
    private String status;

    public int getPnhbncCount() {
        return pnhbncCount;
    }

    public void setPnhbncCount(int pnhbncCount) {
        this.pnhbncCount = pnhbncCount;
    }

    public int getANTT2() {
        return ANTT2;
    }

    public void setANTT2(int ANTT2) {
        this.ANTT2 = ANTT2;
    }

    public int getANTT1() {
        return ANTT1;
    }

    public void setANTT1(int ANTT1) {
        this.ANTT1 = ANTT1;
    }

    public int getTermsCount() {
        return termsCount;
    }

    public void setTermsCount(int termsCount) {
        this.termsCount = termsCount;
    }

    public int getPNMotherRiskCount() {
        return PNMotherRiskCount;
    }

    public void setPNMotherRiskCount(int PNMotherRiskCount) {
        this.PNMotherRiskCount = PNMotherRiskCount;
    }

    public int getPNMotherCount() {
        return PNMotherCount;
    }

    public void setPNMotherCount(int PNMotherCount) {
        this.PNMotherCount = PNMotherCount;
    }

    public int getANMotherRiskCount() {
        return ANMotherRiskCount;
    }

    public void setANMotherRiskCount(int ANMotherRiskCount) {
        this.ANMotherRiskCount = ANMotherRiskCount;
    }

    public int getANMothersCount() {
        return ANMothersCount;
    }

    public void setANMothersCount(int ANMothersCount) {
        this.ANMothersCount = ANMothersCount;
    }

    public int getInfantCount() {
        return infantCount;
    }

    public void setInfantCount(int infantCount) {
        this.infantCount = infantCount;
    }

    public int getRiskMothersCount() {
        return riskMothersCount;
    }

    public void setRiskMothersCount(int riskMothersCount) {
        this.riskMothersCount = riskMothersCount;
    }

    public int getSosCount() {
        return sosCount;
    }

    public void setSosCount(int sosCount) {
        this.sosCount = sosCount;
    }

    public int getMothersCount() {
        return mothersCount;
    }

    public void setMothersCount(int mothersCount) {
        this.mothersCount = mothersCount;
    }

   /* public PhcDetails getPhcDetails() {
        return phcDetails;
    }

    public void setPhcDetails(PhcDetails phcDetails) {
        this.phcDetails = phcDetails;
    }*/

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   /* public static class PhcDetails {
        private String vphoto;
        private String facilityName;
        private String phcName;
        private String block;
        private String District;
        private String vhnId;
        private String vhnName;

        public String getVphoto() {
            return vphoto;
        }

        public void setVphoto(String vphoto) {
            this.vphoto = vphoto;
        }

        public String getFacilityName() {
            return facilityName;
        }

        public void setFacilityName(String facilityName) {
            this.facilityName = facilityName;
        }

        public String getPhcName() {
            return phcName;
        }

        public void setPhcName(String phcName) {
            this.phcName = phcName;
        }

        public String getBlock() {
            return block;
        }

        public void setBlock(String block) {
            this.block = block;
        }

        public String getDistrict() {
            return District;
        }

        public void setDistrict(String District) {
            this.District = District;
        }

        public String getVhnId() {
            return vhnId;
        }

        public void setVhnId(String vhnId) {
            this.vhnId = vhnId;
        }

        public String getVhnName() {
            return vhnName;
        }

        public void setVhnName(String vhnName) {
            this.vhnName = vhnName;
        }
    }*/

    private String vphoto;
    private String facilityName;
    private String phcName;
    private String block;
    private String District;
    private String vhnId;
    private String vhnName;

    public String getVphoto() {
        return vphoto;
    }

    public void setVphoto(String vphoto) {
        this.vphoto = vphoto;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getPhcName() {
        return phcName;
    }

    public void setPhcName(String phcName) {
        this.phcName = phcName;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String District) {
        this.District = District;
    }

    public String getVhnId() {
        return vhnId;
    }

    public void setVhnId(String vhnId) {
        this.vhnId = vhnId;
    }

    public String getVhnName() {
        return vhnName;
    }

    public void setVhnName(String vhnName) {
        this.vhnName = vhnName;
    }

}
