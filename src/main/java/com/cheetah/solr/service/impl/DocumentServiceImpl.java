package com.cheetah.solr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cheetah.solr.common.SolrConnection;
import com.cheetah.solr.common.SolrConstant;
import com.cheetah.solr.model.SolrData;
import com.cheetah.solr.model.SolrPage;
import com.cheetah.solr.model.SolrQueryParam;
import com.cheetah.solr.service.IDocumentService;
import com.cheetah.solr.utils.ReflectionUtilEX;

@Service
public class DocumentServiceImpl implements IDocumentService {

	@Value("${spring.data.solr.host}")
	private String solrHost;

	@Override
	public void add(String collection, Map<String, Object> doc) throws Exception {
		SolrInputDocument document = createDocument(doc);
		SolrClient solrClient = getClient();
		solrClient.add(collection, document);
		commitAndClose(solrClient, collection);
	}

	@Override
	public void batchAdd(String collection, List<Map<String, Object>> docs) throws Exception {
		ArrayList<SolrInputDocument> documents = new ArrayList<>(docs.size());
		SolrInputDocument document = null;
		for (Map<String, Object> doc : docs) {
			document = createDocument(doc);
			documents.add(document);
		}
		SolrClient solrClient = getClient();
		solrClient.add(collection, documents);
		commitAndClose(solrClient, collection);
	}

	@Override
	public void update(String collection, Map<String, Object> doc) throws Exception {
		this.add(collection, doc);
	}

	@Override
	public void batchUpdate(String collection, List<Map<String, Object>> docs) throws Exception {
		this.batchAdd(collection, docs);
	}

	@Override
	public void remove(String collection, String id) throws Exception {
		SolrClient solrClient = getClient();
		solrClient.deleteById(collection, id);
		commitAndClose(solrClient, collection);
	}

	@Override
	public void batchRemove(String collection, List<String> ids) throws Exception {
		SolrClient solrClient = getClient();
		solrClient.deleteById(collection, ids);
		commitAndClose(solrClient, collection);
	}

	@Override
	public void removeByQuery(String collection, String query) throws Exception {
		SolrClient solrClient = getClient();
		solrClient.deleteByQuery(collection, query);
		commitAndClose(solrClient, collection);
	}

	@Override
	public void clear(String collection) throws Exception {
		this.removeByQuery(collection, "*:*");
	}

	@Override
	public Object query(String collection, Map<String, String> queryParam) throws Exception {
		SolrClient solrClient = getClient();
		if (null == queryParam || queryParam.size() == 0) {
			queryParam = new HashMap<>();
			queryParam.put(SolrConstant.QUERY_CONDITION_Q, "*:*");
		}
		MapSolrParams mapSolrParams = new MapSolrParams(queryParam);
		QueryResponse response = solrClient.query(collection, mapSolrParams);
		SolrDocumentList docs = response.getResults();
		commitAndClose(solrClient, collection);
		SolrData data = new SolrData(docs);
		return data;
	}
	
	@Override
	public long queryCount(String collection, Map<String, String> queryParam) throws Exception {
		SolrClient solrClient = getClient();
		if (null == queryParam || queryParam.size() == 0) {
			queryParam = new HashMap<>();
			queryParam.put(SolrConstant.QUERY_CONDITION_Q, "*:*");
		}
		MapSolrParams mapSolrParams = new MapSolrParams(queryParam);
		QueryResponse response = solrClient.query(collection, mapSolrParams);
		return response.getResults().getNumFound();
	}

	@Override
	public Object queryForPage(String collection, int pageNumber, int pageSize, Map<String, String> queryParam)
			throws Exception {
		SolrClient solrClient = getClient();
		if (null == queryParam || queryParam.size() == 0) {
			queryParam = new HashMap<>();
			queryParam.put(SolrConstant.QUERY_CONDITION_Q, "*:*");
		} else {
			queryParam.remove("start");
			queryParam.remove("rows");
		}
		// 查询总数
		long total = this.queryCount(collection, queryParam);
		SolrQuery solrQuery = new SolrQuery();
		for (Map.Entry<String, String> entry : queryParam.entrySet()) {
			solrQuery.set(entry.getKey(), entry.getValue());
		}
		int start = (pageNumber - 1) * pageSize;
		solrQuery.setStart(start);
		solrQuery.setRows(pageSize);
		QueryResponse response = solrClient.query(collection, solrQuery);
		SolrDocumentList docs = response.getResults();
		commitAndClose(solrClient, collection);
		SolrPage solrPage = new SolrPage(total, pageNumber, pageSize, docs);
		return solrPage;
	}
	
	@Override
	public Object query(SolrQueryParam solrQueryParam) throws Exception{
		SolrClient solrClient = getClient();
		Map<String,Object> map = ReflectionUtilEX.convertEntityToMap(solrQueryParam);
		SolrQuery solrQuery = new SolrQuery();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if(null != entry.getValue()) {
				solrQuery.set(SolrQueryParam.getFieldName(entry.getKey()) , String.valueOf(entry.getValue()));
			}
		}
		QueryResponse response = solrClient.query(solrQueryParam.getCollection(), solrQuery);
		//如果高亮
		if(solrQueryParam.isHl()) {
			return response.getHighlighting();
		}
		//如果统计
		if(solrQueryParam.isFacet() && ( null != solrQueryParam.getFacetField() && !"".equals(solrQueryParam.getFacetField()))) {
			List<FacetField> facets = response.getFacetFields();//返回的facet列表
			List<Map<String,Long>> data = new ArrayList<>();
			Map<String,Long> res = null;
			for (FacetField facet : facets) {
				List<Count> counts = facet.getValues();
				res = new HashMap<>();
				for (Count count : counts) {
					res.put(count.getName(), count.getCount());
			    }
				data.add(res);
			}
			return data;
		}
		return response.getResults();
	}

	private SolrInputDocument createDocument(Map<String, Object> doc) {
		SolrInputDocument document = new SolrInputDocument();
		for (Map.Entry<String, Object> entry : doc.entrySet()) {
			document.addField(entry.getKey(), entry.getValue());
		}
		return document;
	}

	private SolrClient getClient() {
		return SolrConnection.getClient(solrHost);
	}

	private void commitAndClose(SolrClient solrClient, String collection) throws Exception {
		SolrConnection.commitAndClose(solrClient, collection);
	}

}
