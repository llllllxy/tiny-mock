package org.tinycloud.tinymock.modules.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.tinycloud.tinymock.modules.bean.vo.DictInfoVo;
import org.tinycloud.tinymock.modules.bean.vo.DictItemInfoVo;
import org.tinycloud.tinymock.modules.mapper.DictMapper;

import java.util.*;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2025-04-01 10:29
 */
@Service
public class DictService {

    @Autowired
    private DictMapper dictMapper;

    public Map<String, DictInfoVo> mapDicts() {
        List<Map<String, Object>> dictList = dictMapper.getDictList();
        if (CollectionUtils.isEmpty(dictList)) {
            return null;
        }

        Map<String, DictInfoVo> dictInfoVOMap = new HashMap<>();
        for (Map<String, Object> item : dictList) {
            DictInfoVo dictInfoVO = new DictInfoVo();
            HashMap<String, DictItemInfoVo> map = new HashMap<>();
            List<DictItemInfoVo> list = new ArrayList<>();

            String dictCode = item.get("dict_code").toString();
            dictInfoVO.setDictCode(dictCode);
            dictInfoVO.setDictName(item.get("dict_name").toString());

            String dictKeyStr = item.get("dict_key").toString();
            String dictValueStr = item.get("dict_value").toString();
            String backgroundStr = item.get("background").toString();
            List<String> dictKeys = Arrays.asList(dictKeyStr.split(","));
            List<String> dictValues = Arrays.asList(dictValueStr.split(","));
            List<String> backgrounds = Arrays.asList(backgroundStr.split(","));

            int length = dictKeys.size();
            for (int i = 0; i < length; i++) {
                String code = dictKeys.get(i);
                String codeName = dictValues.get(i);
                String background = backgrounds.get(i);

                // 构建list
                DictItemInfoVo itemInfoVo = new DictItemInfoVo();
                itemInfoVo.setDictKey(code);
                itemInfoVo.setDictValue(codeName);
                itemInfoVo.setBackground(background);
                list.add(itemInfoVo);
                // 构建map
                map.put(code, itemInfoVo);
            }
            dictInfoVO.setMap(map);
            dictInfoVO.setList(list);
            dictInfoVOMap.put(dictCode, dictInfoVO);
        }
        return dictInfoVOMap;
    }
}
