package org.tinycloud.tinymock.modules.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/18 21:22
 */
@Service
public class SeqService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long getSeqValue(String seqCode) {
        String sql = "select seq_value from t_seq where seq_code = ?";
        try {
            Map<String, Object> result = this.jdbcTemplate.queryForMap(sql, seqCode);
            // 在原本的基础上加10万，防止时间回滚
            return Long.parseLong(result.get("seq_value").toString()) + 100000;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updateSeqValue(String seqCode, Long seqValue) {
        String sql = "select seq_value from t_seq where seq_code = ?";
        // 无则新增，有则更新
        List<Map<String, Object>> lists = this.jdbcTemplate.queryForList(sql, seqCode);
        if (!CollectionUtils.isEmpty(lists)) {
            int rows = this.jdbcTemplate.update("update t_seq set seq_value = ? where seq_code = ?", seqValue, seqCode);
        } else {
            int rows = this.jdbcTemplate.update("insert into t_seq (seq_code, seq_value) values (?,?)", seqCode, seqValue);
        }
    }

}
