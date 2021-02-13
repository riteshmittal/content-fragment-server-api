package com.aem.community.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.FragmentTemplate;
import com.aem.community.core.services.MyService;
import com.aem.community.core.services.SampleConfiguration;

@Component(property = { "service.ranking:Integer=103" }, service = ContentFragmentService.class)
public class ContentFragmentServiceImpl implements ContentFragmentService{

	@Reference
	private ResourceResolverFactory resolverFactory;

	@Activate
	protected void activate(SampleConfiguration config) {

	}

	@Override
	public String createCF() {

		try (ResourceResolver resourceResolver = getResourceResolverForPage("datawrite")) {
			Resource templateOrModelRsc = resourceResolver
					.getResource("/conf/cf-aem-tech-talk/settings/dam/cfm/models/cf-model-aem-tech-talk");
			Resource pareentRes = resourceResolver.getResource("/content/dam/cf-aem-tech-talk");
			FragmentTemplate tpl = templateOrModelRsc.adaptTo(FragmentTemplate.class);
			ContentFragment newFragment = tpl.createFragment(pareentRes, "New CF for demo", "New content fragment");
			newFragment.setMetaData("my prop", "my prop value");
			resourceResolver.commit();

		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return "CF created";

	}

	/**
	 * Gets the Resource resolver object in service.
	 *
	 * @return resourceResolver the resourceResolverObject
	 */
	@Override
	public ResourceResolver getResourceResolverForPage(String name) {
		ResourceResolver resourceResolver = null;
		try {
			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, name);
			resourceResolver = resolverFactory.getServiceResourceResolver(param);
		} catch (Exception e) {
		}
		return resourceResolver;
	}

}