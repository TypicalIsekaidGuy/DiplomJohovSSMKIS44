package silmex.apps.airdropcryptopoints.data.networkdata.dto;

import com.google.gson.annotations.SerializedName;
public class ConfigDTO {

        @SerializedName("unityID")
        public String unityID;

        @SerializedName("unityBlock")
        public String unityBlock;

        @SerializedName("partnerImageURL")
        public String partnerImageURL;

        @SerializedName("partnerURL")
        public String partnerURL;

        @SerializedName("partnerName")
        public String partnerName;

        @SerializedName("maxValue")
        public double maxValue;

        @SerializedName("minValue")
        public double minValue;

        @SerializedName("referalBonusToUser")
        public int referalBonusToUser;

        @SerializedName("referalBonusForOthers")
        public int referalBonusForOthers;

        @SerializedName("convertValueToOneUsdt")
        public int convertValueToOneUsdt;

        @SerializedName("widthdrawalDelay")
        public long widthdrawalDelay;

        @SerializedName("learningText")
        public String learningText;

        @SerializedName("refferalText1")
        public String refferalText1;

        @SerializedName("refferealText2")
        public String refferealText2;

}
