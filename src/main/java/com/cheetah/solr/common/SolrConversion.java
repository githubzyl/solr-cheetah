package com.cheetah.solr.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;

public class SolrConversion {

	@SuppressWarnings("rawtypes")
	public static LinkedHashMap<String, Object> convertResponseHeader(NamedList headers) {
		final String paramsHeader = "params";
		String headerName = null;
		Object headerVal = null;
		Map<String, Object> params = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		for (int i = 0; i < headers.size(); i++) {
			headerName = headers.getName(i);
			headerVal = headers.getVal(i);
			if (paramsHeader.equals(headerName) && headerVal instanceof SimpleOrderedMap) {
				SimpleOrderedMap sop = (SimpleOrderedMap) headerVal;
				params = new HashMap<>();
				for (int j = 0; j < sop.size(); j++) {
					params.put(sop.getName(j), sop.getVal(j));
				}
				map.put(paramsHeader, params);
			} else {
				map.put(headerName, headerVal);
			}
		}
		return map;
	}

	public static LinkedHashMap<String, Object> convertResponse(NamedList<Object> response) {
		final String reponseKey = "response";
		String responseName = null;
		Object responseVal = null;
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		for (int i = 0; i < response.size(); i++) {
			responseName = response.getName(i);
			responseVal = response.getVal(i);
			if (reponseKey.equals(responseName)) {
				SolrDocumentList solrDocumentList = (SolrDocumentList) responseVal;
				map.put("numFound", solrDocumentList.getNumFound());
				map.put("start", solrDocumentList.getStart());
				map.put("docs", solrDocumentList);
			}
		}
		return map;
	}

	public static LinkedHashMap<String, Object> convertFactetCounts(QueryResponse response) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("facet_fields", getFacetCount(response.getFacetFields()));
		return map;
	}

	public static Map<String, List<Map<String, Long>>> getFacetCount(List<FacetField> facets) {
		Map<String, List<Map<String, Long>>> map = new HashMap<>();
		List<Map<String, Long>> data = null;
		Map<String, Long> res = null;
		for (FacetField facet : facets) {
			data = new ArrayList<>();
			res = new HashMap<>();
			List<Count> counts = facet.getValues();
			for (Count count : counts) {
				res.put(count.getName(), count.getCount());
			}
			data.add(res);
			map.put(facet.getName(), data);
		}
		return map;
	}

}
