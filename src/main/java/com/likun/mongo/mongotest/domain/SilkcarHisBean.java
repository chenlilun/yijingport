package com.likun.mongo.mongotest.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
@Data
public class SilkcarHisBean {

    /**
     * eventId : 5ce0d06fe6ea200001cbcb99
     * deleted : false
     * deleteOperator : null
     * packageBox : {"id":"5ce0d06fe6ea200001cbd248"}
     * deleteDateTime : null
     * type : PackageBoxEvent
     * operator : {"id":"if_warehouse"}
     * command : {"grossWeight":11408.4,"netWeight":11160,"sapT001l":{"id":"3118"},"silkCarRecords":[{"silkCar":{"id":"5cd780d2c4d77b0001136c1d"},"id":"5cdf6510e6ea200001715041"},{"silkCar":{"id":"5cd3e748c4d77b00011260e6"},"id":"5cdf9e82e6ea2000017c294c"},{"silkCar":{"id":"5cd3e838c4d77b00011709ce"},"id":"5cdfa3e7e6ea200001cc8f82"},{"silkCar":{"id":"5cd3e748c4d77b00011260fd"},"id":"5cdffeb5e6ea200001c64f45"},{"silkCar":{"id":"5cd780d2c4d77b0001136c00"},"id":"5cdb6c46c4d77b00015696c8"},{"silkCar":{"id":"5bffa7917979c400014701b5"},"id":"5ce01b54e6ea2000018ac870"},{"silkCar":{"id":"5cd3ea02c4d77b0001217e83"},"id":"5cdf60b8e6ea20000146ecc6"},{"silkCar":{"id":"5cd780d2c4d77b0001136bb5"},"id":"5ce069dbe6ea200001236044"},{"silkCar":{"id":"5cd3e838c4d77b00011709f5"},"id":"5ce01349e6ea200001544d5a"},{"silkCar":{"id":"5cd3e748c4d77b0001125ffa"},"id":"5cdf945ce6ea2000011d2f36"},{"silkCar":{"id":"5cd3e838c4d77b0001170a0b"},"id":"5ce06667e6ea200001f453b1"},{"silkCar":{"id":"5cd3e838c4d77b00011709e8"},"id":"5ce03e83e6ea200001b5f5eb"}],"saleType":"DOMESTIC","budatClass":{"id":"5bfd4a0c67e7ad000188a0db"},"budat":1558224000000,"pipeType":0.345}
     * fireDateTime : 1558237295116
     */
    @SerializedName("eventId")
    public String eventId;
    @SerializedName("deleted")
    public boolean deleted;
    @SerializedName("deleteOperator")
    public String deleteOperator;
    @SerializedName("packageBox")
    public PackageBoxEntity packageBox;
    @SerializedName("deleteDateTime")
    public String deleteDateTime;
    @SerializedName("type")
    public String type;
    @SerializedName("operator")
    public OperatorEntity operator;
    @SerializedName("command")
    public CommandEntity command;
    @SerializedName("fireDateTime")
    public long fireDateTime;

    public class PackageBoxEntity {
        /**
         * id : 5ce0d06fe6ea200001cbd248
         */
        @SerializedName("id")
        public String id;
    }

    public class OperatorEntity {
        /**
         * id : if_warehouse
         */
        @SerializedName("id")
        public String id;
    }

    public class CommandEntity {
        /**
         * grossWeight : 11408.4
         * netWeight : 11160.0
         * sapT001l : {"id":"3118"}
         * silkCarRecords : [{"silkCar":{"id":"5cd780d2c4d77b0001136c1d"},"id":"5cdf6510e6ea200001715041"},{"silkCar":{"id":"5cd3e748c4d77b00011260e6"},"id":"5cdf9e82e6ea2000017c294c"},{"silkCar":{"id":"5cd3e838c4d77b00011709ce"},"id":"5cdfa3e7e6ea200001cc8f82"},{"silkCar":{"id":"5cd3e748c4d77b00011260fd"},"id":"5cdffeb5e6ea200001c64f45"},{"silkCar":{"id":"5cd780d2c4d77b0001136c00"},"id":"5cdb6c46c4d77b00015696c8"},{"silkCar":{"id":"5bffa7917979c400014701b5"},"id":"5ce01b54e6ea2000018ac870"},{"silkCar":{"id":"5cd3ea02c4d77b0001217e83"},"id":"5cdf60b8e6ea20000146ecc6"},{"silkCar":{"id":"5cd780d2c4d77b0001136bb5"},"id":"5ce069dbe6ea200001236044"},{"silkCar":{"id":"5cd3e838c4d77b00011709f5"},"id":"5ce01349e6ea200001544d5a"},{"silkCar":{"id":"5cd3e748c4d77b0001125ffa"},"id":"5cdf945ce6ea2000011d2f36"},{"silkCar":{"id":"5cd3e838c4d77b0001170a0b"},"id":"5ce06667e6ea200001f453b1"},{"silkCar":{"id":"5cd3e838c4d77b00011709e8"},"id":"5ce03e83e6ea200001b5f5eb"}]
         * saleType : DOMESTIC
         * budatClass : {"id":"5bfd4a0c67e7ad000188a0db"}
         * budat : 1558224000000
         * pipeType : 0.345
         */
        @SerializedName("grossWeight")
        public double grossWeight;
        @SerializedName("netWeight")
        public double netWeight;
        @SerializedName("sapT001l")
        public SapT001lEntity sapT001l;
        @SerializedName("silkCarRecords")
        public List<SilkCarRecordsEntity> silkCarRecords;
        @SerializedName("saleType")
        public String saleType;
        @SerializedName("budatClass")
        public BudatClassEntity budatClass;
        @SerializedName("budat")
        public long budat;
        @SerializedName("pipeType")
        public double pipeType;

        public class SapT001lEntity {
            /**
             * id : 3118
             */
            @SerializedName("id")
            public String id;
        }

        public class SilkCarRecordsEntity {
            /**
             * silkCar : {"id":"5cd780d2c4d77b0001136c1d"}
             * id : 5cdf6510e6ea200001715041
             */
            @SerializedName("silkCar")
            public SilkCarEntity silkCar;
            @SerializedName("id")
            public String id;

            public class SilkCarEntity {
                /**
                 * id : 5cd780d2c4d77b0001136c1d
                 */
                @SerializedName("id")
                public String id;
            }
        }

        public class BudatClassEntity {
            /**
             * id : 5bfd4a0c67e7ad000188a0db
             */
            @SerializedName("id")
            public String id;
        }
    }
}
