package com.cheetah.solr.service;

import java.util.List;
import java.util.Map;

import com.cheetah.solr.model.SolrQueryParam;

public interface IDocumentService {

	public void add(String collection, Map<String, Object> doc) throws Exception;

	public void batchAdd(String collection, List<Map<String, Object>> docs) throws Exception;

	public void update(String collection, Map<String, Object> doc) throws Exception;

	public void batchUpdate(String collection, List<Map<String, Object>> docs) throws Exception;

	public void remove(String collection, String id) throws Exception;

	public void batchRemove(String collection, List<String> ids) throws Exception;

	public void removeByQuery(String collection, String query) throws Exception;

	public void clear(String collection) throws Exception;

	public Object query(String collection, Map<String, String> queryParam) throws Exception;

	public long queryCount(String collection, Map<String, String> queryParam) throws Exception;

	public Object queryForPage(String collection, int pageNumber, int pageSize, Map<String, String> queryParam)
			throws Exception;

	public Object query(SolrQueryParam solrQueryParam) throws Exception;

}
