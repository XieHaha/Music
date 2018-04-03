package custom.base.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luozi on 2016/10/17.
 */

public class FilterParameter implements Serializable {
    private static final long serialVersionUID = -559282504062945344L;
    /**
     * 接口类型列表
     * <p>
     * 1.国标；2^0 2特斯拉2^1； 3.ABB 2^2， 4其他 0;除去其他，剩下的标识2的N，或运算
     */
    private int chargerPortType;
    /**
     * 站点类型列表
     */
    private List<String> stationTypes;
    /**
     * 运营商类型列表
     */
    private List<String> operMerchantId;


    public int getChargerPortType() {
        return chargerPortType;
    }

    public void setChargerPortType(int chargerPortType) {
        this.chargerPortType = chargerPortType;
    }

    public List<String> getStationTypes() {
        return stationTypes;
    }

    public void setStationTypes(List<String> stationTypes) {
        this.stationTypes = stationTypes;
    }

    public List<String> getOperMerchantId() {
        return operMerchantId;
    }

    public void setOperMerchantId(List<String> operMerchantId) {
        this.operMerchantId = operMerchantId;
    }

    @Override
    public String toString() {
        return "FilterParameter{" +
                "chargerPortType=" + chargerPortType +
                ", stationTypes=" + stationTypes +
                ", operMerchantId=" + operMerchantId +
                '}';
    }
}
