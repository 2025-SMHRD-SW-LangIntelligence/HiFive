package com.smhrd.gitest.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Long> findRecommendedStoreIdsByFilters(List<String> categories, List<String> addressTags) {
        // 기본 SQL 쿼리문
        StringBuilder sql = new StringBuilder(
            "SELECT s.store_id " +
            "FROM store s " +
            "LEFT JOIN store_tag st ON s.store_id = st.store_id " +
            "LEFT JOIN mood_tag mt ON st.tag_id = mt.tag_id " +
            "LEFT JOIN recommendation_rule rr ON mt.tag_name = rr.tag_name "
        );

        StringBuilder whereClause = new StringBuilder();

        // 1. 카테고리 조건 추가
        if (categories != null && !categories.isEmpty()) {
            whereClause.append("rr.category IN (:categories) ");
        }

        // 2. 주소 태그 조건 추가
        if (addressTags != null && !addressTags.isEmpty()) {
            if (whereClause.length() > 0) {
                whereClause.append("AND ");
            }
            StringBuilder addressConditions = new StringBuilder("(");
            for (int i = 0; i < addressTags.size(); i++) {
                if (i > 0) {
                    addressConditions.append(" AND ");
                }
                addressConditions.append("s.address LIKE :addressTag").append(i).append(" ");
            }
            addressConditions.append(")");
            whereClause.append(addressConditions);
        }

        if (whereClause.length() > 0) {
            sql.append("WHERE ").append(whereClause);
        }

        sql.append(
            "GROUP BY s.store_id " +
            "ORDER BY COUNT(DISTINCT rr.category) DESC, SUM(rr.weight) DESC " +
            "LIMIT 20"
        );
        
        // 네이티브 쿼리 생성
        Query query = entityManager.createNativeQuery(sql.toString());

        // ★★★ 파라미터가 있을 때만 바인딩하도록 다시 한번 확인 ★★★
        if (categories != null && !categories.isEmpty()) {
            query.setParameter("categories", categories);
        }
        if (addressTags != null && !addressTags.isEmpty()) {
            for (int i = 0; i < addressTags.size(); i++) {
                query.setParameter("addressTag" + i, "%" + addressTags.get(i) + "%");
            }
        }

        List<Object> result = query.getResultList();
        return result.stream()
                     .map(obj -> ((Number) obj).longValue())
                     .collect(Collectors.toList());
    }
}