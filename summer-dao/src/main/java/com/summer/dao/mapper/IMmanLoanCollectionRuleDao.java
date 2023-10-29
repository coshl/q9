package com.summer.dao.mapper;

import com.summer.dao.entity.MmanLoanCollectionRule;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface IMmanLoanCollectionRuleDao {

    public List<MmanLoanCollectionRule> findList(MmanLoanCollectionRule mmanLoanCollectionRule);

    public MmanLoanCollectionRule getCollectionRuleById(String id);

    public int update(MmanLoanCollectionRule collectionRule);

    public void insert(MmanLoanCollectionRule mmanLoanCollectionRule);

    public Integer findCompanyGoupOnline(HashMap<String, String> params);
}
