package com.likun.mongo.mongotest.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Command {

    /**
     * silkCarRecords : [{"silkCar":{"id":"5bffa78f7979c4000146e7dc"},"id":"626ebbfc415a7300016c96f0"},{"silkCar":{"id":"5bffa78f7979c4000146e84e"},"id":"626eedbc415a730001d1cc7b"},{"silkCar":{"id":"5bffa78f7979c4000146e82c"},"id":"626eed7a415a730001cfdb68"},{"silkCar":{"id":"5bffa78f7979c4000146e6c7"},"id":"626eee19415a730001d4f2da"},{"silkCar":{"id":"5bffa78f7979c4000146e74b"},"id":"626eef8b415a730001deda48"},{"silkCar":{"id":"5bffa7907979c4000146ee70"},"id":"626ee4f9415a7300018b356e"},{"silkCar":{"id":"5bffa78f7979c4000146e747"},"id":"626ebd7f415a7300017744a9"},{"silkCar":{"id":"5bffa78f7979c4000146e7d1"},"id":"626ed6b5415a73000131251e"},{"silkCar":{"id":"5bffa7907979c4000146ee43"},"id":"626eac45415a730001de6218"},{"silkCar":{"id":"5bffa78f7979c4000146e7f8"},"id":"626ee3ae415a73000184e29b"},{"silkCar":{"id":"5bffa78f7979c4000146e6f1"},"id":"626e8954415a7300011893e1"},{"silkCar":{"id":"5bffa78f7979c4000146e820"},"id":"626e5895415a7300010ea5f3"},{"silkCar":{"id":"5bffa7907979c4000146ee97"},"id":"626ebbfc415a7300016ca2d4"},{"silkCar":{"id":"5bffa78f7979c4000146e7bd"},"id":"626ebe19415a7300018c5a66"},{"silkCar":{"id":"5bffa7907979c4000146eeb0"},"id":"626eb813415a7300015832b1"},{"silkCar":{"id":"5bffa7907979c4000146ee10"},"id":"626eed29415a730001ce20c3"},{"silkCar":{"id":"5bffa7907979c4000146ee83"},"id":"626ee1cf415a7300017bd788"},{"silkCar":{"id":"5bffa7907979c4000146eed9"},"id":"626ea79e415a730001bf41f5"},{"silkCar":{"id":"5bffa7917979c40001470784"},"id":"626dc3b7415a730001a3d526"}]
     * config : {"packageBoxCount":12,"silkCount":72}
     */
    @SerializedName("silkCarRecords")
    public List<SilkCarRecordsEntity> silkCarRecords;
    @SerializedName("config")
    public ConfigEntity config;

    public class SilkCarRecordsEntity {
        /**
         * silkCar : {"id":"5bffa78f7979c4000146e7dc"}
         * id : 626ebbfc415a7300016c96f0
         */
        @SerializedName("silkCar")
        public SilkCarEntity silkCar;
        @SerializedName("id")
        public String id;

        public class SilkCarEntity {
            /**
             * id : 5bffa78f7979c4000146e7dc
             */
            @SerializedName("id")
            public String id;
        }
    }

    public class ConfigEntity {
        /**
         * packageBoxCount : 12
         * silkCount : 72
         */
        @SerializedName("packageBoxCount")
        public int packageBoxCount;
        @SerializedName("silkCount")
        public int silkCount;
    }
}
