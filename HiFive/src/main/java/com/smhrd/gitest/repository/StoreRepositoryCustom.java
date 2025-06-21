package com.smhrd.gitest.repository;

import java.util.List;

public interface StoreRepositoryCustom {
    List<Long> findRecommendedStoreIdsByFilters(List<String> categories, List<String> addressTags);
}