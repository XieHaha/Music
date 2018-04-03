package custom.base.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚合点列表获取包括省聚合点list和市聚合点list
 * Created by luozi on 2016/4/12.
 */
public class GatherList {

    private final List<Gather> provinceGathers = new ArrayList<>();

    /**
     * 添加聚合点
     */
    public void addProvinceGather(Gather gather) {
        provinceGathers.add(gather);
    }

    /**
     * 根据code寻找Gather对象
     *
     * @param code
     * @return 找不到则返回null对象
     */
    public Gather findGatherByCode(int code) {
        for (int i = 0; i < provinceGathers.size(); i++) {
            if (provinceGathers.get(i).getCode() == code)
                return provinceGathers.get(i);
        }
        return null;
    }

    public List<Gather> getProvinceGathers() {
        return provinceGathers;
    }

}
